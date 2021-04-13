import React from "react";
import { hot } from "react-hot-loader";
import { RouteComponentProps } from "react-router";
import Loader from "../components/Loader";
import withFetch from "../hooks/withFetch";

type MatchParams = {
  id: string;
};

interface UserPageProps extends RouteComponentProps<MatchParams> {}

const UserPage = (props: UserPageProps) => {
  const [user] = withFetch({ path: `api/v1/users/${props.match.params.id}` });

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

  return <div></div>;
};

export default hot(module)(UserPage);
