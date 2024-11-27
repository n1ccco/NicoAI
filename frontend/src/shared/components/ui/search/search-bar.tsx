import * as React from 'react';

export type SearchProps = {
  placeholder?: string;
  onSearch: (keyword: string) => void;
};

export const SearchBar = ({
  placeholder = 'Search...',
  onSearch,
}: SearchProps) => {
  const [keyword, setKeyword] = React.useState('');

  const timerRef = React.useRef<NodeJS.Timeout | null>(null);

  const handleChange = (event: React.ChangeEvent<HTMLInputElement>) => {
    const value = event.target.value;
    setKeyword(value);

    if (timerRef.current) {
      clearTimeout(timerRef.current);
    }

    timerRef.current = setTimeout(() => {
      onSearch(value);
    }, 500);
  };

  return (
    <div className="flex w-full max-w-lg items-center space-x-4 rounded-md bg-white shadow-md">
      <input
        type="text"
        value={keyword}
        onChange={handleChange}
        className="w-full rounded-md border border-gray-300 p-2 focus:outline-none focus:ring-2 focus:ring-blue-500"
        placeholder={placeholder}
      />
    </div>
  );
};
