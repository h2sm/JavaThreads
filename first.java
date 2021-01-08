package threads.basic;
import java.util.*;
public class first {
    public static void main(String[] args) throws Exception{
        var threads = new ArrayList<Thread>();
        for (int i = 0; i < 10; i++) threads.add(new Thread(new NewThread(i)));
        threads.forEach(Thread::start);
        System.out.println("Threads started");
        Thread.sleep(4000);
        System.out.println("main finished");
    }
}
class NewThread implements Runnable{
    int num;
    public NewThread(int number) {
        this.num=number;
    }

    @Override
    public void run() {
        System.out.println("hello from thread "+ num );
    }
}
