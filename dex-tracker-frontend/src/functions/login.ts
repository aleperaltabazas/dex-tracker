import axios, { AxiosError, AxiosRequestConfig } from "axios";
import { host } from "../config";
import { User } from "../types/user";
import Cookies from "js-cookie";
import store from "../store";
import {
  invalidateSession,
  loginError,
  noLogin,
  uninitialize,
  updatePicture,
  updateSessionState,
} from "../actions/session";
import {
  GoogleLoginResponse,
  GoogleLoginResponseOffline,
} from "react-google-login";
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
      googleToken: succ.tokenId,
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
    })
    .then(() => {
      store.dispatch(updatePicture(succ.profileObj.imageUrl));
      Cookies.set("picture", succ.profileObj.imageUrl, { expires: 3600000 });
    })
    .catch((err) => {
      console.error("Error in login", err);
      store.dispatch(loginError());
    });
}

export function logout(token: string) {
  let config: AxiosRequestConfig = {
    method: "POST",
    url: `${host}/api/v1/logout`,
    withCredentials: true,
  };

  store.dispatch(uninitialize());

  return fetchAllUsersDex(token)
    .then(() => axios.request(config))
    .catch(console.error)
    .then(invalidateSession)
    .then(store.dispatch);
}

function dispatchUser(user: User) {
  store.dispatch(updateSessionState(Cookies.get("dex-token")!, user));
}

export function openLocallyStoredSession() {
  const dexToken = Cookies.get("dex-token");

  if (dexToken) {
    let config: AxiosRequestConfig = {
      method: "POST",
      url: `${host}/api/v1/login`,
      withCredentials: true,
    };

    axios
      .request<User>(config)
      .then((res) => store.dispatch(updateSessionState(dexToken, res.data)))
      .then(() => {
        const picture = Cookies.get("picture");
        if (picture) {
          store.dispatch(updatePicture(picture));
        }
      })
      .catch((err: AxiosError) => {
        console.error("Error logging in", err);

        if (err.response?.status == 404) {
          Cookies.remove("dex-token");
          store.dispatch(noLogin());
        } else {
          store.dispatch(loginError());
        }
      });
  } else {
    store.dispatch(noLogin());
  }
}
