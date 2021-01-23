import { Grid, makeStyles } from "@material-ui/core";
import React from "react";
import { hot } from "react-hot-loader";
import "./styles.scss";
import classNames from "classnames";
import OpenMenu from "../Menu/Open";
import LinkHome from "../Links/Home";

const useStyles = makeStyles(() => ({
  header: {
    backgroundColor: "#2f2f2f",
    display: "flex",
    justifyContent: "flex-start",
  },
  dexIcon: {
    display: "flex",
    justifyContent: "flex-end",
  },
}));

const Header = () => {
  const classes = useStyles();
  return (
    <header className={classNames(classes.header, "center-v")}>
      <div className="center-v p-1">
        <OpenMenu />
      </div>
      <div>
        <LinkHome>
          <img
            src="https://cdn.bulbagarden.net/upload/9/9f/Key_Pok%C3%A9dex_m_Sprite.png"
            className="cursor-pointer"
          />
        </LinkHome>
      </div>
    </header>
  );
};

export default hot(module)(Header);
