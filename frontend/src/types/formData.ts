export type SigninInput = {
  username: string
  password: string
}
export type SignupInput = {
  username: string
  password: string
}

export type PromptInput = {
  prompt: string
  negativePrompt: string
  height: number
  width: number
  numInterferenceSteps: number
  guidanceScale: number
}
