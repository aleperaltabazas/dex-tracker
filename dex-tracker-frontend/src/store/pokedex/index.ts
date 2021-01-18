import { GamePokedex } from "../../types/pokedex";

export const UPDATE_POKEDEX = "UPDATE_POKEDEX";

interface UpdatePokedexAction {
  type: typeof UPDATE_POKEDEX;
  payload: GamePokedex[];
}

export type PokedexAction = UpdatePokedexAction;

type NotLoaded = {
  loaded: false;
};

type Loaded = {
  loaded: true;
  pokedex: GamePokedex[];
};

export type PokedexState = Loaded | NotLoaded;
