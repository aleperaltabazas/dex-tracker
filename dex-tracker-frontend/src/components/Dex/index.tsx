import {
  Accordion,
  AccordionDetails,
  AccordionSummary,
  Divider,
  Grid,
  Hidden,
  Input,
  InputAdornment,
  makeStyles,
  Typography,
} from "@material-ui/core";
import React, { useState } from "react";
import { hot } from "react-hot-loader";
import ExpandMoreIcon from "@material-ui/icons/ExpandMore";
import classNames from "classnames";
import { Search } from "@material-ui/icons";
import "./styles.scss";
import Column from "../Column";
import Row from "../Row";
import store from "../../store";
import { addToSyncQueue } from "../../actions/syncQueue";
import { Pokemon, UserDex } from "../../types/user";
import { GamePokedex } from "../../types/pokedex";
import PokemonRow from "./PokemonRow";
import useStyles from "./styles";

type DexProps = {
  dex: UserDex;
  gamePokedex: GamePokedex;
};

const Dex = (props: DexProps) => {
  const classes = useStyles();
  const [search, setSearch] = useState<string | undefined>(undefined);
  const [expanded, setExpanded] = useState(false);

  const handleSearchChange = (event: React.ChangeEvent<HTMLInputElement>) =>
    setSearch(event.currentTarget.value);

  const shouldRender = (p: Pokemon) =>
    search == undefined ||
    p.name.toLowerCase().includes(search.toLowerCase()) ||
    p.dexNumber.toString().includes(search);

  const [pokemons, setPokemons] = useState(props.dex.pokemon);

  const updateCaught = (pokemon: Pokemon) => {
    setPokemons(
      pokemons.map((p) =>
        p.dexNumber == pokemon.dexNumber ? { ...p, caught: !p.caught } : p
      )
    );
    store.dispatch(addToSyncQueue(pokemon.dexNumber, !pokemon.caught));
  };

  return (
    <div className={classes.root}>
      <div
        className={classNames(
          "bold",
          "uppercase",
          "center-v",
          "pb-1",
          classes.game
        )}
      >
        <span
          className={`pokemon pokesprite ${props.gamePokedex.game.spritePokemon} pt-2`}
        />
        <span style={{ paddingBottom: "3px" }}>
          {props.gamePokedex.game.title}
        </span>
      </div>
      <Accordion
        expanded={expanded}
        onChange={(_, isExpanded) => setExpanded(isExpanded)}
      >
        <AccordionSummary expandIcon={<ExpandMoreIcon />}>
          <Row className={classNames("ml-2", "mr-2", expanded ? "mt-2" : "")}>
            <Column xs={4}>
              <div>
                <div
                  className={classNames(
                    classes.accordionHeading,
                    "bold",
                    "capitalize"
                  )}
                >
                  {props.dex.region}
                </div>
              </div>
            </Column>
            <Column xs={4}>
              <Typography
                className={classNames(classes.secondaryHeading, "capitalize")}
              >
                {props.dex.type}
              </Typography>
            </Column>
            <Column xs={4} className="center">
              <Typography className={classes.secondaryHeading}>
                {props.dex.pokemon.filter((p) => p.caught).length}/
                {pokemons.length}
              </Typography>
            </Column>
          </Row>
        </AccordionSummary>
        <AccordionDetails
          className={classNames(classes.details, "ml-1 mr-1 ml-md-2 mr-md-2")}
        >
          <Row>
            <Hidden smDown>
              <Column xs={3} md={1} className="center" />
            </Hidden>
            <Hidden smDown>
              <Column
                md={1}
                className={classNames("center", "bold", classes.listItem)}
              >
                Number
              </Column>
            </Hidden>
            <Column
              xs={9}
              md={8}
              className={classNames("center-v", "bold", classes.listItem)}
            >
              <Input
                value={search}
                fullWidth
                onChange={handleSearchChange}
                placeholder="Luxray"
                endAdornment={
                  <InputAdornment position="end">
                    <Search />
                  </InputAdornment>
                }
              />
            </Column>
            <Column
              xs={3}
              md={1}
              className={classNames("center", "bold", classes.listItem)}
            >
              <span className="pokesprite ball poke" />
            </Column>
          </Row>
          <Divider />
          <Row className={classes.dexContainer}>
            {shouldRender(pokemons[0]) && (
              <PokemonRow
                idx={0}
                firstRow
                pokemon={pokemons[0]}
                updateCaught={updateCaught}
              />
            )}
            {pokemons
              .slice(1)
              .filter(shouldRender)
              .map((p, idx) => (
                <PokemonRow
                  idx={idx + 1}
                  firstRow={false}
                  pokemon={p}
                  updateCaught={updateCaught}
                />
              ))}
            {/* {shouldRender(pokemons[0]) && PokemonRow(true)(pokemons[0], 0)}
            {pokemons.slice(1).filter(shouldRender).map(PokemonRow(false))} */}
          </Row>
        </AccordionDetails>
      </Accordion>
    </div>
  );
};

export default hot(module)(Dex);
