import { ContentLayout } from '@/shared/components/layouts';
import { useUser } from '@/shared/lib/auth/auth';
import { ROLES } from '@/shared/lib/auth/authorization';

export const DashboardRoute = () => {
  const user = useUser();
  return (
    <ContentLayout title="Dashboard">
      <h1 className="text-xl">
        Welcome, <b>{user.data?.username || 'User'}</b>
      </h1>
      <h4 className="my-3">
        Your role is: <b>{user.data?.role || 'Unknown'}</b>
      </h4>
      <p className="font-medium">In this application, you can:</p>
      {user.data?.role?.includes(ROLES.USER) && (
        <ul className="my-4 list-inside list-disc">
          <li>Generate images</li>
          <li>Comment on images</li>
          <li>Delete your own comments</li>
          <li>Delete your own images</li>
        </ul>
      )}
      {user.data?.role?.includes(ROLES.ADMIN) && (
        <ul className="my-4 list-inside list-disc">
          <li>Generate images</li>
          <li>Comment on images</li>
          <li>Delete all images</li>
          <li>Delete all comments</li>
        </ul>
      )}
    </ContentLayout>
  );
};
