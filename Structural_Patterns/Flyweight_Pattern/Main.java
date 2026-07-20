package Structural_Patterns.Flyweight_Pattern;

import java.util.*;

// Flyweight
class TreeType {
    private String name;
    private String color;
    private String texture;

    public TreeType(String name, String color, String texture) {
        this.name = name;
        this.color = color;
        this.texture = texture;
    }

    public void draw(int x, int y) {
        System.out.println(
                "Drawing " + name +
                " Tree at (" + x + ", " + y + ")" +
                " Color: " + color +
                " Texture: " + texture
        );
    }
}

// Context
class Tree {
    private int x;
    private int y;
    private TreeType type;

    public Tree(int x, int y, TreeType type) {
        this.x = x;
        this.y = y;
        this.type = type;
    }

    public void draw() {
        type.draw(x, y);
    }
}

// Flyweight Factory
class TreeFactory {
    private static Map<String, TreeType> treeTypeMap = new HashMap<>();

    public static TreeType getTreeType(String name, String color, String texture) {

        String key = name + "-" + color + "-" + texture;

        if (!treeTypeMap.containsKey(key)) {
            System.out.println("Creating TreeType: " + key);
            treeTypeMap.put(key, new TreeType(name, color, texture));
        }

        return treeTypeMap.get(key);
    }
}

// Client
class Forest {
    private List<Tree> trees = new ArrayList<>();

    public void plantTree(int x, int y, String name, String color, String texture) {

        TreeType type = TreeFactory.getTreeType(name, color, texture);
        trees.add(new Tree(x, y, type));
    }

    public void draw() {
        for (Tree tree : trees) {
            tree.draw();
        }
    }
}

// Driver
public class Main {
    public static void main(String[] args) {

        Forest forest = new Forest();

        forest.plantTree(10, 20, "Oak", "Green", "Rough");
        forest.plantTree(30, 40, "Oak", "Green", "Rough");
        forest.plantTree(50, 60, "Pine", "Dark Green", "Smooth");
        forest.plantTree(70, 80, "Oak", "Green", "Rough");
        forest.plantTree(90, 100, "Pine", "Dark Green", "Smooth");
        forest.plantTree(120, 140, "Birch", "Light Green", "Soft");

        System.out.println("\nDrawing Forest:\n");
        forest.draw();
    }
}