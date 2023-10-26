'use client';

import { authSlice } from './slices/authslice/authSlice';

export const reducer = {
  auth: authSlice.reducer,
};
