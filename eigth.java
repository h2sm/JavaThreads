package threads.basic;
import java.util.ArrayList;

public class eight {
    public static void main(String[] args) {
        var piThreads = new ArrayList<Thread>();
        for (int i = 0; i < 5; i++) {
            piThreads.add(new Thread(new Counter()));
        }
        piThreads.forEach(Thread::start);

    }
}
class PiCount{
    private double PI = 0;
    void countPI(){
        for (int i = 1; i < 1000000000; i += 4) {
            PI += 8.0 / (i * (i + 2L));
        }
        System.out.println(PI);
    }
}

class Counter implements Runnable{
PiCount piCount = new PiCount();
    @Override
    public void run() {
        piCount.countPI();
        System.out.println(Thread.currentThread().getName());
    }
}

