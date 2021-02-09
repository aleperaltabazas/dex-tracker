export type DexEntry = {
  name: string;
  number: number;
};

export type Game = {
  title: GameTitle;
  fullTitle: string;
  spritePokemon: string;
};

export type PokedexType = "NATIONAL" | "REGIONAL";

export type GamePokedex = {
  pokemon: DexEntry[];
  region: string;
  type: PokedexType;
  game: Game;
};

export type GameTitle =
  | "rby"
  | "gsc"
  | "rse"
  | "dp"
  | "pt"
  | "hgss"
  | "bw"
  | "b2w2";
