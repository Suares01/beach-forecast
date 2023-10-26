import { User } from '@src/libs/redux/slices/authslice/authSlice';
import { getServerQuery } from '@src/libs/serverQuery';
import { RequestCookies } from 'next/dist/compiled/@edge-runtime/cookies';
import { ReadonlyRequestCookies } from 'next/dist/server/web/spec-extension/adapters/request-cookies';

export default async function getUserData(
  nextCookies: ReadonlyRequestCookies | RequestCookies,
): Promise<User | undefined> {
  try {
    const serverQuery = getServerQuery(nextCookies);
    const { data } = await serverQuery.get<User>('/users/me');

    return data;
  } catch (error: any) {
    return undefined;
  }
}
