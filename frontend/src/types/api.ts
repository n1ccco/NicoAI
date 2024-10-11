export type BaseEntity = {
  id: number
}

export type Entity<T> = {
  [K in keyof T]: T[K]
} & BaseEntity

export type PromptData = Entity<{
  prompt: string
  negativePrompt: string
  height: number
  width: number
  numInterferenceSteps: number
  guidanceScale: number
}>

export type Photo = Entity<{
  promptData: PromptData
  isPublic: boolean
  isLiked: boolean
  authorId: number
  countLikes: number
  authorName: string
  imageData: string
}>

export type PhotoSimplified = Entity<{
  isLiked: boolean
  countLikes: number
}>

export type PhotoBlob = Entity<{
  imageBlob: string
}>

export type User = Entity<{
  username: string
}>

export type AuthResponse = {
  jwt: string
  user: User
}

export type CommentData = Entity<{
  authorId: number
  body: string
  authorName: string
  createdAt: Date
}>

export type RefreshTokenData = {
  token: string
}