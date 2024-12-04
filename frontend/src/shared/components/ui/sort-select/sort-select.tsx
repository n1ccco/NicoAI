import { FC } from 'react';
import { useSearchParams } from 'react-router-dom';

interface SortMenuProps {
  defaultSortBy: string;
  defaultSortDirection: string;
}

export const SortMenu: FC<SortMenuProps> = ({
  defaultSortBy,
  defaultSortDirection,
}) => {
  const [, setSearchParams] = useSearchParams(); // Discarding searchParams if not used

  const handleSortChange = (e: React.ChangeEvent<HTMLSelectElement>) => {
    const [key, value] = e.target.value.split('-');
    setSearchParams({ sortBy: key, sortDirection: value });
  };

  return (
    <div className="flex h-9 justify-between rounded-md pl-3 text-xs shadow-md">
      <div className="flex h-full items-center justify-center">
        <label htmlFor="sortBy" className="text-lg">
          Sort by:
        </label>
      </div>
      <select
        id="sortBy"
        value={`${defaultSortBy}-${defaultSortDirection}`}
        onChange={handleSortChange}
        className="rounded-md bg-transparent px-3 text-sm"
      >
        <option value="date-asc">Date Ascending</option>
        <option value="date-desc">Date Descending</option>
        <option value="likes-asc">Likes Ascending</option>
        <option value="likes-desc">Likes Descending</option>
      </select>
    </div>
  );
};
