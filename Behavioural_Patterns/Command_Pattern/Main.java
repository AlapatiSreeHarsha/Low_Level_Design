package Behavioural_Patterns.Command_Pattern;

import java.util.*;

// Command Interface
interface Command {
    void execute();
    void undo();
}

// Receiver - Light
class Light {

    public void on() {
        System.out.println("Light is ON");
    }

    public void off() {
        System.out.println("Light is OFF");
    }
}

// Receiver - AC
class AC {

    public void on() {
        System.out.println("AC is ON");
    }

    public void off() {
        System.out.println("AC is OFF");
    }
}

// Concrete Command - Light ON
class LightOnCommand implements Command {
    private Light light;

    public LightOnCommand(Light light) {
        this.light = light;
    }

    @Override
    public void execute() {
        light.on();
    }

    @Override
    public void undo() {
        light.off();
    }
}

// Concrete Command - Light OFF
class LightOffCommand implements Command {
    private Light light;

    public LightOffCommand(Light light) {
        this.light = light;
    }

    @Override
    public void execute() {
        light.off();
    }

    @Override
    public void undo() {
        light.on();
    }
}

// Concrete Command - AC ON
class ACOnCommand implements Command {
    private AC ac;

    public ACOnCommand(AC ac) {
        this.ac = ac;
    }

    @Override
    public void execute() {
        ac.on();
    }

    @Override
    public void undo() {
        ac.off();
    }
}

// Concrete Command - AC OFF
class ACOffCommand implements Command {
    private AC ac;

    public ACOffCommand(AC ac) {
        this.ac = ac;
    }

    @Override
    public void execute() {
        ac.off();
    }

    @Override
    public void undo() {
        ac.on();
    }
}

// Invoker
class RemoteControl {

    private Command[] buttons;
    private Stack<Command> commandHistory;

    public RemoteControl(int size) {
        buttons = new Command[size];
        commandHistory = new Stack<>();
    }

    public void setCommand(int slot, Command command) {
        buttons[slot] = command;
    }

    public void pressButton(int slot) {
        if (buttons[slot] != null) {
            buttons[slot].execute();
            commandHistory.push(buttons[slot]);
        } else {
            System.out.println("No command assigned to slot " + slot);
        }
    }

    public void pressUndo() {
        if (!commandHistory.isEmpty()) {
            Command command = commandHistory.pop();
            command.undo();
        } else {
            System.out.println("Nothing to undo.");
        }
    }
}

// Client
public class Main {

    public static void main(String[] args) {

        Light light = new Light();
        AC ac = new AC();

        RemoteControl remote = new RemoteControl(4);

        remote.setCommand(0, new LightOnCommand(light));
        remote.setCommand(1, new LightOffCommand(light));
        remote.setCommand(2, new ACOnCommand(ac));
        remote.setCommand(3, new ACOffCommand(ac));

        remote.pressButton(0); // Light ON
        remote.pressButton(2); // AC ON
        remote.pressButton(1); // Light OFF
        remote.pressButton(3); // AC OFF

        System.out.println("\nUndo Operations:");

        remote.pressUndo();
        remote.pressUndo();
        remote.pressUndo();
        remote.pressUndo();
    }
}