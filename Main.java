package rabin;

import java.nio.file.Path;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Main {

    public static void main(String[] args) {
        if(args.length != 2) {
            System.out.println("Usage:\n" +
                               "\n" +
                               "filefinder path pattern");
            System.exit(1);
        }

        String path = args[0];
        String pattern = args[1];

        // Создаем коллектор результатов, получаем от него входную очередь для результатов
        MatchesCollector collector = new MatchesCollector();
        BlockingQueue<Match> resultsSink = collector.getMatchesSink();
        collector.start();

        // Создаем Thread Pool для задач поиска в файлах
        ExecutorService threadPool = Executors.newFixedThreadPool(4);

        // Запуск потока - краулера, он будет искать файлы и направлять задачи поиска в них в Thread Pool
        Crawler crawler = new Crawler(path, pattern, threadPool, resultsSink);
        crawler.run();

        // Завершаем работу Thread Pool и дожидаемся завершения всех задач
        try {
            threadPool.shutdown();
            threadPool.awaitTermination(Integer.MAX_VALUE, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            threadPool.shutdownNow();
        }

        // Даем коллектору сигнал завершиться поскольку все задачи завершены
        collector.shutdown();
    }
}
