import React from "react";
import { hot } from "react-hot-loader";
import { readLocalPokedex } from "../../functions/storage";
import { GamePokedex } from "../../types/pokedex";
import { UserDex } from "../../types/user";
import Render from "./Render";

type LocalProps = {
  id: string;
  gamePokedex: GamePokedex[];
};

const Local = (props: LocalProps) => {
  const dex: UserDex = readLocalPokedex().find((d) => d.userDexId == props.id)!;

  return <Render dex={dex} gamePokedex={props.gamePokedex} />;
};

export default hot(module)(Local);
