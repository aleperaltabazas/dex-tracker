import React from "react";
import { hot } from "react-hot-loader";
import { Redirect, RouteComponentProps, withRouter } from "react-router";
import { RootState } from "../reducers";
import { connect } from "react-redux";
import { PokedexState } from "../store/pokedex";
import Loader from "../components/Loader";
import { SessionState } from "../store/session";
import Local from "./DexPage/Local";
import Remote from "./DexPage/Remote";

type MatchParams = {
  id: string;
};

interface DexPageProps extends RouteComponentProps<MatchParams> {
  gamePokedex: PokedexState;
  session: SessionState;
}

const DexPage = (props: DexPageProps) => {
  if (!props.gamePokedex.loaded) {
    return (
      <div className="h-100 w-100 center">
        <Loader />
      </div>
    );
  }

  switch (props.session.type) {
    case "NOT_LOGGED_IN":
      return (
        <Local
          id={props.match.params.id}
          gamePokedex={props.gamePokedex.pokedex}
        />
      );
    case "LOGGED_IN": {
      return (
        <Remote
          id={props.match.params.id}
          gamePokedex={props.gamePokedex.pokedex}
        />
      );
    }
    default:
      return <Redirect to="/" />;
  }
};

const mapStateToProps = (root: RootState) => {
  return {
    gamePokedex: root.pokedex,
    session: root.session,
  };
};

export default hot(module)(connect(mapStateToProps)(withRouter(DexPage)));
