import axios, { AxiosError, AxiosRequestConfig } from "axios";
import { host } from "../config";
import { User } from "../types/user";
import Cookies from "js-cookie";
import { CodeResponse } from "@react-oauth/google";

type SessionDispatch = React.Dispatch<any>;

export function oauthLogin(response: CodeResponse, sessionDispatch: SessionDispatch) {
  const config: AxiosRequestConfig = {
    method: "POST",
    url: `${host}/api/v1/login`,
    withCredentials: true,
    data: {
      googleToken: response.code,
    },
  };

  sessionDispatch({ type: "UNINITIALIZE_SESSION" });

  axios
    .request<User>(config)
    .then((res) => {
      const token = Cookies.get("dex-token")!;
      sessionDispatch({
        type: "LOG_IN_ACTION",
        payload: { token, user: res.data, type: "LOGGED_IN" },
      });
    })
    .catch((err) => {
      console.error("Error in login", err);
      sessionDispatch({ type: "LOG_IN_ERROR" });
    });
}

export function logout(token: string, sessionDispatch: SessionDispatch) {
  const config: AxiosRequestConfig = {
    method: "POST",
    url: `${host}/api/v1/logout`,
    withCredentials: true,
  };

  sessionDispatch({ type: "UNINITIALIZE_SESSION" });

  return axios
    .request(config)
    .catch(console.error)
    .then(() => sessionDispatch({ type: "INVALIDATE_SESSION" }));
}

export function openLocallyStoredSession(sessionDispatch: SessionDispatch) {
  const dexToken = Cookies.get("dex-token");

  if (dexToken) {
    const config: AxiosRequestConfig = {
      method: "POST",
      url: `${host}/api/v1/login`,
      withCredentials: true,
    };

    axios
      .request<User>(config)
      .then((res) => {
        sessionDispatch({
          type: "LOG_IN_ACTION",
          payload: { token: dexToken, user: res.data, type: "LOGGED_IN" },
        });
        const picture = Cookies.get("picture");
        if (picture) {
          sessionDispatch({ type: "UPDATE_PICTURE", payload: picture });
        }
      })
      .catch((err: AxiosError) => {
        console.error("Error logging in", err);
        if (err.response?.status == 404) {
          Cookies.remove("dex-token");
          sessionDispatch({ type: "NO_LOG_IN" });
        } else {
          sessionDispatch({ type: "LOG_IN_ERROR" });
        }
      });
  } else {
    sessionDispatch({ type: "NO_LOG_IN" });
  }
}
