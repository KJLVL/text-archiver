package five;
import java.util.Arrays;
import java.util.Scanner;

public class f {
    private static int[][] encoding(int[] startArr, int countRow) {
        int[] BitArr = new int[startArr.length + countRow];
        Arrays.fill(BitArr, -1);
        for (int i = 1; i < startArr.length + 1; i++) {
            if ((i & -i) == i) {
                BitArr[i - 1] = 0;
            }
        }
        int k = 0;
        for (int i = 0; i < BitArr.length; i++) {
            if (BitArr[i] != 0) {
                BitArr[i] = startArr[k];
                k++;
            }
        }
        int[][] matrSum = new int[countRow + 1][BitArr.length];
        System.arraycopy(BitArr, 0, matrSum[0], 0, BitArr.length);
        int ind = 1;
        int f;
        for (int i = 1; i < startArr.length + 1; i++) {
            if ((i & -i) == i) {
                for (int j = i; j < BitArr.length + 1; j += i) {
                    f = 0;
                    while (f < i && (j) < BitArr.length + 1) {
                        matrSum[ind][j - 1] = 1;
                        f++;
                        j++;
                    }
                }
                ind++;
            }
        }
        System.out.println("Контрольные биты стоят в первой строчке на позициях: ");
        for (int i = 1; i < startArr.length + 1; i++) {
            if ((i & -i) == i) {
                System.out.print(i + " ");
            }
        }
        System.out.println("\nМатрица контрольного суммирования");
        for (int[] ints : matrSum) {
            System.out.println(Arrays.toString(ints));
        }

        int[] b = new int[countRow];
        int[] endArray;
        endArray = BitArr;

        for (int i = 1; i < countRow + 1; i++) {
            int sum = 0;
            for (int j = 1; j < BitArr.length + 1; j++) {
                if (matrSum[0][j - 1] * matrSum[i][j - 1] == 1) {
                    sum += 1;
                }
                if (sum % 2 == 0)
                    b[i - 1] = 0;
                else b[i - 1] = 1;
            }
        }
        k = 0;
        for (int q = 1; q < startArr.length + 1; q++) {
            if ((q & -q) == q) {
                endArray[q - 1] = b[k];
                k++;
            }
        }
        System.out.println("\nЗашифрованное сообщение:");
        for (int i : endArray)
            System.out.print(i + " ");

        int[][] m = new int[2][endArray.length];
        for (int i = 0; i < b.length; i++)
            m[0][i] = b[i];
        for (int i = 0; i < endArray.length; i++)
            m[1][i] = endArray[i];

        return m;
    }

    private static int[][] decoding(int[] BitArr, int countRow) {
        for (int i = 1; i < BitArr.length + 1; i++) {
            if ((i & -i) == i) {
                BitArr[i - 1] = 0;
            }
        }
        int[][] matrSum = new int[countRow + 1][BitArr.length];
        System.arraycopy(BitArr, 0, matrSum[0], 0, BitArr.length);
        int ind = 1;
        int f;
        for (int i = 1; i < BitArr.length + 1; i++) {
            if ((i & -i) == i) {
                for (int j = i; j < BitArr.length + 1; j += i) {
                    f = 0;
                    while (f < i && j < BitArr.length + 1) {
                        matrSum[ind][j - 1] = 1;
                        f++;
                        j++;
                    }
                }
                ind++;
            }
        }
        System.out.println("\nМатрица синдромов");
        for (int[] ints : matrSum) {
            System.out.println(Arrays.toString(ints));
        }
        int[] b = new int[countRow];
        int[] endArray;
        endArray = BitArr;

        for (int i = 1; i < countRow + 1; i++) {
            int sum = 0;
            for (int j = 1; j < BitArr.length + 1; j++) {
                if (matrSum[0][j - 1] * matrSum[i][j - 1] == 1) {
                    sum += 1;
                }
                if (sum % 2 == 0)
                    b[i - 1] = 0;
                else b[i - 1] = 1;
            }
        }
        System.out.println("\nРасшифрованное сообщение:");
        for (int i : endArray) {
            System.out.print(i + " ");
        }
        int[][] m = new int[2][endArray.length];
        for (int i = 0; i < b.length; i++)
            m[0][i] = b[i];
        for (int i = 0; i < endArray.length; i++)
            m[1][i] = endArray[i];

        return m;
    }

    //1 0 0 1 1 1 0 1
    public static void main(String[] args) {
        while (true) {
            System.out.println("\nНажмите 1, чтобы закодировать: ");
            Scanner in0 = new Scanner(System.in);
            int s1 = in0.nextInt();
            int[][] arr1;
            int[][] arr2;
            int countRow = 0;
            int[] bit1;
            int[] endA1;
            int[] bit2;
            int[] endA2;
            int[] endS;

            switch (s1) {
                case 1 -> {
                    System.out.println("Введите последовательность символов через пробел длиной 8:");
                    Scanner in = new Scanner(System.in);
                    String s = in.nextLine();
                    String[] a = s.split(" ");
                    int[] startArr = new int[a.length];
                    for (int i = 0; i < a.length; i++)
                        startArr[i] = Integer.parseInt(a[i]);

                    for (int i = 1; i < startArr.length + 1; i++) {
                        if ((i & -i) == i) {
                            countRow++;
                        }
                    }
                    bit1 = new int[countRow];
                    endA1 = new int[startArr.length + countRow];
                    arr1 = encoding(startArr, countRow);
                    System.arraycopy(arr1[0], 0, bit1, 0, countRow);
                    if (startArr.length + countRow >= 0)
                        System.arraycopy(arr1[1], 0, endA1, 0, startArr.length + countRow);
                    System.out.println("\nЗначения контрольных бит:");
                    for (int i : bit1)
                        System.out.print(i + " ");

                    int sss = 0;

                    while (sss != 2) {
                        System.out.println("\nНажмите 2, чтобы расшифровать: ");
                        Scanner ik = new Scanner(System.in);
                        sss = ik.nextInt();
                    }
                    if (sss == 2){
                        countRow = 0;
                        System.out.println("Введите последовательность символов для декодирования:");
                        Scanner in1 = new Scanner(System.in);
                        String ss = in1.nextLine();
                        String[] a1 = ss.split(" ");
                        int[] startArr1 = new int[a1.length];
                        for (int i = 0; i < a1.length; i++)
                            startArr1[i] = Integer.parseInt(a1[i]);
                        for (int i = 1; i < startArr1.length + 1; i++) {
                            if ((i & -i) == i) {
                                countRow++;
                            }
                        }
                        bit2 = new int[countRow];
                        endA2 = new int[startArr1.length];
                        arr2 = decoding(startArr1, countRow);
                        System.arraycopy(arr2[0], 0, bit2, 0, countRow);
                        if (startArr1.length + countRow >= 0)
                            System.arraycopy(arr2[1], 0, endA2, 0, startArr1.length);
                        System.out.println("\nЗначения контрольных бит:");
                        for (int i : bit2)
                            System.out.print(i + " ");

                        int sum = 0;
                        for (int i = 0; i < bit1.length; i++)
                            if (bit2[i] == bit1[i])
                                sum++;
                        if (sum == countRow) {
                            System.out.println("\nОшибок нет");
                            endS = new int[countRow];
                            int k = 0;
                            for (int i = 1; i < startArr.length + 1; i++) {
                                if ((i & -i) == i) {
                                    endS[k] = i;
                                    k++;
                                }
                            }
                            System.out.println("\nПосле отбрасывания контрольных бит:");
                            k = -1;
                            for (int i = 1; i < endA2.length + 1; i++) {
                                if (k < countRow && i < 9) {
                                    k++;
                                    if (i != endS[k]) {
                                        System.out.print(endA2[i - 1] + " ");
                                        k--;
                                    }
                                }
                                else if (i > 8)
                                    System.out.print(endA2[i - 1] + " ");
                            }
                            System.out.println("\nИсходное сообщение:");
                            for (int i : startArr)
                                System.out.print(i + " ");
                        }
                        else {
                            int err = 0;
                            endS = new int[countRow];
                            int k = 0;
                            for (int i = 1; i < startArr.length + 1; i++) {
                                if ((i & -i) == i) {
                                    endS[k] = i;
                                    k++;
                                }
                            }
                            for (int i = 0; i < bit1.length; i++)
                                if (bit2[i] != bit1[i])
                                    err += endS[i];
                            System.out.println("\nОшибка найдена в позиции " + err);
                            if (endA2[err - 1] == 1)
                                endA2[err - 1] = 0;
                            else endA2[err - 1] = 1;
                            System.out.println("\nПосле отбрасывания контрольных бит:");
                            System.out.println("Расшифрованное сообщение после исправления ошибки:");
                            k = -1;
                            for (int i = 1; i < endA2.length + 1; i++) {
                                if (k < countRow && i < 9) {
                                    k++;
                                    if (i != endS[k]) {
                                        System.out.print(endA2[i - 1] + " ");
                                        k--;
                                    }
                                }
                                else if (i > 8)
                                    System.out.print(endA2[i - 1] + " ");
                            }
                            System.out.println("\nИсходное сообщение:");
                            for (int i : startArr)
                                System.out.print(i + " ");

                        }
                    }
                    break;
                }
                default -> System.out.println("Не верное число");
            }
        }
    }
}
