'use client';

import GoogleSocialButton from '@src/components/GoogleSocialButton';
import Image from 'next/image';
import Link from 'next/link';
import * as Form from '@radix-ui/react-form';
import ControlledInput from '@src/components/ControlledInput';
import { useForm } from 'react-hook-form';
import { zodResolver } from '@hookform/resolvers/zod';
import { z } from 'zod';
import { useAuth } from '@src/contexts/AuthContext';
import Spinner from '@src/components/Spinner';
import clsx from 'clsx';

const PASSWORD_REGEX = /^(?=.*[A-Za-z])(?=.*\d)[A-Za-z\d]{8,}$/gm;

const registerFormSchema = z
  .object({
    firstName: z.string().trim().min(1, 'Forneça seu nome.'),
    lastName: z.string().trim().optional(),
    username: z.string().trim().min(1, 'Forneça seu username.'),
    email: z.string().trim().email('Forneça um e-mail válido.'),
    password: z
      .string()
      .trim()
      .regex(
        PASSWORD_REGEX,
        'A senha precisa ter, no mínimo, 8 caracteres, uma letra e um número.',
      ),
    passwordConfirmation: z.string().trim().min(1, 'Confirme a senha.'),
  })
  .refine((fields) => fields.password === fields.passwordConfirmation, {
    message: 'As senhas não condizem.',
    path: ['passwordConfirmation'],
  });

type FormData = z.infer<typeof registerFormSchema>;

export default function Register() {
  const { register } = useAuth();
  const { control, handleSubmit } = useForm<FormData>({
    resolver: zodResolver(registerFormSchema),
    defaultValues: {
      firstName: '',
      lastName: '',
      username: '',
      email: '',
      password: '',
      passwordConfirmation: '',
    },
  });

  const { mutate, isPending } = register;

  function onSubmit(data: FormData) {
    mutate(data);
  }

  return (
    <section className="bg-white">
      <div className="lg:grid lg:min-h-screen lg:grid-cols-12">
        <aside className="relative block h-16 lg:order-last lg:col-span-5 lg:h-full xl:col-span-6">
          <Image
            src="/praia-register.avif"
            alt="Welcome to Beach Forecast"
            className="absolute inset-0 object-cover"
            fill
            priority
          />
        </aside>

        <main className="flex items-center justify-center px-8 py-8 sm:px-12 lg:col-span-7 lg:px-16 lg:py-12 xl:col-span-6">
          <div className="max-w-xl lg:max-w-3xl">
            <a className="block text-blue-600" href="/">
              <span className="sr-only">Home</span>
              <Image
                src="/beach-forecast-logo-removebg.png"
                alt="Logo"
                width={80}
                height={80}
              />
            </a>

            <h1 className="mt-6 text-2xl font-bold text-gray-900 sm:text-3xl md:text-4xl">
              Welcome to Beach Forecast
            </h1>

            <p className="mt-4 leading-relaxed text-gray-500">
              Create your account! The beaches are waiting for you!
            </p>

            <div className="mt-4">
              <GoogleSocialButton />
            </div>

            <Form.Root
              onSubmit={handleSubmit(onSubmit)}
              className="mt-4 grid grid-cols-6 gap-6"
              noValidate
            >
              <div className="col-span-6 sm:col-span-3">
                <ControlledInput
                  id="firstName"
                  name="firstName"
                  type="text"
                  label="First Name"
                  control={control}
                  className="mt-1 w-full rounded-md border-gray-200 bg-white text-sm text-gray-700 shadow-sm"
                  disabled={isPending}
                />
              </div>

              <div className="col-span-6 sm:col-span-3">
                <ControlledInput
                  id="lastName"
                  name="lastName"
                  type="text"
                  label="Last Name"
                  control={control}
                  className="mt-1 w-full rounded-md border-gray-200 bg-white text-sm text-gray-700 shadow-sm"
                  disabled={isPending}
                />
              </div>

              <div className="col-span-6 sm:col-span-3">
                <ControlledInput
                  id="username"
                  name="username"
                  type="text"
                  label="Username"
                  control={control}
                  className="mt-1 w-full rounded-md border-gray-200 bg-white text-sm text-gray-700 shadow-sm"
                  helpText="Seu username precisa ser único."
                  disabled={isPending}
                />
              </div>

              <div className="col-span-6 sm:col-span-3">
                <ControlledInput
                  id="email"
                  name="email"
                  type="email"
                  label="E-mail"
                  control={control}
                  className="mt-1 w-full rounded-md border-gray-200 bg-white text-sm text-gray-700 shadow-sm"
                  helpText="Seu e-mail será usado apenas para fins de autenticação."
                  disabled={isPending}
                />
              </div>

              <div className="col-span-6 sm:col-span-3">
                <ControlledInput
                  id="password"
                  name="password"
                  type="password"
                  label="Password"
                  control={control}
                  className="mt-1 w-full rounded-md border-gray-200 bg-white text-sm text-gray-700 shadow-sm"
                  helpText="A senha precisa ter, no mínimo, 8 caracteres, uma letra e um número."
                  disabled={isPending}
                />
              </div>

              <div className="col-span-6 sm:col-span-3">
                <ControlledInput
                  id="passwordConfirmation"
                  name="passwordConfirmation"
                  type="password"
                  label="Password Confirmation"
                  control={control}
                  className="mt-1 w-full rounded-md border-gray-200 bg-white text-sm text-gray-700 shadow-sm"
                  disabled={isPending}
                />
              </div>

              <div className="col-span-6">
                <p className="text-sm text-gray-500">
                  By creating an account, you agree to our{' '}
                  <Link
                    href="#"
                    target="_blank"
                    className="text-gray-700 underline"
                  >
                    terms and conditions
                  </Link>{' '}
                  and{' '}
                  <Link
                    href="#"
                    target="_blank"
                    className="text-gray-700 underline"
                  >
                    privacy policy
                  </Link>
                  .
                </p>
              </div>

              <div className="col-span-6 sm:flex sm:items-center sm:gap-4">
                <Form.Submit
                  disabled={isPending}
                  className={clsx(
                    'inline-block rounded-lg bg-blue-500 px-5 py-3 text-sm font-medium text-white',
                    {
                      'bg-blue-400': isPending,
                    },
                  )}
                >
                  {isPending ? <Spinner /> : 'Create an account'}
                </Form.Submit>

                <p className="mt-4 text-sm text-gray-500 sm:mt-0">
                  Already have an account?
                  <Link href="/login" className="text-gray-700 underline">
                    Login
                  </Link>
                  .
                </p>
              </div>
            </Form.Root>
          </div>
        </main>
      </div>
    </section>
  );
}
