import { queryOptions, useQuery } from '@tanstack/react-query';

import { api } from '@/shared/lib/api-client';
import { QueryConfig } from '@/shared/lib/react-query';
import { User } from '@/shared/types/api';

export const getUsers = (): Promise<User[]> => {
  return api.get(`/users`);
};

export const getUsersQueryOptions = () => {
  return queryOptions({
    queryKey: ['users'],
    queryFn: getUsers,
  });
};

type UseUsersOptions = {
  queryConfig?: QueryConfig<typeof getUsersQueryOptions>;
};

export const useUsers = ({ queryConfig }: UseUsersOptions = {}) => {
  return useQuery({
    ...getUsersQueryOptions(),
    ...queryConfig,
  });
};
