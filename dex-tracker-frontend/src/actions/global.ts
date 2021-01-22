import {
  GlobalAction,
  UPDATE_CREATE_DEX_FORM_OPEN,
  UPDATE_MENU_OPEN,
} from "../store/global";

export function openMenu(): GlobalAction {
  return {
    type: UPDATE_MENU_OPEN,
    payload: true,
  };
}

export function closeMenu(): GlobalAction {
  return {
    type: UPDATE_MENU_OPEN,
    payload: false,
  };
}

export function openCreateDexForm(): GlobalAction {
  return {
    type: UPDATE_CREATE_DEX_FORM_OPEN,
    payload: true,
  };
}

export function closeCreateDexForm(): GlobalAction {
  return {
    type: UPDATE_CREATE_DEX_FORM_OPEN,
    payload: false,
  };
}
