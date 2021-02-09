import { Game } from "./pokedex";

export type UserDex = {
  game: Game;
  name?: string;
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
  pokedex: UserDexRef[];
  mail?: string;
};

export type UserDexRef = {
  userDexId: string;
  game: Game;
  name?: string;
  caught: number;
};
