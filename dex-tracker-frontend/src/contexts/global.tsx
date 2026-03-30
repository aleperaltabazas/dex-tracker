import React, { createContext, useContext, useReducer, ReactNode } from "react";

// --- State types ---

export type GlobalState = {
  menuOpen: boolean;
  createDexFormOpen: boolean;
};

// --- Action types ---

type GlobalAction =
  | { type: "UPDATE_MENU_OPEN"; payload: boolean }
  | { type: "UPDATE_CREATE_DEX_FORM_OPEN"; payload: boolean };

// --- Reducer ---

const defaultState: GlobalState = { menuOpen: false, createDexFormOpen: false };

function globalReducer(state: GlobalState, action: GlobalAction): GlobalState {
  switch (action.type) {
    case "UPDATE_MENU_OPEN":
      return { ...state, menuOpen: action.payload };
    case "UPDATE_CREATE_DEX_FORM_OPEN":
      return { ...state, createDexFormOpen: action.payload };
    default:
      return state;
  }
}

// --- Context ---

type GlobalContextValue = {
  global: GlobalState;
  dispatch: React.Dispatch<GlobalAction>;
};

const GlobalContext = createContext<GlobalContextValue | undefined>(undefined);

export function GlobalProvider({ children }: { children: ReactNode }) {
  const [global, dispatch] = useReducer(globalReducer, defaultState);
  return (
    <GlobalContext.Provider value={{ global, dispatch }}>
      {children}
    </GlobalContext.Provider>
  );
}

export function useGlobal(): GlobalContextValue {
  const ctx = useContext(GlobalContext);
  if (!ctx) throw new Error("useGlobal must be used within a GlobalProvider");
  return ctx;
}
