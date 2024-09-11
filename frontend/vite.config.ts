import { defineConfig, loadEnv } from 'vite';
import react from '@vitejs/plugin-react';
import path from 'path';

export default defineConfig(({ mode }) => {
  // Load the environment variables based on the current mode (development, production, etc.)
  const env = loadEnv(mode, process.cwd());

  // Merge environment variables into process.env
  process.env = { ...process.env, ...env };

  return {
    plugins: [react()],
    server: {
      port: 3000,
    },
    preview: {
      port: 3000,
    },
    resolve: {
      alias: [{ find: '@', replacement: path.resolve(__dirname, 'src') }],
    },
  };
});