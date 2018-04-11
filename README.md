# student-java-tester

## Note

This library is still a work in progress. Some methods may be incomplete, poorly implemented, or wrong. Take this into consideration if you observe unexpected behavior.
## Purpose

Students often face restrictions on Java programming assignments that would make the use of unit testing software such as JUnit impractical. This library offers a single standalone file that can be transferred into the assignment test file without interfering with grading scripts. The goal is to reduce the time spent worrying about the formatting of test output, so that more time can be spent writing quality tests. API is based on the Unity syntax.

## Sample Output
![Output](sample-output.png)
## Key Features
  * **Modular** - 
  Individual tests can be grouped into separate methods

  * **Exception Handling** - 
  Testing will continue if a test throws an exception

  * **Algorithmic Comlexity Benchmarks (Prototype)** - 
  Multithreaded solution allows for faster big O benchmarking

  * **Colors** - 
  Colorful output to easily spot failures (tested on Unix only)

  * **Failure Descriptions** - 
  As with Unity, appending MESSAGE to any assertion call will allow for the addition of a
  helpful description of what failed

## Basic API

Javadocs are provided in the source code. See [tests](tests) for usage examples.

```java
void BEGIN()
void END()

void RUN_TEST(Runnable test)

<T extends Comparable>> void ASSERT_NUM_WITHIN(Double delta, T expected, T actual)
<T extends Comparable>> void ASSERT_NUM_WITHIN_MESSAGe(Double delta, T expected, T actual, String mesage)

void ASSERT_EQUAL(Object expected, Object actual)
void ASSERT_EQUAL_MESSAGE(Object expected, Object actual, String message)

void ASSERT_NULL(Object actual)
void ASSERT_NULL_MESSAGE(Object actual, String message)

void ASSERT_TRUE(boolean actual)
void ASSERT_TRUE_MESSAGE(boolean actual, String message)

void ASSERT_FALSE(boolean actual)
void ASSERT_FALSE_MESSAGE(boolean actual, String message)

void ASSERT_WITHIN_TIMEOUT(double time_ms, Runnable operation)
void ASSERT_WITHIN_TIMEOUT_MESSAGE(double time_ms, Runnable operation, String message)

// Generates a data structure using supplier. Supplier is passed the size of the data structure that needs to be created
// Averages times for 'num_trials' repeats of operation, with 5 size doublings. Lowest size is initial_size.
<E> void TIME_EXPERIMENT(int num_trials, int initial_size, Function<Integer, E> supplier, BiConsumer<Integer, E> operation)
```


## TODO List

  * Big O benchmarking needs a more efficient distribution of threaded workload
  * Big O benchmarking needs continued verification of validity. The results seem erradic
