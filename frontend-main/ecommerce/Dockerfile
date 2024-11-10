# Step 1: Use nginx:alpine as the final lightweight image
FROM nginx:alpine
 
# Step 2: Set working directory and copy built files from Jenkins build output
WORKDIR /usr/share/nginx/html
COPY build/ .
 
# Step 3: Expose port 80 for nginx
EXPOSE 80
 
# Step 4: Start nginx
CMD ["nginx", "-g", "daemon off;"]