import { Pokedex } from "../../types/pokedex";

export const UPDATE_POKEDEX = "UPDATE_POKEDEX";

interface UpdatePokedexAction {
  type: typeof UPDATE_POKEDEX;
  payload: Pokedex[];
}

export type PokedexAction = UpdatePokedexAction;

export type PokedexNotLoaded = {
  loaded: false;
};

export type PokedexLoaded = {
  loaded: true;
  pokedex: Pokedex[];
};

export type PokedexState = PokedexLoaded | PokedexNotLoaded;
