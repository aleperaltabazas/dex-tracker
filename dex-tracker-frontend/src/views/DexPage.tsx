import React from "react";
import { Navigate, useParams } from "react-router-dom";
import Loader from "../components/Loader";
import { Container } from "@mui/material";
import { makeStyles } from "@mui/styles";
import classNames from "classnames";
import Dex from "../components/Dex";
import withUserDex from "../hooks/withUserDex";
import { usePokedex } from "../contexts/pokedex";
import { useSession } from "../contexts/session";

const useStyles = makeStyles((theme) => ({
  container: {
    backgroundColor: "white",
  },
  noOverflow: {
    [theme.breakpoints.down("md")]: {
      overflowX: "hidden",
    },
  },
}));

const DexPage = () => {
  const classes = useStyles();
  const { userId, dexId } = useParams<{ userId: string; dexId: string }>();
  const { pokedex: gamePokedex } = usePokedex();
  const { session } = useSession();

  const [dex] = withUserDex(userId!, dexId!, [userId, dexId]);

  if (
    !gamePokedex.loaded ||
    session.type == "UNINITIALIZED" ||
    dex.type == "PENDING"
  ) {
    return (
      <div className="h-100 w-100 center">
        <Loader />
      </div>
    );
  }

  if (session.type == "ERROR") {
    return <Navigate to="/" />;
  }

  if (dex.type == "ERROR") {
    return <div>Se rompió algo perro</div>;
  }

  return (
    <Container
      className={classNames(classes.noOverflow, "center")}
      key={dex.value.userDexId}
    >
      <div className={classNames(classes.container, "mt-3 mt-md-5")}>
        <Dex dex={dex.value} />
      </div>
    </Container>
  );
};

export default DexPage;
