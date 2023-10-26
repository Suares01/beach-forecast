'use client';

import { setCookie, deleteCookie } from 'cookies-next';
import { useRouter } from 'next/navigation';
import { PropsWithChildren, useEffect, useState } from 'react';
import { AuthContext } from './context';
import { User } from '@src/libs/redux/slices/authslice/authSlice';
import { useMutation, useQuery } from '@tanstack/react-query';
import getUserData from '@src/libs/api/client/getUserData';
import login from '@src/libs/api/client/login';
import registerUser from '@src/libs/api/client/registerUser';

export interface AuthContextProvider extends PropsWithChildren {
  initialData?: User;
}

export function AuthContextProvider({
  children,
  initialData,
}: AuthContextProvider) {
  const [user, setUser] = useState<User | undefined>(initialData);
  const router = useRouter();

  const { data, isLoading, refetch, status, error } = useQuery({
    queryKey: ['user-data'],
    queryFn: getUserData,
    initialData,
  });

  const loginWithCredentials = useMutation({
    mutationFn: login,
    onSuccess(data) {
      const { access_token, refresh_token } = data;

      setCookie('accessToken', access_token, {
        maxAge: 60 * 5,
      });
      setCookie('refreshToken', refresh_token, {
        maxAge: 60 * 30,
      });

      refetch();
      router.push('/forecasts');
    },
    onError(error) {
      console.error(error);
    },
  });

  const register = useMutation({
    mutationFn: registerUser,
    onSuccess(_, variables) {
      const { username, password } = variables;
      const { mutate } = loginWithCredentials;
      mutate({ username, password });
    },
    onError(error) {
      console.error(error);
    },
  });

  useEffect(() => {
    switch (status) {
      case 'success': {
        setUser(data);
      }
    }
  }, [status, data, error]);

  function logout(): void {
    deleteCookie('accessToken');
    deleteCookie('refreshToken');
    setUser(undefined);
    router.push('/');
  }

  return (
    <AuthContext.Provider
      value={{
        user,
        isAuthenticated: !!user,
        loginWithCredentials,
        isLoading,
        logout,
        register,
      }}
    >
      {children}
    </AuthContext.Provider>
  );
}
