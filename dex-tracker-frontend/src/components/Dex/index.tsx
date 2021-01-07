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
  OutlinedInput,
  Typography,
} from "@material-ui/core";
import React, { useState } from "react";
import { hot } from "react-hot-loader";
import ExpandMoreIcon from "@material-ui/icons/ExpandMore";
import className from "classnames";
import { Dex, Pokemon } from "../../types";
import { Search } from "@material-ui/icons";
import "./styles.scss";
import Column from "../Column";
import Row from "../Row";

type DexProps = {
  dex: Dex;
};

const useStyles = makeStyles((theme) => ({
  root: {
    width: "100%",
  },
  game: {
    fontSize: theme.typography.pxToRem(24),
  },
  accordionHeading: {
    fontSize: theme.typography.pxToRem(15),
  },
  secondaryHeading: {
    fontSize: theme.typography.pxToRem(15),
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

const Dex = (props: DexProps) => {
  const classes = useStyles();
  const [search, setSearch] = useState<string | undefined>(undefined);

  const handleSearchChange = (event: React.ChangeEvent<HTMLInputElement>) =>
    setSearch(event.currentTarget.value);

  const shouldRender = (p: Pokemon) =>
    search == undefined ||
    p.name.toLowerCase().includes(search.toLowerCase()) ||
    p.number.toString().includes(search);

  const [pokemons, setPokemons] = useState(props.dex.pokemons);

  const updateCaught = (dexNumber: number) =>
    setPokemons(
      pokemons.map((p) =>
        p.number == dexNumber ? { ...p, captured: !p.captured } : p
      )
    );

  const PokemonRow = (firstRow: boolean) => (pokemon: Pokemon, idx: number) => (
    <Row key={idx} className={firstRow ? "" : classes.rowLine}>
      <Grid
        item
        xs={3}
        md={1}
        className={className("center-h", classes.listItem)}
        key={`${idx}-sprite`}
      >
        <span className={`pokesprite pokemon ${pokemon.name}`} />
      </Grid>
      <Hidden smDown>
        <Grid
          item
          md={1}
          className={className("center", classes.listItem)}
          key={`${idx}-number`}
        >
          {pokemon.number}
        </Grid>
      </Hidden>
      <Grid
        item
        xs={6}
        md={8}
        className={className("center-v", "capitalize", classes.listItem)}
        key={`${idx}-poke`}
      >
        {pokemon.name}
      </Grid>
      <Column
        item
        xs={3}
        md={1}
        className={className("center", classes.listItem)}
        key={`${idx}-caught`}
        onClick={() => updateCaught(pokemon.number)}
      >
        {pokemon.captured ? (
          <span className="pokesprite ball poke" />
        ) : (
          <span className="pokesprite ball poke gray-scale" />
        )}
      </Column>
    </Row>
  );

  return (
    <div className={classes.root}>
      <div
        className={className(
          "bold",
          "uppercase",
          "center-v",
          "pb-1",
          classes.game
        )}
      >
        <span
          className={`pokemon pokesprite ${props.dex.game.boxArtPokemon} pt-2`}
        />
        <span style={{ paddingBottom: "3px" }}>{props.dex.game.title}</span>
      </div>
      <Accordion defaultExpanded={false}>
        <AccordionSummary expandIcon={<ExpandMoreIcon />}>
          <Row className="ml-2 mr-2">
            <Column xs={4}>
              <div>
                <div
                  className={className(
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
                className={className(classes.secondaryHeading, "capitalize")}
              >
                {props.dex.type}
              </Typography>
            </Column>
            <Column xs={4} className="center">
              <Typography className={classes.secondaryHeading}>
                {pokemons.filter((p) => p.captured).length}/{pokemons.length}
              </Typography>
            </Column>
          </Row>
        </AccordionSummary>
        <AccordionDetails
          className={className(classes.details, "ml-1 mr-1 ml-md-2 mr-md-2")}
        >
          <Row>
            <Hidden smDown>
              <Column xs={3} md={1} className="center" />
            </Hidden>
            <Hidden smDown>
              <Column
                md={1}
                className={className("center", "bold", classes.listItem)}
              >
                Number
              </Column>
            </Hidden>
            <Column
              xs={9}
              md={8}
              className={className("center-v", "bold", classes.listItem)}
            >
              <Input
                value={search}
                fullWidth
                onChange={handleSearchChange}
                placeholder="Luxray"
                startAdornment={
                  <InputAdornment position="start">
                    <Search />
                  </InputAdornment>
                }
              />
            </Column>
            <Column
              xs={3}
              md={1}
              className={className("center", "bold", classes.listItem)}
            >
              <span className="pokesprite ball poke" />
            </Column>
          </Row>
          <Divider />
          <Row className={classes.dexContainer}>
            {shouldRender(pokemons[0]) && PokemonRow(true)(pokemons[0], 0)}
            {pokemons.slice(1).filter(shouldRender).map(PokemonRow(false))}
          </Row>
        </AccordionDetails>
      </Accordion>
    </div>
  );
};

export default hot(module)(Dex);
