import { Typography } from "@material-ui/core";
import React, { useEffect, useState } from "react";
import { hot } from "react-hot-loader";
import classNames from "classnames";
import useStyles from "./styles";
import { UserDex } from "../../types/user";
import { RootState } from "../../reducers";
import { connect } from "react-redux";
import { RouteComponentProps, withRouter } from "react-router";

type MatchParams = {
  id: string;
};

interface CounterProps extends RouteComponentProps<MatchParams> {
  dexId: string;
  dex: Array<UserDex>;
  total: number;
}

const Counter = (props: CounterProps) => {
  const classes = useStyles();

  const [caughtCounter, setCaughtCounter] = useState(
    props.dex.find((d) => d.userDexId == props.dexId)?.caught
  );

  useEffect(() => {
    return () => setCaughtCounter(0);
  }, [props.match.params.id]);

  return (
    <Typography
      className={classNames(classes.secondaryHeading, "pr-1 pr-md-0")}
    >
      <span id="counter">{caughtCounter}</span>/{props.total}
    </Typography>
  );
};

const mapStateToProps = (root: RootState) => {
  switch (root.session.type) {
    case "LOGGED_IN":
      return { dex: root.session.user.pokedex };
    case "NOT_LOGGED_IN":
      return { dex: root.session.localDex };
    default:
      return { dex: [] };
  }
};

export default hot(module)(connect(mapStateToProps)(withRouter(Counter)));
