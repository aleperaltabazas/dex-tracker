import axios, { AxiosRequestConfig } from "axios";
import { clearSynchronizeQueue, resetTimeout } from "../actions/syncQueue";
import { host } from "../config";
import store from "../store";
import { GameTitle, PokedexType } from "../types/pokedex";
import { Sync, MarkPokemon } from "../types/sync";
import { Pokemon, UserDex } from "../types/user";

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
    fireSynchronize(syncQueue)
      .then(() => {
        store.dispatch(clearSynchronizeQueue());
      })
      .catch(() => {
        store.dispatch(resetTimeout());
      });
  }
}

export function fireSynchronize(syncQueue: Sync[]) {
  const curatedSyncQueue: Sync[] = [];
  const markPokemon: MarkPokemon[] = [];
  let newName: string | undefined;

  syncQueue.reverse().forEach((s) => {
    if (s.type == "MARK_POKEMON") {
      if (
        !curatedSyncQueue.some(
          (s2) => s2.type == "MARK_POKEMON" && s2.number == s.number
        )
      ) {
        curatedSyncQueue.push(s);
      }
    }

    if (s.type == "CHANGE_DEX_NAME" && newName == undefined) {
      newName = s.newName;
    }
  });

  let config: AxiosRequestConfig = {
    url: `${host}/api/v1/users/pokedex`,
    method: "PATCH",
    withCredentials: true,
    data: {
      pokemon: markPokemon.map((s) => ({
        pokedexId: s.dexId,
        dexNumber: s.number,
        caught: s.caught,
      })),
      newName: newName,
    },
  };

  return axios.request(config);
}

export async function fetchAllUsersDex(token: string) {
  let config: AxiosRequestConfig = {
    method: "GET",
    url: `${host}/api/v1/users/pokedex`,
    withCredentials: true,
  };

  return axios.request<Array<UserDex>>(config).then((res) => res.data);
}

export type Change = {
  number: number;
  caught: boolean;
};

export function applyChanges(changes: Change[]) {
  const curatedChanges: Change[] = [];
  changes.reverse().forEach((c) => {
    if (!curatedChanges.some((cc) => cc.number == c.number)) {
      curatedChanges.push(c);
    }
  });

  return (d: UserDex): UserDex => {
    const updatedMons: Pokemon[] = d.pokemon.map((p) => ({
      ...p,
      caught:
        curatedChanges.find((cc) => cc.number == p.dexNumber)?.caught !=
        undefined
          ? curatedChanges.find((cc) => cc.number == p.dexNumber)!.caught
          : p.caught,
    }));

    return {
      ...d,
      pokemon: updatedMons,
      caught: updatedMons.filter((p) => p.caught).length,
    };
  };
}
