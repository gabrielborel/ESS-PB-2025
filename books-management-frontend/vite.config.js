import { defineConfig } from "vite";
import react from "@vitejs/plugin-react";

const apiTarget = process.env.API_URL || "http://localhost:8080";

export default defineConfig({
  plugins: [react()],
  server: {
    port: 3000,
    proxy: {
      "/api": {
        target: apiTarget,
        changeOrigin: true,
        secure: false,
        ws: true,
        configure: (proxy) => {
          proxy.on("error", (err) => {
            console.log("[Proxy Error]", err);
          });
          proxy.on("proxyReq", (proxyReq, req) => {
            console.log(`[Proxy] ${req.method} ${req.url} -> ${apiTarget}`);
          });
          proxy.on("proxyRes", (proxyRes, req) => {
            console.log(
              `[Proxy Response] ${req.method} ${req.url} -> ${proxyRes.statusCode}`
            );
          });
        },
      },
    },
  },
});
