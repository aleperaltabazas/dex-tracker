import React from "react";
import { hot } from "react-hot-loader";
import Dex from "../../components/Dex";

type HomePageProps = {};

const HomePage = (props: HomePageProps) => (
  <div className="mt-5">
    <Dex
      dex={{
        type: "national",
        region: "Johto",
        game: {
          title: "hgss",
          fullTitle: "HeartGold & SoulSilver",
          boxArtPokemon: "ho-oh",
        },
        pokemons: [
          { number: 1, name: "bulbasaur", captured: false },
          { number: 2, name: "ivysaur", captured: false },
          { number: 3, name: "venusaur", captured: false },
          { number: 4, name: "charmander", captured: false },
          { number: 5, name: "charmeleon", captured: false },
          { number: 6, name: "charizard", captured: false },
          { number: 7, name: "squirtle", captured: false },
          { number: 8, name: "wartortle", captured: false },
          { number: 9, name: "blastoise", captured: false },
          { number: 10, name: "caterpie", captured: false },
          { number: 11, name: "metapod", captured: false },
          { number: 12, name: "butterfree", captured: false },
          { number: 13, name: "weedle", captured: false },
          { number: 14, name: "kakuna", captured: false },
          { number: 15, name: "beedrill", captured: false },
        ],
      }}
    />
  </div>
);

export default hot(module)(HomePage);
