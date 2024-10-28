import {
  randCompanyName,
  randUserName,
  randParagraph,
  randUuid,
  randPassword,
  randCatchPhrase,
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

const generateTeam = () => ({
  id: randUuid(),
  name: randCompanyName(),
  description: randParagraph(),
  createdAt: Date.now(),
});

export const createTeam = <T extends Partial<ReturnType<typeof generateTeam>>>(
  overrides?: T,
) => {
  return { ...generateTeam(), ...overrides };
};

const generateDiscussion = () => ({
  id: randUuid(),
  title: randCatchPhrase(),
  body: randParagraph(),
  createdAt: Date.now(),
});

export const createDiscussion = <
  T extends Partial<ReturnType<typeof generateDiscussion>>,
>(
  overrides?: T & {
    authorId?: string;
  },
) => {
  return { ...generateDiscussion(), ...overrides };
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
    discussionId?: string;
  },
) => {
  return { ...generateComment(), ...overrides };
};
