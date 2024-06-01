import { useState } from 'react'
import { useNavigate } from 'react-router-dom'
import { IMAGES } from '@/constants/routeContants'
import { PromptInput } from '@/types/formData'
import { postImageDataEffect } from '@/api/effects/images'

const InputField = ({
  label,
  value,
  setValue,
  type = 'text',
}: {
  label: string
  value: string
  setValue: (value: string) => void
  type?: string
}) => (
  <div className="mb-4">
    <label className="mb-2 block text-sm font-bold text-gray-700">
      {label}:
    </label>
    <input
      className="focus:shadow-outline w-full appearance-none rounded border px-3 py-2 leading-tight text-gray-700 shadow focus:outline-none"
      type={type}
      value={value}
      onChange={(e) => setValue(e.target.value)}
    />
  </div>
)

const Create = () => {
  const initialPrompt: PromptInput = {
    prompt: 'cat',
    negativePrompt: 'ugly',
    height: 512,
    width: 512,
    numInterferenceSteps: 50,
    guidanceScale: 7,
  }

  const [promptData, setPromptData] = useState<PromptInput>(initialPrompt)
  const [loading, setLoading] = useState<boolean>(false)
  const navigate = useNavigate()

  const handleSubmit = async () => {
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
    <div className="mx-auto mt-8 max-w-lg">
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
      />
      <InputField
        label="Height"
        value={promptData.height.toString()}
        setValue={(value) =>
          setPromptData({ ...promptData, height: parseInt(value) })
        }
        type="number"
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
        type="number"
      />
      <InputField
        label="Guidance Scale"
        value={promptData.guidanceScale.toString()}
        setValue={(value) =>
          setPromptData({ ...promptData, guidanceScale: parseInt(value) })
        }
        type="number"
      />
      <button
        className="focus:shadow-outline rounded bg-blue-500 px-4 py-2 font-bold text-white hover:bg-blue-700 focus:outline-none"
        onClick={handleSubmit}
        disabled={loading}
      >
        {loading ? 'Generating...' : 'Generate Image'}
      </button>
    </div>
  )
}

export default Create
