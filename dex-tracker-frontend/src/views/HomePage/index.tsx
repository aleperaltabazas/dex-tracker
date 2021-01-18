import React from "react";
import { hot } from "react-hot-loader";
import Dex from "../../components/Dex";

type HomePageProps = {};

const HomePage = (props: HomePageProps) => {
  return (
    <div className="mt-5">
      <Dex
        dex={{
          type: "NATIONAL",
          region: "johto",
          game: "hgss",
          pokemon: [
            { dexNumber: 1, name: "bulbasaur", caught: true },
            { dexNumber: 2, name: "ivysaur", caught: false },
            { dexNumber: 3, name: "venusaur", caught: false },
            { dexNumber: 4, name: "charmander", caught: true },
            { dexNumber: 5, name: "charmeleon", caught: false },
            { dexNumber: 6, name: "charizard", caught: false },
            { dexNumber: 7, name: "squirtle", caught: true },
            { dexNumber: 8, name: "wartortle", caught: false },
            { dexNumber: 9, name: "blastoise", caught: true },
            { dexNumber: 10, name: "caterpie", caught: true },
            { dexNumber: 11, name: "metapod", caught: true },
            { dexNumber: 12, name: "butterfree", caught: false },
            { dexNumber: 13, name: "weedle", caught: false },
            { dexNumber: 14, name: "kakuna", caught: false },
            { dexNumber: 15, name: "beedrill", caught: false },
          ],
        }}
        gamePokedex={{
          type: "NATIONAL",
          region: "johto",
          pokemon: [],
          game: {
            title: "hgss",
            fullTitle: "HeartGold and SoulSilver",
            spritePokemon: "ho-oh",
          },
        }}
      />
    </div>
  );
};

export default hot(module)(HomePage);
