import { GlobalAction, UPDATE_MENU_OPEN } from "../store/global";

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
