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
              { number: 1, name: "bulbasaur", captured: false },
              { number: 1, name: "bulbasaur", captured: false },
              { number: 1, name: "bulbasaur", captured: false },
              { number: 1, name: "bulbasaur", captured: false },
              { number: 1, name: "bulbasaur", captured: false },
              { number: 1, name: "bulbasaur", captured: false },
              { number: 1, name: "bulbasaur", captured: false },
              { number: 1, name: "bulbasaur", captured: false },
              { number: 1, name: "bulbasaur", captured: false },
              { number: 1, name: "bulbasaur", captured: false },
            ],
          }}
        />
      </Container>
      <Footer />
    </div>
  );
};

export default hot(module)(App);
