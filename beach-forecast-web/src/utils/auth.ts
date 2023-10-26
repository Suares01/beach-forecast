import { getCookie } from 'cookies-next';
import { NextRequest } from 'next/server';

export const PROTECTED_ROUTES = ['/forecasts'];

export function isAuthenticated(req: NextRequest): boolean {
  const accessToken = getCookie('accessToken', { req });

  return !!accessToken;
}
