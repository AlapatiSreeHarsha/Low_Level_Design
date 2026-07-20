package Creational_Patterns.Factory_pattern;
interface Logistics{
    void send();
}
class Truck implements Logistics{
    @Override
    public void send() {
        System.out.println("Truck is sending");
    }
}
class Ship implements Logistics{
    @Override
    public void send() {
        System.out.println("Ship is sending");
    }
}
class Airplane implements Logistics{
    @Override
    public void send() {
        System.out.println("Airplane is sending");
    }
}

class LogisticsFactory{
    public static Logistics getLogistics(String type) {
        if(type.equalsIgnoreCase("Truck")) {
            return new Truck();
        } else if(type.equalsIgnoreCase("Ship")) {
            return new Ship();
        } else if(type.equalsIgnoreCase("Airplane")) {
            return new Airplane();
        }
        return null;
    }
}
public class LogisticsCompany {
    public static void main(String[] args) {
        Logistics logistics = LogisticsFactory.getLogistics("Truck");
        logistics.send();
    }
}
