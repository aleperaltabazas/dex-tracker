export type Pokemon = {
  name: string;
  number: number;
  captured: boolean;
};

export type Dex = {
  pokemons: Pokemon[];
  region: string;
  game: Game;
  type: "regional" | "national";
};

export type GameTitle =
  | "rg"
  | "rb"
  | "y"
  | "gs"
  | "c"
  | "rs"
  | "e"
  | "dp"
  | "pt"
  | "hgss"
  | "bw"
  | "b2w2"
  | "xy"
  | "oras"
  | "sm"
  | "usum";

interface Game {
  title: GameTitle;
  fullTitle: string;
  boxArtPokemon: string;
}
