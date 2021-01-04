import { Grid, Paper } from "@material-ui/core";
import React from "react";
import { hot } from "react-hot-loader";
import "./styles.scss";

type HeaderProps = {};

const Header = (props: HeaderProps) => {
  return (
    <header className="header">
      <Grid container>
        <Grid item xs={12}>
          <span className="pokesprite ball dusk" />
        </Grid>
      </Grid>
    </header>
  );
};

export default hot(module)(Header);
