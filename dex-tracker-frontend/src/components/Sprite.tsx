import React from "react";
import { hot } from "react-hot-loader";
import { host } from "../config";

type IconProps = {
  gen: number;
  pokemon: string;
  alternateForm?: string;
};

type SpriteProps = {
  pokemon: string;
  game: string;
};

const PokemonSprite = (props: SpriteProps) => {
  return (
    <div>
      <img
        src={`${host}/dex-tracker/sprites/${props.game}/${props.pokemon}.png`}
        alt={props.pokemon}
      />
    </div>
  );
};

const IconSprite = (props: IconProps) => {
  return (
    <span
      className={`d-inline pokeicon icon-gen${props.gen}${
        props.alternateForm ? "-forms" : ""
      } ${
        props.pokemon + (props.alternateForm ? `-${props.alternateForm}` : "")
      }-gen${props.gen}`}
    />
  );
};

const sprite = {
  pokemon: PokemonSprite,
  icon: IconSprite,
};

export default hot(module)(sprite);
