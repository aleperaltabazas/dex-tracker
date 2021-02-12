import { Container, makeStyles } from "@material-ui/core";
import React from "react";
import { hot } from "react-hot-loader";
import Dex from "../../components/Dex";
import { GamePokedex } from "../../types/pokedex";
import { UserDex } from "../../types/user";
import classNames from "classnames";

type RenderProps = {
  dex: UserDex;
  gamePokedex: GamePokedex[];
};

const useStyles = makeStyles((theme) => ({
  container: {
    backgroundColor: "white",
  },
  noOverflow: {
    [theme.breakpoints.down("md")]: {
      overflowX: "hidden",
    },
  },
}));

const Render = (props: RenderProps) => {
  const classes = useStyles();

  return (
    <Container className={classNames(classes.noOverflow, "center")}>
      <div className={classNames(classes.container, "mt-3 mt-md-5")}>
        <Dex
          dex={props.dex}
          gamePokedex={
            props.gamePokedex.find(
              (gp) => gp.game.title == props.dex.game.title
            )!
          }
        />
      </div>
    </Container>
  );
};

export default hot(module)(Render);
