package threads.basic;
import java.util.ArrayList;

public class third {
    public static void main(String[] args) throws InterruptedException {
        var threads = new ArrayList<Thread>();
        final long timeStart = System.currentTimeMillis();
        for (int i = 0; i < 10; i++) threads.add(new Thread(new newWorker(i)));
        threads.forEach(Thread::start);

        }
    }

class newWorker implements Runnable{
    int numberOfThread;

    public newWorker(int numThread){
        this.numberOfThread=numThread;
    }
    @Override
    public void run() {
        int k =0;
        try {
            while(k!=5) {
                k++;
                System.out.println("hello from thread " + numberOfThread + " I working at " + k + " seconds");
                Thread.sleep(1000);
            }
            System.out.println(Thread.currentThread().getName() + " interrupted");
        }
        catch (InterruptedException e) {
            System.out.println(Thread.currentThread().getName() + " interrupted");
        }
    }
}

