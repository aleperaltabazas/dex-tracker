import React from "react";
import { makeStyles } from "@material-ui/core/styles";
import Drawer from "@material-ui/core/Drawer";
import List from "@material-ui/core/List";
import Divider from "@material-ui/core/Divider";
import ListItem from "@material-ui/core/ListItem";
import ListItemIcon from "@material-ui/core/ListItemIcon";
import ListItemText from "@material-ui/core/ListItemText";
import { hot } from "react-hot-loader";
import { RootState } from "../../reducers";
import { connect } from "react-redux";
import { closeMenu, openCreateDexForm } from "../../actions/global";
import store from "../../store";
import classNames from "classnames";
import CloseMenu from "./Close";
import { UserDexRef } from "../../types/user";
import { Game } from "../../types/pokedex";
import { AddCircle } from "@material-ui/icons";
import { RouteComponentProps, withRouter } from "react-router";

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

interface MenuProps extends RouteComponentProps {
  open: boolean;
  userDex: UserDexRef[];
  games: Game[];
}

const Menu = (props: MenuProps) => {
  const classes = useStyles();
  const [state, setState] = React.useState({
    top: false,
    left: props.open,
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

  return (
    <div>
      <React.Fragment key={"left"}>
        <Drawer
          anchor={"left"}
          open={props.open}
          onClose={() => store.dispatch(closeMenu())}
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
                className={classNames(
                  classes.heading,
                  "pl-1 pt-1",
                  "uppercase"
                )}
              >
                My Pokedex
              </div>
              {props.userDex.map((dex) => {
                return (
                  <ListItem
                    key={dex.userDexId}
                    disableGutters
                    className="cursor-pointer"
                    onClick={() => props.history.push(`/dex/${dex.userDexId}`)}
                  >
                    <ListItemIcon>
                      <span
                        className={`pokesprite pokemon ${dex.game?.spritePokemon}`}
                      />
                    </ListItemIcon>
                    <ListItemText>
                      <span>{dex.name || dex.game?.fullTitle}</span>
                    </ListItemText>
                  </ListItem>
                );
              })}
              <ListItem
                button
                onClick={() => {
                  store.dispatch(closeMenu());
                  store.dispatch(openCreateDexForm());
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

const mapStateToProps = (root: RootState) => {
  return {
    open: root.global.menuOpen,
    userDex: root.session.isLoggedIn ? root.session.user.pokedex : [],
    games: root.games.loaded ? root.games.games : [],
  };
};

export default hot(module)(withRouter(connect(mapStateToProps)(Menu)));
