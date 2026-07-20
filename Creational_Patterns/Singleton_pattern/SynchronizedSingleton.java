package Creational_Patterns.Singleton_pattern;
//Lazyloading+synchronized is thread safe
class JudgeAnalysis{
    private static JudgeAnalysis instance;
    private  JudgeAnalysis() {
    }
    public static synchronized JudgeAnalysis getInstance() {
        if(instance == null) {
            instance = new JudgeAnalysis();
        }
        return instance;
    }
}
public class SynchronizedSingleton {
    public static void main(String[] args) {
        JudgeAnalysis judgeAnalysis = JudgeAnalysis.getInstance();
        System.out.println(judgeAnalysis);
    }
}
