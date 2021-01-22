import {
  Button,
  createStyles,
  Dialog,
  DialogActions,
  DialogContent,
  DialogTitle,
  FormControl,
  InputLabel,
  makeStyles,
  MenuItem,
  Select,
  Theme,
} from "@material-ui/core";
import React, { useState } from "react";
import { hot } from "react-hot-loader";
import Column from "../../components/Column";
import Row from "../../components/Row";
import { Game, GamePokedex, GameTitle, PokedexType } from "../../types/pokedex";
import classNames from "classnames";
import Loader from "react-loader-spinner";
import { createPokedex } from "../../functions/my-dex";
import { addUserDex } from "../../actions/session";
import store from "../../store";
import { RootState } from "../../reducers";
import { connect } from "react-redux";
import { closeCreateDexForm } from "../../actions/global";

type CreatePokedexFormProps = {
  pokedex: GamePokedex[];
  games: Game[];
  open: boolean;
};

const useStyles = makeStyles((theme: Theme) =>
  createStyles({
    form: {
      display: "flex",
      flexDirection: "column",
      margin: "auto",
    },
    formRows: {
      height: "200px",
    },
    overflowScrollMd: {
      [theme.breakpoints.up("md")]: {
        overflow: "scroll",
      },
    },
    buttons: {
      fontWeight: "bolder",
      [theme.breakpoints.down("sm")]: {
        display: "flex",
        justifyContent: "space-around",
      },
    },
    create: {
      color: "#1976d2",
      fontWeight: "bolder",
    },
    close: {
      color: "#d70852",
      fontWeight: "bolder",
    },
    overflowAuto: {
      overflowY: "auto",
    },
    pokemonColumn: {
      maxHeight: "120px",
    },
  })
);

const CreatePokedexForm = (props: CreatePokedexFormProps) => {
  const classes = useStyles();

  const [game, setGame] = useState<GameTitle>("gsc");
  const [type, setType] = useState<PokedexType>("REGIONAL");
  const [loading, setLoading] = useState(false);

  return (
    <React.Fragment>
      <Dialog
        fullWidth
        maxWidth="md"
        open={props.open}
        onClose={() => store.dispatch(closeCreateDexForm())}
        aria-labelledby="max-width-dialog-title"
      >
        <DialogTitle id="max-width-dialog-title">
          Create a new pokedex
        </DialogTitle>
        <DialogContent>
          {loading && (
            <Loader
              type="Puff"
              color="#00BFFF"
              height={100}
              width={100}
              className="center"
            />
          )}
          {!loading && (
            <form className={classes.form} noValidate>
              <Row spacing={2} className={classes.formRows}>
                <Column md={6} xs={12} className="center-v">
                  <Row spacing={2}>
                    <Column md={6} xs={12}>
                      <FormControl fullWidth>
                        <InputLabel>Choose the game</InputLabel>

                        <Select
                          fullWidth
                          onChange={(e) => setGame(e.target.value as GameTitle)}
                          value={game}
                        >
                          {props.games.map((g, idx) => (
                            <MenuItem key={idx} value={g.title}>
                              {g.fullTitle}
                            </MenuItem>
                          ))}
                        </Select>
                      </FormControl>
                    </Column>
                    <Column md={6} xs={12}>
                      <FormControl fullWidth>
                        <InputLabel>Choose the pokedex type</InputLabel>
                        <Select
                          fullWidth
                          onChange={(e, t) =>
                            setType(e.target.value as PokedexType)
                          }
                          value={type}
                        >
                          <MenuItem value={"NATIONAL"}>National</MenuItem>
                          <MenuItem value={"REGIONAL"}>Regional</MenuItem>
                        </Select>
                      </FormControl>
                    </Column>
                  </Row>
                </Column>
                <Column md={6} xs={12} className={classes.overflowAuto}>
                  <div className={classes.pokemonColumn}>
                    <div className={classes.overflowScrollMd}>
                      {props.pokedex
                        .find((p) => p.game.title == game && p.type == type)
                        ?.pokemon.map((p, idx) => (
                          <div key={idx}>
                            <span className="pl-1 pl-md-3 pr-md-1">
                              {p.number}
                            </span>
                            <span className={`pokemon pokesprite ${p.name}`} />
                            <span className="capitalize">{p.name}</span>
                          </div>
                        ))}
                    </div>
                  </div>
                </Column>
              </Row>
            </form>
          )}
        </DialogContent>
        <DialogActions className={classes.buttons}>
          <Button
            onClick={() => store.dispatch(closeCreateDexForm())}
            color="secondary"
            className={classNames(classes.close)}
          >
            Close
          </Button>
          <Button
            onClick={() => {
              setLoading(true);
              createPokedex(game, type)
                .then(addUserDex)
                .then(store.dispatch)
                .then(() => {
                  setLoading(false);
                  store.dispatch(closeCreateDexForm());
                })
                .catch(console.log)
                .then(() => {
                  setLoading(false);
                });
            }}
            color="primary"
            className={classNames(classes.create)}
          >
            Create Dex
          </Button>
        </DialogActions>
      </Dialog>
    </React.Fragment>
  );
};

const mapStateToProps = (root: RootState) => ({
  games: root.games.loaded ? root.games.games : [],
  pokedex: root.pokedex.loaded ? root.pokedex.pokedex : [],
  open: root.global.createDexFormOpen,
});

export default hot(module)(connect(mapStateToProps)(CreatePokedexForm));
