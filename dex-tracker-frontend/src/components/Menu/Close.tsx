import { Button } from "@material-ui/core";
import React from "react";
import { hot } from "react-hot-loader";
import CloseIcon from "@material-ui/icons/Close";
import store from "../../store";
import { closeMenu } from "../../actions/global";

const CloseMenu = () => {
  return (
    <Button onClick={() => store.dispatch(closeMenu())}>
      <CloseIcon fontSize="default" style={{ color: "black" }} />
    </Button>
  );
};

export default hot(module)(CloseMenu);
