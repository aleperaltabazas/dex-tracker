import axios, { AxiosRequestConfig } from "axios";
import { clearSynchronizeQueue, resetTimeout } from "../actions/syncQueue";
import { host } from "../config";
import store from "../store";
import { GameTitle, PokedexType } from "../types/pokedex";
import { Sync } from "../types/sync";
import { UserDex, UserDexRef } from "../types/user";

type CreateDex = {
  game: GameTitle;
  type: PokedexType;
  name?: string;
};

export async function createPokedex({ game, type, name }: CreateDex) {
  let config: AxiosRequestConfig = {
    url: `${host}/api/v1/users/pokedex`,
    method: "POST",
    withCredentials: true,
    data: {
      game: game,
      type: type,
      name: name,
    },
  };

  return axios.request<UserDex>(config).then((res) => res.data);
}

export async function synchronize(syncQueue: Sync[]) {
  if (syncQueue.length > 0) {
    let config: AxiosRequestConfig = {
      url: `${host}/api/v1/users/pokedex`,
      method: "PATCH",
      withCredentials: true,
      data: syncQueue.map((s) => ({
        pokedexId: s.dexId,
        dexNumber: s.number,
        caught: s.caught,
      })),
    };

    axios
      .request(config)
      .then(() => {
        store.dispatch(clearSynchronizeQueue());
      })
      .catch(() => {
        store.dispatch(resetTimeout());
      });
  }
}

export function toRef(dex: UserDex): UserDexRef {
  return {
    name: dex.name,
    game: dex.game,
    userDexId: dex.userDexId,
  };
}
