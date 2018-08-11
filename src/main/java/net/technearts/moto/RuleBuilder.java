package net.technearts.moto;

import java.util.Arrays;

public class RuleBuilder {
  private String name;

  public RuleBuilder(String name) {
    this.name = name;
  }

  public Rule smallerThan(long max) {
    return new SimpleRule<Long>(name, x -> x < max);
  }

  public Rule smallerThan(int max) {
    return new SimpleRule<Integer>(name, x -> x < max);
  }

  public Rule biggerThan(long max) {
    return new SimpleRule<Long>(name, x -> x > max);
  }

  public Rule biggerThan(int max) {
    return new SimpleRule<Integer>(name, x -> x > max);
  }

  public Rule smallerOrEqualThan(long max) {
    return new SimpleRule<Long>(name, x -> x <= max);
  }

  public Rule smallerOrEqualThan(int max) {
    return new SimpleRule<Integer>(name, x -> x <= max);
  }

  public Rule biggerOrEqualThan(long max) {
    return new SimpleRule<Long>(name, x -> x >= max);
  }

  public Rule biggerOrEqualThan(int max) {
    return new SimpleRule<Integer>(name, x -> x >= max);
  }

  public Rule equal(long max) {
    return new SimpleRule<Long>(name, x -> x == max);
  }

  public Rule equal(int max) {
    return new SimpleRule<Integer>(name, x -> x == max);
  }

  public Rule notEqual(long max) {
    return new SimpleRule<Long>(name, x -> x != max);
  }

  public Rule notEqual(int max) {
    return new SimpleRule<Integer>(name, x -> x != max);
  }

  public Rule in(long... args) {
    return new SimpleRule<Long>(name,
        x -> Arrays.stream(args).anyMatch(arg -> arg == x));
  }

  public Rule in(int... args) {
    return new SimpleRule<Integer>(name,
        x -> Arrays.stream(args).anyMatch(arg -> arg == x));
  }

  public Rule notIn(long... args) {
    return new SimpleRule<Long>(name,
        x -> !Arrays.stream(args).anyMatch(arg -> arg == x));
  }

  public Rule notIn(int... args) {
    return new SimpleRule<Integer>(name,
        x -> !Arrays.stream(args).anyMatch(arg -> arg == x));
  }

  public Rule contains(long... args) {
    return new SimpleRule<Long[]>(name, x -> Arrays.stream(args)
        .allMatch(arg -> Arrays.binarySearch(x, arg) >= 0));
  }

  public Rule contains(int... args) {
    return new SimpleRule<Integer[]>(name, x -> Arrays.stream(args)
        .allMatch(arg -> Arrays.binarySearch(x, arg) >= 0));
  }

  public Rule notContains(long... args) {
    return new SimpleRule<Long[]>(name, x -> !Arrays.stream(args)
        .allMatch(arg -> Arrays.binarySearch(x, arg) >= 0));
  }

  public Rule notContains(int... args) {
    return new SimpleRule<Integer[]>(name, x -> Arrays.stream(args)
        .allMatch(arg -> Arrays.binarySearch(x, arg) >= 0));
  }
  
}
