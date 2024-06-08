import { useState } from 'react'
import { useNavigate } from 'react-router-dom'
import { IMAGES } from '@/constants/routeContants'
import { PromptInput } from '@/types/formData'
import { postImageDataEffect } from '@/api/effects/images'

const InputField = ({
                      label,
                      value,
                      className,
                      setValue,
                      type = 'text',
                      min,
                      max,
                      step,
                    }: {
  label: string
  value: string
  className?: string
  setValue: (value: string) => void
  type?: string
  min?: number
  max?: number
  step?: number
}) => (
  <div className="mb-4 w-full">
    <label className="mb-2 block text-sm font-bold text-gray-700">
      {label}:
    </label>
    <div className="flex flex-row px-4 py-2">
      <input
        className={`${className || ''} focus:shadow-outline w-full appearance-none rounded border px-3 py-2 leading-tight text-gray-700 shadow focus:outline-none`}
        type={type}
        value={value}
        min={min}
        max={max}
        step={step}
        onChange={(e) => setValue(e.target.value)}
      />
      {className === 'slider' ? (
        <div className="value-display ml-5 text-center text-xl">{value}</div>
      ) : (
        ''
      )}
    </div>
  </div>
)

const Create = () => {
  const initialPrompt: PromptInput = {
    prompt: '',
    negativePrompt: '',
    height: 512,
    width: 512,
    numInterferenceSteps: 50,
    guidanceScale: 7,
  }

  const [promptData, setPromptData] = useState<PromptInput>(initialPrompt)
  const [loading, setLoading] = useState<boolean>(false)
  const [error, setError] = useState<string>('')
  const navigate = useNavigate()

  const handleSubmit = async () => {
    if (promptData.height % 8 !== 0 || promptData.width % 8 !== 0) {
      setError('Height and Width must be divisible by 8.')
      return
    }
    setLoading(true)
    postImageDataEffect(promptData)
      .then((res) => {
        if (res.type === 'success') {
          const imageId = res.state.imageId
          console.log(imageId)
          navigate(`/${IMAGES}/${imageId}`)
        } else {
          console.error(res.state.error)
        }
      })
      .finally(() => {
        setLoading(false)
      })
  }

  return (
    <div className="mx-auto mt-8 max-w-lg flex items-center flex-col">
      <h1 className="mb-4 text-3xl font-bold">
        Generate Image with Stable Diffusion
      </h1>
      <InputField
        label="Prompt"
        value={promptData.prompt}
        setValue={(value) => setPromptData({ ...promptData, prompt: value })}
      />
      <InputField
        label="Negative Prompt"
        value={promptData.negativePrompt}
        setValue={(value) =>
          setPromptData({ ...promptData, negativePrompt: value })
        }
        step={8}
      />
      <InputField
        label="Height"
        value={promptData.height.toString()}
        setValue={(value) =>
          setPromptData({ ...promptData, height: parseInt(value) })
        }
        type="number"
        step={8}
      />
      <InputField
        label="Width"
        value={promptData.width.toString()}
        setValue={(value) =>
          setPromptData({ ...promptData, width: parseInt(value) })
        }
        type="number"
      />
      <InputField
        label="Number of Interference Steps"
        value={promptData.numInterferenceSteps.toString()}
        setValue={(value) =>
          setPromptData({
            ...promptData,
            numInterferenceSteps: parseInt(value),
          })
        }
        className="slider"
        type="range"
        min={10}
        max={50}
      />
      <InputField
        label="Guidance Scale"
        value={promptData.guidanceScale.toString()}
        setValue={(value) =>
          setPromptData({ ...promptData, guidanceScale: parseInt(value) })
        }
        className="slider"
        type="range"
        min={2}
        max={15}
      />
      <button
        className="focus:shadow-outline rounded bg-blue-500 px-4 py-2 font-bold text-white hover:bg-blue-700 focus:outline-none"
        onClick={handleSubmit}
        disabled={loading}
      >
        {loading ? 'Generating...' : 'Generate Image'}
      </button>
      {error && <p className="mt-2 text-red-500">{error}</p>}
    </div>
  )
}

export default Create
