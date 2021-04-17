export type Game = {
  name: string;
  displayName: string;
};

export type PokedexType = "NATIONAL" | "REGIONAL";

export type Pokedex = {
  name: string;
  displayName: string;
  region: string;
  type: PokedexType;
  gen: number;
  entries: Array<DexEntry>;
};

export type DexEntry = {
  name: string;
  number: number;
};
