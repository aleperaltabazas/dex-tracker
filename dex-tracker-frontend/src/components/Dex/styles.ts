import { makeStyles } from "@material-ui/core";

const styles = makeStyles((theme) => ({
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

export default styles;
