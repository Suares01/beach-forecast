'use client';

import GoogleSocialButton from '@src/components/GoogleSocialButton';
import { useAuth } from '@src/contexts/AuthContext';
import * as Form from '@radix-ui/react-form';
import Image from 'next/image';
import Link from 'next/link';
import ControlledInput from '@src/components/ControlledInput';
import { BsAt, BsLockFill } from 'react-icons/bs';
import { useForm } from 'react-hook-form';
import { zodResolver } from '@hookform/resolvers/zod';
import { z } from 'zod';
import clsx from 'clsx';
import Spinner from '@src/components/Spinner';

const loginCredentialsFormSchema = z.object({
  username: z.string().trim().min(1, 'Forneça seu nome.'),
  password: z.string().trim().min(1, 'Forneça sua senha.'),
});

type FormData = z.infer<typeof loginCredentialsFormSchema>;

export default function Login() {
  const { loginWithCredentials } = useAuth();
  const { control, handleSubmit } = useForm<FormData>({
    defaultValues: {
      username: '',
      password: '',
    },
    resolver: zodResolver(loginCredentialsFormSchema),
  });

  const { mutate, isPending } = loginWithCredentials;

  function onSubmit({ username, password }: FormData) {
    mutate({ username, password });
  }

  return (
    <section className="relative flex flex-wrap lg:h-screen lg:items-center">
      <div className="w-full px-4 py-12 sm:px-6 sm:py-16 lg:w-1/2 lg:px-8 lg:py-24">
        <div className="mx-auto max-w-lg text-center">
          <h1 className="text-2xl font-bold sm:text-3xl">Welcome back!</h1>

          <p className="mt-4 text-gray-500">
            Do not waste time! We have new predictions for your beaches!
          </p>
        </div>

        <div className="mx-auto max-w-lg text-center mt-4">
          <GoogleSocialButton disabled={isPending} />
        </div>

        <Form.Root
          onSubmit={handleSubmit(onSubmit)}
          className="mx-auto mb-0 mt-4 max-w-md space-y-4"
        >
          <ControlledInput
            id="username"
            name="username"
            type="text"
            placeholder="Enter username"
            control={control}
            className="w-full rounded-lg border-gray-200 p-4 pe-12 text-sm shadow-sm"
            icon={BsAt}
            disabled={isPending}
          />
          <ControlledInput
            id="password"
            name="password"
            type="password"
            placeholder="Enter password"
            control={control}
            className="w-full rounded-lg border-gray-200 p-4 pe-12 text-sm shadow-sm"
            icon={BsLockFill}
            disabled={isPending}
          />

          <div className="flex items-center justify-between">
            <p className="text-sm text-gray-500">
              No account?
              <Link className="underline" href="/register">
                Register
              </Link>
            </p>

            <Form.Submit
              disabled={isPending}
              className={clsx(
                'inline-block rounded-lg bg-blue-500 px-5 py-3 text-sm font-medium text-white',
                {
                  'bg-blue-400': isPending,
                },
              )}
            >
              {isPending ? <Spinner /> : 'Login'}
            </Form.Submit>
          </div>
        </Form.Root>
      </div>

      <div className="relative h-64 w-full sm:h-96 lg:h-full lg:w-1/2">
        <Image
          src="/praia-login.avif"
          alt="Welcome"
          className="absolute inset-0 object-cover"
          fill
          priority
        />
      </div>
    </section>
  );
}
