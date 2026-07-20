package Structural_Patterns.Decorator_Pattern;

interface Pizza{
    String getDescription();
    double getCost();
}

class MargheritaPizza implements Pizza{

    @Override
    public String getDescription() {
        return "Margherita Pizza";
    }

    @Override
    public double getCost() {
        return 200.0;
    }
}

abstract class PizzaDecorator implements Pizza{

    protected Pizza pizza;

    public PizzaDecorator(Pizza pizza){
        this.pizza = pizza;
    }
}

class ExtraCheese extends PizzaDecorator{

    public ExtraCheese(Pizza pizza){
        super(pizza);
    }

    @Override
    public String getDescription() {
        return pizza.getDescription() + ", Extra Cheese";
    }

    @Override
    public double getCost() {
        return pizza.getCost() + 50.0;
    }
}

class Olives extends PizzaDecorator{

    public Olives(Pizza pizza){
        super(pizza);
    }

    @Override
    public String getDescription() {
        return pizza.getDescription() + ", Olives";
    }

    @Override
    public double getCost() {
        return pizza.getCost() + 40.0;
    }
}

class StuffedCrust extends PizzaDecorator{

    public StuffedCrust(Pizza pizza){
        super(pizza);
    }

    @Override
    public String getDescription() {
        return pizza.getDescription() + ", Stuffed Crust";
    }

    @Override
    public double getCost() {
        return pizza.getCost() + 80.0;
    }
}

public class Main{

    public static void main(String[] args) {

        Pizza pizza = new MargheritaPizza();

        pizza = new ExtraCheese(pizza);
        pizza = new Olives(pizza);
        pizza = new StuffedCrust(pizza);

        System.out.println("Description : " + pizza.getDescription());
        System.out.println("Total Cost  : Rs. " + pizza.getCost());
    }
}