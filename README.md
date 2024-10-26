[![GitHub license](https://img.shields.io/badge/license-MIT-blue.svg)](https://github.com/n1ccco/NicoAI/blob/main/LICENSE)
[![npm version](https://img.shields.io/npm/v/react.svg?style=flat)](https://www.npmjs.com/package/react)
[![Spring Boot version](https://img.shields.io/maven-central/v/org.springframework.boot/spring-boot.svg?label=spring%20boot)](https://search.maven.org/artifact/org.springframework.boot/spring-boot)
[![PRs Welcome](https://img.shields.io/badge/PRs-welcome-brightgreen.svg)](https://github.com/n1ccco/NicoAI/issues)
[![Build](https://github.com/n1ccco/NicoAI/actions/workflows/ci-docker-image-frontend.yml/badge.svg)](https://github.com/n1ccco/NicoAI/actions/workflows/ci-docker-image-frontend.yml)
[![Build](https://github.com/n1ccco/NicoAI/actions/workflows/ci-docker-image-backend.yml/badge.svg)](https://github.com/n1ccco/NicoAI/actions/workflows/ci-docker-image-backend.yml)
[![Build](https://github.com/n1ccco/NicoAI/actions/workflows/ci-docker-image-generator.yml/badge.svg)](https://github.com/n1ccco/NicoAI/actions/workflows/ci-docker-image-generator.yml)
# 🎨 Stable Diffusion Web UI 🌐

Welcome to **Stable Diffusion Web UI**, a full-stack web application that lets you generate stunning AI-powered images with ease! This project combines **Java Spring Boot** for the backend, **React TypeScript** for the frontend, and a **Python Flask microservice** integrated with **Stable Diffusion** to handle image generation.

## 🌟 Features

- **Image Generation**: Generate high-quality images based on custom prompts.
- **Image Gallery**: Browse previously generated images in an elegant gallery.
- **Image Details**: View image details, leave comments, like images, and engage with other users.
- **User's Image Page**: Manage and view all your generated images in one place.
- **User Communication**: Interact with other users through likes and comments.
- **Authentication & Authorization**: Secure access with role-based authorization and user login.

## 🖼️ Screenshots

1. **Gallery Page**
   ![Gallery Page](/screenshots/gallery-page.png)
   *Browse through user-generated artwork.*

2. **Generate Page**
   ![Generate Page](/screenshots/generate-page.png)
   *Enter your prompt and generate images with the power of AI.*

3. **Image Details Page with Comments**
   ![Image Details Page](/screenshots/image-details-page.png)
   *View image details, leave comments, and like images.*

4. **User's Images Page**
   ![User's Images Page](/screenshots/users-images-page.png)
   *View and manage all your generated images.*

5. **User Authentication**
   ![Login Page](/screenshots/signin-page.png)
   *Log in securely to access features like commenting, liking and generating.*

## 🚀 Getting Started

### Prerequisites

Ensure you have the following installed:

- Docker
- Docker Compose

## 🛳️ Deployment Guide

You can easily deploy the entire stack using Docker Compose and the provided shell scripts.

1. **Clone the repository**:
   ```bash
   git clone https://github.com/n1ccco/NicoAI.git
   cd NicoAI
   ```

2. **Set up environment variables**:
   You'll need to configure both the frontend and backend environment variables.

   - For the **frontend**, use the `.env.frontend.example` file to create your `.env.frontend` file:
     ```bash
     cp .env.frontend.example .env.frontend
     ```

   - For the **backend**, use the `.env.backend.example` file to create your `.env.backend` file:
     ```bash
     cp .env.backend.example .env.backend
     ```

3. **Run the scripts**:
   - For development:
     ```bash
     ./run-dev.sh
     ```

   - For production:
     ```bash
     ./run-prod.sh
     ```

4. **Access the app**:
   The frontend will be available on `http://localhost:3000`.

## 🛠️ Tech Stack

- **Frontend**: React (TypeScript)
- **Backend**: Java Spring Boot
- **Stable Diffusion Microservice**: Python Flask
- **Database**: PostgreSQL
- **Containerization**: Docker, Docker Compose

## 🎯 Key Components

- **Java Spring Boot Backend**: Manages API requests, user authentication, database interaction, and likes/comments functionality.
- **React Frontend**: Provides a sleek and intuitive user interface, including pages for image browsing, generation, and communication.
- **Stable Diffusion Microservice**: Processes image generation requests via the Stable Diffusion model.

## 🤝 Contributing

Contributions are welcome! Please submit a pull request or open an issue to discuss improvements, bug fixes, or new features.

## 📄 License

This project is licensed under the MIT License. See the [LICENSE](/LICENSE) file for details.