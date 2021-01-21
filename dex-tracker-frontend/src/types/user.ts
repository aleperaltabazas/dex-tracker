import { GameTitle } from "./pokedex";

export type UserDex = {
  game: GameTitle;
  type: "NATIONAL" | "REGIONAL";
  region: string;
  pokemon: Pokemon[];
  userDexId: string;
};

export type Pokemon = {
  name: string;
  dexNumber: number;
  caught: boolean;
};

export type User = {
  username?: string;
  pokedex: UserDex[];
};
