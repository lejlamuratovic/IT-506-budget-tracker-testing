import '@testing-library/jest-dom'

Object.defineProperty(global, 'importMeta', {
    value: {
      env: {
        VITE_BASE_URL: 'http://localhost:5173/',
      },
    },
  });
  
process.env.VITE_BASE_URL = 'http://localhost:5173/';
