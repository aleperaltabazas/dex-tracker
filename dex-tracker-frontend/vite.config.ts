import { defineConfig } from "vite";
import react from "@vitejs/plugin-react";
import fs from "fs";
import path from "path";

const getPomVersion = (): string => {
  const pomPath = path.resolve(__dirname, "../dex-tracker-backend/pom.xml");
  const content = fs.readFileSync(pomPath, "utf8");
  const match = content.match(/<version>([^<]+)<\/version>/);
  if (!match) throw new Error("Could not find <version> in pom.xml");
  return match[1];
};

const version = getPomVersion();
const isProduction = process.env.NODE_ENV === "production";

export default defineConfig({
  plugins: [react({ jsxRuntime: "classic" })],

  publicDir: "public",

  define: {
    VERSION: JSON.stringify(version),
  },

  css: {
    preprocessorOptions: {
      scss: {
        api: "modern-compiler",
        sourceMap: true,
      },
    },
  },

  build: {
    outDir: isProduction
      ? path.resolve(
          __dirname,
          `../dex-tracker-backend/src/main/resources/assets/static/dex-tracker/versions/${version}`
        )
      : "dist",
    emptyOutDir: true,
    sourcemap: isProduction ? "hidden" : true,
    rollupOptions: {
      output: {
        manualChunks: {
          vendor: ["react", "react-dom", "prop-types"],
        },
      },
    },
  },

  server: {
    hot: true,
  },
});
