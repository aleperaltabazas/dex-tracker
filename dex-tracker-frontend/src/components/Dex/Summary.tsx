import { makeStyles, Paper, Typography } from "@material-ui/core";
import React from "react";
import { hot } from "react-hot-loader";
import DexLink from "../Links/Dex";
import classNames from "classnames";
import LinearProgress from "../Progress/Linear";
import { UserDex } from "../../types/user";

type SummaryProps = {
  dex: UserDex;
  userId: string;
};

const useStyles = makeStyles(() => ({
  summary: {
    display: "flex",
    justifyContent: "space-between",
  },
  title: {
    fontSize: "24px",
  },
}));

const Summary = (props: SummaryProps) => {
  const classes = useStyles();

  return (
    <Paper className="p-1">
      <DexLink userId={props.userId} dexId={props.dex.userDexId}>
        <div className={classNames("center-v", classes.summary)}>
          <Typography variant="h4">
            <div className="center-v ellipsis">
              <span className={`pokesprite pokemon bulbasaur pt-1 pr-1`} />
              <span className={classNames(classes.title, "bold")}>
                {props.dex.name || props.dex.game.displayName}
              </span>
            </div>
          </Typography>
          <div className="pr-1">
            {props.dex.caught}/{props.dex.pokemon.length}
          </div>
        </div>
      </DexLink>
      <div className="mt-1">
        <LinearProgress
          value={(props.dex.caught * 100) / props.dex.pokemon.length}
        />
      </div>
    </Paper>
  );
};

export default hot(module)(Summary);
