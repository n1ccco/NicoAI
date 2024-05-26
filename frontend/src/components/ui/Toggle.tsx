import { useState } from 'react'

interface ToggleProps {
  toggleName: string
  initialState: boolean
  onToggle: (newState: boolean) => void
}

function Toggle({ initialState, onToggle, toggleName }: ToggleProps) {
  const [toggleState, setToggleState] = useState<boolean>(initialState)

  const handleToggle = () => {
    const newState = !toggleState
    setToggleState(newState)
    onToggle(newState)
  }

  return (
    <div className="flex items-center space-x-2">
      <input
        type="checkbox"
        checked={toggleState}
        onChange={handleToggle}
        id="toggle"
        className="hidden"
      />
      <label
        htmlFor="toggle"
        className={`${
          toggleState ? 'bg-blue-500' : 'bg-gray-200'
        } relative inline-block h-6 w-10 cursor-pointer rounded-full transition-all duration-300`}
      >
        <span
          className={`${
            toggleState ? 'translate-x-6' : 'translate-x-0'
          } inline-block h-4 w-4 transform rounded-full bg-white shadow-md transition-all duration-300`}
        />
      </label>
      <span className="text-gray-300">{toggleName}</span>
    </div>
  )
}

export default Toggle
