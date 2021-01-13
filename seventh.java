package threads.basic;
import java.util.ArrayList;
import java.util.Arrays;
public class seventh {
    public static void main(String[] args) {
        var producerThread = new ArrayList<Thread>();
        var consumerThread = new ArrayList<Thread>();
        for (int i = 0; i < 10; i++) {
            producerThread.add(new Thread(new ProducerSeven()));
            consumerThread.add(new Thread(new ConsumerSeven()));
        }

        producerThread.forEach(Thread::start);
        consumerThread.forEach(Thread::start);

//        Thread.sleep(4000);
//
//        producerThread.forEach(Thread::interrupt);
//        consumerThread.forEach(Thread::interrupt);
    }

}
class BufferSeven{
    private static int[] SHAREDMEMORY = new int[1];
    public void addSmth() throws InterruptedException {//добавляем в буфер
        synchronized (SHAREDMEMORY){
            while (SHAREDMEMORY[0]!=1)
            SHAREDMEMORY[0]++;
        }
        showSum("add");
    }
    public void retrieveSmth() throws InterruptedException {//забираем из буфера
        synchronized (SHAREDMEMORY){
            while (SHAREDMEMORY[0]!=0)
            SHAREDMEMORY[0]--;
        }
        showSum("retrieve");
    }
    private void showSum(String str){
        System.out.println(Arrays.toString(SHAREDMEMORY) + " from " + str);
    }

}

class ProducerSeven implements Runnable{
    BufferSeven bufferSeven = new BufferSeven();
    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                bufferSeven.addSmth();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
class ConsumerSeven implements Runnable{
    BufferSeven bufferSeven = new BufferSeven();
    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                bufferSeven.retrieveSmth();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}


