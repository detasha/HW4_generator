package org.example;

import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class Main {

    public static BlockingQueue<String> queueA = new ArrayBlockingQueue<>(100);
    public static BlockingQueue<String> queueB = new ArrayBlockingQueue<>(100);
    public static BlockingQueue<String> queueC = new ArrayBlockingQueue<>(100);

    public static void main(String[] args) throws InterruptedException {
        Thread textGenerator = new Thread(() -> {
            for (int i = 0; i < 10000; i++) {
                String text = generateText("abc", 100000);
                try {
                    queueA.put(text);
                    queueB.put(text);
                    queueC.put(text);
                } catch (InterruptedException e) {
                    return;
                }
            }
        });
        textGenerator.start();

        Thread threadA = new Thread(() -> {
            char letter = 'a';
            int maxQuantityA = findMaxQuantityLetter(queueA, letter);
            System.out.println("Символов " + "'" + letter + "'" + ": " + maxQuantityA + " шт.");
        });
        threadA.start();

        Thread threadB = new Thread(() -> {
            char letter = 'b';
            int maxQuantityB = findMaxQuantityLetter(queueB, letter);
            System.out.println("Символов " + "'" + letter + "'" + ": " + maxQuantityB + " шт.");
        });
        threadB.start();

        Thread threadC = new Thread(() -> {
            char letter = 'c';
            int maxQuantityC = findMaxQuantityLetter(queueC, letter);
            System.out.println("Символов " + "'" + letter + "'" + ": " + maxQuantityC + " шт.");
        });
        threadC.start();

        threadA.join();
        threadB.join();
        threadC.join();
    }

    public static int findMaxQuantityLetter(BlockingQueue<String> queue, char letter) {
        int quantity = 0;
        int max = 0;
        String text;
        try {
            for (int i = 0; i < 10000; i++) {
                text = queue.take();
                for (char j : text.toCharArray()) {
                    if (j == letter) quantity++;
                }
                if (quantity > max) max = quantity;
                quantity = 0;
            }
        } catch (InterruptedException e) {
            System.out.println("Ошибка в потоке " + Thread.currentThread().getName());
            return 0;
        }
        return max;
    }

    public static String generateText(String letters, int length) {
        Random random = new Random();
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < length; i++) {
            text.append(letters.charAt(random.nextInt(letters.length())));
        }
        return text.toString();
    }
}