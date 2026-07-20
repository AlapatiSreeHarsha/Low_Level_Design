package Structural_Patterns.Composite_Pattern;

import java.util.ArrayList;
import java.util.List;

interface CartItem {
    double getPrice();
    void displayItem();
}

class Product implements CartItem {
    private String name;
    private double price;

    public Product(String name, double price) {
        this.name = name;
        this.price = price;
    }

    @Override
    public double getPrice() {
        return price;
    }

    @Override
    public void displayItem() {
        System.out.println("Product: " + name + ", Price: " + price);
    }
}

class ProductBundle implements CartItem {
    private String bundleName;
    private List<CartItem> items;

    public ProductBundle(String bundleName) {
        this.bundleName = bundleName;
        this.items = new ArrayList<>();
    }

    public void addItem(CartItem item) {
        items.add(item);
    }

    @Override
    public double getPrice() {
        double totalPrice = 0;
        for (CartItem item : items) {
            totalPrice += item.getPrice();
        }
        return totalPrice;
    }

    @Override
    public void displayItem() {
        System.out.println("Product Bundle: " + bundleName);
        for (CartItem item : items) {
            item.displayItem();
        }
    }
}

public class Cart {
    public static void main(String[] args) {
        Product product1 = new Product("Laptop", 1000);
        Product product2 = new Product("Mouse", 20);
        Product product3 = new Product("Keyboard", 50);

        ProductBundle bundle1 = new ProductBundle("Office Setup");
        bundle1.addItem(product1);
        bundle1.addItem(product2);
        bundle1.addItem(product3);

        Product product4 = new Product("Monitor", 200);
        ProductBundle bundle2 = new ProductBundle("Gaming Setup");
        bundle2.addItem(bundle1);
        bundle2.addItem(product4);

        System.out.println("Total Price of Gaming Setup: " + bundle2.getPrice());
        System.out.println("Items in Gaming Setup:");
        bundle2.displayItem();
    }
}
