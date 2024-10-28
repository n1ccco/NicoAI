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
    <div>
      <label htmlFor="sortBy" className="mr-2">
        Sort by:
      </label>
      <select
        id="sortBy"
        value={`${defaultSortBy}-${defaultSortDirection}`}
        onChange={handleSortChange}
        className="border p-2"
      >
        <option value="date-asc">Date Ascending</option>
        <option value="date-desc">Date Descending</option>
        <option value="likes-asc">Likes Ascending</option>
        <option value="likes-desc">Likes Descending</option>
      </select>
    </div>
  );
};
