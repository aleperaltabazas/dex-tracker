import axios, { AxiosRequestConfig } from "axios";
import { loadPokedex } from "../actions/pokedex";
import { host } from "../config";
import store from "../store";
import { GamePokedex } from "../types/pokedex";

export function fetchGamesPokedex() {
  const config: AxiosRequestConfig = {
    method: "GET",
    url: `${host}/api/v1/pokedex`,
  };

  axios
    .request<GamePokedex[]>(config)
    .then((res) => res.data)
    .then(loadPokedex)
    .then(store.dispatch)
    .catch((err) => console.error("Error getting pokedex", err));
}
