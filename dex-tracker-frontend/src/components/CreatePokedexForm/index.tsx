import {
  Button,
  Dialog,
  DialogActions,
  DialogContent,
  DialogTitle,
  Divider,
  FormControl,
  InputLabel,
  MenuItem,
  Select,
  TextField,
} from "@material-ui/core";
import React, { useState } from "react";
import { hot } from "react-hot-loader";
import Column from "../../components/Column";
import Row from "../../components/Row";
import { Game, GamePokedex, GameTitle, PokedexType } from "../../types/pokedex";
import classNames from "classnames";
import { createPokedex } from "../../functions/my-dex";
import { addUserDex } from "../../actions/session";
import store from "../../store";
import { RootState } from "../../reducers";
import { connect } from "react-redux";
import { closeCreateDexForm } from "../../actions/global";
import { RouteComponentProps, withRouter } from "react-router";
import Loader from "../Loader";
import useStyles from "./styles";
import { addLocalPokedex } from "../../functions/storage";
import { SessionState } from "../../store/session";

interface CreatePokedexFormProps extends RouteComponentProps {
  pokedex: GamePokedex[];
  games: Game[];
  open: boolean;
  session: SessionState;
}

const CreatePokedexForm = (props: CreatePokedexFormProps) => {
  const classes = useStyles();

  const [game, setGame] = useState<GameTitle>("gsc");
  const [type, setType] = useState<PokedexType>("REGIONAL");
  const [name, setName] = useState<string | undefined>(undefined);
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
        <Divider />
        <DialogContent>
          {loading && <Loader />}
          {!loading && (
            <form className={classes.form} noValidate>
              <Row spacing={2}>
                <Column md={6} xs={12} className="center-v">
                  <Row spacing={2}>
                    <Column md={6} xs={12}>
                      <FormControl fullWidth>
                        <InputLabel>Game</InputLabel>
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
                        <InputLabel>Pokedex type</InputLabel>
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
                    <Column xs={12}>
                      <div className="mt-1 mt-md-2">
                        <div className={classes.namingPrimary}>
                          You can give your Pokedex a name as well!
                        </div>
                        <div className={classes.namingSecondary}>
                          Useful if you have multiple Pokedex for the same game
                        </div>
                      </div>
                      <FormControl fullWidth>
                        <TextField
                          label="Name"
                          fullWidth
                          onChange={(e) => setName(e.target.value)}
                          value={name}
                        />
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
                              {idx + 1}
                            </span>
                            <span className={`pokemon pokesprite ${p}`} />
                            <span className="capitalize">{p}</span>
                          </div>
                        ))}
                    </div>
                  </div>
                </Column>
              </Row>
            </form>
          )}
        </DialogContent>
        <Divider />
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
              createPokedex({ game, type, name })
                .then((dex) => {
                  if (props.session.type == "NOT_LOGGED_IN") {
                    addLocalPokedex(dex);
                  }
                  props.history.push(`/dex/${dex.userDexId}`);
                  return dex;
                })
                .then(addUserDex)
                .then(store.dispatch)
                .then(() => {
                  setLoading(false);
                  store.dispatch(closeCreateDexForm());
                })
                .catch(console.error)
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
  session: root.session,
});

export default hot(module)(
  connect(mapStateToProps)(withRouter(CreatePokedexForm))
);
