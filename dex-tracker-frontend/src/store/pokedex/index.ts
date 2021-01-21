import { GamePokedex } from "../../types/pokedex";

export const UPDATE_POKEDEX = "UPDATE_POKEDEX";

interface UpdatePokedexAction {
  type: typeof UPDATE_POKEDEX;
  payload: GamePokedex[];
}

export type PokedexAction = UpdatePokedexAction;

export type PokedexNotLoaded = {
  loaded: false;
};

export type PokedexLoaded = {
  loaded: true;
  pokedex: GamePokedex[];
};

export type PokedexState = PokedexLoaded | PokedexNotLoaded;
