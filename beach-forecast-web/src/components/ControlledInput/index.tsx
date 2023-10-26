import { InputHTMLAttributes } from 'react';
import { Control, Controller } from 'react-hook-form';
import * as Form from '@radix-ui/react-form';
import clsx from 'clsx';
import { IconType } from 'react-icons';

export interface ControlledInputProps
  extends InputHTMLAttributes<HTMLInputElement> {
  name: string;
  label?: string;
  control: Control<any>;
  icon?: IconType;
  helpText?: string;
}

export default function ControlledInput({
  name,
  control,
  label,
  className,
  icon: Icon,
  helpText,
  ...rest
}: ControlledInputProps) {
  return (
    <Controller
      name={name}
      control={control}
      render={({ field, fieldState: { error } }) => (
        <Form.Field name={name}>
          {label && (
            <Form.Label className="block text-sm font-medium text-gray-700">
              {label}
            </Form.Label>
          )}
          <div className="relative">
            <Form.Control asChild>
              <input
                {...field}
                {...rest}
                className={clsx(
                  `relative disabled:bg-slate-50 disabled:text-slate-500 disabled:border-slate-200 disabled:shadow-none ${className}`,
                  {
                    'border-red-700 focus:ring-red-700 focus:border-red-700':
                      !!error,
                  },
                )}
              />
            </Form.Control>
            {Icon && (
              <span className="absolute inset-y-0 end-0 grid place-content-center px-4">
                <Icon className="h-4 w-4 text-gray-400" />
              </span>
            )}
          </div>
          {(error || helpText) && (
            <Form.Message
              className={clsx('text-xs', {
                'text-gray-400': !error,
                'text-red-700': !!error,
              })}
            >
              {error?.message ?? helpText}
            </Form.Message>
          )}
        </Form.Field>
      )}
    />
  );
}
