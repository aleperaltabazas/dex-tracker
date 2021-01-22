import { Container, makeStyles } from "@material-ui/core";
import React from "react";
import { hot } from "react-hot-loader";
import Loader from "react-loader-spinner";
import { RouteComponentProps, withRouter } from "react-router";
import withUserDex from "../hooks/withUserDex";
import classNames from "classnames";
import Dex from "../components/Dex";
import { GamePokedex } from "../types/pokedex";
import { RootState } from "../reducers";
import { connect } from "react-redux";

type MatchParams = {
  id: string;
};

interface DexPageProps extends RouteComponentProps<MatchParams> {
  gamePokedex: GamePokedex[];
}

const useStyles = makeStyles(() => ({
  container: {
    backgroundColor: "white",
  },
}));

const DexPage = (props: DexPageProps) => {
  const classes = useStyles();

  const [userDex] = withUserDex(props.match.params.id, props.match.params.id);

  if (userDex.type == "ERROR") {
    return <div>se rompió algo perrito</div>;
  }

  if (userDex.type == "PENDING") {
    return (
      <div className="h-100 w-100 center">
        <Loader
          type="ThreeDots"
          color="#00BFFF"
          height={100}
          width={100}
          className="center"
        />
      </div>
    );
  }

  console.log(userDex.value);

  return (
    <Container>
      <div className={classNames(classes.container, "mt-3 mt-md-5")}>
        <Dex
          dex={userDex.value}
          gamePokedex={
            props.gamePokedex.find(
              (gp) => gp.game.title == userDex.value.game.title
            )!
          }
        />
      </div>
    </Container>
  );
};

const mapStateToProps = (root: RootState) => {
  return {
    gamePokedex: root.pokedex.loaded ? root.pokedex.pokedex : [],
  };
};

export default hot(module)(connect(mapStateToProps)(withRouter(DexPage)));
