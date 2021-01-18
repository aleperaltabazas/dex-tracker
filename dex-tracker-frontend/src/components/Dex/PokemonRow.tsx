import { Hidden } from "@material-ui/core";
import React from "react";
import { hot } from "react-hot-loader";
import Column from "../Column";
import Row from "../Row";
import classNames from "classnames";
import { Pokemon } from "../../types/user";
import useStyles from "./styles";

type PokemonRowProps = {
  idx: number;
  firstRow: boolean;
  pokemon: Pokemon;
  updateCaught: (p: Pokemon) => void;
};

const PokemonRow = (props: PokemonRowProps) => {
  const classes = useStyles();
  return (
    <Row key={props.idx} className={props.firstRow ? "" : classes.rowLine}>
      <Column
        item
        xs={3}
        md={1}
        className={classNames("center-h", classes.listItem)}
        key={`${props.idx}-sprite`}
      >
        <span className={`pokesprite pokemon ${props.pokemon.name}`} />
      </Column>
      <Hidden smDown>
        <Column
          item
          md={1}
          className={classNames("center", classes.listItem)}
          key={`${props.idx}-number`}
        >
          {props.pokemon.dexNumber}
        </Column>
      </Hidden>
      <Column
        item
        xs={6}
        md={8}
        className={classNames("center-v", "capitalize", classes.listItem)}
        key={`${props.idx}-poke`}
      >
        {props.pokemon.name}
      </Column>
      <Column
        item
        xs={3}
        md={1}
        className={classNames("center", classes.listItem)}
        key={`${props.idx}-caught`}
        onClick={() => props.updateCaught(props.pokemon)}
      >
        {props.pokemon.caught ? (
          <span className="pokesprite ball poke" />
        ) : (
          <span className="pokesprite ball poke gray-scale" />
        )}
      </Column>
    </Row>
  );
};

export default hot(module)(PokemonRow);
