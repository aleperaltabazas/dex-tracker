import React from "react";
import { hot } from "react-hot-loader";
import Loader from "../../components/Loader";
import withUserDex from "../../hooks/withUserDex";
import { GamePokedex } from "../../types/pokedex";
import Render from "./Render";

type RemoteProps = {
  id: string;
  gamePokedex: GamePokedex[];
};

const Remote = (props: RemoteProps) => {
  const [userDex] = withUserDex(props.id, props.id);

  if (userDex.type == "ERROR") {
    return <div>se rompió algo perrote</div>;
  }

  if (userDex.type == "PENDING") {
    return (
      <div className="h-100 w-100 center">
        <Loader />
      </div>
    );
  }

  return <Render dex={userDex.value} gamePokedex={props.gamePokedex} />;
};

export default hot(module)(Remote);
