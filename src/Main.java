import java.util.EnumMap;
import java.util.Random;
import java.util.Scanner;


public class Main {
    private static int count;

    public static enum direction {IpJp, ImJm, IpJm, ImJp, I0Jp, I0Jm, ImJ0, IpJ0}

    static final int SIZE = 3;
    static final int DOTS_TO_WIN = 3;
    static final char DOT_X = 'X';
    static final char DOT_O = 'O';
    static final char DOT_EMPTY = '.';
    static final int I_INCREMENTS = 0;
    static final int J_INCREMENTS = 1;


    static char[][] map;
    static int[] increments = new int[2];
    static Scanner sc = new Scanner(System.in);
    static Random random = new Random();


    public static void main(String[] args) {
        initMap();
        printMap();

        while (true) {
            humanTurn();
            printMap();
            printValidDirection();
//            if (checkWin(DOT_X)) {
            if (smartCheckWin(DOT_X)) {
                System.out.println("Вы выиграли!!!");
                break;
            }
            if (isFull()) {
                System.out.println("Ничья");
                break;
            }

            aiTurn();
            printMap();
            printValidDirection();
            if (smartCheckWin(DOT_O)) {
                System.out.println("Комьютер победил");
                break;
            }
            if (isFull()) {
                System.out.println("Ничья");
                break;
            }
        }
    }

    private static void printValidDirection() {
        for (int i = 0; i <SIZE ; i++) {
            for (int j = 0; j < SIZE; j++) {
                System.out.print(getValidIncrement(i,j)[0]+","+getValidIncrement(i,j)[1]+" ");
            }
            System.out.println();
        }
    }

    static void initMap() {
        map = new char[SIZE][SIZE];
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                map[i][j] = DOT_EMPTY;
            }
        }
    }

    static void printMap() {
        System.out.print("  ");
        for (int i = 0; i < SIZE; i++) {
            System.out.print(i + 1 + " ");


        }
        System.out.println();
        for (int i = 0; i < SIZE; i++) {
            System.out.print(i + 1 + " ");
            for (int j = 0; j < SIZE; j++) {
                System.out.printf("%c ", map[i][j]);

            }
            System.out.println();
        }
    }

    static void humanTurn() {
        int x;
        int y;
        do {
            System.out.println("input coord X Y");
            x = sc.nextInt() - 1;
            y = sc.nextInt() - 1;
        } while (!isCellValid(y, x));
        map[y][x] = DOT_X;
    }

    static void aiTurn() {
        int x;
        int y;
        do {
            x = random.nextInt(SIZE);
            y = random.nextInt(SIZE);
        } while (!isCellValid(y, x));
        map[y][x] = DOT_O;
    }


    static boolean isCellValid(int y, int x) {
        if (y < 0 || x < 0 || y >= SIZE || x >= SIZE) {
            return false;
        }
        return map[y][x] == DOT_EMPTY;
    }

    static boolean isFull() {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (map[i][j] == DOT_EMPTY) {
                    return false;
                }
            }
        }
        return true;
    }


    static boolean smartCheckWin(char c) {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (map[i][j] == c) {
                    if (checkLine(map[i][j], i, j)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    static boolean checkLine(char c, int i, int j) {

        for (direction dir : direction.values()) {
            if (vectorDirection(dir, i, j, c)) {
                return true;
            }
        }
        return false;
    }


    static boolean vectorDirection(direction ijDirection, int i, int j, char c) {
        count = 0;
        int deltaI = 0;
        int deltaJ = 0;
        switch (ijDirection) {
            case I0Jm:
                if ((j-(DOTS_TO_WIN-1) < 0)) {
                    return false;
                }
                deltaI = 0;
                deltaJ = -1;
                break;
            case I0Jp:
                if ((j + DOTS_TO_WIN > SIZE)) {
                    return false;
                }
                deltaI = 0;
                deltaJ = 1;
                break;
            case ImJ0:
                if ((i-(DOTS_TO_WIN-1) < 0)) {
                    return false;
                }
                deltaI = -1;
                deltaJ = 0;
                break;
            case ImJm:
                if (((i-(DOTS_TO_WIN-1)) < 0) || ((j-(DOTS_TO_WIN-1)) < 0)) {
                    return false;
                }
                deltaI = -1;
                deltaJ = -1;
                break;
            case ImJp:
                if ((i-(DOTS_TO_WIN-1) < 0) | (j + DOTS_TO_WIN > SIZE)) {
                    return false;
                }
                deltaI = -1;
                deltaJ = 1;
                break;
            case IpJ0:
                if ((i + DOTS_TO_WIN > SIZE)) {
                    return false;
                }
                deltaI = 1;
                deltaJ = 0;
                break;
            case IpJm:

                if ((i + DOTS_TO_WIN > SIZE) | ( j-(DOTS_TO_WIN-1) < 0)) {
                    return false;
                }
                deltaI = 1;
                deltaJ = -1;
                break;
            case IpJp:
                if ((i + DOTS_TO_WIN > SIZE) | (j + DOTS_TO_WIN > SIZE)) {
                    return false;
                }
                deltaI = 1;
                deltaJ = 1;
                break;
            default:
        }

        for (int k = 0; k < DOTS_TO_WIN; k++) {
            if (map[i][j] == c) {
                char tmp = map[i][j];
                count++;
            } else {
                return false;
            }
            i += deltaI;
            j += deltaJ;
        }

        if (count == 3) {
            return true;
        } else {
            return false;
        }

    }

static int[] getValidIncrement(int i, int j) {

    for (direction dir : direction.values()) {
        switch (dir) {
            case I0Jm:
                if ((j - DOTS_TO_WIN < 0)) {
                    break;
                }
                increments[I_INCREMENTS] = 0;
                increments[J_INCREMENTS] = -1;
                break;
            case I0Jp:
                if ((j + DOTS_TO_WIN > SIZE)) {
                    break;
                }
                increments[I_INCREMENTS] = 0;
                increments[J_INCREMENTS] = 1;
                break;
            case ImJ0:
                if ((i - DOTS_TO_WIN < 0)) {
                    break;
                }
                increments[I_INCREMENTS] = -1;
                increments[J_INCREMENTS] = 0;
                break;
            case ImJm:
                if (((i - DOTS_TO_WIN) < 0) || ((j - DOTS_TO_WIN) < 0)) {
                    break;
                }
                increments[I_INCREMENTS] = -1;
                increments[J_INCREMENTS] = -1;
                break;
            case ImJp:
                if (((i - DOTS_TO_WIN) < 0) | (j + DOTS_TO_WIN > SIZE)) {
                    break;
                }
                increments[I_INCREMENTS] = -1;
                increments[J_INCREMENTS] = 1;
                break;
            case IpJ0:
                if ((i + DOTS_TO_WIN > SIZE)) {
                    break;
                }
                increments[I_INCREMENTS] = 1;
                increments[J_INCREMENTS] = 0;
                break;
            case IpJm:
                if ((i + DOTS_TO_WIN > SIZE) | (j - DOTS_TO_WIN < 0)) {
                    break;
                }
                increments[I_INCREMENTS] = 1;
                increments[J_INCREMENTS] = -1;
                break;
            case IpJp:
                if ((i + DOTS_TO_WIN > SIZE) | (j + DOTS_TO_WIN > SIZE)) {
                    break;
                }
                increments[I_INCREMENTS] = 1;
                increments[J_INCREMENTS] = 1;
                break;
            default:
                System.out.println("Switch не сработал");
                increments[I_INCREMENTS] = 0;
                increments[J_INCREMENTS] = 0;
        }
    }
    return increments;
}
}


