import { PokedexAction, UPDATE_POKEDEX } from "../../store/pokedex";
import { Pokedex } from "../../types/pokedex";

export function loadPokedex(pokedex: Pokedex[]): PokedexAction {
  return {
    type: UPDATE_POKEDEX,
    payload: pokedex,
  };
}
