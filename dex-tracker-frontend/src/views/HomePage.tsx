import React from "react";
import { hot } from "react-hot-loader";
import { RootState } from "../reducers";
import { PokedexState } from "../store/pokedex";
import { SessionState } from "../store/session";
import { connect } from "react-redux";
import classNames from "classnames";
import { Container, makeStyles, Typography } from "@material-ui/core";
import { GamesState } from "../store/games";
import store from "../store";
import { openCreateDexForm } from "../actions/global";
import Loader from "../components/Loader";
import DexSummary from "../components/Dex/Summary";
import { UserDex } from "../types/user";
import { GamePokedex } from "../types/pokedex";

type HomePageProps = {
  pokedex: PokedexState;
  session: SessionState;
  games: GamesState;
};

const useStyles = makeStyles({
  noPokedexHeading: {
    fontSize: "24px",
    fontWeight: "bolder",
  },
  noPokedexSubtitle: {
    fontSize: "20px",
  },
});

const HomePage = (props: HomePageProps) => {
  const classes = useStyles();

  if (props.session.type == "ERROR") {
    return <div>se rompió algo perrito :(</div>;
  }

  if (
    props.session.type == "UNINITIALIZED" ||
    !props.pokedex.loaded ||
    !props.games.loaded
  ) {
    return (
      <div className="center h-100 w-100">
        <Loader />
      </div>
    );
  }

  const PokedexList = (props: {
    gamePokedex: GamePokedex[];
    dex: UserDex[];
  }) => (
    <div className="mt-5 h-100">
      <Container>
        {props.dex.length > 0 && (
          <>
            <Typography
              variant="h3"
              className="center-h"
              style={{ fontWeight: 500 }}
            >
              My games
            </Typography>
            {props.dex.map((p) => (
              <DexSummary
                dex={p}
                gamePokedex={
                  props.gamePokedex.find((d) => d.game.title == p.game.title)!
                }
                key={p.userDexId}
              />
            ))}
          </>
        )}
        {props.dex.length == 0 && (
          <div>
            <div className={classNames("center-h", classes.noPokedexHeading)}>
              It seems like you don't have a Pokedex yet
            </div>
            <div className={classNames("center-h", classes.noPokedexSubtitle)}>
              Click on the pokedex below to create one!
            </div>
            <div className="w-100 center-h">
              <img
                className="p-1 cursor-pointer center-h"
                src="https://cdn.bulbagarden.net/upload/9/9f/Key_Pok%C3%A9dex_m_Sprite.png"
                onClick={() => store.dispatch(openCreateDexForm())}
              />
            </div>
          </div>
        )}
      </Container>
    </div>
  );

  if (props.session.type == "LOGGED_IN") {
    return (
      <PokedexList
        dex={props.session.user.pokedex}
        gamePokedex={props.pokedex.pokedex}
      />
    );
  }

  return (
    <PokedexList
      dex={props.session.localDex}
      gamePokedex={props.pokedex.pokedex}
    />
  );
};

const mapStateToProps = (root: RootState) => ({
  session: root.session,
  pokedex: root.pokedex,
  games: root.games,
});

export default hot(module)(connect(mapStateToProps)(HomePage));
