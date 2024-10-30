import { z } from 'zod';

export const loginInputSchema = z.object({
  username: z.string().min(1, 'Required'),
  password: z.string().min(5, 'Required'),
});

export const registerInputSchema = z.object({
  username: z.string().min(5, 'Required'),
  password: z.string().min(5, 'Required'),
});

export type LoginInput = z.infer<typeof loginInputSchema>;
export type RegisterInput = z.infer<typeof registerInputSchema>;
