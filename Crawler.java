package rabin;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;

// Рекурсивный поиск файлов в субдиректориях, запуск задач поиска в файле
public class Crawler extends Thread {
    String root_directory;
    String search_pattern;
    ExecutorService threadPool;
    BlockingQueue<Match> resultsQueue;


    public Crawler(String root_directory, String search_pattern, ExecutorService threadPool, BlockingQueue<Match> resultsQueue) {
        this.root_directory = root_directory;
        this.search_pattern = search_pattern;
        this.threadPool = threadPool;
        this.resultsQueue = resultsQueue;
    }

    @Override
    public void run() {
        Path start_path = Paths.get(this.root_directory);
        try {
            Files.walk(start_path)
                    .filter(Files::isRegularFile)
                    .forEach((path) -> create_task(path));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void create_task(Path path) {
        ScanTask task = new ScanTask(path, search_pattern, resultsQueue);
        threadPool.execute(task);
    }
}
