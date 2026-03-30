import React from "react";
import AccountCircleIcon from "@mui/icons-material/AccountCircle";
import { makeStyles } from "@mui/styles";
import classNames from "classnames";
import { oauthLogin } from "../../functions/login";
import { useSession } from "../../contexts/session";
import { useGoogleLogin } from "@react-oauth/google";

const useStyles = makeStyles(() => ({
  account: {
    fontSize: "48px",
    color: "white",
  },
}));

const Login = () => {
  const classes = useStyles();
  const { dispatch: sessionDispatch } = useSession();

  const login = useGoogleLogin({
    onSuccess: (response) => oauthLogin(response, sessionDispatch),
    onError: () => sessionDispatch({ type: "LOG_IN_ERROR" }),
    flow: "auth-code",
  });

  return (
    <AccountCircleIcon
      onClick={() => login()}
      className={classNames(classes.account, "cursor-pointer")}
    />
  );
};

export default Login;
