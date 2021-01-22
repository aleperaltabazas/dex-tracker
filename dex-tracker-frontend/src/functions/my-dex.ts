import axios, { AxiosRequestConfig } from "axios";
import { clearSynchronizeQueue } from "../actions/syncQueue";
import { host } from "../config";
import store from "../store";
import { GameTitle, PokedexType } from "../types/pokedex";
import { Sync } from "../types/sync";
import { UserDex, UserDexRef } from "../types/user";

export async function createPokedex(game: GameTitle, type: PokedexType) {
  let config: AxiosRequestConfig = {
    url: `${host}/api/v1/users/pokedex`,
    method: "POST",
    withCredentials: true,
    data: {
      game: game,
      type: type,
    },
  };

  return axios.request<UserDex>(config).then((res) => res.data);
}

export async function synchronize(syncQueue: Sync[]) {
  console.log("sincronizado perro:");
  console.log(syncQueue);
  store.dispatch(clearSynchronizeQueue());
}

export function toRef(dex: UserDex): UserDexRef {
  return {
    name: dex.name,
    game: dex.game,
    userDexId: dex.userDexId,
  };
}
