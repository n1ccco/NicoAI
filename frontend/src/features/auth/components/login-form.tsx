import { Link, useSearchParams } from 'react-router-dom';

import { Button } from '@/components/ui/button';
import { Form, Input } from '@/components/ui/form';
import { useLogin, loginInputSchema } from '@/lib/auth';

type LoginFormProps = {
  onSuccess: () => void;
};

export const LoginForm = ({ onSuccess }: LoginFormProps) => {
  const login = useLogin({
    onSuccess,
  });
  const [searchParams] = useSearchParams();
  const redirectTo = searchParams.get('redirectTo');

  return (
    <div>
      <Form
        onSubmit={(values) => {
          login.mutate(values);
        }}
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
            to={`/auth/register${redirectTo ? `?redirectTo=${encodeURIComponent(redirectTo)}` : ''}`}
            className="font-medium text-blue-600 hover:text-blue-500"
          >
            Register
          </Link>
        </div>
      </div>
    </div>
  );
};