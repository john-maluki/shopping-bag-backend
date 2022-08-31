# SHOPPING-BAG BACKEND [![Build Status](https://app.travis-ci.com/john-maluki/shopping-bag-backend.svg?branch=main)](https://app.travis-ci.com/john-maluki/shopping-bag-backend)

## Overview
A shopping bag is a backend online store application that supports restful services. 
It enables system-user (anyone logged in as a user) to do 
[window shopping](https://www.google.com/search?q=what+is+window+shopping&oq=what+is+window+shopping&aqs=chrome..69i57j0i512l5j0i22i30l4.12149j0j7&sourceid=chrome&ie=UTF-8)
---user can view a list of products provided by various shops, decide to save them on his/her `ShoppingBag` (it contains all products selected 
from several shops during one window shopping event). The running applications 
have several products and shops that are created by the shop owner (any user that is allowed 
to create shops). 

It supports features such as: registering and logging users, creating shopping bags, 
and trashing shopping bags, this is before permanent deletion, which can be done afterward. 
To show the operation in practice, a front-end client is required. In this case, the 
[ShoppingBag Frontend](https://github.com/john-maluki/shopping-bag-frontend) application is available for use.

## Requirements
For building and running the application you need:
- [ Java 17](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html)
- [ Spring boot 2.6.7](https://spring.io/blog/2022/04/21/spring-boot-2-6-7-available-now)
- [Maven 3.8.4+](https://maven.apache.org)

## Running the application locally
There are several ways to run a Spring Boot application on your local machine. One way is to execute the `main` method in the `dev.johnmaluki.shoppingbagbackend.ShoppingBagBackendApplication` class from your IDE.

Alternatively you can use the [Spring Boot Maven plugin](https://docs.spring.io/spring-boot/docs/current/reference/html/build-tool-plugins-maven-plugin.html) like so:

```shell
mvn spring-boot:run
```


## Copyright
Released under the MIT License. See the [LICENSE](https://github.com/john-maluki/shopping-bag-backend/blob/main/LICENSE.md) file.