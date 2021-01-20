package threads.basic;
import java.util.ArrayList;
import java.util.Arrays;
public class seventh {
    public static void main(String[] args) {
        BufferSeven bufferSeven = new BufferSeven();
        var producerThread = new ArrayList<Thread>();
        var consumerThread = new ArrayList<Thread>();
        for (int i = 0; i < 10; i++) {
            producerThread.add(new Thread(new ProducerSeven(bufferSeven)));
            consumerThread.add(new Thread(new ConsumerSeven(bufferSeven)));
        }

        producerThread.forEach(Thread::start);
        consumerThread.forEach(Thread::start);

//        Thread.sleep(4000);
//
//        producerThread.forEach(Thread::interrupt);
//        consumerThread.forEach(Thread::interrupt);
    }

}
class BufferSeven{//представим, что sharedmemory увеличивается от 0 до 3
    private int SHAREDMEMORY = 0;
    synchronized public void addSmth() throws InterruptedException {//добавляем в буфер
       while (SHAREDMEMORY>=3){
           try {
               wait();
           }
           catch (InterruptedException e) {
           }
       }
        SHAREDMEMORY++;
        System.out.println("Added " + SHAREDMEMORY);
        notify();

    }
    synchronized public void retrieveSmth() throws InterruptedException {//забираем из буфера
        while (SHAREDMEMORY<1){
            try {
                wait();
            }
            catch (InterruptedException e) {
            }
        }
        SHAREDMEMORY--;
        System.out.println("Retrieved " + SHAREDMEMORY);
         notify();
    }
}

class ProducerSeven implements Runnable{
    BufferSeven buf;
    public ProducerSeven(BufferSeven bufferSeven){
        this.buf = bufferSeven;
    }
    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                buf.addSmth();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
class ConsumerSeven implements Runnable{
    BufferSeven buf;
    public ConsumerSeven(BufferSeven bufferSeven){
        this.buf=bufferSeven;
    }
    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                buf.retrieveSmth();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}


