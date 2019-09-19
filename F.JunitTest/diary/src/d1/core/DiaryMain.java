package d1.core;

public class DiaryMain {

    public static void main(String[] args) {
        System.out.println(System.getProperty("java.class.path"));
        if (args == null || args.length < 1) return;
        for (String item : args) {
            System.out.println(item);
        }
    }

}
