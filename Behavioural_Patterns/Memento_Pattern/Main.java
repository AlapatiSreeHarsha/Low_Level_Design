package Behavioural_Patterns.Memento_Pattern;

import java.util.*;

// Memento
class Memento {

    private String name;
    private String education;
    private String experience;
    private List<String> skills;

    public Memento(String name, String education,
                   String experience, List<String> skills) {

        this.name = name;
        this.education = education;
        this.experience = experience;
        this.skills = new ArrayList<>(skills);
    }

    public String getName() {
        return name;
    }

    public String getEducation() {
        return education;
    }

    public String getExperience() {
        return experience;
    }

    public List<String> getSkills() {
        return new ArrayList<>(skills);
    }
}

// Originator
class ResumeEditor {

    private String name;
    private String education;
    private String experience;
    private List<String> skills;

    public ResumeEditor() {
        skills = new ArrayList<>();
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEducation(String education) {
        this.education = education;
    }

    public void setExperience(String experience) {
        this.experience = experience;
    }

    public void setSkills(List<String> skills) {
        this.skills = new ArrayList<>(skills);
    }

    public void printResume() {

        System.out.println("Name       : " + name);
        System.out.println("Education  : " + education);
        System.out.println("Experience : " + experience);
        System.out.println("Skills     : " + skills);
        System.out.println();
    }

    public Memento save() {
        return new Memento(name, education, experience, skills);
    }

    public void restore(Memento memento) {
        name = memento.getName();
        education = memento.getEducation();
        experience = memento.getExperience();
        skills = memento.getSkills();
    }
}

// Caretaker
class ResumeHistory {

    private Stack<Memento> history;

    public ResumeHistory() {
        history = new Stack<>();
    }

    public void save(ResumeEditor editor) {
        history.push(editor.save());
    }

    public void undo(ResumeEditor editor) {

        if (!history.isEmpty()) {
            editor.restore(history.pop());
        } else {
            System.out.println("No previous version available.");
        }
    }
}

// Client
public class Main {

    public static void main(String[] args) {

        ResumeEditor editor = new ResumeEditor();
        ResumeHistory history = new ResumeHistory();

        editor.setName("Sree Harsha");
        editor.setEducation("B.Tech CSE");
        editor.setExperience("1 Year");
        editor.setSkills(Arrays.asList("Java", "SQL"));

        history.save(editor);

        System.out.println("Resume Version 1");
        editor.printResume();

        editor.setExperience("2 Years");
        editor.setSkills(Arrays.asList("Java", "SQL", "Spring Boot"));

        history.save(editor);

        System.out.println("Resume Version 2");
        editor.printResume();

        System.out.println("Undo");

        history.undo(editor);
        editor.printResume();

        System.out.println("Undo");

        history.undo(editor);
        editor.printResume();
    }
}
