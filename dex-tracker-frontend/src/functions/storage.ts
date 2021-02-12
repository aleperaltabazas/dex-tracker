import { UserDex } from "../types/user";

const LOCAL_DEX = "local_dex";

export function readLocalPokedex(): UserDex[] {
  const localDex = localStorage.getItem(LOCAL_DEX);

  return localDex ? JSON.parse(localDex) : [];
}

export function addLocalPokedex(dex: UserDex) {
  const localDex = readLocalPokedex();
  writeLocalPokedex(localDex.concat(dex));
}

export function writeLocalPokedex(dex: UserDex[]) {
  localStorage.setItem(LOCAL_DEX, JSON.stringify(dex));
}

export function clearLocalPokedex() {
  localStorage.removeItem(LOCAL_DEX);
}

export function updateCaughtLocalPokedex(
  dexId: string,
  dexNumber: number,
  caught: boolean
) {
  const localDex = readLocalPokedex();
  writeLocalPokedex(
    localDex.map((d) => {
      if (d.userDexId == dexId) {
        return {
          ...d,
          pokemon: d.pokemon.map((p) => {
            if (p.dexNumber == dexNumber) {
              return {
                ...p,
                caught: caught,
              };
            } else {
              return p;
            }
          }),
        };
      } else {
        return d;
      }
    })
  );
}
