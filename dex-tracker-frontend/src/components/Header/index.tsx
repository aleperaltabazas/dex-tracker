import { Grid, makeStyles } from "@material-ui/core";
import React from "react";
import { hot } from "react-hot-loader";
import "./styles.scss";
import className from "classnames";

type HeaderProps = {};

const useStyles = makeStyles(() => ({
  header: {
    backgroundColor: "#2f2f2f",
  },
}));

const Header = (props: HeaderProps) => {
  const styles = useStyles();
  return (
    <header className={className(styles.header)}>
      <Grid container>
        <Grid item xs={12}>
          <img src="https://cdn.bulbagarden.net/upload/9/9f/Key_Pok%C3%A9dex_m_Sprite.png" />
        </Grid>
      </Grid>
    </header>
  );
};

export default hot(module)(Header);
