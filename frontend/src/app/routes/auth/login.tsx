import { useNavigate, useSearchParams } from 'react-router-dom';

import { LoginForm } from '@/features/auth/components/login-form';
import { AuthLayout } from '@/shared/components/layouts/auth-layout';

export const LoginRoute = () => {
  const navigate = useNavigate();
  const [searchParams] = useSearchParams();
  const redirectTo = searchParams.get('redirectTo');

  return (
    <AuthLayout title="Log in to your account">
      <LoginForm
        onSuccess={() => {
          navigate(`${redirectTo ? `${redirectTo}` : '/app'}`, {
            replace: true,
          });
        }}
      />
    </AuthLayout>
  );
};
