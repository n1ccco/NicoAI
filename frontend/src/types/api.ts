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
  discussionId: string;
  author: User;
}>;

export interface AuthenticationRequest {
  username: string;
  password: string;
}
export interface CommentRequest {
  body: string;
  imageId: number;
}
export interface CommentResponse {
  id: number;
  authorId: number;
  authorName: string;
  body: string;
  createdAt: string;
}
export interface GenerateResponse {
  imageId: number;
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
  imageData: Array<string>;
  promptData: PromptData;
  isPublic: boolean;
}
export interface InteractionImageRequest {
  action?: string;
}
export interface JwtRefreshResponse {
  token: string;
}
export interface PromptData {
  id?: number;
  prompt?: string;
  negativePrompt?: string;
  height?: number;
  width?: number;
  numInterferenceSteps?: number;
  guidanceScale?: number;
}
export interface PromptRequest {
  prompt?: string;
  negativePrompt?: string;
  height?: number;
  width?: number;
  numInterferenceSteps?: number;
  guidanceScale?: number;
}
