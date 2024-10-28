import { Navigate, useLocation } from 'react-router-dom';

import { paths } from '@/config/paths';
import { useUser } from '@/shared/lib/auth/auth';

export const ProtectedRoute = ({ children }: { children: React.ReactNode }) => {
  const user = useUser();
  const location = useLocation();

  if (!user.data) {
    return (
      <Navigate to={paths.auth.login.getHref(location.pathname)} replace />
    );
  }

  return children;
};
