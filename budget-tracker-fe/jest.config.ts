import type { Config } from '@jest/types';

const config: Config.InitialOptions = {
  preset: 'ts-jest',
  testEnvironment: 'jsdom',
  extensionsToTreatAsEsm: ['.ts', '.tsx'], // Treat TypeScript files as ESM
  transform: {
    '^.+\\.(ts|tsx|js|jsx)$': [
      'ts-jest',
      {
        useESM: true, // Enable ESM support in ts-jest
      },
    ],
  },
  setupFilesAfterEnv: ['<rootDir>/jest.setup.ts'], // Include setup
  moduleNameMapper: {
    '\\.(css|scss)$': 'identity-obj-proxy', // CSS Mocking
  },
  testMatch: ['**/__tests__/**/*.(spec|test).ts?(x)'],
  globals: {
    'ts-jest': {
      babelConfig: true,
      useESM: true,
    },
  },
};

export default config;
