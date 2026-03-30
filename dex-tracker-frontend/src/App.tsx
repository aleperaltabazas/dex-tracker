import React, { useEffect } from "react";
import Header from "./components/Header";
import "./styles.scss";
import Menu from "./components/Menu";
import CreatePokedexForm from "./components/CreatePokedexForm";
import { BrowserRouter as Router, Route, Routes } from "react-router-dom";
import HomePage from "./views/HomePage";
import DexPage from "./views/DexPage";
import { openLocallyStoredSession } from "./functions/login";
import { fetchPokedex } from "./functions/pokedex";
import { fireSynchronize } from "./functions/my-dex";
import UserPage from "./views/UserPage";
import Footer from "./components/Footer";
import sprite from "./components/Sprite";
import "./icons.scss";
import { useSession } from "./contexts/session";
import { usePokedex } from "./contexts/pokedex";
import { useSyncQueue } from "./contexts/syncQueue";

const App = () => {
  const { session, dispatch: sessionDispatch } = useSession();
  const { dispatch: pokedexDispatch } = usePokedex();
  const { syncQueue } = useSyncQueue();

  useEffect(() => {
    openLocallyStoredSession(sessionDispatch);
    fetchPokedex(pokedexDispatch);

    const handleBeforeUnload = () => {
      if (session.type === "LOGGED_IN") {
        const { queue, timeout } = syncQueue;
        if (queue.length !== 0 && timeout !== undefined) {
          clearTimeout(timeout);
          fireSynchronize(session.user.userId, queue);
        }
      }
    };

    window.addEventListener("beforeunload", handleBeforeUnload);
    return () => window.removeEventListener("beforeunload", handleBeforeUnload);
  }, []);

  return (
    <Router>
      <Header />
      <Menu />
      <Routes>
        <Route path="/users/:userId/dex/:dexId" element={<DexPage />} />
        <Route path="/users/:userId" element={<UserPage />} />
        <Route path="/" element={<HomePage />} />
      </Routes>
      <CreatePokedexForm />
    </Router>
  );
};

export default App;
