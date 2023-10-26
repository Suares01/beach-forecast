'use client';

import { Tokens } from '@src/contexts/AuthContext/context';
import axios from 'axios';
import { redirect } from 'next/navigation';
import { deleteCookie, getCookie, setCookie } from 'cookies-next';

const BASE_URL = 'http://localhost:8080' as const;

export const clientQuery = axios.create({
  baseURL: BASE_URL,
});

clientQuery.interceptors.request.use(
  (config) => {
    const accessToken = getCookie('accessToken');
    console.log('token', accessToken);
    if (accessToken) {
      config.headers.Authorization = `Bearer ${accessToken}`;
    }

    return config;
  },
  (error) => Promise.reject(error),
);

clientQuery.interceptors.response.use(
  (response) => response,
  (error) => {
    const originalRequest = error.config;
    const refreshToken = getCookie('refreshToken');

    if (error.response.status === 401 && refreshToken) {
      const body = {
        refreshToken,
      };

      clientQuery
        .post<Tokens>('/auth/refresh-token', body)
        .then(({ data }) => {
          setCookie('accessToken', data.access_token);
          setCookie('refreshToken', data.refresh_token);

          originalRequest.headers.Authorization = `Bearer ${data.access_token}`;

          clientQuery(originalRequest)
            .then((response) => {
              return response;
            })
            .catch((error) => {
              return Promise.reject(error);
            });
        })
        .catch(() => {
          deleteCookie('accessToken');
          deleteCookie('refreshToken');
          redirect('/');
        });
    }

    return Promise.reject(error);
  },
);
