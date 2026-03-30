import { Button } from "@mui/material";
import React from "react";
import CloseIcon from "@mui/icons-material/Close";
import { useGlobal } from "../../contexts/global";

const CloseMenu = () => {
  const { dispatch } = useGlobal();
  return (
    <Button onClick={() => dispatch({ type: "UPDATE_MENU_OPEN", payload: false })}>
      <CloseIcon fontSize="default" style={{ color: "black" }} />
    </Button>
  );
};

export default CloseMenu;
