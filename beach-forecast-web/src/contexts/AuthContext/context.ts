'use client';

import { User } from '@src/libs/redux/slices/authslice/authSlice';
import { UseMutationResult } from '@tanstack/react-query';
import { createContext } from 'react';

export interface Tokens {
  access_token: string;
  refresh_token: string;
}

export interface LoginCredentials {
  username: string;
  password: string;
}

export interface RegisterUserData {
  username: string;
  email: string;
  password: string;
  firstName: string;
  lastName?: string;
}

export interface AuthContext {
  user?: User;
  isAuthenticated: boolean;
  isLoading: boolean;
  loginWithCredentials: UseMutationResult<
    Tokens,
    Error,
    LoginCredentials,
    unknown
  >;
  register: UseMutationResult<void, Error, RegisterUserData, unknown>;
  logout(): void;
}

export const AuthContext = createContext<AuthContext>({} as AuthContext);
