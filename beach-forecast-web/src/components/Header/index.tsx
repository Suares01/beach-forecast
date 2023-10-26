'use client';

import { useAuth } from '@src/contexts/AuthContext';
import * as DropdownMenu from '@radix-ui/react-dropdown-menu';
import * as NavigationMenu from '@radix-ui/react-navigation-menu';
import Image from 'next/image';
import Link from 'next/link';

const LINKS = [
  { label: 'About', href: '/' },
  { label: 'Features', href: '/' },
  { label: 'Contribute', href: '/' },
  { label: 'Help', href: '/' },
  { label: 'FAQ', href: '/' },
];

export default function Header() {
  const { isAuthenticated, isLoading } = useAuth();

  return (
    <header className="bg-white">
      <div className="mx-auto max-w-screen-xl px-4 sm:px-6 lg:px-8">
        <div className="flex h-16 items-center justify-between">
          <div className="md:flex md:items-center md:gap-12">
            <Link className="block text-sky-600" href="/">
              <span className="sr-only">Home</span>
              <Image
                src="/beach-forecast-logo-removebg.png"
                alt="Logo"
                height={70}
                width={70}
                priority
              />
            </Link>
          </div>

          <NavigationMenu.Root className="hidden md:block">
            <NavigationMenu.List className="flex items-center gap-6 text-sm">
              {LINKS.map(({ label, href }) => (
                <NavigationMenu.Item key={label}>
                  <NavigationMenu.Link asChild>
                    <Link
                      href={href}
                      className="text-gray-500 transition hover:text-gray-500/75"
                    >
                      {label}
                    </Link>
                  </NavigationMenu.Link>
                </NavigationMenu.Item>
              ))}
            </NavigationMenu.List>
          </NavigationMenu.Root>

          <div className="flex items-center gap-4">
            {isLoading ? (
              <div
                className="inline-block h-8 w-8 animate-spin rounded-full border-4 border-solid border-current border-r-transparent align-[-0.125em] text-sky-600 motion-reduce:animate-[spin_1.5s_linear_infinite]"
                role="status"
              >
                <span className="!absolute !-m-px !h-px !w-px !overflow-hidden !whitespace-nowrap !border-0 !p-0 ![clip:rect(0,0,0,0)]">
                  Loading...
                </span>
              </div>
            ) : isAuthenticated ? (
              <Link
                className="rounded-md bg-sky-600 px-5 py-2.5 text-sm font-medium text-white shadow"
                href="/forecasts"
              >
                Entrar
              </Link>
            ) : (
              <div className="sm:flex sm:gap-4">
                <Link
                  className="rounded-md bg-sky-600 px-5 py-2.5 text-sm font-medium text-white shadow"
                  href="/login"
                >
                  Login
                </Link>

                <div className="hidden sm:flex">
                  <Link
                    className="rounded-md bg-gray-100 px-5 py-2.5 text-sm font-medium text-sky-600"
                    href="/register"
                  >
                    Register
                  </Link>
                </div>
              </div>
            )}

            <div className="block md:hidden">
              <DropdownMenu.Root>
                <DropdownMenu.Trigger asChild>
                  <button className="rounded bg-gray-100 p-2 text-gray-600 transition hover:text-gray-600/75">
                    <svg
                      xmlns="http://www.w3.org/2000/svg"
                      className="h-5 w-5"
                      fill="none"
                      viewBox="0 0 24 24"
                      stroke="currentColor"
                      strokeWidth="2"
                    >
                      <path
                        strokeLinecap="round"
                        strokeLinejoin="round"
                        d="M4 6h16M4 12h16M4 18h16"
                      />
                    </svg>
                  </button>
                </DropdownMenu.Trigger>

                <DropdownMenu.Portal>
                  <DropdownMenu.Content
                    sideOffset={14}
                    className="z-20 rounded-md border border-gray-100 bg-white shadow-lg"
                  >
                    <DropdownMenu.Group className="p-1">
                      {LINKS.map(({ label, href }) => (
                        <DropdownMenu.Item key={label} asChild>
                          <Link
                            href={href}
                            className="block rounded-lg px-4 py-2 text-sm text-gray-500 hover:bg-gray-50 hover:text-gray-700 cursor-pointer"
                          >
                            {label}
                          </Link>
                        </DropdownMenu.Item>
                      ))}
                    </DropdownMenu.Group>
                    {!isAuthenticated && (
                      <>
                        <DropdownMenu.Separator className="h-[1px] m-1 bg-gray-100 flex sm:hidden" />
                        <DropdownMenu.Item asChild>
                          {isLoading ? (
                            <div className="border-t-transparent border-solid animate-spin rounded-full border-blue-400 border-2 h-8 w-8"></div>
                          ) : (
                            <div className="flex sm:hidden p-1 items-center w-full">
                              <Link
                                className="rounded-md bg-sky-600 px-5 py-2.5 text-sm font-medium text-white shadow"
                                href="/register"
                              >
                                Register
                              </Link>
                            </div>
                          )}
                        </DropdownMenu.Item>
                      </>
                    )}
                  </DropdownMenu.Content>
                </DropdownMenu.Portal>
              </DropdownMenu.Root>
            </div>
          </div>
        </div>
      </div>
    </header>
  );
}
