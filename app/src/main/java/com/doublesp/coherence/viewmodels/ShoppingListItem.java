package com.doublesp.coherence.viewmodels;


public class ShoppingListItem {
    private String shoppingItem;
    private String shoppingImageUrl;

    public ShoppingListItem() {
    }

    public ShoppingListItem(String shoppintItem, String shoppingImageUrl) {
        this.shoppingItem = shoppintItem;
        this.shoppingImageUrl = shoppingImageUrl;
    }

    public String getShoppingItem() {
        return shoppingItem;
    }

    public String getShoppingImageUrl() {
        return shoppingImageUrl;
    }
}
