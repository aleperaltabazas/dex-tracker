import {
  PokedexState,
  PokedexAction,
  UPDATE_POKEDEX,
} from "../../store/pokedex";

const defaultPokedexState: PokedexState = {
  loaded: false,
};

function pokedexReducer(state = defaultPokedexState, action: PokedexAction) {
  switch (action.type) {
    case UPDATE_POKEDEX: {
      return {
        loaded: true,
        pokedex: action.payload,
      };
    }
    default:
      return state;
  }
}

export default pokedexReducer;
