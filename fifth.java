package threads.basic;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class fifth {
    public static void main(String[] args) {

        var producerThreads = new ArrayList<Thread>();
        var consumerThreads = new ArrayList<Thread>();
        for (int i = 0; i < 10; i++) {
            producerThreads.add(new Thread(new Producer()));
            consumerThreads.add(new Thread(new Consumer()));
        }

        producerThreads.forEach(Thread::start);
        consumerThreads.forEach(Thread::start);
    }

}
class Buffer{
    //private static ArrayList<Integer> MEMORY = new ArrayList<>();//пускай размер массива-буфера будет ограничен 5 элементами int
    private static int[] SHAREDMEMORY = new int[1];
    void addSmth(int num){//добавляем в буфер какое-то значене
//        if (MEMORY.size()<=5) {
//            MEMORY.add(num);
//        }
//        else {
//            System.out.println("num + " +num + " wasnt add due to buffer limit");
//        }
        SHAREDMEMORY[num]++;
        showSum();
    }
    void retrieveSmth(int num){//забираем какую-нибудь позицию
        SHAREDMEMORY[num]--;
//        MEMORY.remove(num);
        showSum();
    }
    void showSum(){
        System.out.println(Arrays.toString(SHAREDMEMORY));
        //System.out.println(MEMORY);
    }

}

class Producer implements Runnable{
    Buffer buffer = new Buffer();
    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted())
        buffer.addSmth(new Random().nextInt(1));
    }
}
class Consumer implements Runnable{
    Buffer buffer = new Buffer();
    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted())
            buffer.retrieveSmth(new Random().nextInt(1));
    }
}


