import { Container, Grid, makeStyles } from "@material-ui/core";
import React from "react";
import { hot } from "react-hot-loader";
import { RouteComponentProps } from "react-router";
import Loader from "../components/Loader";
import Row from "../components/Row";
import Column from "../components/Column";
import withFetch from "../hooks/withFetch";
import { User } from "../types/user";
import classNames from "classnames";
import Summary from "../components/Dex/Summary";

type MatchParams = {
  userId: string;
};

interface UserPageProps extends RouteComponentProps<MatchParams> {}

const useStyles = makeStyles(() => ({
  picture: {
    height: "100%",
    width: "100%",
    borderRadius: "50%",
    backgroundColor: "white",
  },
  username: {
    fontSize: "24px",
    fontWeight: "bolder",
  },
}));

const UserPage = (props: UserPageProps) => {
  const classes = useStyles();

  const [user] = withFetch<User>(
    {
      path: `api/v1/users/${props.match.params.userId}`,
    },
    [props.match.params.userId]
  );

  if (user.type == "PENDING") {
    return (
      <div className="h-100 w-100">
        <Loader />
      </div>
    );
  }

  if (user.type == "ERROR") {
    return <div>Se rompió algo perrote :(</div>;
  }

  return (
    <Container className="mt-3">
      <Row spacing={3}>
        <Column xs={12} md={3} className="h-100">
          <img
            src={
              user.value.picture ||
              "https://art.pixilart.com/c437aec56759fc7.png"
            }
            className={classes.picture}
          />
          <div className={classNames("center-h mt-1", classes.username)}>
            {user.value.username}
          </div>
        </Column>
        <Column xs={12} md={9} container>
          {user.value.pokedex.map((d) => (
            <Column xs={12}>
              <Summary userId={user.value.userId} dex={d} />
            </Column>
          ))}
        </Column>
      </Row>
    </Container>
  );
};

export default hot(module)(UserPage);
