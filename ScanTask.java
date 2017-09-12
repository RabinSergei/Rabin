package rabin;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Scanner;
import java.util.concurrent.BlockingQueue;

// Класс для поиска подстроки в файле
public class ScanTask implements Runnable {
    Path path;
    String pattern;
    BlockingQueue<Match> sink;

    public ScanTask(Path path, String pattern, BlockingQueue<Match> sink) {
        this.path = path;
        this.pattern = pattern;
        this.sink = sink;
    }

    @Override
    public void run() {
        File file = path.toFile();

        try {
            Scanner scanner = new Scanner(file);
            int lineNo = 0;
            while(scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if(line.contains(pattern)) {
                    sink.put(new Match(file.getCanonicalPath(), lineNo, line));
                }
                lineNo++;
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            // Пропускаем не найденные файлы
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
