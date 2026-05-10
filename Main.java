package семестровая_работа_2;
import java.util.*;
import java.io.*;

public class Main {
    public static void main(String[] args) throws IOException {
        int SIZE = 10000;
        int SEARCH = 100;
        int DELETE = 1000;

        Random rand = new Random();
        int[] data = new int[SIZE];
        for (int i = 0; i < SIZE; i++) {
            data[i] = rand.nextInt(200000) + 1;
        }

        TwoThreeTree tree = new TwoThreeTree();

        long[] insertTimes = new long[SIZE];
        int[] insertOps = new int[SIZE];
        long[] searchTimes = new long[SEARCH];
        int[] searchOps = new int[SEARCH];
        long[] deleteTimes = new long[DELETE];
        int[] deleteOps = new int[DELETE];

        // ВСТАВКА
        for (int i = 0; i < SIZE; i++) {
            tree.resetOps();
            long start = System.nanoTime();
            tree.insert(data[i]);
            long end = System.nanoTime();
            insertTimes[i] = end - start;
            insertOps[i] = tree.getOps();
        }

        // ПОИСК
        for (int i = 0; i < SEARCH; i++) {
            int idx = rand.nextInt(SIZE);
            int key = data[idx];
            tree.resetOps();
            long start = System.nanoTime();
            tree.search(key);
            long end = System.nanoTime();
            searchTimes[i] = end - start;
            searchOps[i] = tree.getOps();
        }

        // УДАЛЕНИЕ
        List<Integer> indices = new ArrayList<>();
        for (int i = 0; i < SIZE; i++) indices.add(i);
        Collections.shuffle(indices);

        for (int i = 0; i < DELETE; i++) {
            int key = data[indices.get(i)];
            tree.resetOps();
            long start = System.nanoTime();
            tree.delete(key);
            long end = System.nanoTime();
            deleteTimes[i] = end - start;
            deleteOps[i] = tree.getOps();
        }

        // ПОДСЧЁТ СРЕДНИХ
        double avgInsertTime = 0, avgInsertOps = 0;
        double avgSearchTime = 0, avgSearchOps = 0;
        double avgDeleteTime = 0, avgDeleteOps = 0;

        for (int i = 0; i < SIZE; i++) {
            avgInsertTime += insertTimes[i];
            avgInsertOps += insertOps[i];
        }
        avgInsertTime = avgInsertTime / SIZE / 1000.0;
        avgInsertOps = avgInsertOps / SIZE;

        for (int i = 0; i < SEARCH; i++) {
            avgSearchTime += searchTimes[i];
            avgSearchOps += searchOps[i];
        }
        avgSearchTime = avgSearchTime / SEARCH / 1000.0;
        avgSearchOps = avgSearchOps / SEARCH;

        for (int i = 0; i < DELETE; i++) {
            avgDeleteTime += deleteTimes[i];
            avgDeleteOps += deleteOps[i];
        }
        avgDeleteTime = avgDeleteTime / DELETE / 1000.0;
        avgDeleteOps = avgDeleteOps / DELETE;

        // СОХРАНЕНИЕ В ФАЙЛ
        PrintWriter writer = new PrintWriter(new File("results.txt"));
        writer.println("2-3 дерево. Результаты замеров");
        writer.println();
        writer.println("Вставка (10000 элементов):");
        writer.println("  среднее время: " + String.format("%.2f", avgInsertTime) + " мкс");
        writer.println("  среднее кол-во операций: " + String.format("%.2f", avgInsertOps));
        writer.println();
        writer.println("Поиск (100 элементов):");
        writer.println("  среднее время: " + String.format("%.2f", avgSearchTime) + " мкс");
        writer.println("  среднее кол-во операций: " + String.format("%.2f", avgSearchOps));
        writer.println();
        writer.println("Удаление (1000 элементов):");
        writer.println("  среднее время: " + String.format("%.2f", avgDeleteTime) + " мкс");
        writer.println("  среднее кол-во операций: " + String.format("%.2f", avgDeleteOps));
        writer.println();
        writer.println("Теоретическая оценка log2(10000) = 13.3");
        writer.close();

        System.out.println("\nРезультаты сохранены в файл results.txt");
    }
}