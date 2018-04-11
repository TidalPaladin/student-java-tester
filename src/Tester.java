/**
 *  Demonstration / Tests for student-java-tester
 * 
 *  To use Test.java in an assigment, copy class Test and its required imports and paste it into your tst file.
 *  Remember to make class Test a nested or private class
 * 
 */

import java.util.ArrayList;
import java.util.function.Function;
import java.util.function.BiConsumer;

public class Tester {

    public static void main(String[] args) {

        Test.BEGIN();

        // Unit tests
        Test.RUN_TEST(Tester::test1_pass_assert_equal);
        Test.RUN_TEST(Tester::test2_pass_assert_true);
        Test.RUN_TEST(Tester::test3_pass_assert_false);
        Test.RUN_TEST(Tester::test4_pass_assert_within);
        Test.RUN_TEST(Tester::test5_fail_assert_equal);
        Test.RUN_TEST(Tester::test6_fail_assert_true);
        Test.RUN_TEST(Tester::test7_fail_assert_false);
        Test.RUN_TEST(Tester::test8_fail_assert_within);
        Test.RUN_TEST(Tester::test9_exception);
        Test.RUN_TEST(Tester::test10_timeout);
        Test.RUN_TEST(Tester::test11_pass_equal_objects);
        Test.RUN_TEST(Tester::test12_fail_equal_objects);
        Test.RUN_TEST(Tester::test13_pass_null);
        Test.RUN_TEST(Tester::test14_fail_null);

        // Big O tests
        Test.RUN_TEST(Tester::time_arraylist_add);
        Test.RUN_TEST(Tester::time_arraylist_get);

        Test.END();
    }

    public static void test1_pass_assert_equal() {
        int expected = 10;
        int actual = 10;
        Test.ASSERT_EQUAL(expected, actual);
    }

    public static void test2_pass_assert_true() {
        int expected = 10;
        int actual = 10;
        Test.ASSERT_TRUE(expected == actual);
    }

    public static void test3_pass_assert_false() {
        int expected = 10;
        int actual = 10;
        Test.ASSERT_FALSE(expected != actual);
    }

    public static void test4_pass_assert_within() {
        int expected = 10;
        int actual = 12;
        double delta = 2;
        Test.ASSERT_NUM_WITHIN(delta, expected, actual);
    }

    public static void test5_fail_assert_equal() {
        int expected = 10;
        int actual = 12;
        Test.ASSERT_EQUAL_MESSAGE(expected, actual, "OK, This should have failed!");
    }

    public static void test6_fail_assert_true() {
        int expected = 10;
        int actual = 12;
        Test.ASSERT_TRUE_MESSAGE(expected == actual, "OK, This should have failed!");
    }

    public static void test7_fail_assert_false() {
        int expected = 10;
        int actual = 12;
        Test.ASSERT_FALSE_MESSAGE(expected != actual, "OK, This should have failed!");
    }

    public static void test8_fail_assert_within() {
        int expected = 10;
        int actual = 12;
        double delta = 1;
        Test.ASSERT_NUM_WITHIN_MESSAGE(delta, expected, actual, "OK, This should have failed!");
    }

    public static void test9_exception() {
        int x = 1 / 0;
    }

    public static void test10_timeout() {
        ArrayList<Integer> list = new ArrayList<>();

        Runnable nSquared = () -> {
            for (int elem = 0; elem < 100000; elem++) {
                list.add(0, elem);
            }
        };

        double timeout_ms = 1000;
        Test.ASSERT_WITHIN_TIMEOUT(timeout_ms, nSquared);
        Test.ASSERT_WITHIN_TIMEOUT_MESSAGE(timeout_ms/10, nSquared, "OK, This should have failed");
    }

    public static void test11_pass_equal_objects() {
        String expected = "dog";
        String actual = "dog";
        Test.ASSERT_EQUAL(expected, actual);
    }

    public static void test12_fail_equal_objects() {
        String expected = "dog";
        String actual = "cat";
        Test.ASSERT_EQUAL_MESSAGE(expected, actual, "OK, This should have failed!");
    }

    public static void test13_pass_null() {
        String nullStr = null;
        Test.ASSERT_NULL(nullStr);
    }

    public static void test14_fail_null() {
        String expected = "dog";
        Test.ASSERT_NULL_MESSAGE(expected, "OK, This should have failed!");
    }

    public static void time_arraylist_add() {

        // Lambda function that accepts a size and should return an arraylist with that many elements
        Function<Integer, ArrayList<Integer>> arrayListGenerator = (size) -> {
            ArrayList<Integer> result = new ArrayList<Integer>(size);
            while (result.size() < size) {
                result.add(0);
            }
            return result;
        };

        int repeats_for_given_size = 100;
        int initial_arraylist_size = 100000;
        Test.TIME_EXPERIMENT(repeats_for_given_size, initial_arraylist_size, arrayListGenerator,
                Tester::arraylist_test);

    }

    // Non-lambda function representing the test that should be run on the arraylist generated above
    private static void arraylist_test(Integer size, ArrayList<Integer> data) {
        data.add(0, 0);
        data.remove(data.size()-1);
    }

    public static void time_arraylist_get() {

        // Lambda function that accepts a size and should return an arraylist with that many elements
        // Use this to create a data structure of a given size prior to testing
        Function<Integer, ArrayList<Integer>> arrayListGenerator = (size) -> {
            ArrayList<Integer> result = new ArrayList<Integer>(size);
            while (result.size() < size) {
                result.add(0);
            }
            return result;
        };

        // The actual operation to be tested
        BiConsumer<Integer, ArrayList<Integer>> get = (size, data) -> {
            data.get(0);
        };

        int repeats_for_given_size = 10000;
        int initial_arraylist_size = 100000;
        Test.TIME_EXPERIMENT(repeats_for_given_size, initial_arraylist_size, arrayListGenerator,
               get);
    }
}