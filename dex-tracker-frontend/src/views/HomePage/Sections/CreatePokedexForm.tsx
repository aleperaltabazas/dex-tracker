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
import Column from "../../../components/Column";
import Row from "../../../components/Row";
import {
  Game,
  GamePokedex,
  GameTitle,
  PokedexType,
} from "../../../types/pokedex";

type CreatePokedexFormProps = {
  pokedex: GamePokedex[];
  games: Game[];
};

const useStyles = makeStyles((theme: Theme) =>
  createStyles({
    form: {
      display: "flex",
      flexDirection: "column",
      margin: "auto",
    },
  })
);

const CreatePokedexForm = (props: CreatePokedexFormProps) => {
  const classes = useStyles();

  const [open, setOpen] = useState(false);
  const [game, setGame] = useState("gsc");
  const [type, setType] = useState<PokedexType>("REGIONAL");

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
            <Row spacing={2}>
              <Column md={6} xs={12}>
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
              <Column md={6} xs={12}>
                <div style={{ maxHeight: "120px" }}>
                  <div style={{ overflow: "auto" }}>
                    {props.pokedex
                      .find((p) => p.game.title == game && p.type == type)
                      ?.pokemon.map((p, idx) => (
                        <div key={idx}>
                          <span className="pl-3 pr-1">{p.number}</span>
                          <span className={`pokemon pokesprite ${p.name}`} />
                          <span>{p.name}</span>
                        </div>
                      ))}
                  </div>
                </div>
              </Column>
            </Row>
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
