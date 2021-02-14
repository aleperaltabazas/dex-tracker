import {
  Divider,
  Hidden,
  Input,
  InputAdornment,
  Typography,
} from "@material-ui/core";
import React from "react";
import { hot } from "react-hot-loader";
import { GamePokedex } from "../types/pokedex";
import { UserDex } from "../types/user";
import useStyles from "./Dex/styles";
import classNames from "classnames";
import Row from "./Row";
import Column from "./Column";
import { Search } from "@material-ui/icons";
import PokemonRow from "./Dex/PokemonRow";

type DexV2Props = {
  dex: UserDex;
  gamePokedex: GamePokedex;
};

const DexV2 = (props: DexV2Props) => {
  const classes = useStyles();
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
            {props.dex.caught}/{props.dex.pokemon.length}
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
            fullWidth
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
          <Typography variant="button" style={{ fontSize: "14px" }}>
            Caught
          </Typography>
        </Column>
      </Row>
      <Divider />
      <Row className={classes.dexContainer}>
        <PokemonRow
          dexId={props.dex.userDexId}
          idx={0}
          firstRow
          pokemon={props.dex.pokemon[0]}
          incrementCounter={() => {}}
          decrementCounter={() => {}}
        />
        {props.dex.pokemon
          .slice(1)
          //   .filter(shouldRender)
          .map((p, idx) => (
            <PokemonRow
              dexId={props.dex.userDexId}
              key={idx}
              idx={idx + 1}
              firstRow={false}
              pokemon={p}
              incrementCounter={() => {}}
              decrementCounter={() => {}}
            />
          ))}
      </Row>
    </div>
  );
};

export default hot(module)(DexV2);
