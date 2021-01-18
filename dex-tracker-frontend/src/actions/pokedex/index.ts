import { PokedexAction, UPDATE_POKEDEX } from "../../store/pokedex";
import { GamePokedex } from "../../types/pokedex";

export function loadPokedex(pokedex: GamePokedex[]): PokedexAction {
  return {
    type: UPDATE_POKEDEX,
    payload: pokedex,
  };
}
