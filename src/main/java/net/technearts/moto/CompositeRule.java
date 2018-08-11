package net.technearts.moto;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class CompositeRule extends Rule {

  private final String name;
  private int min;
  private int max;
  private final List<Rule> rules;

  private CompositeRule(String name, int min, int max, Rule... rules) {
    this.name = name;
    this.min = min;
    this.max = max;
    this.rules = new ArrayList<>();
    this.rules.addAll(Arrays.asList(rules));
  }

  @Override
  public String toString() {
    return name;
  }

  public static Rule noneMatch(String name, Rule... rules) {
    return new CompositeRule(name, 0, 0, rules);
  }

  public static Rule allMatch(String name, Rule... rules) {
    return new CompositeRule(name, rules.length, rules.length, rules);
  }

  public static Rule anyMatch(String name, Rule... rules) {
    return new CompositeRule(name, 1, rules.length, rules);
  }

  public static Rule exactMatch(String name, int qty, Rule... rules) {
    return new CompositeRule(name, qty, qty, rules);
  }

  public static Rule minMatch(String name, int min, Rule... rules) {
    return new CompositeRule(name, min, rules.length, rules);
  }

  @Override
  public boolean test(Map<String, List<Object>> params) {
    return test(params, true);
  }

  @Override
  public boolean test(Map<String, List<Object>> params, boolean defaultResult) {
    long qty = rules.stream().filter(rule -> rule.test(params)).count();
    return qty >= min && qty <= max;
  }

  @Override
  public Set<String> paramsOk(Map<String, List<Object>> params) {
    return paramsOk(params, true);
  }

  @Override
  public Set<String> paramsOk(Map<String, List<Object>> params, boolean defaultResult) {
    Set<String> result = new HashSet<>();
    rules.stream().forEach(rule -> result.addAll(rule.paramsOk(params, defaultResult)));
    return result;
  }

  @Override
  public Set<String> paramsNotOk(Map<String, List<Object>> params) {
    return paramsNotOk(params, true);
  }

  @Override
  public Set<String> paramsNotOk(Map<String, List<Object>> params, boolean defaultResult) {
    Set<String> result = new HashSet<>();
    rules.stream().forEach(rule -> result.addAll(rule.paramsNotOk(params, defaultResult)));
    return result;
  }

  @Override
  public Set<String> paramsMissing(Map<String, List<Object>> params) {
    final HashSet<String> result = new HashSet<>();
    result.addAll(allParams());
    result.removeAll(params.keySet());
    return result;
  }

  @Override
  public Set<String> allParams() {
    final HashSet<String> result = new HashSet<>();
    this.rules.stream().map(rule -> rule.allParams()).forEach(child -> result.addAll(child));;
    return result;
  }
}
