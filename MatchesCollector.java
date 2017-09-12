package rabin;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public class MatchesCollector extends Thread {
    BlockingQueue<Match> matches = new LinkedBlockingQueue<>();
    AtomicBoolean shutDownFlag = new AtomicBoolean(false);

    @Override
    public void run() {
        while(!shutDownFlag.get()) {
            try {
                Match match = matches.poll(100, TimeUnit.MILLISECONDS);
                if(match != null) {
                    System.out.println(match);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public BlockingQueue<Match> getMatchesSink() {
        return matches;
    }

    public void shutdown() {
        this.shutDownFlag.set(true);
    }
}
