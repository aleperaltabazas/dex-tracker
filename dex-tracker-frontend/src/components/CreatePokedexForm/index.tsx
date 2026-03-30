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
} from "@mui/material";
import React, { useState } from "react";
import Column from "../../components/Column";
import Row from "../../components/Row";
import { Pokedex } from "../../types/pokedex";
import classNames from "classnames";
import { createPokedex } from "../../functions/my-dex";
import { useNavigate } from "react-router-dom";
import Loader from "../Loader";
import useStyles from "./styles";
import { LoggedInState } from "../../contexts/session";
import sprite from "../Sprite";
import { useSession } from "../../contexts/session";
import { usePokedex } from "../../contexts/pokedex";
import { useGlobal } from "../../contexts/global";

const CreatePokedexForm = () => {
  const classes = useStyles();
  const navigate = useNavigate();
  const { session, dispatch: sessionDispatch } = useSession();
  const { pokedex: pokedexState } = usePokedex();
  const { global, dispatch: globalDispatch } = useGlobal();

  const [game, setGame] = useState<string>("gsc-national");
  const [name, setName] = useState<string | undefined>(undefined);
  const [loading, setLoading] = useState(false);

  const pokedex: Pokedex[] = pokedexState.loaded ? pokedexState.pokedex : [];
  const dex = pokedex.find((p) => p.name == game);

  return (
    <React.Fragment>
      <Dialog
        fullWidth
        maxWidth="md"
        open={global.createDexFormOpen}
        onClose={() => globalDispatch({ type: "UPDATE_CREATE_DEX_FORM_OPEN", payload: false })}
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
                    <Column xs={12}>
                      <FormControl fullWidth>
                        <InputLabel>Pokedex</InputLabel>
                        <Select
                          fullWidth
                          onChange={(e) => setGame(e.target.value as string)}
                          value={game}
                        >
                          {pokedex.map((g, idx) => (
                            <MenuItem key={idx} value={g.name}>
                              {g.displayName}
                            </MenuItem>
                          ))}
                        </Select>
                      </FormControl>
                    </Column>
                    <Column xs={12}>
                      <div className="mt-1 mt-md-2">
                        <div className={classes.namingPrimary}>
                          Give a name to your dex for easier identification
                        </div>
                        <div className={classes.namingSecondary}>
                          You can change it later, if you want to
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
                      {dex?.entries.map((p) => (
                        <div key={p.number}>
                          <span className="pl-1 pl-md-3 pr-md-1">
                            {p.number}
                          </span>
                          <sprite.icon pokemon={p.name} gen={dex?.gen} />
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
        <Divider />
        <DialogActions className={classes.buttons}>
          <Button
            onClick={() => globalDispatch({ type: "UPDATE_CREATE_DEX_FORM_OPEN", payload: false })}
            color="secondary"
            className={classNames(classes.close)}
          >
            Close
          </Button>
          <Button
            onClick={() => {
              setLoading(true);
              createPokedex({ game, name }, session)
                .then((newDex) => {
                  navigate(
                    `/users/${(session as LoggedInState).user.userId}/dex/${newDex.userDexId}`
                  );
                  return newDex;
                })
                .then((newDex) => {
                  sessionDispatch({ type: "ADD_USER_DEX", payload: newDex });
                  setLoading(false);
                  globalDispatch({ type: "UPDATE_CREATE_DEX_FORM_OPEN", payload: false });
                })
                .catch((err) => {
                  console.error(err);
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

export default CreatePokedexForm;
