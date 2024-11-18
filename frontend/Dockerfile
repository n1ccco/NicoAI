FROM node:22-alpine AS base
WORKDIR /app/frontend

COPY package*.json ./
COPY yarn.lock ./
RUN yarn install

COPY . .

FROM base AS dev
EXPOSE 3000
CMD [ "yarn", "dev", "--", "--host" ]

FROM base AS builder
RUN yarn build

FROM nginx:alpine AS prod
# Remove default nginx configuration
RUN rm /etc/nginx/conf.d/default.conf
# Copy custom nginx configuration
COPY nginx.conf /etc/nginx/conf.d/

COPY --from=builder /app/frontend/dist /usr/share/nginx/html
EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]