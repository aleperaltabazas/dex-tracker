import { defineConfig } from "vite";
import react from "@vitejs/plugin-react";
import path from "path";

export default defineConfig(({ mode }) => ({
  plugins: [react({ jsxRuntime: "classic" })],
  css: {
    preprocessorOptions: {
      scss: {
        api: "modern-compiler",
      },
    },
  },
  build: {
    outDir:
      mode === "production"
        ? path.resolve(__dirname, "../dex-tracker-backend/src/main/resources/assets/static")
        : path.resolve(__dirname, "build"),
    emptyOutDir: true,
  },
  server: {
    port: 8080,
    proxy: {
      "/api": {
        target: "http://localhost:9290",
        changeOrigin: true,
      },
    },
  },
}));
