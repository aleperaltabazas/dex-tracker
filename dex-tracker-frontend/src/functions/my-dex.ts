import axios, { AxiosRequestConfig } from "axios";
import { clearSynchronizeQueue, resetTimeout } from "../actions/syncQueue";
import { host } from "../config";
import store from "../store";
import { SessionState } from "../store/session";
import { DexUpdate, Sync } from "../types/sync";
import { Pokemon, UserDex } from "../types/user";

type CreateDex = {
  game: string;
  name?: string;
};

export async function createPokedex(
  { game, name }: CreateDex,
  session: SessionState
): Promise<UserDex> {
  switch (session.type) {
    case "LOGGED_IN": {
      let config: AxiosRequestConfig = {
        url: `${host}/api/v1/users/${session.user.userId}/pokedex`,
        method: "POST",
        withCredentials: true,
        data: {
          game: game,
          name: name,
        },
      };

      return axios.request<UserDex>(config).then((res) => res.data);
    }
    default:
      return Promise.reject(
        new Error(`Illegal session state: ${session.type}`)
      );
  }
}

export async function synchronize(userId: string, syncQueue: Array<Sync>) {
  if (syncQueue.length > 0) {
    fireSynchronize(userId, syncQueue)
      .then(() => {
        store.dispatch(clearSynchronizeQueue());
      })
      .catch(() => {
        store.dispatch(resetTimeout());
      });
  }
}

export function fireSynchronize(userId: string, syncQueue: Array<Sync>) {
  const dex: any = {};
  syncQueue.forEach((u) => {
    dex[u.dexId] = {
      caught: u.caught,
      name: u.name,
    };
  });

  let config: AxiosRequestConfig = {
    url: `${host}/api/v1/users/${userId}`,
    method: "PATCH",
    withCredentials: true,
    data: {
      dex: dex,
    },
  };

  return axios.request(config);
}

export type Change = {
  number: number;
  caught: boolean;
};

export function applyChanges(changes: Array<number>) {
  return (d: UserDex): UserDex => {
    const updatedMons: Pokemon[] = d.pokemon.map((p) => ({
      ...p,
      caught: changes.includes(p.dexNumber),
    }));

    return {
      ...d,
      pokemon: updatedMons,
      caught: updatedMons.filter((p) => p.caught).length,
    };
  };
}
