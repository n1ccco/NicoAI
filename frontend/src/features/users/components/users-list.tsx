import { Spinner } from '@/shared/components/ui/spinner';
import { Table } from '@/shared/components/ui/table';
import { formatDate } from '@/shared/utils/format';

import { useUsers } from '../api/get-users';

import { DeleteUser } from './delete-user';
import { Link } from '@/shared/components/ui/link';
import { paths } from '@/config/paths';

export const UsersList = () => {
  const usersQuery = useUsers();

  if (usersQuery.isLoading) {
    return (
      <div className="flex h-48 w-full items-center justify-center">
        <Spinner size="lg" />
      </div>
    );
  }

  const users = usersQuery.data;

  if (!users) return null;

  return (
    <Table
      data={users}
      columns={[
        {
          title: 'Id',
          field: 'id',
        },
        {
          title: 'Username',
          field: 'username',
          Cell({ entry: { id, username } }) {
            return <Link to={paths.app.user.getHref(id)}>{username}</Link>;
          },
        },
        {
          title: 'Role',
          field: 'role',
        },
        {
          title: 'Created At',
          field: 'createdAt',
          Cell({ entry: { createdAt } }) {
            return <span>{formatDate(createdAt)}</span>;
          },
        },
        {
          title: 'Delete user',
          field: 'id',
          Cell({ entry: { id } }) {
            return <DeleteUser id={id} />;
          },
        },
      ]}
    />
  );
};
