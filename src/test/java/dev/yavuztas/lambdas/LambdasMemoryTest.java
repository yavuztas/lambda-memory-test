package dev.yavuztas.lambdas;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class LambdasMemoryTest {

  static final int FILTER_OPERATION_COUNT = 1000000;

  List<String> dictionary = new ArrayList<>();

  static class NameFilter implements Predicate<String> {

    @Override
    public boolean test(String s) {
      return s.startsWith("a");
    }
  }

  abstract static class AbstractNameFilter implements Predicate<String> {

  }

  static long memoryUsage(){
    final long heapUsed = (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / (1024);
    System.out.println(
        heapUsed + " KB / " + Runtime.getRuntime().totalMemory()/(1024*1024) + " MB");
    return heapUsed;
  }

  static void printMemoryConsumed(long heapUsedBefore){
    System.out.println("Amount consumed: " + (memoryUsage() - heapUsedBefore) + " KB");
  }

  @BeforeEach
  void setup(){
    for (int i = 0; i < 10; i++) {
      this.dictionary.add(String.valueOf(Math.random() * 1000));
    }
    this.dictionary.add("a00");
  }

  @Test
  void testObjectMemoryConsumption(){
    final long heapUsedBefore = memoryUsage();
    for (int i = 0; i < FILTER_OPERATION_COUNT; i++) {
      final Optional<String> first = this.dictionary.stream().filter(new NameFilter()).findFirst();
    }
    printMemoryConsumed(heapUsedBefore);
  }

  @Test
  void testAICMemoryConsumption(){
    final long heapUsedBefore = memoryUsage();
    for (int i = 0; i < FILTER_OPERATION_COUNT; i++) {
      final Optional<String> first = this.dictionary.stream().filter(new AbstractNameFilter() {
        @Override
        public boolean test(String s) {
          return s.startsWith("a");
        }
      }).findFirst();
    }
    printMemoryConsumed(heapUsedBefore);
  }

  @Test
  void testLambdaMemoryConsumption(){
    final long heapUsedBefore = memoryUsage();
    for (int i = 0; i < FILTER_OPERATION_COUNT; i++) {
      final Optional<String> first = this.dictionary.stream().filter(s -> s.startsWith("a")).findFirst();
    }
    printMemoryConsumed(heapUsedBefore);
  }

}
