export type Game = {
  title: GameTitle;
  fullTitle: string;
  spritePokemon: string;
};

export type PokedexType = "NATIONAL" | "REGIONAL";

export type GamePokedex = {
  pokemon: string[];
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
