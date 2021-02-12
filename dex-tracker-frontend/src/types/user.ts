import { Game } from "./pokedex";

export type UserDex = {
  userDexId: string;
  game: Game;
  type: "NATIONAL" | "REGIONAL";
  region: string;
  name?: string;
  pokemon: Pokemon[];
};

export type Pokemon = {
  name: string;
  dexNumber: number;
  caught: boolean;
};

export type User = {
  username?: string;
  pokedex: UserDexRef[];
  mail: string;
};

export type UserDexRef = {
  userDexId: string;
  game: Game;
  name?: string;
  caught: number;
};
