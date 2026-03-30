import React from "react";
import { createRoot } from "react-dom/client";
import { ThemeProvider, createTheme } from "@mui/material/styles";
import { ThemeProvider as StylesThemeProvider } from "@mui/styles";
import CssBaseline from "@mui/material/CssBaseline";
import App from "./App";
import { SessionProvider } from "./contexts/session";
import { PokedexProvider } from "./contexts/pokedex";
import { SyncQueueProvider } from "./contexts/syncQueue";
import { GlobalProvider } from "./contexts/global";
import { GoogleOAuthProvider } from "@react-oauth/google";
import { googleClientId } from "./config";

const theme = createTheme();

const container = document.getElementById("root")!;
const root = createRoot(container);

root.render(
  <ThemeProvider theme={theme}>
    <StylesThemeProvider theme={theme}>
      <CssBaseline />
      <GoogleOAuthProvider clientId={googleClientId}>
        <SessionProvider>
          <PokedexProvider>
            <SyncQueueProvider>
              <GlobalProvider>
                <App />
              </GlobalProvider>
            </SyncQueueProvider>
          </PokedexProvider>
        </SessionProvider>
      </GoogleOAuthProvider>
    </StylesThemeProvider>
  </ThemeProvider>
);
