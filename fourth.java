package threads.basic;

import java.util.ArrayList;

public class fourth {
    public static void main(String[] args) throws InterruptedException {
        var threads = new ArrayList<Thread>();
        for (int i = 0; i < 10; i++) threads.add(new Thread(new newWorker4(i)));
        threads.forEach(Thread::start);

        Thread.sleep(5000);
        threads.forEach(Thread::interrupt);

        threads.forEach(fourth::join);
        System.out.println("main finished");

    }
    private static void join(Thread t) {
        try { t.join(); }
        catch (Exception e) { /*nothing*/ }
    }
}

class newWorker4 implements Runnable{
    int numberOfThread;
    private boolean isActive = true;
    final private long timeStart = System.currentTimeMillis();//время запуска треда

    public newWorker4(int numThread){
        this.numberOfThread=numThread;

    }
    @Override
    public void run() {
        try {
            while(isActive) {
                long timeEnd = System.currentTimeMillis();
                System.out.println("thread " + numberOfThread + " working for " + (float) (timeEnd - timeStart) / 1000F);
                Thread.sleep(1000);
            }
            System.out.println(Thread.currentThread().getName() + " interrupted");
        }
        catch (InterruptedException e) {
            System.out.println(Thread.currentThread().getName() + " interrupted");
        }
    }
}

