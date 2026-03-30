import { Button } from "@material-ui/core";
import React from "react";
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

export default Open;
