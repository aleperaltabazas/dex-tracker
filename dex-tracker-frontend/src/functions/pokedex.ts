import axios, { AxiosRequestConfig } from "axios";
import { host } from "../config";
import { Pokedex } from "../types/pokedex";

export function fetchPokedex(pokedexDispatch: React.Dispatch<any>) {
  const config: AxiosRequestConfig = {
    method: "GET",
    url: `${host}/api/v1/pokedex`,
  };

  axios
    .request<Array<Pokedex>>(config)
    .then((res) => res.data)
    .then((pokedex) => pokedexDispatch({ type: "UPDATE_POKEDEX", payload: pokedex }))
    .catch((err) => console.error("Error getting pokedex", err));
}
