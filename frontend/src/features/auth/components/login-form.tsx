import { Link, useSearchParams } from 'react-router-dom';

import { paths } from '@/config/paths';
import { Button } from '@/shared/components/ui/button';
import { Form, Input } from '@/shared/components/ui/form';
import { useLogin } from '@/shared/lib/auth/auth';
import { loginInputSchema } from '@/shared/lib/auth/types';
import { useState } from 'react';

type LoginFormProps = {
  onSuccess: () => void;
};

export const LoginForm = ({ onSuccess }: LoginFormProps) => {
  const [error, setError] = useState<string | undefined>(undefined);
  const login = useLogin({
    onSuccess,
    onError: (err: any) => {
      setError(err.response.data || 'An unknown error occurred.');
    },
  });
  const [searchParams] = useSearchParams();
  const redirectTo = searchParams.get('redirectTo');

  return (
    <div>
      <Form
        onSubmit={(values) => {
          login.mutate(values);
        }}
        error={error}
        schema={loginInputSchema}
      >
        {({ register, formState }) => (
          <>
            <Input
              type="text"
              label="Username"
              error={formState.errors['username']}
              registration={register('username')}
            />
            <Input
              type="password"
              label="Password"
              error={formState.errors['password']}
              registration={register('password')}
            />
            <div>
              <Button
                isLoading={login.isPending}
                type="submit"
                className="w-full"
              >
                Log in
              </Button>
            </div>
          </>
        )}
      </Form>
      <div className="mt-2 flex items-center justify-end">
        <div className="text-sm">
          <Link
            to={paths.auth.register.getHref(redirectTo)}
            className="font-medium text-blue-600 hover:text-blue-500"
          >
            Register
          </Link>
        </div>
      </div>
    </div>
  );
};
