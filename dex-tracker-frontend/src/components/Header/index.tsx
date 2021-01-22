import { Grid, makeStyles } from "@material-ui/core";
import React from "react";
import { hot } from "react-hot-loader";
import "./styles.scss";
import classNames from "classnames";
import Row from "../Row";
import Column from "../Column";
import OpenMenu from "../Menu/Open";
import { RouteComponentProps, withRouter } from "react-router";

interface HeaderProps extends RouteComponentProps {}

const useStyles = makeStyles(() => ({
  header: {
    backgroundColor: "#2f2f2f",
  },
  dexIcon: {
    display: "flex",
    justifyContent: "flex-end",
  },
}));

const Header = (props: HeaderProps) => {
  const classes = useStyles();
  return (
    <header className={classNames(classes.header)}>
      <Row>
        <Column xs={6} className="center-v p-1">
          <OpenMenu />
        </Column>
        <Column
          xs={6}
          className={classNames(classes.dexIcon, "p-1")}
          onClick={() => props.history.push("/")}
        >
          <img
            src="https://cdn.bulbagarden.net/upload/9/9f/Key_Pok%C3%A9dex_m_Sprite.png"
            className="cursor-pointer"
          />
        </Column>
      </Row>
    </header>
  );
};

export default hot(module)(withRouter(Header));
