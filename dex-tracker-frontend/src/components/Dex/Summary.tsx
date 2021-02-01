import {
  Box,
  createStyles,
  Hidden,
  makeStyles,
  Paper,
  Typography,
  withStyles,
} from "@material-ui/core";
import React from "react";
import { hot } from "react-hot-loader";
import { GamePokedex } from "../../types/pokedex";
import { UserDexRef } from "../../types/user";
import DexLink from "../Links/Dex";
import classNames from "classnames";
import LinearProgress from "../Progress/Linear";

type SummaryProps = {
  dex: UserDexRef;
  gamePokedex: GamePokedex;
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
    <div className="mt-3 mb-3">
      <Paper className="p-1">
        <DexLink dexId={props.dex.userDexId}>
          <div className={classNames("center-v", classes.summary)}>
            <Typography variant="h4">
              <div className="center-v ellipsis">
                <span
                  className={`pokesprite pokemon ${props.gamePokedex.game.spritePokemon} pt-1 pr-1`}
                />
                <span className={classNames(classes.title, "bold")}>
                  {props.dex.name || props.dex.game.fullTitle}
                </span>
              </div>
            </Typography>
            <div className="pr-1">
              {props.dex.caught}/{props.gamePokedex.pokemon.length}
            </div>
          </div>
        </DexLink>
        <div className="mt-1">
          <LinearProgress
            value={(props.dex.caught * 100) / props.gamePokedex.pokemon.length}
          />
        </div>
      </Paper>
    </div>
  );
};

export default hot(module)(Summary);
