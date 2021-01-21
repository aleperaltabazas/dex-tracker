import React, { useEffect } from "react";
import { hot } from "react-hot-loader";
import Dex from "../../components/Dex";
import { RootState } from "../../reducers";
import { PokedexState } from "../../store/pokedex";
import { SessionState } from "../../store/session";
import { connect } from "react-redux";
import { openLocallyStoredSession } from "../../functions/login";
import { fetchGamesPokedex } from "../../functions/poedex";
import classNames from "classnames";
import { makeStyles } from "@material-ui/core";
import CreatePokedexForm from "./Sections/CreatePokedexForm";

type HomePageProps = {
  pokedex: PokedexState;
  session: SessionState;
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
  useEffect(() => {
    openLocallyStoredSession();
    fetchGamesPokedex();
  }, []);

  if (props.session.isError) {
    return <div>se rompió algo perrito :(</div>;
  }

  if (!props.session.isLoggedIn) {
    return <div>Cargando perrote</div>;
  }

  if (!props.pokedex.loaded) {
    return <div>Cargando perrote</div>;
  }

  const gamesPokedex = props.pokedex.pokedex;

  return (
    <div className="mt-5">
      {props.session.user.pokedex.length > 0 &&
        props.session.user.pokedex.map((p, idx) => {
          const dex = gamesPokedex.find((d) => d.game.title == p.game)!;
          return <Dex dex={p} gamePokedex={dex} key={idx} />;
        })}
      {props.session.user.pokedex.length == 0 && (
        <div>
          <div className={classNames("center-h", classes.noPokedexHeading)}>
            It seems like you don't have a Pokedex yet
          </div>
          <div className={classNames("center-h", classes.noPokedexSubtitle)}>
            Click on the pokedex below to create one!
          </div>
        </div>
      )}
      <CreatePokedexForm games={gamesPokedex} />
    </div>
  );
};

const mapStateToProps = (root: RootState) => ({
  session: root.session,
  pokedex: root.pokedex,
});

export default hot(module)(connect(mapStateToProps)(HomePage));
