export type BaseEntity = {
  id: number
}

export type Entity<T> = {
  [K in keyof T]: T[K]
} & BaseEntity

export type Photo = Entity<{
  description: string
  isPublic: boolean
  isLiked: boolean
  authorId: number
  imageData: string
}>

export type User = Entity<{
  username: string
}>

export type AuthResponse = {
  jwt: string
  user: User
}

export type Comment = Entity<{
  body: string
  author: User
}>
