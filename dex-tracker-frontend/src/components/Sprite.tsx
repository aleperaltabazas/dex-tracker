import React from "react";
import { hot } from "react-hot-loader";
import { host } from "../config";
import classNames from "classnames";

type IconProps = {
  gen: number;
  pokemon: string;
  alternateForm?: string;
  className?: string;
};

type SpriteProps = {
  pokemon: string;
  game: string;
};

const PokemonSprite = (props: SpriteProps) => {
  return (
    <span>
      <img
        src={`${host}/dex-tracker/sprites/${props.game}/${props.pokemon}.png`}
        alt={props.pokemon}
      />
    </span>
  );
};

const IconSprite = (props: IconProps) => {
  return (
    <span
      className={classNames(
        `d-inline pokeicon icon-gen${props.gen}${
          props.alternateForm ? "-forms" : ""
        } ${
          props.pokemon + (props.alternateForm ? `-${props.alternateForm}` : "")
        }-gen${props.gen}`,
        props.className
      )}
    />
  );
};

const sprite = {
  pokemon: PokemonSprite,
  icon: IconSprite,
};

export default hot(module)(sprite);
