import {
  randUserName,
  randParagraph,
  randUuid,
  randPassword,
  randNumber,
  randBoolean,
  randSentence,
} from '@ngneat/falso';

const generateUser = () => ({
  id: randUuid() + Math.random(),
  username: randUserName({ withAccents: false }),
  password: randPassword(),
  role: 'ADMIN',
  createdAt: Date.now(),
});

export const createUser = <T extends Partial<ReturnType<typeof generateUser>>>(
  overrides?: T,
) => {
  return { ...generateUser(), ...overrides };
};

const generateImage = () => ({
  id: randUuid(),
  countLikes: randNumber(),
  isLiked: randBoolean(),
  prompt: randSentence(),
  negativePrompt: randSentence(),
  height: 512,
  width: 512,
  numInterferenceSteps: randNumber({ min: 10, max: 50 }),
  guidanceScale: randNumber({ min: 2, max: 15 }),
});

export const createImage = <
  T extends Partial<ReturnType<typeof generateImage>>,
>(
  overrides?: T & {
    authorId?: string;
  },
) => {
  return { ...generateImage(), ...overrides };
};

const generateComment = () => ({
  id: randUuid(),
  body: randParagraph(),
  createdAt: Date.now(),
});

export const createComment = <
  T extends Partial<ReturnType<typeof generateComment>>,
>(
  overrides?: T & {
    authorId?: string;
    imageId?: string;
  },
) => {
  return { ...generateComment(), ...overrides };
};
