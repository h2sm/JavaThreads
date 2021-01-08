package threads.basic;
import java.util.ArrayList;

public class second {
    public static void main(String[] args) throws InterruptedException {
        var threads = new ArrayList<Thread>();
        for (int i = 0; i < 10; i++) threads.add(new Thread(new Worker(i)));
        threads.forEach(Thread::start);
    }

}
class Worker implements Runnable{
    final private long timeStart = System.currentTimeMillis();//время запуска треда
    int numberOfThread;

    public Worker(int numThread){
        this.numberOfThread=numThread;
    }
    @Override
    public void run() {
        while (! Thread.currentThread().isInterrupted()) {
            long timeEnd = System.currentTimeMillis();
            System.out.println("Thread number " + numberOfThread + ". Elapsed time is : " + (float) (timeEnd - timeStart) / 1000F);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
