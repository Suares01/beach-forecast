import { RegisterUserData } from '@src/contexts/AuthContext/context';
import { clientQuery } from '../../clientQuery';

export default async function registerUser({
  email,
  firstName,
  password,
  username,
  lastName,
}: RegisterUserData): Promise<void> {
  await clientQuery.post<void>('/auth/register', {
    email,
    firstName,
    password,
    username,
    lastName,
  });
}
