export type MarkPokemon = {
  number: number;
  caught: boolean;
  dexId: string;
  type: "MARK_POKEMON";
};

export type ChangeDexName = {
  newName: string;
  type: "CHANGE_DEX_NAME";
};

export type Sync = MarkPokemon | ChangeDexName;
