# Etapa 1 - Build Angular
FROM node:20 AS build
WORKDIR /app
COPY . .
RUN npm install -g @angular/cli@17
RUN npm install
RUN ng build --configuration production

# Etapa 2 - Servir com Nginx
FROM nginx:alpine
COPY --from=build /dist/web/browser /usr/share/nginx/html
EXPOSE 80
