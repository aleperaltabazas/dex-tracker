import React, { useCallback, useEffect, useRef, useState } from "react";
import { hot } from "react-hot-loader";
import { Pokemon, UserDex } from "../types/user";
import { TextField, Typography } from "@material-ui/core";
import useStyles from "./Dex/styles";
import classNames from "classnames";
import { applyChanges, fireSynchronize } from "../functions/my-dex";
import store from "../store";
import { updatePokedex as updateUserDex } from "../actions/session";
import EditIcon from "@material-ui/icons/Edit";
import CheckCircleIcon from "@material-ui/icons/CheckCircle";
import {
  caughtPokemon,
  clearSynchronizeQueue,
  updateDexName,
} from "../actions/syncQueue";
import DexGrid from "./Dex/DexGrid";

type DexProps = {
  dex: UserDex;
};

const Dex = (props: DexProps) => {
  const changes = useRef<Array<number>>(
    props.dex.pokemon.filter((p) => p.caught).map((p) => p.dexNumber)
  );

  const handleChange = (b: boolean, n: number) => {
    if (b && !changes.current.includes(n)) {
      changes.current.push(n);
    } else if (!b && changes.current.includes(n)) {
      changes.current = changes.current.filter((n) => n != n);
    }

    console.log(changes);
  };

  const classes = useStyles();

  const [isEditing, setIsEditing] = useState(false);
  const [name, setName] = useState(props.dex.name);

  const forceSynchronize = () => {
    const session = store.getState().session;

    if (session.type == "LOGGED_IN") {
      const sync = store.getState().syncQueue;

      if (sync.timeout) {
        clearTimeout(sync.timeout);
      }

      fireSynchronize(
        session.user.userId,
        sync.queue.some((u) => u.dexId == props.dex.userDexId)
          ? sync.queue.map((u) =>
              u.dexId == props.dex.userDexId
                ? {
                    ...u,
                    caught: changes.current,
                    name: name,
                  }
                : u
            )
          : [
              {
                dexId: props.dex.userDexId,
                name: name,
                caught: changes.current,
              },
            ]
      );
      store.dispatch(clearSynchronizeQueue());
    }
  };

  useEffect(() => {
    window.addEventListener("beforeunload", forceSynchronize);
    return () => {
      window.removeEventListener("beforeunload", forceSynchronize);
      store.dispatch(
        updateUserDex(props.dex.userDexId, applyChanges(changes.current))
      );
      if (changes.current.length > 0) {
        store.dispatch(caughtPokemon(props.dex.userDexId, changes.current));
      }
    };
  }, [props.dex.userDexId]);

  return (
    <div className={classes.root}>
      <Typography variant="h5">
        <div className={classNames("bold", "center-v", "pb-1")}>
          <span className={`pokemon pokesprite bulbasaur pt-1`} />
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
                  store.dispatch(
                    updateDexName({ name: name!, dexId: props.dex.userDexId })
                  );
                }}
              />
            )}
          </span>
        </div>
      </Typography>
      <DexGrid pokemon={props.dex.pokemon} handleChange={handleChange} />
    </div>
  );
};

export default hot(module)(Dex);
