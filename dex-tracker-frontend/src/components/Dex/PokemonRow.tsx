import { Checkbox, Hidden } from "@material-ui/core";
import React, { useCallback, useEffect, useState } from "react";
import { hot } from "react-hot-loader";
import Column from "../Column";
import Row from "../Row";
import classNames from "classnames";
import { Pokemon } from "../../types/user";
import useStyles from "./styles";
import store from "../../store";
import { addToSyncQueue } from "../../actions/syncQueue";
import { SessionState } from "../../store/session";
import { RootState } from "../../reducers";
import { connect } from "react-redux";
import { updateCaughtLocalPokedex } from "../../functions/storage";

type PokemonRowProps = {
  idx: number;
  firstRow: boolean;
  pokemon: Pokemon;
  dexId: string;
  session: SessionState;
  onChange: (b: boolean) => void;
};

const PokemonRow = (props: PokemonRowProps) => {
  const classes = useStyles();

  useEffect(() => {
    return () => {};
  }, []);

  const updateCaught = useCallback((_: any, caught: boolean) => {
    const counter = document.getElementById("counter");
    if (counter) {
      const current = Number.parseInt(counter.innerHTML);
      counter.innerHTML = (caught ? current + 1 : current - 1).toString();
    }

    props.onChange(caught);
    if (props.session.type == "LOGGED_IN") {
      store.dispatch(
        addToSyncQueue(props.pokemon.dexNumber, caught, props.dexId)
      );
    } else if (props.session.type == "NOT_LOGGED_IN") {
      updateCaughtLocalPokedex(props.dexId, props.pokemon.dexNumber, caught);
    }
  }, []);

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
        onClick={() => {}}
      >
        <Checkbox
          onChange={updateCaught}
          color="default"
          defaultChecked={props.pokemon.caught}
        />
      </Column>
    </Row>
  );
};

const mapStateToProps = (root: RootState) => ({ session: root.session });

export default hot(module)(connect(mapStateToProps)(PokemonRow));
