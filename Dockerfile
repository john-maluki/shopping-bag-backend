# Define base docker image
FROM openjdk:17
LABEL maintainer="johnmaluki.dev"

WORKDIR /shopping-bag-backend

COPY target/shopping-bag-backend-0.0.1-SNAPSHOT.jar .
RUN mkdir -p shopping_bag_product_images

# ADD target/shopping-bag-backend-0.0.1-SNAPSHOT.jar shopping-bag-backend-0.0.1-SNAPSHOT.jar

ENTRYPOINT ["java", "-jar", "shopping-bag-backend-0.0.1-SNAPSHOT.jar"]