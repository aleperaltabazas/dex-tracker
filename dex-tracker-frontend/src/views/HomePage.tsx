import React, { useState } from "react";
import { hot } from "react-hot-loader";
import { RootState } from "../reducers";
import { PokedexState } from "../store/pokedex";
import { SessionState } from "../store/session";
import { connect } from "react-redux";
import classNames from "classnames";
import {
  Container,
  InputLabel,
  makeStyles,
  MenuItem,
  Select,
  Typography,
} from "@material-ui/core";
import store from "../store";
import { openCreateDexForm } from "../actions/global";
import Loader from "../components/Loader";
import DexSummary from "../components/Dex/Summary";
import { UserDex } from "../types/user";
import { Pokedex } from "../types/pokedex";
import Row from "../components/Row";
import Column from "../components/Column";
import DexGrid from "../components/Dex/DexGrid";

type HomePageProps = {
  pokedex: PokedexState;
  session: SessionState;
};

const useStyles = makeStyles((theme) => ({
  noPokedexHeading: {
    fontSize: "24px",
    fontWeight: "bolder",
  },
  noPokedexSubtitle: {
    fontSize: "20px",
  },
  subtitle: {
    [theme.breakpoints.down("sm")]: {
      fontSize: "14px",
    },
    [theme.breakpoints.up("md")]: {
      fontSize: "20px",
    },
    color: "#787878",
  },
}));

const HomePage = (props: HomePageProps) => {
  const classes = useStyles();

  const [previewDex, setPreviewDex] = useState("gsc-regional");

  if (props.session.type == "ERROR") {
    return <div>se rompió algo perrito :(</div>;
  }

  if (props.session.type == "UNINITIALIZED" || !props.pokedex.loaded) {
    return (
      <div className="center h-100 w-100">
        <Loader />
      </div>
    );
  }

  const PokedexList = (props: {
    gamePokedex: Pokedex[];
    dex: UserDex[];
    userId: string;
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
              <div className="mt-3 mb-3">
                <DexSummary userId={props.userId} dex={p} key={p.userDexId} />
              </div>
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

  if (props.session.type == "NONE") {
    const pokemon = props.pokedex.pokedex
      .find((d) => d.name == previewDex)!
      .entries.map((p) => ({
        dexNumber: p.number,
        name: p.name,
        caught: false,
      }));

    return (
      <div className="mt-5 text-align-center">
        <Container>
          <div className="center">
            <img
              src="https://cdn.bulbagarden.net/upload/9/9f/Key_Pok%C3%A9dex_m_Sprite.png"
              style={{
                borderRadius: "50%",
                height: "128px",
                width: "128px",
                background: "white",
              }}
            />
          </div>
          <div className="mt-3">
            <Typography variant="h4">
              <div className="bold">Dex Tracker</div>
            </Typography>
          </div>
          <div className="mt-1">
            <Typography
              variant="h4"
              className={classNames(classes.subtitle, "mt-3")}
            >
              <div className="text-align-center">
                Record your progress across the different regions and
                generations in one place!
              </div>
            </Typography>
          </div>
        </Container>
        <div className="bg-white pt-3 pb-3 mt-3">
          <Container maxWidth="xl">
            <Row>
              <Column xs={12} md={4}>
                <div className="pl-md-5 pr-md-5">
                  <Typography variant="h5">
                    Track your captures from different games
                  </Typography>
                  <div className="text-align-left mt-3">
                    <ul>
                      <li>
                        Choose a Pokedex from gen 1 to gen 5, both national and
                        regional
                      </li>
                      <li>
                        Or choose a form-dex, if you are collecting pokemon like
                        Unown, or Alcremie
                      </li>
                      <li>
                        Quickly find the Pokemon you have (and those you don't)
                      </li>
                      <li>Check more info about those you're missing</li>
                      <li>Share it online to ease your trades!</li>
                    </ul>
                  </div>
                </div>
              </Column>
              <Column xs={12} md={8}>
                <div className="pl-md-5 pr-md-5">
                  <div className="center">
                    <Select
                      value={previewDex}
                      onChange={(e) => setPreviewDex(e.target.value as string)}
                    >
                      {props.pokedex.pokedex.map((d) => (
                        <MenuItem key={d.name} value={d.name}>
                          {d.displayName}
                        </MenuItem>
                      ))}
                    </Select>
                  </div>
                  <DexGrid
                    desktopHeight={360}
                    mobileHeight={160}
                    handleChange={() => {}}
                    pokemon={pokemon}
                  />
                </div>
              </Column>
            </Row>
          </Container>
        </div>
      </div>
    );
  }

  return (
    <PokedexList
      userId={props.session.user.userId}
      dex={props.session.user.pokedex}
      gamePokedex={props.pokedex.pokedex}
    />
  );
};

const mapStateToProps = (root: RootState) => ({
  session: root.session,
  pokedex: root.pokedex,
});

export default hot(module)(connect(mapStateToProps)(HomePage));
