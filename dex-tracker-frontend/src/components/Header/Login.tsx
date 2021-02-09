import React from "react";
import { hot } from "react-hot-loader";
import { GoogleLogin } from "react-google-login";
import AccountCircleIcon from "@material-ui/icons/AccountCircle";
import { makeStyles } from "@material-ui/core";
import classNames from "classnames";
import { googleClientId as googleClientId } from "../../config";
import { oauthLogin } from "../../functions/login";

type LoginProps = {};

const useStyles = makeStyles(() => ({
  account: {
    fontSize: "48px",
    color: "white",
  },
}));

const Login = (props: LoginProps) => {
  const classes = useStyles();

  return (
    <GoogleLogin
      clientId={googleClientId}
      render={(props) => (
        <AccountCircleIcon
          onClick={props.onClick}
          className={classNames(classes.account, "cursor-pointer")}
        />
      )}
      accessType="online"
      onSuccess={oauthLogin}
    />
  );
};

export default hot(module)(Login);
