import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'

// https://vite.dev/config/
export default defineConfig({
  plugins: [react()],
  resolve: {
    alias: {
      src: "/src",
    },
  },
  define: {
    'process.env.VITE_BASE_URL': JSON.stringify('http://localhost:5173/'),
  },
  build: {
    outDir: 'dist',
    assetsDir: 'assets',
    rollupOptions: {
      external: (id) => id.includes('__tests__'),
    },
  },
  base: '/',
})
