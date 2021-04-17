import React, { useEffect, useState } from "react";
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
import withUserDex from "../hooks/withUserDex";

type MatchParams = {
  userId: string;
  dexId: string;
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

  const [dex] = withUserDex(
    props.match.params.userId,
    props.match.params.dexId,
    [props.match.params.userId, props.match.params.dexId]
  );

  if (
    !props.gamePokedex.loaded ||
    props.session.type == "UNINITIALIZED" ||
    dex.type == "PENDING"
  ) {
    return (
      <div className="h-100 w-100 center">
        <Loader />
      </div>
    );
  }

  if (props.session.type == "ERROR") {
    return <Redirect to="/" />;
  }

  if (dex.type == "ERROR") {
    return <div>Se rompió algo perro</div>;
  }

  return (
    <Container
      className={classNames(classes.noOverflow, "center")}
      key={dex.value.userDexId}
    >
      <div className={classNames(classes.container, "mt-3 mt-md-5")}>
        <Dex dex={dex.value} />
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
