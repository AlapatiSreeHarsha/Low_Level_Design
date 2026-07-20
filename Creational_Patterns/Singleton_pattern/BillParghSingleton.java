package Creational_Patterns.Singleton_pattern;
//lazyloading + Bill Pugh Singleton is thread safe
class JudgeAnalysis{
    private  JudgeAnalysis() {
    }
    public static class Holder{
        private static final JudgeAnalysis INSTANCE = new JudgeAnalysis();
    }
    public static JudgeAnalysis getInstance() {
        return Holder.INSTANCE;
    }
}
public class BillParghSingleton {
    public static void main(String[] args) {
        JudgeAnalysis judgeAnalysis = JudgeAnalysis.getInstance();
        System.out.println(judgeAnalysis);
    }
}
