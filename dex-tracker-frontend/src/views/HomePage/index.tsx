import React, { useEffect } from "react";
import { hot } from "react-hot-loader";
import Dex from "../../components/Dex";
import { RootState } from "../../reducers";
import { PokedexState } from "../../store/pokedex";
import { SessionState } from "../../store/session";
import Cookies from "js-cookie";
import { connect } from "react-redux";
import store from "../../store";
import { updateSessionState } from "../../actions/session";
import { createUser, login } from "../../functions/login";
import { fetchGamesPokedex } from "../../functions/poedex";

type HomePageProps = {
  pokedex: PokedexState;
  session: SessionState;
};

const HomePage = (props: HomePageProps) => {
  useEffect(() => {
    const dexToken = Cookies.get("dex-token");
    console.log("checking for dex-token", dexToken);

    if (dexToken) {
      login(dexToken)
        .then((res) => {
          console.log(res);
          return res;
        })
        .then((res) => {
          console.error("Logged in user");
          store.dispatch(updateSessionState(dexToken, res.data));
        })
        .catch((err) => console.error("Error logging in", err));
    } else {
      createUser()
        .then((res) => {
          console.log("Created user", Cookies.get("dex-token"));
          store.dispatch(
            updateSessionState(Cookies.get("dex-token")!, res.data)
          );
        })
        .catch((err) => console.error("Error creating the user", err));
    }

    fetchGamesPokedex();
  }, []);

  if (!props.session.isLoggedIn) {
    return <div>Cargando perrote</div>;
  }

  if (!props.pokedex.loaded) {
    return <div>Cargando perrote</div>;
  }

  const gamesPokedex = props.pokedex.pokedex;

  return (
    <div className="mt-5">
      {props.session.user.pokedex.map((p, idx) => {
        const dex = gamesPokedex.find((d) => d.game.title == p.game)!;
        return <Dex dex={p} gamePokedex={dex} key={idx} />;
      })}
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

const mapStateToProps = (root: RootState) => ({
  session: root.session,
  pokedex: root.pokedex,
});

export default hot(module)(connect(mapStateToProps)(HomePage));
