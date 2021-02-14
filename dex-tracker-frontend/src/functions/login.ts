import axios, { AxiosError, AxiosRequestConfig } from "axios";
import { host } from "../config";
import { User, UserDex } from "../types/user";
import Cookies from "js-cookie";
import store from "../store";
import {
  invalidateSession,
  loginError,
  notLoggedIn,
  uninitialize,
  updatePicture,
  updateSessionState,
} from "../actions/session";
import {
  GoogleLoginResponse,
  GoogleLoginResponseOffline,
} from "react-google-login";
import { readLocalPokedex, writeLocalPokedex } from "./storage";
import { fetchAllUsersDex } from "./my-dex";

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

  store.dispatch(uninitialize());

  axios
    .request<User>(config)
    .then((res) => res.data)
    .then((u) => {
      dispatchUser({
        ...u,
        pokedex: u.pokedex,
      });
      writeLocalPokedex(u.pokedex);
    })
    .then(() => store.dispatch(updatePicture(succ.profileObj.imageUrl)))
    .catch((err) => console.error("Error in login", err));
}

export function logout(token: string) {
  let config: AxiosRequestConfig = {
    method: "POST",
    url: `${host}/api/v1/logout`,
    withCredentials: true,
  };

  store.dispatch(uninitialize());

  return fetchAllUsersDex(token)
    .then(writeLocalPokedex)
    .then(() => axios.request(config))
    .then(invalidateSession)
    .then(store.dispatch)
    .catch(console.error);
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
