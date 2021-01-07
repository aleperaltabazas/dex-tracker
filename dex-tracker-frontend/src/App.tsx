import React from "react";
import { hot } from "react-hot-loader";
import Footer from "./components/Footer";
import Header from "./components/Header";
import "./styles.scss";
import "pokesprite-spritesheet/assets/pokesprite-inventory.css";
import "pokesprite-spritesheet/assets/pokesprite-pokemon-gen8.css";
import { Container } from "@material-ui/core";
import HomePage from "./views/HomePage";

const App = () => {
  return (
    <div>
      <Header />
      <Container>
        <HomePage />
      </Container>
      <Footer />
    </div>
  );
};

export default hot(module)(App);
