package threads.basic;
import java.util.ArrayList;
import java.util.concurrent.locks.ReentrantLock;

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
    static ReentrantLock reentrantLock = new ReentrantLock();
    static int i = 1;
    static int EPS = 1000000000;
    static private double PI = 0;
    static void countPI(){
        reentrantLock.lock();
        try {
            if (i<EPS) {
                PI += 8.0 / (i * (i + 2L));
                i+=4;
            }
            System.out.println(PI);
        }
        finally {
            reentrantLock.unlock();
        }
    }
}

class Counter implements Runnable{
    PiCount piCount = new PiCount();
    @Override
    public void run() {
        while (PiCount.i != PiCount.EPS) {
            piCount.countPI();
            System.out.println(Thread.currentThread().getName() +  " " + PiCount.i + " i " + PiCount.EPS + " eps");
        }
    }
}
