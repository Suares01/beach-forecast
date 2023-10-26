import { NextResponse } from 'next/server';
import type { NextRequest } from 'next/server';
import { PROTECTED_ROUTES, isAuthenticated } from './utils/auth';

export default function middleware(req: NextRequest) {
  if (
    !isAuthenticated(req) &&
    PROTECTED_ROUTES.includes(req.nextUrl.pathname)
  ) {
    const absoluteURL = new URL('/', req.nextUrl.origin);
    return NextResponse.redirect(absoluteURL.toString());
  }
}
