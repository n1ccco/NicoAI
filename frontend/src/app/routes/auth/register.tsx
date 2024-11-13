import { useNavigate, useSearchParams } from 'react-router-dom';

import { RegisterForm } from '@/features/auth/components/register-form';
import { AuthLayout } from '@/shared/components/layouts/auth-layout';

export const RegisterRoute = () => {
  const navigate = useNavigate();
  const [searchParams] = useSearchParams();
  const redirectTo = searchParams.get('redirectTo');

  return (
    <AuthLayout title="Register your account">
      <RegisterForm
        onSuccess={() =>
          navigate(`${redirectTo ? `${redirectTo}` : '/app'}`, {
            replace: true,
          })
        }
      />
    </AuthLayout>
  );
};
