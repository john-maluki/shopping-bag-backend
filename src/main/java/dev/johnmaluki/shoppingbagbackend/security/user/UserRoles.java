package dev.johnmaluki.shoppingbagbackend.security.user;

import java.util.HashSet;
import java.util.Set;

public final class UserRoles {
    public static String USER = "USER";
    public static String ADMIN = "ADMIN";
    public static String SHOPKEEPER = "SHOPKEEPER";

    public static Set<String> getUserRoles(){
        Set<String> userRoles =  new HashSet<>();

        userRoles.add(UserPermissions.CREATE_SHOPPING_BAG);
        userRoles.add(UserPermissions.UPDATE_SHOPPING_BAG);
        userRoles.add(UserPermissions.REMOVE_SHOPPING_BAG);
        userRoles.add(UserPermissions.REMOVE_FROM_TRASH_SHOPPING_BAG);
        userRoles.add(UserPermissions.TRASH_SHOPPING_BAG);

        return userRoles;
    }

    public static Set<String> getShopkeeperRoles(){
        Set<String> shopkeeperRoles =  new HashSet<>();

        shopkeeperRoles.add(UserPermissions.CREATE_SHOP);
        shopkeeperRoles.add(UserPermissions.UPDATE_SHOP);
        shopkeeperRoles.add(UserPermissions.REMOVE_SHOP);
        shopkeeperRoles.add(UserPermissions.CREATE_SHOPKEEPER);
        shopkeeperRoles.add(UserPermissions.UPDATE_SHOPKEEPER);
        shopkeeperRoles.add(UserPermissions.REMOVE_SHOPKEEPER);

        return shopkeeperRoles;
    }

    public static Set<String> getAdminRoles(){
        Set<String> adminRoles =  new HashSet<>();

        adminRoles.add(UserPermissions.EXPIRE_USER_ACCOUNT);
        adminRoles.add(UserPermissions.CREATE_USER_ACCOUNT);
        adminRoles.add(UserPermissions.UPDATE_USER_ACCOUNT);
        adminRoles.add(UserPermissions.REMOVE_USER_ACCOUNT);
        adminRoles.add(UserPermissions.ACTIVATE_USER_ACCOUNT);
        adminRoles.add(UserPermissions.INACTIVATE_USER_ACCOUNT);
        adminRoles.add(UserPermissions.CREATE_PRODUCT);
        adminRoles.add(UserPermissions.REMOVE_PRODUCT);
        adminRoles.add(UserPermissions.UPDATE_PRODUCT);

        return adminRoles;
    }
}
