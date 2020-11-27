public class Test {
    static int i = 0;
    static int j = 9;
    static int DOTS_TO_WIN = 5;
    static int SIZE=10;

    public static  void main(String[] args) {
        System.out.println(j-(DOTS_TO_WIN-1));
        if ((i + DOTS_TO_WIN > SIZE) || ( j-(DOTS_TO_WIN-1) < 0)) {
            System.out.println("ПОЧЕМУ?");
        }

    }


    }




