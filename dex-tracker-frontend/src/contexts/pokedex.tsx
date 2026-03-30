import React, { createContext, useContext, useReducer, ReactNode } from "react";
import { Pokedex } from "../types/pokedex";

// --- State types ---

export type PokedexNotLoaded = { loaded: false };
export type PokedexLoaded = { loaded: true; pokedex: Pokedex[] };
export type PokedexState = PokedexLoaded | PokedexNotLoaded;

// --- Action types ---

type PokedexAction = { type: "UPDATE_POKEDEX"; payload: Pokedex[] };

// --- Reducer ---

const defaultState: PokedexState = { loaded: false };

function pokedexReducer(state: PokedexState, action: PokedexAction): PokedexState {
  switch (action.type) {
    case "UPDATE_POKEDEX":
      return { loaded: true, pokedex: action.payload };
    default:
      return state;
  }
}

// --- Context ---

type PokedexContextValue = {
  pokedex: PokedexState;
  dispatch: React.Dispatch<PokedexAction>;
};

const PokedexContext = createContext<PokedexContextValue | undefined>(undefined);

export function PokedexProvider({ children }: { children: ReactNode }) {
  const [pokedex, dispatch] = useReducer(pokedexReducer, defaultState);
  return (
    <PokedexContext.Provider value={{ pokedex, dispatch }}>
      {children}
    </PokedexContext.Provider>
  );
}

export function usePokedex(): PokedexContextValue {
  const ctx = useContext(PokedexContext);
  if (!ctx) throw new Error("usePokedex must be used within a PokedexProvider");
  return ctx;
}
