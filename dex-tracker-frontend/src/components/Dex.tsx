import React, { useEffect, useRef, useState } from "react";
import { Pokemon, UserDex } from "../types/user";
import { TextField, Typography } from "@mui/material";
import useStyles from "./Dex/styles";
import classNames from "classnames";
import { applyChanges, fireSynchronize } from "../functions/my-dex";
import EditIcon from "@mui/icons-material/Edit";
import CheckCircleIcon from "@mui/icons-material/CheckCircle";
import DexGrid from "./Dex/DexGrid";
import sprite from "./Sprite";
import { useSession } from "../contexts/session";
import { useSyncQueue } from "../contexts/syncQueue";

type DexProps = {
  dex: UserDex;
};

const Dex = (props: DexProps) => {
  const { session, dispatch: sessionDispatch } = useSession();
  const { syncQueue, dispatch: syncDispatch } = useSyncQueue();

  const changes = useRef<Array<number>>(
    props.dex.pokemon.filter((p) => p.caught).map((p) => p.dexNumber)
  );

  const handleChange = (b: boolean, n: number) => {
    if (b && !changes.current.includes(n)) {
      changes.current.push(n);
    } else if (!b && changes.current.includes(n)) {
      changes.current = changes.current.filter((x) => x != n);
    }
  };

  const classes = useStyles();

  const [isEditing, setIsEditing] = useState(false);
  const [name, setName] = useState(props.dex.name);

  const forceSynchronize = () => {
    if (session.type !== "LOGGED_IN") return;
    const userId = session.user.userId;
    const queue = syncQueue.queue;

    if (syncQueue.timeout) {
      clearTimeout(syncQueue.timeout);
    }

    const syncData = queue.some((u) => u.dexId == props.dex.userDexId)
      ? queue.map((u) =>
          u.dexId == props.dex.userDexId
            ? { ...u, caught: changes.current, name }
            : u
        )
      : [{ dexId: props.dex.userDexId, name, caught: changes.current }];

    fireSynchronize(userId, syncData);
    syncDispatch({ type: "CLEAR_SYNCHRONIZE_QUEUE" });
  };

  useEffect(() => {
    window.addEventListener("beforeunload", forceSynchronize);
    return () => {
      window.removeEventListener("beforeunload", forceSynchronize);
      sessionDispatch({
        type: "UPDATE_USER_DEX",
        payload: {
          dexId: props.dex.userDexId,
          update: applyChanges(changes.current),
        },
      });
      if (changes.current.length > 0 && session.type === "LOGGED_IN") {
        syncDispatch(
          { type: "UPDATE_DEX", payload: { dexId: props.dex.userDexId, caught: changes.current } },
          session.user.userId
        );
      }
    };
  }, [props.dex.userDexId]);

  return (
    <div className={classes.root}>
      <Typography variant="h5">
        <div className={classNames("bold", "center-v", "pb-1")}>
          <sprite.icon pokemon="bulbasaur" gen={props.dex.game.gen} />
          <span style={{ paddingBottom: "3px" }}>
            {!isEditing && (props.dex.name || props.dex.game.displayName)}
            {isEditing && (
              <TextField
                onChange={(e) => setName(e.target.value)}
                value={name}
              />
            )}
          </span>
          <span className="pl-1 2 cursor-pointer">
            {!isEditing && <EditIcon onClick={() => setIsEditing(true)} />}
            {isEditing && (
              <CheckCircleIcon
                color="action"
                onClick={() => {
                  setIsEditing(false);
                  if (session.type === "LOGGED_IN") {
                    syncDispatch(
                      { type: "UPDATE_DEX", payload: { dexId: props.dex.userDexId, name: name! } },
                      session.user.userId
                    );
                  }
                }}
              />
            )}
          </span>
        </div>
      </Typography>
      <DexGrid
        gen={props.dex.game.gen}
        pokemon={props.dex.pokemon}
        handleChange={handleChange}
      />
    </div>
  );
};

export default Dex;
