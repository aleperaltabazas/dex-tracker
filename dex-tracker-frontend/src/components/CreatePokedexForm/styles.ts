import { createStyles, makeStyles, Theme } from "@material-ui/core";

const useStyles = makeStyles((theme: Theme) =>
  createStyles({
    form: {
      display: "flex",
      flexDirection: "column",
      margin: "auto",
    },
    overflowScrollMd: {
      [theme.breakpoints.up("md")]: {
        overflow: "scroll",
      },
    },
    buttons: {
      fontWeight: "bolder",
      [theme.breakpoints.down("sm")]: {
        display: "flex",
        justifyContent: "space-around",
      },
    },
    create: {
      color: "#1976d2",
      fontWeight: "bolder",
    },
    close: {
      color: "#d70852",
      fontWeight: "bolder",
    },
    overflowAuto: {
      [theme.breakpoints.up("md")]: {
        overflowY: "auto",
      },
    },
    pokemonColumn: {
      maxHeight: "120px",
    },
    namingPrimary: {
      fontSize: "18px",
    },
    namingSecondary: {
      fontSize: "14px",
    },
  })
);

export default useStyles;
