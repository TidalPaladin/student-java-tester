
/**
 * @author Scott Chase Waggener <tidal@utexas.edu>
 * 
 * Provides simple unit testing features. Based on the Unity syntax.
 */

import java.util.function.Function;
import java.util.function.BiConsumer;
import java.util.Queue;
import java.lang.management.*;
import java.util.PriorityQueue;

public class Test {

    private static boolean allPassed;
    private static String currentFile;
    private static String currentFunc;
    private static int currentLine;
    private static final Queue<String> testMessages = new PriorityQueue<>();

    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_RED = "\u001B[31;1m";
    public static final String ANSI_GREEN = "\u001B[32;1m";

    /**
     * Carry out a test using the given function
     * 
     * @param test  The function containing a test. Call using ClassName::FunctionName
     */
    public static void RUN_TEST(Runnable test) {
        if (test == null)
            throw new IllegalArgumentException("Test was null!");
        allPassed = true;

        try {
            test.run();
            end();
        } catch (Exception e) {
            System.out.printf("%-20s", "[" + ANSI_RED + "EXCEPT" + ANSI_RESET + "]");
            StackTraceElement trace = e.getStackTrace()[0];
            System.out.printf("%s:%d", trace.getFileName(), trace.getLineNumber());
            System.out.printf(" - %s - %s\n", e.getClass().getName(), e.getMessage());

            while (!testMessages.isEmpty()) {
                System.out.println(testMessages.poll());
            }
        }
    }

    /**
     * Call this before beginning unit testing.
     */
    public static void BEGIN() {
        String file = Thread.currentThread().getStackTrace()[2].getFileName();
        System.out.println("\n===== Testing " + file + " =====\n");
    }

    /**
     * Call this after testing. Currently does nothing important
     */
    public static void END() {
        System.exit(0);
    }

    private static void end() {

        if (allPassed) {
            System.out.printf("%-20s", "[" + ANSI_GREEN + "PASSED" + ANSI_RESET + "]");
        } else {
            System.out.printf("%-20s", "[" + ANSI_RED + "FAILED" + ANSI_RESET + "]");
        }
        System.out.printf("%s\n", function());

        // Dump errors
        while (!testMessages.isEmpty()) {
            System.out.println("\t* " + testMessages.poll());
        }
        if (!allPassed)
            System.out.println();
    }

    private static StackTraceElement[] readStack() {
        StackTraceElement[] traces;
        try {
            traces = Thread.currentThread().getStackTrace();
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new IllegalStateException(
                    "Couldn't read stack trace - make sure main() calls RUN_TEST with test as parameter");
        }
        return traces;
    }

    private static String function() {
        return currentFunc;
    }

    private static void initTest() {

        StackTraceElement[] stack = readStack();
        StackTraceElement trace = stack[stack.length - 3];

        currentFunc = trace.getMethodName();
        currentLine = trace.getLineNumber();
        currentFile = trace.getFileName();
    }

    private static <T> void handleFailedTest(T expected, T actual, String message) {
        allPassed = false;

        String failMsg = currentFile + ":" + currentLine;
        if (expected != null && actual != null) {
            failMsg = String.format(failMsg + " - Expected %s, was %s", expected.toString(), actual.toString());
        } else if (expected == null && actual != null) {
            failMsg = String.format(failMsg + " - Expected %s, was %s", "null", actual.toString());
        }

        if (message != null) {
            failMsg += " - " + message;
        }
        testMessages.add(failMsg);
    }

    private static <T> void handleFailedTestWithin(Double delta, T expected, T actual, String message) {
        allPassed = false;

        String failMsg = currentFile + ":" + currentLine;
        if (expected != null || actual != null) {
            failMsg = String.format(failMsg + " - Expected %s within %s, was %s", expected.toString(), delta.toString(),
                    actual.toString());
        }
        if (message != null) {
            failMsg += " - " + message;
        }
        testMessages.add(failMsg);
    }

    /**
     * Test that two values are within a given delta when compared using compareTo
     * 
     * @param delta     The maximum allowed difference between expected and actual. delta > 0
     * @param expected  Expected value, expected != null
     * @param actual    Actual value, actual != null
     * @param message   A descriptive message to be printed if the test fails
     * 
     * post: Difference between expected and actual computed using expected.comapreTo(actual)
     */
    public static <T extends Comparable<T>> void ASSERT_NUM_WITHIN_MESSAGE(Double delta, T expected, T actual,
            String message) {
        initTest();
        if (Math.abs(expected.compareTo(actual)) > delta)
            handleFailedTestWithin(delta, expected, actual, message);
    }

    /**
    * Test that two values are within a given delta when compared using compareTo
    * 
    * @param delta     The maximum allowed difference between expected and actual. delta > 0
    * @param expected  Expected value, expected != null
    * @param actual    Actual value, actual != null
    * 
    * post: Difference between expected and actual computed using expected.comapreTo(actual)
    */
    public static <T extends Comparable<T>> void ASSERT_NUM_WITHIN(Double delta, T expected, T actual) {
        ASSERT_NUM_WITHIN_MESSAGE(delta, expected, actual, null);
    }

    /**
     * Assert that two objects are equal. Comparison is done using the equals() method
     * 
     * @param expected  Expected value, expected != null
     * @param actual    Actual value, actual != null
     * @param message   A descriptive message to be printed if the test fails
     * 
     */
    public static void ASSERT_EQUAL_MESSAGE(Object expected, Object actual, String message) {
        initTest();
        if (!expected.equals(actual)) {
            handleFailedTest(expected, actual, message);
        }
    }

    /**
     * Assert a reference is null
     * 
     * @param actual    Actual value
     * 
     */
    public static void ASSERT_NULL(Object actual) {
        ASSERT_NULL_MESSAGE(actual, null);
    }

    /**
     * Assert a reference is null
     * 
     * @param actual    Actual value
     * @param message   A descriptive message to be printed if the test fails
     * 
     */
    public static void ASSERT_NULL_MESSAGE(Object actual, String message) {
        initTest();
        if (actual != null) {
            handleFailedTest(null, actual, message);
        }
    }

    /**
     * Assert that two objects are equal. Comparison is done using the equals() method
     * 
     * @param expected  Expected value, expected != null
     * @param actual    Actual value, actual != null
     * 
     */
    public static void ASSERT_EQUAL(Object expected, Object actual) {
        ASSERT_EQUAL_MESSAGE(expected, actual, null);
    }

    /**
     * Assert a boolean result is true
     * 
     * @param test    A boolean test
     * 
     */
    public static void ASSERT_TRUE(boolean test) {
        ASSERT_TRUE_MESSAGE(test, null);
    }

    /**
     * Assert a boolean result is false
     * 
     * @param test    A boolean test
     * 
     */
    public static void ASSERT_FALSE(boolean test) {
        ASSERT_FALSE_MESSAGE(test, null);
    }

    /**
     * Assert a boolean result is true
     * 
     * @param test      A boolean test
     * @param message   A descriptive message to be printed if the test fails
     * 
     */
    public static void ASSERT_TRUE_MESSAGE(boolean test, String message) {
        initTest();
        if (!test)
            handleFailedTest(true, test, message);

    }

    /**
    * Assert a boolean result is false
    * 
    * @param test       A boolean test
    * @param message    A descriptive message to be printed if the test fails
    * 
    */
    public static void ASSERT_FALSE_MESSAGE(boolean test, String message) {
        initTest();
        if (test)
            handleFailedTest(false, test, message);
    }

    /**
    * Prototype. Attempts to run a given function, testing that the function completes within a given time
    * 
    * @param time_ms    Maximum time in milliseconds to allow the function to run before triggering a failed test
    *                   0 < time_ms
    * @param func       The function to time. func != null
    * @param message   A descriptive message to be printed if the test fails
    * 
    */
    public static void ASSERT_WITHIN_TIMEOUT_MESSAGE(double time_ms, Runnable func, String message) {
        initTest();
        Thread thread = new Thread(func);

        try {
            thread.start();
            thread.join((long) time_ms);
        } catch (Exception e) {
            handleFailedTestWithin(time_ms / 2, time_ms / 2, time_ms + 1, message);
        }
        if (thread.isAlive()) {
            handleFailedTestWithin(time_ms / 2, time_ms / 2, time_ms + 1, message);
        }
    }

    /**
    * Prototype. Attempts to run a given function, testing that the function completes within a given time
    * 
    * @param time_ms    Maximum time in milliseconds to allow the function to run before triggering a failed test
    *                   0 < time_ms
    * @param func       The function to time. func != null
    * 
    */
    public static void ASSERT_WITHIN_TIMEOUT(double time_ms, Runnable r) {
        ASSERT_WITHIN_TIMEOUT_MESSAGE(time_ms, r, null);
    }

    /**
     * Prototype. Attempts to run big O experiments on a given operation
     * 
     * @param num_trials    How many experiments to run, num_trials > 0
     * @param initial_size  The initial size of the data set. This is doubled for each subsequent run.
     *                      This is passed as the input to the supplier function to allow for generation of sample data
     * 
     * @param supplier      A function that accepts initial_size and returns a parameter that will be fed into the operation function
     * 
     * @param operation     The operation to time. Accepts a generic <E> input parameter from the output of supplier
     */
    public static <E> void TIME_EXPERIMENT(int num_trials, final int initial_size, Function<Integer, E> supplier,
            BiConsumer<Integer, E> operation) {
        Experiment<E> exp = new Experiment<>(num_trials, initial_size, supplier, operation);
        wrapExperiment(exp);
    }

    private static class Experiment<E> {

        private static final int NUM_SIZE_STEPS = 5;
        private static final int MAX_THREADS = 20;
        private static final int NANOS_PER_SEC = 1_000_000_000;

        private Function<Integer, E> supplier;
        private BiConsumer<Integer, E> operation;
        private Thread[] threads;
        private final int numTrials;
        private final int initialSize;

        double[] averages;

        public Experiment(int num_trials, int initial_size, Function<Integer, E> supplier,
                BiConsumer<Integer, E> operation) {
            this.supplier = supplier;
            this.operation = operation;
            numTrials = num_trials;
            initialSize = initial_size;
            threads = new Thread[NUM_SIZE_STEPS];
        }

        public void run() {
            averages = new double[NUM_SIZE_STEPS];
            System.out.printf("\n== Testing %s ==\n", currentFunc);
            System.out.printf("Repetitions: %d\n", numTrials);

            prepareThreads();
            startThreads();
            waitForThreads();
            processResults();
        }

        // Run trials for a given size, return the average time
        private double runExpOfSize(int size) {

            // Reset total counter and generate input data
            ThreadMXBean bean = ManagementFactory.getThreadMXBean();
            long total = 0;
            E input = supplier.apply(size);
            if (!bean.isCurrentThreadCpuTimeSupported()) {
                throw new UnsupportedOperationException();
            }

            // Run num_trials experiments and average the time
            for (int test = 0; test < numTrials; test++) {
                long start = bean.getCurrentThreadCpuTime();
                operation.accept(size, input);
                total = Math.addExact(total, Math.subtractExact(bean.getCurrentThreadCpuTime(), start));
            }
            return (double) total / numTrials * 1000 / NANOS_PER_SEC;
        }

        private void prepareThreads() {
            for (int exp_size = 0; exp_size < NUM_SIZE_STEPS; exp_size++) {
                final int exp_size_f = exp_size;
                threads[exp_size] = new Thread(() -> {
                    int size = initialSize * (0x01 << exp_size_f);
                    averages[exp_size_f] = runExpOfSize(size);
                });
            }
        }

        private void startThreads() {
            for (Thread thread : threads) {
                thread.start();
            }
        }

        private void waitForThreads() {
            int waitingOnThread = 0;
            while (waitingOnThread < NUM_SIZE_STEPS) {
                try {
                    threads[waitingOnThread].join();
                    waitingOnThread++;
                } catch (InterruptedException e) {
                }
            }
        }

        private void processResults() {

            double ratioTotal = 0;
            for (int exp_size = 0; exp_size < NUM_SIZE_STEPS; exp_size++) {

                int size = initialSize * (0x01 << exp_size);
                double average = averages[exp_size];
                double ratio = exp_size != 0 && averages[exp_size - 1] != 0 ? average / averages[exp_size - 1] : 0;
                ratioTotal += ratio;
                System.out.printf("Size: %d, Time(ms): %f, Ratio: %f\n", size, average, ratio);
            }

            System.out.printf("Average ratio: %f\n", ratioTotal / (NUM_SIZE_STEPS - 1));
        }

    }

    // Wrapper needed to provide correct function name
    private static <E> void wrapExperiment(Experiment<E> exp) {
        initTest();
        exp.run();
    }

}