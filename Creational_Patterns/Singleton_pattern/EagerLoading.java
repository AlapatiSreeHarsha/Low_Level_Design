package Creational_Patterns.Singleton_pattern;
//Eager loading is Thread Safe
class JudgeAnalysis{
    private static JudgeAnalysis instance = new JudgeAnalysis();
    private  JudgeAnalysis() {
    }
    public static JudgeAnalysis getInstance() {
        return instance;
    }
}
public class EagerLoading {
    public static void main(String[] args) {
        JudgeAnalysis judgeAnalysis = JudgeAnalysis.getInstance();
        System.out.println(judgeAnalysis);
    }
    
}
