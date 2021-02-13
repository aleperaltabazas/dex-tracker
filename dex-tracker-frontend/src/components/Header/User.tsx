import { makeStyles, Menu, MenuItem } from "@material-ui/core";
import React, { useState } from "react";
import { hot } from "react-hot-loader";
import { LoggedInState } from "../../store/session";
import ExitToAppIcon from "@material-ui/icons/ExitToApp";
import classNames from "classnames";
import { logout } from "../../functions/login";

type UserProps = {
  session: LoggedInState;
};

const useStyles = makeStyles(() => ({
  userPicture: {
    height: "48px",
    width: "48px",
    borderRadius: "50%",
  },
}));

const User = (props: UserProps) => {
  const classes = useStyles();

  const [anchorEl, setAnchorEl] = useState<null | HTMLElement>(null);

  const handleUserMenu = (e: React.MouseEvent<HTMLImageElement>) =>
    setAnchorEl(e.currentTarget);

  const handleClose = () => setAnchorEl(null);

  return (
    <div className={classes.userPicture}>
      <img
        src={props.session.picture}
        className={classNames(classes.userPicture, "cursor-pointer")}
        aria-controls="user-menu"
        aria-haspopup="true"
        onClick={(e) => handleUserMenu(e)}
      />
      <Menu
        id="user-menu"
        anchorEl={anchorEl}
        keepMounted
        open={Boolean(anchorEl)}
        onClose={handleClose}
      >
        <MenuItem
          onClick={() => {
            handleClose();
            logout();
          }}
        >
          <ExitToAppIcon /> Log out
        </MenuItem>
      </Menu>
    </div>
  );
};

export default hot(module)(User);
