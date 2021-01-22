import { Container, makeStyles } from "@material-ui/core";
import React from "react";
import { hot } from "react-hot-loader";
import Loader from "react-loader-spinner";
import { RouteComponentProps, withRouter } from "react-router";
import withUserDex from "../hooks/withUserDex";

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

  const [userDex] = withUserDex(props.match.params.id);

  if (userDex.type == "ERROR") {
    return <div>se rompió algo perrito</div>;
  }

  if (userDex.type == "PENDING") {
    return (
      <div className="h-100 w-100 center">
        <Loader
          type="ThreeDots"
          color="#00BFFF"
          height={100}
          width={100}
          className="center"
        />
      </div>
    );
  }

  return (
    <Container>
      <div className={classes.container}>a</div>
    </Container>
  );
};

export default hot(module)(withRouter(DexPage));
