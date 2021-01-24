import React, { useEffect } from "react";
import { hot } from "react-hot-loader";
import Footer from "./components/Footer";
import Header from "./components/Header";
import "./styles.scss";
import "pokesprite-spritesheet/assets/pokesprite-inventory.css";
import "pokesprite-spritesheet/assets/pokesprite-pokemon-gen8.css";
import Menu from "./components/Menu";
import CreatePokedexForm from "./components/CreatePokedexForm";
import { BrowserRouter as Router, Route, Switch } from "react-router-dom";
import HomePage from "./views/HomePage";
import DexPage from "./views/DexPage";
import { openLocallyStoredSession } from "./functions/login";
import { fetchGamesPokedex } from "./functions/pokedex";
import { fetchGames } from "./functions/games";
import { fireSynchronize } from "./functions/my-dex";
import store from "./store";
import { RootState } from "./reducers";

const App = () => {
  useEffect(() => {
    openLocallyStoredSession();
    fetchGamesPokedex();
    fetchGames();

    window.addEventListener("beforeunload", (e) => {
      const sync = store.getState().syncQueue;

      if (sync.queue.length != 0 && sync.timeout != undefined) {
        clearTimeout(sync.timeout);
        fireSynchronize(sync.queue);
      }
    });
  }, []);
  return (
    <Router>
      <Header />
      <Menu />
      <Switch>
        <Route path="/dex/:id" component={DexPage} />
        <Route path="/" exact component={HomePage} />
      </Switch>
      <CreatePokedexForm />
      <Footer />
    </Router>
  );
};

export default hot(module)(App);
