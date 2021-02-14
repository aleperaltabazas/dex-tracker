import { Game } from "./pokedex";

export type UserDex = {
  userDexId: string;
  game: Game;
  type: "NATIONAL" | "REGIONAL";
  region: string;
  name?: string;
  pokemon: Pokemon[];
  caught: number;
};

export type Pokemon = {
  name: string;
  dexNumber: number;
  caught: boolean;
};

export type User = {
  username?: string;
  pokedex: UserDex[];
  mail: string;
};
