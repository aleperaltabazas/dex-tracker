export type Pokemon = {
  name: string;
  number: number;
  captured: boolean;
};

export type Dex = {
  pokemons: Pokemon[];
  region: string;
  game: string;
};
