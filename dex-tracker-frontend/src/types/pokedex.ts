export type Form = {
  name: string;
};

export type DexEntry = {
  name: string;
  number: number;
  forms: Form[];
};

export type Game = {
  title: GameTitle;
  fullTitle: string;
  spritePokemon: string;
};

export type GamePokedex = {
  pokemon: DexEntry[];
  region: string;
  type: "NATIONAL" | "REGIONAL";
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