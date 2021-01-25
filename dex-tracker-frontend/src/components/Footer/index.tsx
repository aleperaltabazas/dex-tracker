import { Divider, Hidden, makeStyles } from "@material-ui/core";
import React from "react";
import { hot } from "react-hot-loader";
import GitHubIcon from "@material-ui/icons/GitHub";
import classNames from "classnames";

const useStyles = makeStyles((theme) => ({
  footer: {
    [theme.breakpoints.down("sm")]: {
      backgroundColor: "#282828",
    },
    [theme.breakpoints.up("md")]: {
      backgroundColor: "white",
    },
    fontSize: "16px",
  },
  githubIcon: {
    color: "white",
  },
}));

const Footer = () => {
  const classes = useStyles();
  return (
    <footer className={classNames(classes.footer)}>
      <Hidden smDown>
        <Divider />
        <div className="center-v pl-3" style={{ height: "64px" }}></div>
      </Hidden>
      <Hidden mdUp>
        <div className="center pt-1 pb-1">
          <a
            href="https://github.com/aleperaltabazas/dex-tracker"
            target="blank_"
          >
            <GitHubIcon
              fontSize="large"
              style={{ color: "white" }}
              color="inherit"
            />
          </a>
        </div>
      </Hidden>
    </footer>
  );
};

export default hot(module)(Footer);
