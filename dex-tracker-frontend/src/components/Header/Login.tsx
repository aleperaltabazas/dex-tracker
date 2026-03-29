import React from "react";
import { useGoogleLogin } from "@react-oauth/google";
import AccountCircleIcon from "@material-ui/icons/AccountCircle";
import { makeStyles } from "@material-ui/core";
import classNames from "classnames";
import { oauthLogin } from "../../functions/login";

const useStyles = makeStyles(() => ({
  account: {
    fontSize: "48px",
    color: "white",
  },
}));

const Login = () => {
  const classes = useStyles();

  const login = useGoogleLogin({
    onSuccess: oauthLogin,
    onError: (err) => console.error("Google login error", err),
  });

  return (
    <AccountCircleIcon
      onClick={() => login()}
      className={classNames(classes.account, "cursor-pointer")}
    />
  );
};

export default Login;
