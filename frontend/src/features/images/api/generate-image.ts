import { useMutation, useQueryClient } from '@tanstack/react-query';
import { z } from 'zod';

import { api } from '@/shared/lib/api-client';
import { MutationConfig } from '@/shared/lib/react-query';
import { GenerateResponse } from '@/shared/types/api';

export const generateImageInputSchema = z.object({
  prompt: z.string().min(1, 'Required'),
  negativePrompt: z.string().min(1, 'Required'),
  height: z
    .number()
    .int()
    .min(8, 'Min number is 8')
    .max(1024, 'Max number is 1024')
    .refine((val) => val % 8 === 0),
  width: z
    .number()
    .int()
    .min(8, 'Min number is 8')
    .max(1024, 'Max number is 1024')
    .refine((val) => val % 8 === 0),
  numInterferenceSteps: z
    .number()
    .int()
    .min(10, 'Min number is 10')
    .max(50, 'Max number is 50'),
  guidanceScale: z.number().int().min(2, 'Required').max(15, 'Required'),
});

export type GenerateImageInput = z.infer<typeof generateImageInputSchema>;

export const generateImage = ({
  prompt,
}: {
  prompt: GenerateImageInput;
}): Promise<GenerateResponse> => {
  return api.post(`/images`, prompt);
};

type UseGenerateImageOptions = {
  mutationConfig?: MutationConfig<typeof generateImage>;
};

export const useGenerateImage = ({
  mutationConfig,
}: UseGenerateImageOptions = {}) => {
  const queryClient = useQueryClient();

  const { onSuccess, ...restConfig } = mutationConfig || {};

  return useMutation({
    onSuccess: (...args) => {
      queryClient.invalidateQueries({
        queryKey: ['images'],
      });
      onSuccess?.(...args);
    },
    ...restConfig,
    mutationFn: generateImage,
  });
};
