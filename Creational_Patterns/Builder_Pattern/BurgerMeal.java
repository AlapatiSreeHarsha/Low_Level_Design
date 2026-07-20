package Creational_Patterns.Builder_Pattern;

import java.util.ArrayList;

public class BurgerMeal {
    private final String bunType;
    private final String pattyType;
    private final ArrayList<String> toppings;
    private final String cheese;
    private final String side;
    private final String drink;

    private BurgerMeal(Builder builder) {
        this.bunType = builder.bunType;
        this.pattyType = builder.pattyType;
        this.toppings = builder.toppings;
        this.cheese = builder.cheese;
        this.side = builder.side;
        this.drink = builder.drink;
    }

    @Override
    public String toString() {
        return "BurgerMeal{" +
                "bunType='" + bunType + '\'' +
                ", pattyType='" + pattyType + '\'' +
                ", toppings=" + toppings +
                ", cheese='" + cheese + '\'' +
                ", side='" + side + '\'' +
                ", drink='" + drink + '\'' +
                '}';
    }

    public static class Builder {
        private final String bunType;
        private final String pattyType;
        private ArrayList<String> toppings = new ArrayList<>();
        private String cheese;
        private String side;
        private String drink;

        public Builder(String bunType, String pattyType) {
            this.bunType = bunType;
            this.pattyType = pattyType;
        }

        public Builder addTopping(String topping) {
            this.toppings.add(topping);
            return this;
        }

        public Builder addCheese(String cheese) {
            this.cheese = cheese;
            return this;
        }

        public Builder addSide(String side) {
            this.side = side;
            return this;
        }

        public Builder addDrink(String drink) {
            this.drink = drink;
            return this;
        }

        public BurgerMeal build() {
            return new BurgerMeal(this);
        }
    }

    public static void main(String[] args) {
        BurgerMeal meal = new BurgerMeal.Builder("Sesame Bun", "Chicken Patty")
                .addTopping("Lettuce")
                .addTopping("Tomato")
                .addCheese("Cheddar")
                .addSide("Fries")
                .addDrink("Coke")
                .build();

        System.out.println(meal);
    }
}