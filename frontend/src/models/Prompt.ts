interface Prompt {
  prompt: string
  negativePrompt: string
  height: number
  width: number
  numInterferenceSteps: number
  guidanceScale: number
}

export default Prompt
