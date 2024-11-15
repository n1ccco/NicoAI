export type BaseEntity = {
  id: string;
  createdAt: number;
};

export type Entity<T> = {
  [K in keyof T]: T[K];
} & BaseEntity;

export type Meta = {
  page: number;
  total: number;
  totalPages: number;
};

export type User = Entity<{
  username: string;
  role: 'ADMIN' | 'USER';
}>;

export type AuthResponse = {
  jwt: string;
  user: User;
};

export type Discussion = Entity<{
  title: string;
  body: string;
  author: User;
}>;

export type Comment = Entity<{
  body: string;
  author: User;
}>;

export interface GenerateResponse {
  imageId: string;
}

export interface ImageBlob {
  imageBlob: string;
}

export interface ImageSimplified {
  id: string;
  countLikes: number;
  isLiked: boolean;
  imageBlob?: string;
}

export interface ImageDetailed extends ImageSimplified {
  authorId: string;
  authorName: string;
  promptData: Prompt;
  isPublic: boolean;
}
export interface InteractionImageRequest {
  action?: string;
}
export interface JwtRefreshResponse {
  token: string;
}
export interface Prompt {
  id: string;
  prompt: string;
  negativePrompt: string;
  height: string;
  width: string;
  numInterferenceSteps: string;
  guidanceScale: string;
}
