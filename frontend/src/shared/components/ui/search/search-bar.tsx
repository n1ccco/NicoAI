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
    <input
      type="text"
      value={keyword}
      onChange={handleChange}
      className="h-9 rounded-md px-3 text-sm shadow-md"
      placeholder={placeholder}
    />
  );
};
