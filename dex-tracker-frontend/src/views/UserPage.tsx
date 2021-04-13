import { Container, Grid, makeStyles } from "@material-ui/core";
import React from "react";
import { hot } from "react-hot-loader";
import { RouteComponentProps } from "react-router";
import Loader from "../components/Loader";
import Row from "../components/Row";
import Column from "../components/Column";
import withFetch from "../hooks/withFetch";
import { User } from "../types/user";

type MatchParams = {
  id: string;
};

interface UserPageProps extends RouteComponentProps<MatchParams> {}

const useStyles = makeStyles(() => ({
  picture: {
    height: "256px",
    width: "256px",
    borderRadius: "50%",
  },
}));

const UserPage = (props: UserPageProps) => {
  const classes = useStyles();

  const [user] = withFetch<User>({
    path: `api/v1/users/${props.match.params.id}`,
  });

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
      <Row>
        <Column xs={12} md={3}>
          <img
            src={
              user.value.picture ||
              "https://art.ngfiles.com/images/386000/386577_stardoge_8-bit-pokeball.png?f1446737358"
            }
            className={classes.picture}
          />
          <div>{user.value.username}</div>
        </Column>
        <Column xs={12} md={9}>
          {user.value.pokedex.map((d) => (
            <div>{d.name || d.game.fullTitle} </div>
          ))}
        </Column>
      </Row>
    </Container>
  );
};

export default hot(module)(UserPage);
