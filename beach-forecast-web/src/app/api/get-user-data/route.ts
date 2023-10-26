import getUserData from '@src/libs/api/server/getUserData';
import { NextResponse } from 'next/server';
import { cookies } from 'next/headers';

export async function GET() {
  const data = getUserData(cookies());

  return NextResponse.json(data);
}
