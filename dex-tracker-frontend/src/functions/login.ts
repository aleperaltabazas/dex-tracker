import axios, { AxiosError, AxiosRequestConfig } from "axios";
import { host } from "../config";
import { User } from "../types/user";
import Cookies from "js-cookie";
import store from "../store";
import {
  loginError,
  notLoggedIn,
  updatePicture,
  updateSessionState,
} from "../actions/session";
import {
  GoogleLoginResponse,
  GoogleLoginResponseOffline,
} from "react-google-login";
import { clearLocalPokedex, readLocalPokedex } from "./storage";

export function oauthLogin(
  response: GoogleLoginResponse | GoogleLoginResponseOffline
) {
  const succ = response as GoogleLoginResponse;
  let config: AxiosRequestConfig = {
    method: "POST",
    url: `${host}/api/v1/login`,
    withCredentials: true,
    data: {
      mail: succ.profileObj.email,
      localDex: readLocalPokedex(),
    },
  };

  axios
    .request<User>(config)
    .then((res) => res.data)
    .then(dispatchUser)
    .then(() => store.dispatch(updatePicture(succ.profileObj.imageUrl)))
    .then(clearLocalPokedex)
    .catch((err) => console.error("Error in login", err));
}

function dispatchUser(user: User) {
  store.dispatch(updateSessionState(Cookies.get("dex-token")!, user));
}

export function openLocallyStoredSession() {
  const dexToken = Cookies.get("dex-token");
  console.log("checking for dex-token", dexToken);

  if (dexToken) {
    let config: AxiosRequestConfig = {
      method: "POST",
      url: `${host}/api/v1/login`,
      withCredentials: true,
    };

    axios
      .request<User>(config)
      .then((res) => store.dispatch(updateSessionState(dexToken, res.data)))
      .catch((err: AxiosError) => {
        console.error("Error logging in", err);

        if (err.response?.status == 404) {
          Cookies.remove("dex-token");
          store.dispatch(notLoggedIn());
        } else {
          store.dispatch(loginError());
        }
      });
  } else {
    console.log("No user");
    store.dispatch(notLoggedIn());
  }
}
