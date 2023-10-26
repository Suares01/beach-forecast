'use client';

import { PropsWithChildren } from 'react';
import { Provider } from 'react-redux';
import { reduxStore } from './store';

interface ReduxProviderProps extends PropsWithChildren {}

export const ReduxProvider = ({ children }: ReduxProviderProps) => {
  return <Provider store={reduxStore}>{children}</Provider>;
};
