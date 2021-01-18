import { Hidden, makeStyles } from "@material-ui/core";
import React from "react";
import { hot } from "react-hot-loader";
import Column from "../Column";
import Row from "../Row";
import classNames from "classnames";
import { Pokemon } from "../../types/user";

type PokemonRowProps = {
  idx: number;
  firstRow: boolean;
  pokemon: Pokemon;
  updateCaught: (p: Pokemon) => void;
};

const useStyles = makeStyles((theme) => ({
  root: {
    width: "100%",
  },
  game: {
    fontSize: theme.typography.pxToRem(24),
  },
  accordionHeading: {
    fontSize: theme.typography.pxToRem(18),
  },
  secondaryHeading: {
    fontSize: theme.typography.pxToRem(18),
    color: theme.palette.text.secondary,
  },
  details: {
    display: "block",
  },
  listItem: {
    fontSize: "16px",
    height: "72px",
  },
  dexContainer: {
    overflow: "scroll",
    [theme.breakpoints.only("xs")]: {
      maxHeight: "320px",
    },
    [theme.breakpoints.only("sm")]: {
      maxHeight: "400px",
    },
    [theme.breakpoints.only("md")]: {
      maxHeight: "480px",
    },
    [theme.breakpoints.only("lg")]: {
      maxHeight: "560px",
    },
    [theme.breakpoints.only("xl")]: {
      maxHeight: "640px",
    },
  },
  rowLine: {
    borderTop: "solid 1px rgba(0, 0, 0, 0.12)",
  },
}));

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
