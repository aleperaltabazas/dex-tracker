import {
  Button,
  createStyles,
  Dialog,
  DialogActions,
  DialogContent,
  DialogContentText,
  DialogTitle,
  FormControl,
  InputLabel,
  makeStyles,
  MenuItem,
  Select,
  Switch,
  Theme,
} from "@material-ui/core";
import React, { useState } from "react";
import { hot } from "react-hot-loader";
import { GamePokedex } from "../../../types/pokedex";

type CreatePokedexFormProps = {
  games: GamePokedex[];
};

const useStyles = makeStyles((theme: Theme) =>
  createStyles({
    form: {
      display: "flex",
      flexDirection: "column",
      margin: "auto",
    },
    formControl: {
      marginTop: theme.spacing(2),
      minWidth: 120,
    },
    formControlLabel: {
      marginTop: theme.spacing(1),
    },
  })
);

const CreatePokedexForm = (props: CreatePokedexFormProps) => {
  const classes = useStyles();

  const [open, setOpen] = useState(false);

  return (
    <React.Fragment>
      <div className="center-h">
        <img
          className="p-1 cursor-pointer"
          src="https://cdn.bulbagarden.net/upload/9/9f/Key_Pok%C3%A9dex_m_Sprite.png"
          onClick={() => setOpen(true)}
        />
      </div>
      <Dialog
        fullWidth
        maxWidth="md"
        open={open}
        onClose={() => setOpen(false)}
        aria-labelledby="max-width-dialog-title"
      >
        <DialogTitle id="max-width-dialog-title">
          Create a new pokedex
        </DialogTitle>
        <DialogContent>
          <form className={classes.form} noValidate>
            <FormControl className={classes.formControl}>
              <InputLabel htmlFor="max-width">Choose the game</InputLabel>
              <Select onChange={console.log}>
                {props.games.map((g, idx) => (
                  <MenuItem key={idx} value={g.game.title}>
                    {g.game.fullTitle}
                  </MenuItem>
                ))}
              </Select>
            </FormControl>
          </form>
        </DialogContent>
        <DialogActions>
          <Button onClick={() => setOpen(false)} color="primary">
            Close
          </Button>
        </DialogActions>
      </Dialog>
    </React.Fragment>
  );
};

export default hot(module)(CreatePokedexForm);
