import React from "react";
import { makeStyles } from "@mui/styles";
import Drawer from "@mui/material/Drawer";
import List from "@mui/material/List";
import Divider from "@mui/material/Divider";
import ListItem from "@mui/material/ListItem";
import ListItemIcon from "@mui/material/ListItemIcon";
import ListItemText from "@mui/material/ListItemText";
import classNames from "classnames";
import CloseMenu from "./Close";
import { UserDex } from "../../types/user";
import { AddCircle } from "@mui/icons-material";
import DexLink from "../Links/Dex";
import { Typography } from "@mui/material";
import sprite from "../Sprite";
import { useSession } from "../../contexts/session";
import { useGlobal } from "../../contexts/global";

const useStyles = makeStyles({
  list: {
    width: 250,
  },
  fullList: {
    width: "auto",
  },
  heading: {
    fontSize: "20px",
    fontWeight: "bold",
  },
  newPokedex: {
    fontWeight: "bold",
  },
});

type Anchor = "top" | "left" | "bottom" | "right";

const Menu = () => {
  const classes = useStyles();
  const { session } = useSession();
  const { global, dispatch: globalDispatch } = useGlobal();

  const [state, setState] = React.useState({
    top: false,
    left: global.menuOpen,
    bottom: false,
    right: false,
  });

  const toggleDrawer = (anchor: Anchor, open: boolean) => (
    event: React.KeyboardEvent | React.MouseEvent
  ) => {
    if (
      event.type === "keydown" &&
      ((event as React.KeyboardEvent).key === "Tab" ||
        (event as React.KeyboardEvent).key === "Shift")
    ) {
      return;
    }
    setState({ ...state, [anchor]: open });
  };

  if (session.type !== "LOGGED_IN") return null;

  const userDex: UserDex[] = session.user.pokedex;

  return (
    <div>
      <React.Fragment key={"left"}>
        <Drawer
          anchor={"left"}
          open={global.menuOpen}
          onClose={() => globalDispatch({ type: "UPDATE_MENU_OPEN", payload: false })}
        >
          <div
            className={classNames(classes.list)}
            role="presentation"
            onClick={toggleDrawer("left", false)}
            onKeyDown={toggleDrawer("left", false)}
          >
            <List>
              <CloseMenu />
              <Divider />
              <div
                className={classNames(classes.heading, "pl-1 pt-1", "uppercase")}
              >
                My Pokedex
              </div>
              {userDex.map((dex) => (
                <DexLink
                  userId={session.user.userId}
                  dexId={dex.userDexId}
                  key={dex.userDexId}
                >
                  <ListItem
                    disableGutters
                    className="cursor-pointer"
                    onClick={() => globalDispatch({ type: "UPDATE_MENU_OPEN", payload: false })}
                    style={{ height: "72px" }}
                  >
                    <ListItemIcon>
                      <sprite.icon gen={dex.game.gen} pokemon="bulbasaur" />
                    </ListItemIcon>
                    <ListItemText className="h-100 center">
                      <Typography noWrap>
                        {dex.name || dex.game.displayName}
                      </Typography>
                    </ListItemText>
                  </ListItem>
                </DexLink>
              ))}
              <Divider />
              <ListItem
                button
                onClick={() => {
                  globalDispatch({ type: "UPDATE_MENU_OPEN", payload: false });
                  globalDispatch({ type: "UPDATE_CREATE_DEX_FORM_OPEN", payload: true });
                }}
              >
                <ListItemIcon>
                  <AddCircle />
                </ListItemIcon>
                <ListItemText>
                  <span className={classNames("uppercase", classes.newPokedex)}>
                    New Pokedex
                  </span>
                </ListItemText>
              </ListItem>
            </List>
          </div>
        </Drawer>
      </React.Fragment>
    </div>
  );
};

export default Menu;
