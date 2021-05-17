import React, { useState } from "react";
import { hot } from "react-hot-loader";
import { RootState } from "../reducers";
import { PokedexState } from "../store/pokedex";
import { SessionState } from "../store/session";
import { connect } from "react-redux";
import classNames from "classnames";
import {
  Button,
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
import GitHubIcon from "@material-ui/icons/GitHub";
import sprite from "../components/Sprite";

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
  github: {
    [theme.breakpoints.down("sm")]: {
      fontSize: "16px",
      fontWeight: "400",
    },
  },
  githubIcon: {
    [theme.breakpoints.up("md")]: {
      fontSize: "120px",
    },
    [theme.breakpoints.down("sm")]: {
      fontSize: "80px",
    },
    color: "black",
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
        <>
          <Typography
            variant="h3"
            className="center-h"
            style={{ fontWeight: 500 }}
          >
            My games
          </Typography>
          {props.dex.length > 0 ? (
            props.dex.map((p) => (
              <div className="mt-3 mb-3">
                <DexSummary userId={props.userId} dex={p} key={p.userDexId} />
              </div>
            ))
          ) : (
            <>
              <div
                className={classNames(
                  "center-h",
                  classes.noPokedexSubtitle,
                  "mt-3"
                )}
              >
                Not even a nibble...
              </div>

              <div className="center-h mt-1">
                <Button
                  color="primary"
                  onClick={() => store.dispatch(openCreateDexForm())}
                  variant="contained"
                >
                  Create your first Pokedex
                </Button>
              </div>
            </>
          )}
        </>
      </Container>
    </div>
  );

  if (props.session.type == "NONE") {
    const selectedPreviewPokedex = props.pokedex.pokedex.find(
      (d) => d.name == previewDex
    )!;
    const pokemon = selectedPreviewPokedex.entries.map((p) => ({
      dexNumber: p.number,
      name: p.name,
      caught: false,
    }));

    return (
      <div className="mt-5 text-align-center">
        <Container>
          <div className="mt-3">
            <Typography variant="h4">
              <div className="bold uppercase">Dex Tracker</div>
            </Typography>
          </div>
          <div className="mt-1">
            <Typography
              variant="h4"
              className={classNames(classes.subtitle, "mt-3")}
            >
              <div className="text-align-center">An online Pokedex tracker</div>
            </Typography>
          </div>
        </Container>
        <div className="bg-white pt-3 pb-3 mt-3">
          <Container maxWidth="xl">
            <Row spacing={5}>
              <Column xs={12} md={4}>
                <div className="pl-lg-5">
                  <Typography variant="h6">
                    Record your progress across the different regions and
                    generations in one place
                  </Typography>
                  <div className="text-align-left mt-3">
                    <ul>
                      <li>
                        Choose a Pokedex from gen 1 to gen 5, both national and
                        regional
                      </li>
                      <li className="mt-1">
                        Or choose a form-dex, if you are collecting pokemon like
                        Unown, or Alcremie
                      </li>
                      <li className="mt-1">
                        Quickly find the Pokemon you have (and those you don't)
                      </li>
                      <li className="mt-1">
                        Check where to find or how to evolve the ones you're
                        missing
                      </li>
                      <li className="mt-1">
                        Share it online to ease your trades
                      </li>
                    </ul>
                  </div>
                </div>
              </Column>
              <Column xs={12} md={8}>
                <div className="pr-md-5">
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
                    gen={selectedPreviewPokedex.gen}
                    desktopHeight={240}
                    mobileHeight={240}
                    handleChange={() => {}}
                    pokemon={pokemon}
                  />
                </div>
              </Column>
            </Row>
          </Container>
        </div>
        <div className="mt-3">
          <Container maxWidth="lg">
            <Row spacing={2}>
              <Column xs={12} md={1}>
                <a
                  href="https://github.com/aleperaltabazas/dex-tracker"
                  target="_blank"
                >
                  <GitHubIcon className={classNames(classes.githubIcon)} />
                </a>
              </Column>
              <Column xs={12} md={10}>
                <Typography
                  variant="h6"
                  className={classNames(classes.github, "center-v", "h-100")}
                >
                  Dex Tracker is an open source project. Feel free to report any
                  issue you come across when using the website, requesting a
                  missing feature, or contributing to the project
                </Typography>
              </Column>
            </Row>
          </Container>
        </div>
      </div>
    );
  }

  return (
    <div className="text-align-center pl-1 pr-1">
      <Typography
        variant="h5"
        className="center pt-3 pb-3"
        style={{ fontWeight: "bold" }}
      >
        FireRead and LeafGreen
      </Typography>
      <Row spacing={1}>
        <Column xs={6}>
          <Typography variant="h6">My Pokedex</Typography>
          <Row spacing={1}>
            <Column xs={12}>
              <div style={{ backgroundColor: "#14d92b" }} className="center-h">
                <sprite.icon gen={3} pokemon="bulbasaur" />
                <span className="center">Bulbasaur</span>
              </div>
            </Column>
            <Column xs={12}>
              <div style={{ backgroundColor: "#14d92b" }} className="center-h">
                <sprite.icon gen={3} pokemon="ivysaur" />
                <span className="center">Ivysaur</span>
              </div>
            </Column>
            <Column xs={12}>
              <div style={{ backgroundColor: "white" }} className="center-h">
                <sprite.icon gen={3} pokemon="venusaur" />
                <span className="center">Venusaur</span>
              </div>
            </Column>
            <Column xs={12}>
              <div style={{ backgroundColor: "#14d92b" }} className="center-h">
                <sprite.icon gen={3} pokemon="charmander" />
                <span className="center">Charmander</span>
              </div>
            </Column>
            <Column xs={12}>
              <div style={{ backgroundColor: "white" }} className="center-h">
                <sprite.icon gen={3} pokemon="charmeleon" />
                <span className="center">Charmeleon</span>
              </div>
            </Column>
          </Row>
        </Column>
        <Column xs={6}>
          <Typography variant="h6">Ulises's pokedex</Typography>
          <Row spacing={1}>
            <Column xs={12}>
              <div style={{ backgroundColor: "#14d92b" }} className="center-h">
                <sprite.icon gen={3} pokemon="bulbasaur" />
                <span className="center">Bulbasaur</span>
              </div>
            </Column>
            <Column xs={12}>
              <div style={{ backgroundColor: "white" }} className="center-h">
                <sprite.icon gen={3} pokemon="ivysaur" />
                <span className="center">Ivysaur</span>
              </div>
            </Column>
            <Column xs={12}>
              <div style={{ backgroundColor: "#14d92b" }} className="center-h">
                <sprite.icon gen={3} pokemon="venusaur" />
                <span className="center">Venusaur</span>
              </div>
            </Column>
            <Column xs={12}>
              <div style={{ backgroundColor: "#14d92b" }} className="center-h">
                <sprite.icon gen={3} pokemon="charmander" />
                <span className="center">Charmander</span>
              </div>
            </Column>
            <Column xs={12}>
              <div style={{ backgroundColor: "white" }} className="center-h">
                <sprite.icon gen={3} pokemon="charmeleon" />
                <span className="center">Charmeleon</span>
              </div>
            </Column>
          </Row>
        </Column>
      </Row>
    </div>
  );
  // return (
  //   <PokedexList
  //     userId={props.session.user.userId}
  //     dex={props.session.user.pokedex}
  //     gamePokedex={props.pokedex.pokedex}
  //   />
  // );
};

const mapStateToProps = (root: RootState) => ({
  session: root.session,
  pokedex: root.pokedex,
});

export default hot(module)(connect(mapStateToProps)(HomePage));
