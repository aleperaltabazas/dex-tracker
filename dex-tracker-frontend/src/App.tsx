import React from "react";
import { hot } from "react-hot-loader";
import Footer from "./components/Footer";
import Header from "./components/Header";
import "./styles.scss";
import "pokesprite-spritesheet/assets/pokesprite-inventory.css";
import "pokesprite-spritesheet/assets/pokesprite-pokemon-gen8.css";
import { Container } from "@material-ui/core";
import Dex from "./components/Dex";

const App = () => {
  return (
    <div>
      <Header />
      <Container>
        <Dex
          dex={{
            region: "Johto",
            game: "HGSS",
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
      </Container>
      <Footer />
    </div>
  );
};

export default hot(module)(App);
