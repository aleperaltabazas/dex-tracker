import React from "react";
import { hot } from "react-hot-loader";
import { Redirect, RouteComponentProps, withRouter } from "react-router";
import { RootState } from "../reducers";
import { connect } from "react-redux";
import { PokedexState } from "../store/pokedex";
import Loader from "../components/Loader";
import { SessionState } from "../store/session";
import { Container, makeStyles } from "@material-ui/core";
import classNames from "classnames";
import { UserDex } from "../types/user";
import Dex from "../components/Dex";

type MatchParams = {
  id: string;
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

interface DexPageProps extends RouteComponentProps<MatchParams> {
  gamePokedex: PokedexState;
  session: SessionState;
}

const DexPage = (props: DexPageProps) => {
  const classes = useStyles();
  if (!props.gamePokedex.loaded || props.session.type == "UNINITIALIZED") {
    return (
      <div className="h-100 w-100 center">
        <Loader />
      </div>
    );
  }

  if (props.session.type == "ERROR") {
    return <Redirect to="/" />;
  }

  let dex: UserDex | undefined;

  if (props.session.type == "LOGGED_IN") {
    dex = props.session.user.pokedex.find(
      (d) => d.userDexId == props.match.params.id
    );
  } else {
    dex = props.session.localDex.find(
      (d) => d.userDexId == props.match.params.id
    );
  }

  if (!dex) {
    return <Redirect to="/" />;
  }

  return (
    <Container className={classNames(classes.noOverflow, "center")}>
      <div className={classNames(classes.container, "mt-3 mt-md-5")}>
        <Dex
          dex={dex}
          gamePokedex={
            props.gamePokedex.pokedex.find(
              (gp) => gp.game.title == dex!.game.title
            )!
          }
        />
      </div>
    </Container>
  );
};

const mapStateToProps = (root: RootState) => {
  return {
    gamePokedex: root.pokedex,
    session: root.session,
  };
};

export default hot(module)(connect(mapStateToProps)(withRouter(DexPage)));
