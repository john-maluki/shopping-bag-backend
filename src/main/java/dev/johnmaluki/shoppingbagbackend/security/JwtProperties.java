package dev.johnmaluki.shoppingbagbackend.security;

public final class JwtProperties {
    public static final String SECRET = "shop@123?!"; // Temporary usage, should be hidden.
    public static final String TOKEN_PREFIX= "Bearer ";
    public static final String HEADER_STRING= "Authorization";
    public static final int EXPIRATION_TIME = 10000 * 60 * 1000;
}
