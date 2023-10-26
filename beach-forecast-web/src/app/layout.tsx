import './global.css';
import Favicon from '/public/metadata/favicon.ico';
import type { Metadata } from 'next';
// import { cookies } from 'next/headers';
import { Inter } from 'next/font/google';
import { ReduxProvider } from '@src/libs/redux/ReduxProvider';
import { AuthContextProvider } from '@src/contexts/AuthContext';
import Providers from '@src/libs/QueryClientProvider';
// import getUserData from '@src/libs/api/server/getUserData';

const inter = Inter({ subsets: ['latin'] });

export const metadata: Metadata = {
  icons: [{ rel: 'icon', url: Favicon.src }],
};

export default async function RootLayout({
  children,
}: {
  children: React.ReactNode;
}) {
  const initialUserData = await fetch(
    'http://localhost:3000/api/get-user-data',
  );
  const jsoninitialUserData = await initialUserData.json();
  console.log(jsoninitialUserData);
  return (
    <ReduxProvider>
      <html lang="en">
        <body className={inter.className}>
          <Providers>
            <AuthContextProvider initialData={jsoninitialUserData}>
              {children}
            </AuthContextProvider>
          </Providers>
        </body>
      </html>
    </ReduxProvider>
  );
}
