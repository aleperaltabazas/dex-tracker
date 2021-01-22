import React from "react";
import { makeStyles } from "@material-ui/core/styles";
import Drawer from "@material-ui/core/Drawer";
import List from "@material-ui/core/List";
import Divider from "@material-ui/core/Divider";
import ListItem from "@material-ui/core/ListItem";
import ListItemIcon from "@material-ui/core/ListItemIcon";
import ListItemText from "@material-ui/core/ListItemText";
import InboxIcon from "@material-ui/icons/MoveToInbox";
import MailIcon from "@material-ui/icons/Mail";
import { hot } from "react-hot-loader";
import { RootState } from "../../reducers";
import { connect } from "react-redux";
import { closeMenu } from "../../actions/global";
import store from "../../store";
import classNames from "classnames";
import CloseMenu from "./Close";
import { UserDex } from "../../types/user";
import { Game } from "../../types/pokedex";

const useStyles = makeStyles({
  list: {
    width: 250,
  },
  fullList: {
    width: "auto",
  },
});

type Anchor = "top" | "left" | "bottom" | "right";

type MenuProps = {
  open: boolean;
  userDex: UserDex[];
  games: Game[];
};

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
              {props.userDex.map((dex, idx) => (
                <ListItem button key={dex.userDexId}>
                  <ListItemIcon>
                    <span
                      className={`pokesprite pokemon ${
                        props.games.find((g) => g.title == dex.game)
                          ?.spritePokemon
                      }`}
                    />
                    <span> {dex.game} </span>
                  </ListItemIcon>
                </ListItem>
              ))}
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

export default hot(module)(connect(mapStateToProps)(Menu));
