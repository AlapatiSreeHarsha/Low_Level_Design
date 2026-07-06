//A class should have only one job (responsibility) and only one reason to change.

/*
✅ One class = One responsibility
❌ One class = Many unrelated responsibilities 
*/
class Restaurant {

    Menu menu = new Menu();
    Order order = new Order();
    Cashier cashier = new Cashier();
    Waiter waiter = new Waiter();
    Chef chef = new Chef();
    Receptionist receptionist = new Receptionist();
    Security security = new Security();

    void startRestaurant() {

        menu.addItem("Pizza");
        menu.removeItem("Burger");

        order.addItem("Pizza");
        order.printOrder();

        waiter.takeOrder(order);

        chef.prepareOrder(order);

        cashier.processPayment(100.0);

        receptionist.greetCustomer();

        security.monitorRestaurant();
    }
}
class Menu{
    void addItem(String item){
        //add item to menu
    }
    void removeItem(String item){
        //remove item from menu
    }
    void printMenu(){
        //print menu
    }
}
class Order{
    void addItem(String item){
        //add item to order
    }
    void removeItem(String item){
        //remove item from order
    }
    void printOrder(){
        //print order
    }
}
class Payment{
    void processPayment(double amount){
        //process payment
    }
}
class Waiter{
    void takeOrder(Order order){
        //take order
    }
    void serveOrder(Order order){
        //serve order
    }
}
class Chef{
    void prepareOrder(Order order){
        //prepare order
    }
}
class Receptionist{
    void greetCustomer(){
        //greet customer
    }
    void answerPhone(){
        //answer phone
    }
}
class Cashier{
    void processPayment(double amount){
        //process payment
    }
}
class Security{
    void monitorRestaurant(){
        //monitor restaurant
    }
}

public class SRP {

    public static void main(String[] args) {

        Restaurant restaurant = new Restaurant();
        restaurant.startRestaurant();

    }
}