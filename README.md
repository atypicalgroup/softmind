# 📌 Softmind

<p align="center">
Plataforma de pesquisas comportamentais e de bem-estar corporativo, que conecta empresas e colaboradores através de questionários, análises e relatórios dinâmicos.
O sistema foi desenvolvido para apoiar equipes de RH na aplicação de pesquisas internas e fornecer insights em conformidade com a NR-1.
</p>

---

## 🏗️ Arquitetura do Projeto

O Softmind é composto por três principais camadas:

**Backend:**

- Desenvolvido em Java 21 com Spring Boot 3

- Banco de dados: MongoDB Atlas

- Autenticação e autorização via JWT

- Documentação de API com Swagger

- Arquitetura em camadas (MVC + Services)

**Frontend Web:**

- Construído em Angular

- Comunicação com o backend via REST API

- Interface responsiva para administradores de RH gerenciarem pesquisas e relatórios

**Mobile:**

- Aplicativo desenvolvido em Kotlin (Android)

- Experiência amigável para colaboradores responderem pesquisas de forma simples e rápida

- Integração direta com os serviços do backend

---

## 🔑 Funcionalidades Principais

Autenticação e Autorização com JWT

Cadastro de Empresas e seus colaboradores

Gestão de Pesquisas (criação, edição e aplicação de questionários)

Tipos de Perguntas: múltipla escolha, escala, texto aberto

Relatórios Dinâmicos com insights e análises

Sugestões de Canais de Apoio para colaboradores

Plataforma Multicanal: Web (RH/Admin) e Mobile (colaboradores)

---
## ⚙️ Tecnologias Utilizadas
**🔹 Backend**

- Java 21
- Spring Boot 3
- Spring Security + JWT
- MongoDB Atlas
- Swagger / OpenAPI
- API IMDB Filmes por indicação

**🔹 Frontend Web**

- Angular 17+
- TypeScript
- RxJS / HttpClient
- Tailwind / Bootstrap

**🔹 Mobile**

- Kotlin (Android)
- Retrofit (consumo da API)
- Jetpack Compose

---

## 📂 Estrutura do Repositório

**Softmind/**
 ├── backend/        # Código do Spring Boot
 ├── frontend-web/   # Aplicação Angular
 ├── mobile/         # Aplicativo Kotlin
 └── README.md

 ---

 ## 🚀 Como Executar
🔹 **Backend (Spring Boot)**
```
cd backend
./mvnw spring-boot:run
```

🔹 **Frontend Web (Angular)**
```
cd frontend-web
npm install
ng serve
```

🔹 **Mobile (Kotlin)**

- Abrir a pasta mobile/ no Android Studio
- Rodar em um emulador ou dispositivo físico
- Out utilizar o APK Disponivel em /mobile

---

## 📊 Fluxo de Uso

1. Admin/RH acessa o painel web e cria uma pesquisa

2. Colaboradores recebem a pesquisa no aplicativo mobile

3. Respostas são registradas no backend (Spring Boot + MongoDB)

4. RH acessa relatórios com insights e sugestões

---

## 📜 Licença

Projeto desenvolvido para fins acadêmicos no **Global Solution FIAP 2025**.