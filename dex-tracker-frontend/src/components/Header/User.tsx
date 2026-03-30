import { Menu, MenuItem } from "@mui/material";
import { makeStyles } from "@mui/styles";
import React, { useState } from "react";
import { LoggedInState } from "../../contexts/session";
import ExitToAppIcon from "@mui/icons-material/ExitToApp";
import classNames from "classnames";
import { logout } from "../../functions/login";
import { Link } from "react-router-dom";
import AccountCircleIcon from "@mui/icons-material/AccountCircle";
import { useSession } from "../../contexts/session";

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
  const { dispatch: sessionDispatch } = useSession();

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
        <MenuItem>
          <Link
            className="normalize-link center-v"
            to={`/users/${props.session.user.userId}`}
            onClick={handleClose}
          >
            <AccountCircleIcon className="pr-1" />
            Profile
          </Link>
        </MenuItem>
        <MenuItem
          onClick={() => {
            handleClose();
            logout(props.session.token, sessionDispatch);
          }}
        >
          <ExitToAppIcon className="pr-1" /> Log out
        </MenuItem>
      </Menu>
    </div>
  );
};

export default User;
