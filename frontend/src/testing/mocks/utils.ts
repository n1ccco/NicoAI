import Cookies from 'js-cookie';
import { delay } from 'msw';

import { db } from './db';

const JWT_EXPIRATION_TIME = 3600;

export const encode = (obj: any) => {
  const base64UrlEncode = (str: string) => {
    return btoa(str).replace(/\+/g, '-').replace(/\//g, '_').replace(/=+$/, '');
  };

  const header = {
    alg: 'HS256',
    typ: 'JWT',
  };

  const expiration = Math.floor(Date.now() / 1000) + JWT_EXPIRATION_TIME;
  const payload = { ...obj, exp: expiration };

  const headerEncoded = base64UrlEncode(JSON.stringify(header));
  const payloadEncoded = base64UrlEncode(JSON.stringify(payload));

  const signature = base64UrlEncode(`${headerEncoded}.${payloadEncoded}`);

  return `${headerEncoded}.${payloadEncoded}.${signature}`;
};

export const decode = (token: string) => {
  const [headerEncoded, payloadEncoded, signature] = token.split('.');

  const base64UrlDecode = (str: string) => {
    return atob(str.replace(/-/g, '+').replace(/_/g, '/'));
  };

  try {
    const payloadDecoded = base64UrlDecode(payloadEncoded);
    return JSON.parse(payloadDecoded);
  } catch {
    throw new Error('Invalid token');
  }
};

export const hash = (str: string) => {
  let hash = 5381,
    i = str.length;

  while (i) {
    hash = (hash * 33) ^ str.charCodeAt(--i);
  }
  return String(hash >>> 0);
};

export const networkDelay = () => {
  const delayTime = import.meta.env.TEST
    ? 200
    : Math.floor(Math.random() * 700) + 300;
  return delay(delayTime);
};

const omit = <T extends object>(obj: T, keys: string[]): T => {
  const result = {} as T;
  for (const key in obj) {
    if (!keys.includes(key)) {
      result[key] = obj[key];
    }
  }

  return result;
};

export const sanitizeUser = <O extends object>(user: O) =>
  omit<O>(user, ['password', 'iat']);

export function authenticate({
  username,
  password,
}: {
  username: string;
  password: string;
}) {
  const user = db.user.findFirst({
    where: {
      username: {
        equals: username,
      },
    },
  });

  if (user?.password === hash(password)) {
    const sanitizedUser = sanitizeUser(user);
    const encodedToken = encode(sanitizedUser);
    return { user: sanitizedUser, jwt: encodedToken };
  }

  const error = new Error('Invalid username or password');
  throw error;
}

export const AUTH_COOKIE = `bulletproof_react_app_token`;

export function requireAuth(request: {
  headers?: Headers;
  cookies?: Record<string, string>;
}) {
  try {
    let encodedToken = null;
    if (request.headers) {
      const authHeader = request.headers.get('Authorization');
      if (authHeader && authHeader.startsWith('Bearer ')) {
        encodedToken = authHeader.substring(7);
      }
    }
    if (!encodedToken && request.cookies) {
      encodedToken = request.cookies[AUTH_COOKIE] || Cookies.get(AUTH_COOKIE);
    }

    if (!encodedToken) {
      return { error: 'Unauthorized', user: null };
    }

    const decodedToken = decode(encodedToken) as { id: string; exp: number };

    if (decodedToken.exp < Math.floor(Date.now() / 1000)) {
      return { error: 'Token expired', user: null };
    }
    const user = db.user.findFirst({
      where: {
        id: {
          equals: decodedToken.id,
        },
      },
    });

    if (!user) {
      return { error: 'Unauthorized', user: null };
    }

    return { user: sanitizeUser(user), error: null };
  } catch {
    return { error: 'Unauthorized', user: null };
  }
}

export function requireAdmin(user: any) {
  if (user.role !== 'ADMIN') {
    throw Error('Unauthorized');
  }
}
