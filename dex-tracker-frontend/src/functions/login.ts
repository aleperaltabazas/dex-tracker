import axios, { AxiosError, AxiosRequestConfig } from "axios";
import { host } from "../config";
import { User } from "../types/user";
import Cookies from "js-cookie";
import store from "../store";
import { loginError, updateSessionState } from "../actions/session";
import {
  GoogleLoginResponse,
  GoogleLoginResponseOffline,
} from "react-google-login";

export function oauthLogin(
  response: GoogleLoginResponse | GoogleLoginResponseOffline
) {
  const succ = response as GoogleLoginResponse;
  let config: AxiosRequestConfig = {
    method: "POST",
    url: `${host}/login`,
    withCredentials: true,
  };

  axios
    .request<User>(config)
    .then((res) => res.data)
    .then(dispatchUser)
    .catch((err) => console.error("Error in login", err));
}

function fetchLocalStoredUser() {
  let config: AxiosRequestConfig = {
    method: "GET",
    url: `${host}/api/v1/users`,
    withCredentials: true,
  };

  return axios.request<User>(config);
}

export function createUser() {
  let config: AxiosRequestConfig = {
    url: `${host}/api/v1/users`,
    method: "POST",
    withCredentials: true,
  };

  return axios.request(config);
}

function dispatchUser(user: User) {
  store.dispatch(updateSessionState(Cookies.get("dex-token")!, user));
}

export function openLocallyStoredSession() {
  const dexToken = Cookies.get("dex-token");
  console.log("checking for dex-token", dexToken);

  function createUserAndDispatchToStore() {
    createUser()
      .then((res) => res.data)
      .then(dispatchUser)
      .catch((err) => console.error("Error creating the user", err));
  }

  if (dexToken) {
    fetchLocalStoredUser()
      .then((res) => store.dispatch(updateSessionState(dexToken, res.data)))
      .catch((err: AxiosError) => {
        console.error("Error logging in", err);

        if (err.response?.status == 404) {
          Cookies.remove("dex-token");
          createUserAndDispatchToStore();
        } else {
          store.dispatch(loginError());
        }
      });
  } else {
    createUserAndDispatchToStore();
  }
}
