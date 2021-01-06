import {
  Accordion,
  AccordionActions,
  AccordionDetails,
  AccordionSummary,
  Button,
  Chip,
  Divider,
  Grid,
  Hidden,
  Input,
  InputAdornment,
  InputLabel,
  makeStyles,
  OutlinedInput,
  TextField,
  Typography,
} from "@material-ui/core";
import React, { ReactNode, useEffect, useState } from "react";
import { hot } from "react-hot-loader";
import ExpandMoreIcon from "@material-ui/icons/ExpandMore";
import className from "classnames";
import { Dex } from "../../types";
import { Search } from "@material-ui/icons";

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
  icon: {
    verticalAlign: "bottom",
    height: 20,
    width: 20,
  },
  details: {
    display: "block",
  },
  column: {
    flexBasis: "33.33%",
  },
  helper: {
    borderLeft: `2px solid ${theme.palette.divider}`,
    padding: theme.spacing(1, 2),
  },
  link: {
    color: theme.palette.primary.main,
    textDecoration: "none",
    "&:hover": {
      textDecoration: "underline",
    },
  },
  listItem: {
    fontSize: "16px",
  },
}));

const Dex = (props: DexProps) => {
  const classes = useStyles();
  const [search, setSearch] = useState<string | undefined>(undefined);

  const pokemons = () => {
    const columns: ReactNode[] = [];

    props.dex.pokemons
      .filter(
        (p) =>
          search == undefined ||
          p.name.toLowerCase().includes(search.toLowerCase()) ||
          p.number.toString().includes(search)
      )
      .forEach((p, idx) => {
        columns.push(
          <Grid
            item
            xs={3}
            md={1}
            className={className("center", classes.listItem)}
            key={`${idx}-sprite`}
          >
            <span className={`pokesprite pokemon ${p.name}`} />
          </Grid>
        );
        columns.push(
          <Hidden smDown>
            <Grid
              item
              md={1}
              className={className("center", classes.listItem)}
              key={`${idx}-number`}
            >
              {p.number}
            </Grid>
          </Hidden>
        );
        columns.push(
          <Grid
            item
            xs={6}
            md={9}
            className={className("center-v", classes.listItem)}
            key={`${idx}-poke`}
          >
            {p.name}
          </Grid>
        );
        columns.push(
          <Grid
            item
            xs={3}
            md={1}
            className={className("center", classes.listItem)}
            key={`${idx}-caught`}
          >
            {p.captured}
          </Grid>
        );
      });

    return columns;
  };

  const handleSearchChange = (event: React.ChangeEvent<HTMLInputElement>) =>
    setSearch(event.currentTarget.value);

  return (
    <div className={classes.root}>
      <Accordion defaultExpanded>
        <AccordionSummary
          expandIcon={<ExpandMoreIcon />}
          aria-controls="panel1c-content"
          id="panel1c-header"
        >
          <div className={classes.column}>
            <Typography className={classes.heading}>
              {props.dex.game}
            </Typography>
          </div>
          <div className={classes.column}>
            <Typography className={classes.secondaryHeading}>
              Regional
            </Typography>
          </div>
        </AccordionSummary>
        <AccordionDetails className={classes.details}>
          <Grid container>
            <Hidden smDown>
              <Grid
                item
                xs={12}
                md={12}
                className="pb-2 pl-1 pr-1 pl-md-2 pr-md-2"
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
              </Grid>
            </Hidden>
            <Hidden mdUp>
              <Grid
                item
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
              </Grid>
            </Hidden>
            <Grid item xs={3} md={1} className="center">
              <span className="pokesprite ball poke" />
            </Grid>
            <Hidden smDown>
              <Grid
                item
                md={1}
                className={className("center", "bold", classes.listItem)}
              >
                Number
              </Grid>
            </Hidden>
            <Grid
              item
              xs={6}
              md={9}
              className={className("center-v", "bold", classes.listItem)}
            >
              Pokemon
            </Grid>
            <Grid
              item
              xs={3}
              md={1}
              className={className("center", "bold", classes.listItem)}
            >
              Caught
            </Grid>
          </Grid>
          <Divider />
          <Grid container className="dex-container">
            {pokemons()}
          </Grid>
        </AccordionDetails>
        <Divider />
        <AccordionActions>
          <Button size="small">Cancel</Button>
          <Button size="small" color="primary">
            Save
          </Button>
        </AccordionActions>
      </Accordion>
    </div>
  );
};

export default hot(module)(Dex);
