import { makeStyles } from "@material-ui/core";
import React from "react";
import { hot } from "react-hot-loader";
import { RouteComponentProps, withRouter } from "react-router";

type MatchParams = {
  id: string;
};

interface DexPageProps extends RouteComponentProps<MatchParams> {}

const useStyles = makeStyles(() => ({
  container: {
    backgroundColor: "white",
  },
}));

const DexPage = (props: DexPageProps) => {
  const classes = useStyles();

  return <div className={classes.container}>a</div>;
};

export default hot(module)(withRouter(DexPage));
