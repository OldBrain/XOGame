import java.util.Random;
import java.util.Scanner;
// Принцип интеллекта бота (НЕ ЗАКОНЧЕН ТРЕБУЕТ АНАЛИЗА И ДОРАБОТКИ)
//                1. ПУСТОЕ ПОЛЕ
//Создаем массив приоритетов, он синхронизирован с массивом поля.
// Проходим по всем валидным направлениям линий и добавляем +1 к значению.
// В результате получаем 6 4 6
//                       4 8 4
//                       6 4 6. (двоится так как каждая линия проверяется дважды, пока не критично).
// Центральная клетка имеет приоритет т.к. она занимает наибольшее количестао линий, это правильно.
//После хода игрока снова проходим по всем валидным линиям.
// ********************* Далее требует анализа и доработки******************
// Пока расчет приоритета выполнин так:
// 1.Если клетка занята символом то приоритет 0 в нее уже ходить нельзя.
// 2.Если в линии  присутствуют и Х и 0 то линия не перспективна, ничего не делаем.
// 3.Если в линии только X или только 0 то увеличиваем значение пустой ячейки
// на количество символов умноженное на "коэффициент агрессии"(для тактики
// победы коэффициент агрессии 0 больше чем X.  надо анализировать и доработать).
//3. Ищем максимальный элемент масива приоритетов и делаем ход.


public class Main {
    private static int count;

    public static enum direction {IpJp, ImJm, IpJm, ImJp, I0Jp, I0Jm, ImJ0, IpJ0}

    static final int SIZE = 3;
    static final int DOTS_TO_WIN = 3;
    //    Коэффициенты агрессии
    static final int KX = 1;
    static final int K0 = 1;
    static final char DOT_X = 'X';
    static final char DOT_O = 'O';
    static final char DOT_EMPTY = '.';


    static final int I_INCREMENTS = 0;
    static final int J_INCREMENTS = 1;


    static char[][] map;
    static int[][] priority = new int[SIZE][SIZE];
    static int[] increments = new int[2];
    static Scanner sc = new Scanner(System.in);
    static Random random = new Random();


    public static void main(String[] args) {
        initMap();
        printMap();
//        initPriorityCells();
//        printPriority();

        while (true) {
            humanTurn();
            printMap();


//            printPriority();
            if (smartCheckWin(DOT_X)) {
                System.out.println("Вы выиграли!!!");
                break;
            }
            if (isFull()) {
                System.out.println("Ничья");
                break;
            }
            smartBotTurn();
            printMap();
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

    private static void smartBotTurn() {
        initPriorityCells();
        int max;
        int maxI = 0;
        int maxJ = 0;
        max = priority[0][0];
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (priority[i][j] > max) {
                    max = priority[i][j];
                    maxI = i;
                    maxJ = j;
                }
            }
        }

        map[maxI][maxJ] = DOT_O;
    }

    private static void initPriorityCells() {

        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {

                for (direction dir : direction.values()) {
                    int deltaI = getValidIncrement(dir, i, j)[I_INCREMENTS];
                    int deltaJ = getValidIncrement(dir, i, j)[J_INCREMENTS];
                    if (deltaI != 0 | deltaJ != 0) {
                        initCells(i, j, deltaI, deltaJ);

//                        for (int k = 0; k < DOTS_TO_WIN; k++) {
//                        priority[i + deltaI][j + deltaJ] = priority[i + deltaI][j + deltaJ] + 1;
//                        }
                    }

                }
            }
            System.out.println();
        }
    }

    private static void initCells(int i, int j, int deltaI, int deltaJ) {
        int emptyCount = getCountSymbol(DOT_EMPTY, i, j, deltaI, deltaJ);
        ;
        int countO = getCountSymbol(DOT_O, i, j, deltaI, deltaJ);
        int countX = getCountSymbol(DOT_X, i, j, deltaI, deltaJ);


        for (int k = 0; k < DOTS_TO_WIN; k++) {
            if (map[i][j] == DOT_EMPTY) {
//                emptyCount++;

                priority[i][j] = priority[i][j] + 1;
                if (countO == 0 || countX == 0) {/*Перспективная линия для победы*/
                    priority[i][j] = priority[i][j] + KX * countX + K0 * countO;
//                    System.out.printf("Координаты %d,%d. добавляю %d кол.X=%d кол.0=%d",i,j,(KX * countX + K0 * countO),countX,countO);
//                    System.out.println();
                } else {/*Эта линия бесперспективна*/
//                    priority[i][j] = priority[i][j] + countO + countX;
                }

            } else {
                priority[i][j] = 0;

            }

            i += deltaI;
            j += deltaJ;
        }


    }

    private static int getCountSymbol(char symbol, int i, int j, int deltaI, int deltaJ) {
        int result = 0;
        for (int k = 0; k < DOTS_TO_WIN; k++) {
            if (map[i][j] == symbol) {
                result++;
            }
            i += deltaI;
            j += deltaJ;
        }
        return result;
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

    static void printPriority() {
        System.out.print("  ");
        for (int i = 0; i < SIZE; i++) {
            System.out.print(i + 1 + " ");


        }
        System.out.println();
        for (int i = 0; i < SIZE; i++) {
            System.out.print(i + 1 + " ");
            for (int j = 0; j < SIZE; j++) {
                System.out.printf("%d ", priority[i][j]);

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
            if (victoryDirection(dir, i, j, c)) {
                return true;
            }
        }
        return false;
    }


    static boolean victoryDirection(direction ijDirection, int i, int j, char c) {
        count = 0;
        int deltaI = 0;
        int deltaJ = 0;
        switch (ijDirection) {
            case I0Jm:
                if ((j - (DOTS_TO_WIN - 1) < 0)) {
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
                if ((i - (DOTS_TO_WIN - 1) < 0)) {
                    return false;
                }
                deltaI = -1;
                deltaJ = 0;
                break;
            case ImJm:
                if (((i - (DOTS_TO_WIN - 1)) < 0) || ((j - (DOTS_TO_WIN - 1)) < 0)) {
                    return false;
                }
                deltaI = -1;
                deltaJ = -1;
                break;
            case ImJp:
                if ((i - (DOTS_TO_WIN - 1) < 0) | (j + DOTS_TO_WIN > SIZE)) {
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

                if ((i + DOTS_TO_WIN > SIZE) | (j - (DOTS_TO_WIN - 1) < 0)) {
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

    static int[] getValidIncrement(direction dir, int i, int j) {

        increments[I_INCREMENTS] = 0;
        increments[J_INCREMENTS] = 0;
        int k = 0;

        switch (dir) {
            case I0Jm:
                if ((j - (DOTS_TO_WIN - 1) < 0)) {


                    increments[I_INCREMENTS] = 0;
                    increments[J_INCREMENTS] = 0;
                    return increments;
                }

                increments[I_INCREMENTS] = 0;
                increments[J_INCREMENTS] = -1;
                return increments;
            case I0Jp:
                if ((j + DOTS_TO_WIN > SIZE)) {
                    return increments;
                }
                increments[I_INCREMENTS] = 0;
                increments[J_INCREMENTS] = 1;
                return increments;
            case ImJ0:
                if ((i - (DOTS_TO_WIN - 1) < 0)) {
                    return increments;
                }
                increments[I_INCREMENTS] = -1;
                increments[J_INCREMENTS] = 0;
                return increments;
            case ImJm:
                if ((i - (DOTS_TO_WIN - 1) < 0) || ((j - (DOTS_TO_WIN - 1) < 0))) {
                    return increments;
                }
                increments[I_INCREMENTS] = -1;
                increments[J_INCREMENTS] = -1;
                return increments;
            case ImJp:
                if ((i - (DOTS_TO_WIN - 1) < 0) | (j + DOTS_TO_WIN > SIZE)) {
                    return increments;
                }
                increments[I_INCREMENTS] = -1;
                increments[J_INCREMENTS] = 1;
                return increments;
            case IpJ0:
                if ((i + DOTS_TO_WIN > SIZE)) {
                    return increments;
                }
                increments[I_INCREMENTS] = 1;
                increments[J_INCREMENTS] = 0;
                return increments;
            case IpJm:
                if ((i + DOTS_TO_WIN > SIZE) | ((j - (DOTS_TO_WIN - 1) < 0))) {
                    return increments;
                }
                increments[I_INCREMENTS] = 1;
                increments[J_INCREMENTS] = -1;
                return increments;
            case IpJp:
                if ((i + DOTS_TO_WIN > SIZE) | (j + DOTS_TO_WIN > SIZE)) {
                    return increments;
                }
                increments[I_INCREMENTS] = 1;
                increments[J_INCREMENTS] = 1;
                return increments;
            default:
                System.out.println("Switch не сработал");
                increments[I_INCREMENTS] = 0;
                increments[J_INCREMENTS] = 0;
        }

        return increments;
    }


}


