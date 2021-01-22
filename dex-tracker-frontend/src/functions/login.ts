import axios, { AxiosError, AxiosRequestConfig } from "axios";
import { host } from "../config";
import { User } from "../types/user";
import Cookies from "js-cookie";
import store from "../store";
import { loginError, updateSessionState } from "../actions/session";

export function login() {
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

export function openLocallyStoredSession() {
  const dexToken = Cookies.get("dex-token");
  console.log("checking for dex-token", dexToken);

  function createUserAndDispatchToStore() {
    createUser()
      .then((res) => {
        console.log("Created user", Cookies.get("dex-token"));
        store.dispatch(updateSessionState(Cookies.get("dex-token")!, res.data));
      })
      .catch((err) => console.error("Error creating the user", err));
  }

  if (dexToken) {
    login()
      .then((res) => {
        console.log(res);
        return res;
      })
      .then((res) => {
        console.error("Logged in user");
        store.dispatch(updateSessionState(dexToken, res.data));
      })
      .catch((err: AxiosError) => {
        console.error("Error logging in", err);

        if (err.response?.status == 404) {
          Cookies.remove("dex-token");
          console.log("404");
          createUserAndDispatchToStore();
        } else {
          console.log("wtf");
          store.dispatch(loginError());
        }
      });
  } else {
    createUserAndDispatchToStore();
  }
}
