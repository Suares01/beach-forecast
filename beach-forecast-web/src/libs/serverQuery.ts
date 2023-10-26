import { Tokens } from '@src/contexts/AuthContext/context';
import axios from 'axios';
import { redirect } from 'next/navigation';
import { type ReadonlyRequestCookies } from 'next/dist/server/web/spec-extension/adapters/request-cookies';
import { RequestCookies } from 'next/dist/compiled/@edge-runtime/cookies';

const BASE_URL = 'http://localhost:8080' as const;

export function getServerQuery(
  nextCookies: RequestCookies | ReadonlyRequestCookies,
) {
  const serverQuery = axios.create({
    baseURL: BASE_URL,
  });

  serverQuery.interceptors.request.use(
    (config) => {
      const accessToken = nextCookies.get('accessToken');
      console.log('token', accessToken);
      if (accessToken) {
        config.headers.Authorization = `Bearer ${accessToken}`;
      }

      return config;
    },
    (error) => Promise.reject(error),
  );

  serverQuery.interceptors.response.use(
    (response) => response,
    (error) => {
      const originalRequest = error.config;
      const refreshToken = nextCookies.get('refreshToken');

      if (error.response.status === 401 && refreshToken) {
        const body = {
          refreshToken,
        };

        serverQuery
          .post<Tokens>('/auth/refresh-token', body)
          .then(({ data }) => {
            nextCookies.set('accessToken', data.access_token);
            nextCookies.set('refreshToken', data.refresh_token);

            originalRequest.headers.Authorization = `Bearer ${data.access_token}`;

            serverQuery(originalRequest)
              .then((response) => {
                return response;
              })
              .catch((error) => {
                return Promise.reject(error);
              });
          })
          .catch(() => {
            nextCookies.delete('accessToken');
            nextCookies.delete('refreshToken');
            redirect('/');
          });
      }

      return Promise.reject(error);
    },
  );

  return serverQuery;
}
