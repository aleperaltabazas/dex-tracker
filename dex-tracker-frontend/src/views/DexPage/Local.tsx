import React from "react";
import { hot } from "react-hot-loader";
import { GamePokedex } from "../../types/pokedex";
import { UserDex } from "../../types/user";
import Render from "./Render";

type LocalProps = {
  id: string;
  gamePokedex: GamePokedex[];
};

const Local = (props: LocalProps) => {
  const dex: UserDex = (JSON.parse(
    localStorage.getItem("localDex")!
  ) as Array<UserDex>).find((d) => d.userDexId == props.id)!;
  return <Render dex={dex} gamePokedex={props.gamePokedex} />;
};

export default hot(module)(Local);
