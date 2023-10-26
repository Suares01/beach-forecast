import { useSelector } from './useSelector';

export function useUser() {
  const user = useSelector((state) => state.auth);
  return user;
}
