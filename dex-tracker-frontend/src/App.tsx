import React from "react";
import { hot } from "react-hot-loader";
import Footer from "./components/Footer";
import Header from "./components/Header";
import "./styles.scss";
import "pokesprite-spritesheet/assets/pokesprite-inventory.css";
import "pokesprite-spritesheet/assets/pokesprite-pokemon-gen8.css";
import { Container } from "@material-ui/core";
import HomePage from "./views/HomePage";
import Menu from "./components/Menu";
import CreatePokedexForm from "./components/CreatePokedexForm";

const App = () => {
  return (
    <div>
      <Menu />
      <Header />
      <Container>
        <HomePage />
      </Container>
      <Footer />
      <CreatePokedexForm />
    </div>
  );
};

export default hot(module)(App);
