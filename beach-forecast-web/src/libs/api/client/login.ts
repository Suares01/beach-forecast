import { LoginCredentials, Tokens } from '@src/contexts/AuthContext/context';
import { clientQuery } from '../../clientQuery';

export default async function login(
  credentials: LoginCredentials,
): Promise<Tokens> {
  const { data } = await clientQuery.post<Tokens>('/auth/login', credentials);

  return data;
}
