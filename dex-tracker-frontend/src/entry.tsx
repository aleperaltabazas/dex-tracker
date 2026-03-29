import React from "react";
import ReactDOM from "react-dom";
import { Provider } from "react-redux";
import { GoogleOAuthProvider } from "@react-oauth/google";
import App from "./App";
import store from "./store";
import { googleClientId } from "./config";

const app = (
  <GoogleOAuthProvider clientId={googleClientId}>
    <Provider store={store}>
      <App />
    </Provider>
  </GoogleOAuthProvider>
);

ReactDOM.render(app, document.getElementById("root"));
