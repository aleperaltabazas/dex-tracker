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
import { TokenResponse } from "@react-oauth/google";

export async function oauthLogin(tokenResponse: TokenResponse) {
  store.dispatch(uninitialize());

  try {
    // Fetch user profile from Google userinfo endpoint using the access token
    const userInfo = await axios.get<{ email: string; picture: string }>(
      "https://www.googleapis.com/oauth2/v3/userinfo",
      { headers: { Authorization: `Bearer ${tokenResponse.access_token}` } }
    );

    const { email, picture } = userInfo.data;

    const config: AxiosRequestConfig = {
      method: "POST",
      url: `${host}/api/v1/login`,
      withCredentials: true,
      data: {
        mail: email,
        googleToken: tokenResponse.access_token,
      },
    };

    const res = await axios.request<User>(config);
    const user = res.data;

    dispatchUser({ ...user, pokedex: user.pokedex });
    store.dispatch(updatePicture(picture));
    Cookies.set("picture", picture, { expires: 3600000 });
  } catch (err) {
    console.error("Error in login", err);
    store.dispatch(loginError());
  }
}

export function logout(token: string) {
  const config: AxiosRequestConfig = {
    method: "POST",
    url: `${host}/api/v1/logout`,
    withCredentials: true,
  };

  store.dispatch(uninitialize());

  return axios
    .request(config)
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
    const config: AxiosRequestConfig = {
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
