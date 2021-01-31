import {
  Divider,
  Hidden,
  Input,
  InputAdornment,
  Typography,
} from "@material-ui/core";
import React, { useState } from "react";
import { hot } from "react-hot-loader";
import classNames from "classnames";
import { Search } from "@material-ui/icons";
import "./styles.scss";
import Column from "../Column";
import Row from "../Row";
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

  const handleSearchChange = (event: React.ChangeEvent<HTMLInputElement>) =>
    setSearch(event.currentTarget.value);

  const shouldRender = (p: Pokemon) =>
    search == undefined ||
    p.name.toLowerCase().includes(search.toLowerCase()) ||
    p.dexNumber.toString().includes(search);

  const [pokemon, setPokemon] = useState(props.dex.pokemon);

  const [caughtCounter, setCaughtCounter] = useState(
    props.dex.pokemon.filter((p) => p.caught).length
  );

  const incrementCounter = () => setCaughtCounter(caughtCounter + 1);
  const decrementCounter = () => setCaughtCounter(caughtCounter - 1);

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
          {props.dex.name || props.gamePokedex.game.fullTitle}
        </span>
      </div>
      <Row className={classNames("ml-2", "mr-2")}>
        <Column xs={7} md={8}>
          <div>
            <div
              className={classNames(
                classes.accordionHeading,
                "bold",
                "capitalize"
              )}
            >
              {props.dex.type.toLowerCase()}
            </div>
          </div>
        </Column>
        <Column xs={5} md={4} className="center">
          <Typography
            className={classNames(classes.secondaryHeading, "pr-1 pr-md-0")}
          >
            {caughtCounter}/{pokemon.length}
          </Typography>
        </Column>
      </Row>
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
          className={classNames(
            "center-v",
            "bold",
            "pl-3",
            "pl-md-0",
            classes.listItem
          )}
        >
          <Input
            value={search}
            fullWidth
            onChange={handleSearchChange}
            placeholder="Bulbasaur"
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
        {shouldRender(pokemon[0]) && (
          <PokemonRow
            dexId={props.dex.userDexId}
            idx={0}
            firstRow
            pokemon={pokemon[0]}
            incrementCounter={incrementCounter}
            decrementCounter={decrementCounter}
          />
        )}
        {pokemon
          .slice(1)
          .filter(shouldRender)
          .map((p, idx) => (
            <PokemonRow
              dexId={props.dex.userDexId}
              key={idx}
              idx={idx + 1}
              firstRow={false}
              pokemon={p}
              incrementCounter={incrementCounter}
              decrementCounter={decrementCounter}
            />
          ))}
      </Row>
    </div>
  );
};

export default hot(module)(Dex);
