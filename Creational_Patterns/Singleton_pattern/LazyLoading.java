package Creational_Patterns.Singleton_pattern;
//Lazy loading is not thread safe
class JudgeAnalysis{
    private static JudgeAnalysis instance;
    private  JudgeAnalysis() {
    }
    public static JudgeAnalysis getInstance() {
        if(instance == null) {
            instance = new JudgeAnalysis();
        }
        return instance;
    }
}
public class LazyLoading {
    public static void main(String[] args) {
        JudgeAnalysis judgeAnalysis = JudgeAnalysis.getInstance();
        System.out.println(judgeAnalysis);
    }
}
