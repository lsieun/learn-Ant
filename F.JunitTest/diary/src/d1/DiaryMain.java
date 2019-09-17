package d1;

public class DiaryMain {

    public static void main(String[] args) {
        if (args == null || args.length < 1) return;
        for (String item : args) {
            System.out.println(item);
        }
    }

}
