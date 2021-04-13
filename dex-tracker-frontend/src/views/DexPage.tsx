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

  const [dex, setDex] = useState<UserDex | undefined>();

  useEffect(() => {
    setDex(undefined);
    if (props.session.type == "LOGGED_IN") {
      setDex(
        props.session.user.pokedex.find(
          (d) => d.userDexId == props.match.params.id
        )
      );
    } else if (props.session.type == "NOT_LOGGED_IN") {
      setDex(
        props.session.localDex.find((d) => d.userDexId == props.match.params.id)
      );
    }
  }, [props.match.params.id, props.session.type]);

  if (
    !props.gamePokedex.loaded ||
    props.session.type == "UNINITIALIZED" ||
    !dex
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

  return (
    <Container
      className={classNames(classes.noOverflow, "center")}
      key={dex.userDexId}
    >
      <div className={classNames(classes.container, "mt-3 mt-md-5")}>
        <Dex dex={dex} />
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
