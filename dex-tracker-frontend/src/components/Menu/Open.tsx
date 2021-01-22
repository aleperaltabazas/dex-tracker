import { Button } from "@material-ui/core";
import React from "react";
import { hot } from "react-hot-loader";
import MenuIcon from "@material-ui/icons/Menu";
import { openMenu } from "../../actions/global";
import store from "../../store";

const Open = () => {
  return (
    <Button onClick={() => store.dispatch(openMenu())}>
      <MenuIcon fontSize="large" style={{ color: "white" }} />
    </Button>
  );
};

export default hot(module)(Open);
