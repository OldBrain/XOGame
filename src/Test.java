public class Test {
    static final int DOTS_TO_WIN = 3;
    public static void main(String[] args) {
        for (int k = (DOTS_TO_WIN/2)*-1; k < DOTS_TO_WIN/2+1; k++) {
            for (int n = (DOTS_TO_WIN/2)*-1; n <DOTS_TO_WIN/2+1 ; n++) {
                if (k != 0 || n != 0) {

                    System.out.println(k+"."+n);
                }
            }
        }
    }
}
