import axios, { AxiosRequestConfig } from "axios";
import { loadGames } from "../actions/games";
import { host } from "../config";
import store from "../store";
import { Game } from "../types/pokedex";

export function fetchGames() {
  const config: AxiosRequestConfig = {
    method: "GET",
    url: `${host}/api/v1/games`,
  };

  axios
    .request<Game[]>(config)
    .then((res) => res.data)
    .then(loadGames)
    .then(store.dispatch)
    .catch((err) => console.error("Error getting pokedex", err));
}
