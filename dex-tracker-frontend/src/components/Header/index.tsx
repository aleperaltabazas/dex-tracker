import { makeStyles } from "@mui/styles";
import React from "react";
import "./styles.scss";
import classNames from "classnames";
import OpenMenu from "../Menu/Open";
import LinkHome from "../Links/Home";
import Login from "./Login";
import { useSession } from "../../contexts/session";
import User from "./User";

const useStyles = makeStyles(() => ({
  header: {
    backgroundColor: "#2f2f2f",
    display: "flex",
    justifyContent: "space-between",
  },
  dexIcon: {
    display: "flex",
    justifyContent: "flex-end",
  },
}));

const Header = () => {
  const classes = useStyles();
  const { session } = useSession();

  return (
    <header className={classNames(classes.header, "center-v")}>
      <div className="center-v p-1">
        {session.type == "LOGGED_IN" && <OpenMenu />}
        <LinkHome>
          <img
            src="https://cdn.bulbagarden.net/upload/9/9f/Key_Pok%C3%A9dex_m_Sprite.png"
            className="cursor-pointer"
          />
        </LinkHome>
      </div>
      <div className="center-v p-1 pr-2 pr-md-3">
        {session.type == "LOGGED_IN" ? (
          <User session={session} />
        ) : (
          <Login />
        )}
      </div>
    </header>
  );
};

export default Header;
