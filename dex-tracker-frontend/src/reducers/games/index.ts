import { GamesAction, GamesState, UPDATE_GAMES } from "../../store/games";

const defaultGamesState: GamesState = {
  loaded: false,
};

export function gamesReducer(state = defaultGamesState, action: GamesAction) {
  switch (action.type) {
    case UPDATE_GAMES: {
      return {
        loaded: true,
        games: action.payload,
      };
    }
    default: {
      return state;
    }
  }
}

export default gamesReducer;
