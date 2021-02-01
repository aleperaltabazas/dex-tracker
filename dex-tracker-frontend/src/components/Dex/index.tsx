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
import { RootState } from "../../reducers";
import { connect } from "react-redux";
import { Sync } from "../../types/sync";

type DexProps = {
  dex: UserDex;
  gamePokedex: GamePokedex;
  unsynched: Sync[];
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

  const [pokemon] = useState(props.dex.pokemon);

  function mergeWithUnsynched(p: Pokemon): Pokemon {
    const unsynched = props.unsynched.find(
      (s) => s.dexId == props.dex.userDexId && s.number == p.dexNumber
    );
    console.log(p.name, unsynched);
    return {
      ...p,
      caught: unsynched ? unsynched.caught : p.caught,
    };
  }

  const [caughtCounter, setCaughtCounter] = useState(
    props.dex.pokemon.map(mergeWithUnsynched).filter((p) => p.caught).length
  );

  const incrementCounter = () => setCaughtCounter(caughtCounter + 1);
  const decrementCounter = () => setCaughtCounter(caughtCounter - 1);

  return (
    <div className={classes.root}>
      <Typography variant="h5">
        <div className={classNames("bold", "center-v", "pb-1")}>
          <span
            className={`pokemon pokesprite ${props.gamePokedex.game.spritePokemon} pt-1`}
          />
          <span style={{ paddingBottom: "3px" }}>
            {props.dex.name || props.gamePokedex.game.fullTitle}
          </span>
        </div>
      </Typography>
      <Row className={classNames("ml-2", "mr-2")}>
        <Column xs={7} md={8}>
          <div>
            <Typography
              variant="h6"
              color="textSecondary"
              className={classNames("capitalize")}
            >
              {props.dex.type.toLowerCase()}
            </Typography>
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
            <Typography variant="button" style={{ fontSize: "14px" }}>
              Number
            </Typography>
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
            pokemon={mergeWithUnsynched(pokemon[0])}
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
              pokemon={mergeWithUnsynched(p)}
              incrementCounter={incrementCounter}
              decrementCounter={decrementCounter}
            />
          ))}
      </Row>
    </div>
  );
};

const mapStateToProps = (root: RootState) => ({
  unsynched: root.syncQueue.queue,
});

export default hot(module)(connect(mapStateToProps)(Dex));
