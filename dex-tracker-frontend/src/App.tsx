import React, { useEffect } from "react";
import { hot } from "react-hot-loader";
import Header from "./components/Header";
import "./styles.scss";
import Menu from "./components/Menu";
import CreatePokedexForm from "./components/CreatePokedexForm";
import { BrowserRouter as Router, Route, Switch } from "react-router-dom";
import HomePage from "./views/HomePage";
import DexPage from "./views/DexPage";
import { openLocallyStoredSession } from "./functions/login";
import { fetchPokedex } from "./functions/pokedex";
import { fireSynchronize } from "./functions/my-dex";
import store from "./store";
import UserPage from "./views/UserPage";
import Footer from "./components/Footer";
import sprite from "./components/Sprite";
import "./icons.scss";

const App = () => {
  useEffect(() => {
    openLocallyStoredSession();
    fetchPokedex();

    window.addEventListener("beforeunload", (e) => {
      const session = store.getState().session;

      if (session.type == "LOGGED_IN") {
        const sync = store.getState().syncQueue;

        if (sync.queue.length != 0 && sync.timeout != undefined) {
          clearTimeout(sync.timeout);
          fireSynchronize(session.user.userId, sync.queue);
        }
      }
    });
  }, []);

  return (
    <Router>
      <Header />
      <Menu />
      <Switch>
        <Route
          path="/users/:userId/dex/:dexId"
          component={DexPage}
          key={window.location.pathname}
        />
        <Route
          path="/users/:userId"
          component={UserPage}
          key={window.location.pathname}
        />
        <Route path="/" exact component={HomePage} />
      </Switch>
      <CreatePokedexForm />
    </Router>
  );
};

export default hot(module)(App);
