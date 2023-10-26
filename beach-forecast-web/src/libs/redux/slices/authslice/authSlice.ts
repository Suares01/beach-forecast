import { createSlice, type PayloadAction } from '@reduxjs/toolkit';

export interface User {
  id: string;
  username: string;
  enabled: boolean;
  emailVerified: boolean;
  email: string;
  firstName: string;
  lastName?: string;
}

export interface AuthSliceState {
  user?: User;
  isAuthenticated: boolean;
}

const initialState: AuthSliceState = {
  user: undefined,
  isAuthenticated: false,
};

export const authSlice = createSlice({
  name: 'auth',
  initialState,
  reducers: {
    setUser(state, action: PayloadAction<User>) {
      state.user = action.payload;
      state.isAuthenticated = true;
    },
    removeUser(state) {
      state.user = undefined;
      state.isAuthenticated = false;
    },
  },
});

export const { setUser, removeUser } = authSlice.actions;
