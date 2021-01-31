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
    <>
      <Hidden smDown>
        <div className="mt-3 mb-3">
          <Paper className="p-1">
            <DexLink dexId={props.dex.userDexId}>
              <div className={classNames("center-v", classes.summary)}>
                <div className="center-v">
                  <span
                    className={`pokesprite pokemon ${props.gamePokedex.game.spritePokemon} pr-1`}
                  />
                  <span className={classes.title}>
                    {props.dex.name || props.dex.game.fullTitle}
                  </span>
                </div>
                <div className="pr-1">
                  {props.dex.caught}/{props.gamePokedex.pokemon.length}
                </div>
              </div>
            </DexLink>
            <div className="mt-2">
              <LinearProgress
                value={
                  (props.dex.caught * 100) / props.gamePokedex.pokemon.length
                }
              />
            </div>
          </Paper>
        </div>
      </Hidden>
      <Hidden mdUp></Hidden>
    </>
  );
};

export default hot(module)(Summary);
