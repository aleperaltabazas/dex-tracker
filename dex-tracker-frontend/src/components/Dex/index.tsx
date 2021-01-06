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
  heading: {
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
      <Accordion defaultExpanded>
        <AccordionSummary expandIcon={<ExpandMoreIcon />}>
          <Row>
            <Column xs={4}>
              <div className="ml-2">
                <div className={className(classes.heading, "bold")}>
                  {props.dex.game}
                </div>
              </div>
            </Column>
            <Column xs={4}>
              <Typography className={classes.secondaryHeading}>
                Regional
              </Typography>
            </Column>
          </Row>
        </AccordionSummary>
        <AccordionDetails
          className={className(classes.details, "ml-1 mr-1 ml-md-2 mr-md-2")}
        >
          <Row>
            <Hidden smDown>
              <Column xs={12} md={12} className="pb-2">
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
            </Hidden>
            <Hidden mdUp>
              <Column
                xs={12}
                md={12}
                className="pb-2 pl-1 pr-1 pl-md-2 pr-md-2"
              >
                <OutlinedInput
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
            </Hidden>
            <Column xs={3} md={1} className="center" />
            <Hidden smDown>
              <Column
                md={1}
                className={className("center", "bold", classes.listItem)}
              >
                Number
              </Column>
            </Hidden>
            <Column
              xs={6}
              md={8}
              className={className("center-v", "bold", classes.listItem)}
            >
              Pokemon
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
            {shouldRender(props.dex.pokemons[0]) &&
              PokemonRow(true)(props.dex.pokemons[0], 0)}
            {props.dex.pokemons
              .slice(1)
              .filter(shouldRender)
              .map(PokemonRow(false))}
          </Row>
        </AccordionDetails>
      </Accordion>
    </div>
  );
};

export default hot(module)(Dex);
