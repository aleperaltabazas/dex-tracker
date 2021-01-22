export const UPDATE_MENU_OPEN = "UPDATE_MENU_OPEN";

interface UpdateMenuOpenAction {
  type: typeof UPDATE_MENU_OPEN;
  payload: boolean;
}

export type GlobalAction = UpdateMenuOpenAction;

export type GlobalState = {
  menuOpen: boolean;
};
