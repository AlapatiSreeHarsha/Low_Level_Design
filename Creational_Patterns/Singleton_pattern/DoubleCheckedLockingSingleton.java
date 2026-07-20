package Creational_Patterns.Singleton_pattern;
//LazyLoading+double checked locking is thread safe
class JudgeAnalysis{
    private static volatile JudgeAnalysis instance;
    private  JudgeAnalysis() {
    }
    public static JudgeAnalysis getInstance() {
        if(instance == null) {
            synchronized (JudgeAnalysis.class) {
                if(instance == null) {
                    instance = new JudgeAnalysis();
                }
            }
        }
        return instance;
    }
}
public class DoubleCheckedLockingSingleton {
    public static void main(String[] args) {
        JudgeAnalysis judgeAnalysis = JudgeAnalysis.getInstance();
        System.out.println(judgeAnalysis);
    }
}
