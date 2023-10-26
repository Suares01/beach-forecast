'use client';

import { configureStore } from '@reduxjs/toolkit';
import { reducer } from './rootReducer';

export const reduxStore = configureStore({
  reducer,
});

export type ReduxStore = typeof reduxStore;
export type ReduxState = ReturnType<typeof reduxStore.getState>;
export type ReduxDispatch = typeof reduxStore.dispatch;
