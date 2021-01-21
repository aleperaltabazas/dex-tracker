import { Game } from "../../types/pokedex";

export const UPDATE_GAMES = "UPDATE_GAMES";

interface UpdateGamesAction {
  type: typeof UPDATE_GAMES;
  payload: Game[];
}

export type GamesAction = UpdateGamesAction;

export type GamesNotLoaded = {
  loaded: false;
};

export type GamesLoaded = {
  loaded: true;
  games: Game[];
};

export type GamesState = GamesLoaded | GamesNotLoaded;
