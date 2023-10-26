import { clientQuery } from '@src/libs/clientQuery';
import { User } from '@src/libs/redux/slices/authslice/authSlice';

export default async function getUserData(): Promise<User | undefined> {
  try {
    const { data } = await clientQuery.get<User>('/users/me');

    return data;
  } catch (error: any) {
    return undefined;
  }
}
