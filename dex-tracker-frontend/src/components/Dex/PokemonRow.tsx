import { Hidden } from "@material-ui/core";
import React, { useState } from "react";
import { hot } from "react-hot-loader";
import Column from "../Column";
import Row from "../Row";
import classNames from "classnames";
import { Pokemon } from "../../types/user";
import useStyles from "./styles";
import store from "../../store";
import { addToSyncQueue } from "../../actions/syncQueue";
import { incrementCaught, decrementCaught } from "../../actions/session";

type PokemonRowProps = {
  idx: number;
  firstRow: boolean;
  pokemon: Pokemon;
  dexId: string;
  incrementCounter: () => void;
  decrementCounter: () => void;
};

const PokemonRow = (props: PokemonRowProps) => {
  const classes = useStyles();
  const [caught, setCaught] = useState(props.pokemon.caught);

  const updateCaught = () => {
    const newCaught = !caught;
    setCaught(newCaught);

    if (newCaught) {
      props.incrementCounter();
    } else {
      props.decrementCounter();
    }

    store.dispatch(
      addToSyncQueue(props.pokemon.dexNumber, newCaught, props.dexId)
    );
    store.dispatch(
      newCaught ? incrementCaught(props.dexId) : decrementCaught(props.dexId)
    );
  };

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
        onClick={() => updateCaught()}
      >
        {caught ? (
          <span className="pokesprite ball poke" />
        ) : (
          <span className="pokesprite ball poke gray-scale" />
        )}
      </Column>
    </Row>
  );
};

export default hot(module)(PokemonRow);
