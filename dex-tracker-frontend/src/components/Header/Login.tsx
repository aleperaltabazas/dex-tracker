import React from "react";
import { hot } from "react-hot-loader";
import { GoogleLogin } from "react-google-login";
import AccountCircleIcon from "@material-ui/icons/AccountCircle";
import { makeStyles } from "@material-ui/core";
import classNames from "classnames";
import { googleClientId as googleClientId } from "../../config";
import { oauthLogin } from "../../functions/login";
import { SessionState } from "../../store/session";
import { RootState } from "../../reducers";
import { connect } from "react-redux";

type LoginProps = {
  session: SessionState;
};

const useStyles = makeStyles(() => ({
  account: {
    fontSize: "48px",
    color: "white",
  },
  userPicture: {
    height: "48px",
    width: "48px",
    borderRadius: "50%",
  },
}));

const Login = (props: LoginProps) => {
  const classes = useStyles();

  if (props.session.type == "LOGGED_IN") {
    return (
      <div className={classes.userPicture}>
        <img src={props.session.picture} className={classes.userPicture} />
      </div>
    );
  }
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

const mapStateToProps = (root: RootState) => ({ session: root.session });

export default hot(module)(connect(mapStateToProps)(Login));
