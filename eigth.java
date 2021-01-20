package threads.basic;

public class eight {
    public static void main(String[] args) throws InterruptedException {
        double pi = 0.0;
        int threadCount = 5;//треды
        int N = 100000;//"элементы"
        PiThread[] threads = new PiThread[threadCount];
        for (int i = 0; i < threadCount; i++) {
            threads[i] = new PiThread(threadCount, i, N);
            threads[i].start();
        }
        for (int i = 0; i < threadCount; i++) {
            threads[i].join();
        }
        for (int i = 0; i < threadCount; i++) {
            pi += threads[i].getSum();
        }
        System.out.print(pi*4);
    }
}
class PiThread extends Thread {
    private final int threadCount;
    private final int threadRemainder;
    private final int N;
    private double sum  = 0;

    public PiThread(int threadCount, int threadRemainder, int n) {
        this.threadCount = threadCount;
        this.threadRemainder = threadRemainder;
        this.N = n;
    }
    @Override
    public void run() {
        for (int i = 0; i <= N; i++) {//поток0 N%5==0 элементы. поток 1n%5==1 элементы....
            if (i % threadCount == threadRemainder) {
                sum += Math.pow(-1, i) / (2 * i + 1);
            }
        }
    }
    public double getSum() {
            return sum;
        }
}
