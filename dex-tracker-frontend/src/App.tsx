import React from "react";
import { hot } from "react-hot-loader";
import Footer from "./components/Footer";
import Header from "./components/Header";
import "./styles.scss";
import "pokesprite-spritesheet/assets/pokesprite-inventory.css";
import "pokesprite-spritesheet/assets/pokesprite-pokemon-gen8.css";
import { Container } from "@material-ui/core";
import Menu from "./components/Menu";
import CreatePokedexForm from "./components/CreatePokedexForm";
import { BrowserRouter as Router, Route, Switch } from "react-router-dom";
import HomePage from "./views/HomePage";
import DexPage from "./views/DexPage";

const App = () => {
  return (
    <div>
      <Header />
      <Container>
        <Router>
          <Menu />
          <Switch>
            <Route path="/dex/:id" component={DexPage} />
            <Route path="/" exact component={HomePage} />
          </Switch>
          <CreatePokedexForm />
        </Router>
      </Container>
      <Footer />
    </div>
  );
};

export default hot(module)(App);
