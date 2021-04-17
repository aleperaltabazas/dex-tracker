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
  userId: string;
  username?: string;
  pokedex: UserDex[];
  mail: string;
  picture?: string;
};
