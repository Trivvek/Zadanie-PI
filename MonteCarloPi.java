import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class MonteCarloPi {

    public static void main(String[] args) {
        int totalPoints = 1000000; 
        int numThreads = 4;
        int pointsPerThread = totalPoints / numThreads;

        ExecutorService executorService = Executors.newFixedThreadPool(numThreads);

        MonteCarloPiCalculator[] calculators = new MonteCarloPiCalculator[numThreads];
        Future<Integer>[] futures = new Future[numThreads];

        for (int i = 0; i < numThreads; i++) {
            calculators[i] = new MonteCarloPiCalculator(pointsPerThread);
            futures[i] = executorService.submit(calculators[i]);
        }

        executorService.shutdown();

        int totalInsideCircle = 0;
        for (Future<Integer> future : futures) {
            try {
                totalInsideCircle += future.get();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        double piApproximation = (double) totalInsideCircle / totalPoints * 4;

        System.out.println("Przyblizona wartosc liczby PI: " + piApproximation);
    }
}

class MonteCarloPiCalculator implements Callable<Integer> {
    private int pointsToGenerate;
    private int pointsInsideCircle;

    public MonteCarloPiCalculator(int pointsToGenerate) {
        this.pointsToGenerate = pointsToGenerate;
        this.pointsInsideCircle = 0;
    }

    @Override
    public Integer call() {
        for (int i = 0; i < pointsToGenerate; i++) {
            double x = Math.random();
            double y = Math.random();

            if (x * x + y * y <= 1) {
                pointsInsideCircle++;
            }
        }
        return pointsInsideCircle;
    }
}