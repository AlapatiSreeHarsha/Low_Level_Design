package Behavioural_Patterns.Visitor_Pattern;

// Visitor Interface
interface ItemVisitor {

    void visit(PhysicalProduct product);

    void visit(DigitalProduct product);

    void visit(GiftCard product);
}

// Element Interface
interface Item {

    void accept(ItemVisitor visitor);
}

// Concrete Element - Physical Product
class PhysicalProduct implements Item {

    private String name;
    private double weight;

    public PhysicalProduct(String name, double weight) {
        this.name = name;
        this.weight = weight;
    }

    public String getName() {
        return name;
    }

    public double getWeight() {
        return weight;
    }

    @Override
    public void accept(ItemVisitor visitor) {
        visitor.visit(this);
    }
}

// Concrete Element - Digital Product
class DigitalProduct implements Item {

    private String name;

    public DigitalProduct(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public void accept(ItemVisitor visitor) {
        visitor.visit(this);
    }
}

// Concrete Element - Gift Card
class GiftCard implements Item {

    private String name;

    public GiftCard(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public void accept(ItemVisitor visitor) {
        visitor.visit(this);
    }
}

// Concrete Visitor - Invoice
class InvoiceVisitor implements ItemVisitor {

    @Override
    public void visit(PhysicalProduct product) {
        System.out.println("Invoice generated for Physical Product: "
                + product.getName());
    }

    @Override
    public void visit(DigitalProduct product) {
        System.out.println("Invoice generated for Digital Product: "
                + product.getName());
    }

    @Override
    public void visit(GiftCard product) {
        System.out.println("Invoice generated for Gift Card: "
                + product.getName());
    }
}

// Concrete Visitor - Shipping Cost
class ShippingCostVisitor implements ItemVisitor {

    @Override
    public void visit(PhysicalProduct product) {
        double cost = product.getWeight() * 50;
        System.out.println("Shipping Cost for "
                + product.getName() + " = ₹" + cost);
    }

    @Override
    public void visit(DigitalProduct product) {
        System.out.println("No shipping cost for Digital Product: "
                + product.getName());
    }

    @Override
    public void visit(GiftCard product) {
        System.out.println("No shipping cost for Gift Card: "
                + product.getName());
    }
}

// Driver
public class Main {

    public static void main(String[] args) {

        Item[] items = {
                new PhysicalProduct("Laptop", 2.5),
                new DigitalProduct("Java Course"),
                new GiftCard("Amazon Gift Card")
        };

        ItemVisitor invoiceVisitor = new InvoiceVisitor();
        ItemVisitor shippingVisitor = new ShippingCostVisitor();

        System.out.println("=== Invoice Visitor ===");
        for (Item item : items) {
            item.accept(invoiceVisitor);
        }

        System.out.println();

        System.out.println("=== Shipping Cost Visitor ===");
        for (Item item : items) {
            item.accept(shippingVisitor);
        }
    }
}