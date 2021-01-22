export const UPDATE_MENU_OPEN = "UPDATE_MENU_OPEN";
export const UPDATE_CREATE_DEX_FORM_OPEN = "UPDATE_CREATE_DEX_FORM_OPEN";

interface UpdateMenuOpenAction {
  type: typeof UPDATE_MENU_OPEN;
  payload: boolean;
}

interface UpdateCreateDexFormOpenAction {
  type: typeof UPDATE_CREATE_DEX_FORM_OPEN;
  payload: boolean;
}

export type GlobalAction = UpdateMenuOpenAction | UpdateCreateDexFormOpenAction;

export type GlobalState = {
  menuOpen: boolean;
  createDexFormOpen: boolean;
};
