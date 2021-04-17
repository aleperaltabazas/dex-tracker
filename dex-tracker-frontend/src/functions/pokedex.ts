import axios, { AxiosRequestConfig } from "axios";
import { loadPokedex } from "../actions/pokedex";
import { host } from "../config";
import store from "../store";
import { Pokedex } from "../types/pokedex";

export function fetchPokedex() {
  const config: AxiosRequestConfig = {
    method: "GET",
    url: `${host}/api/v1/pokedex`,
  };

  axios
    .request<Array<Pokedex>>(config)
    .then((res) => res.data)
    .then(loadPokedex)
    .then(store.dispatch)
    .catch((err) => console.error("Error getting pokedex", err));
}
