FROM node:slim

WORKDIR /app
COPY . .
RUN npm install -g @angular/cli
RUN npm ci --legacy-peer-deps && npm cache clean --force

EXPOSE 4200

CMD ["ng","serve","--host","0.0.0.0", "--poll", "100"]