import { GamesAction, UPDATE_GAMES } from "../../store/games";
import { Game } from "../../types/pokedex";

export function loadGames(games: Game[]): GamesAction {
  return {
    type: UPDATE_GAMES,
    payload: games,
  };
}
