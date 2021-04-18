import { Container, Divider, makeStyles, Typography } from "@material-ui/core";
import React from "react";
import { hot } from "react-hot-loader";
import classNames from "classnames";

const useStyles = makeStyles((theme) => ({
  footer: {
    [theme.breakpoints.down("sm")]: {
      backgroundColor: "#e5e5e5",
    },
    [theme.breakpoints.up("md")]: {
      backgroundColor: "#e5e5e5",
    },
    fontSize: "16px",
  },
  githubIcon: {
    color: "#e5e5e5",
  },
  links: {
    display: "flex",
    justifyContent: "space-evenly",
    fontSize: "14px",
  },
}));

const Footer = () => {
  const classes = useStyles();
  return (
    <footer className={classNames(classes.footer, "mt-md-5")}>
      <Divider />
      <Container style={{ height: "64px" }} maxWidth="md">
        <Typography
          variant="h6"
          className={classNames("uppercase h-100 center-v", classes.links)}
        >
          <div>
            <a href="" className="normalize-link">
              Home
            </a>
          </div>
          <div>
            <a
              href="https://github.com/aleperaltabazas/dex-tracker"
              className="normalize-link"
              target="_blank"
            >
              Github
            </a>
          </div>
        </Typography>
      </Container>
    </footer>
  );
};

export default hot(module)(Footer);
