import { Button } from "@mui/material";
import React from "react";
import MenuIcon from "@mui/icons-material/Menu";
import { useGlobal } from "../../contexts/global";

const Open = () => {
  const { dispatch } = useGlobal();
  return (
    <Button onClick={() => dispatch({ type: "UPDATE_MENU_OPEN", payload: true })}>
      <MenuIcon fontSize="large" style={{ color: "white" }} />
    </Button>
  );
};

export default Open;
