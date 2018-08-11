package sandbox;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class SimpleRule<T> extends Rule implements Predicate<T> {

  private final String name;
  private final Predicate<T> predicate;

  public SimpleRule(String name, Predicate<T> predicate) {
    this.name = name;
    this.predicate = t -> {
      try {
        return predicate.test(t);
      } catch (ClassCastException e) {
        return false;
      }
    };
  }

  @Override
  public String toString() {
    return name;
  }

  @Override
  public boolean test(T t) {
    return predicate.test(t);
  }

  public boolean test(Map<String, List<Object>> params) {
    return test(params, true);
  }

  public boolean test(Map<String, List<Object>> params, boolean defaultResult) {
    if (params.get(name) != null)
      return params.get(name).stream().map(this::cast).allMatch(predicate);
    return defaultResult;
  }

  public Set<String> paramsOk(Map<String, List<Object>> params) {
    return paramsOk(params, true);
  }

  public Set<String> paramsOk(Map<String, List<Object>> params, boolean defaultResult) {
    return params.entrySet().stream()
        .filter(e -> e.getValue().stream().map(this::cast).allMatch(predicate)).map(e -> e.getKey())
        .collect(Collectors.toSet());
  }

  public Set<String> paramsNotOk(Map<String, List<Object>> params) {
    return paramsNotOk(params, true);
  }

  public Set<String> paramsNotOk(Map<String, List<Object>> params, boolean defaultResult) {
    return params.entrySet().stream()
        .filter(e -> e.getValue().stream().map(this::cast).allMatch(predicate.negate()))
        .map(e -> e.getKey()).collect(Collectors.toSet());
  }

  @SuppressWarnings("unchecked")
  private T cast(Object o) {
    return (T) o;
  }

  @Override
  public Set<String> paramsMissing(Map<String, List<Object>> params) {
    HashSet<String> result = new HashSet<>();
    if (!params.containsKey(name)) {
      result.add(name);
    }
    return result;
  }

  @Override
  public Set<String> allParams() {
    HashSet<String> result = new HashSet<>();
    result.add(name);
    return result;
  }

  public static RuleBuilder rule(String name) {
    return new RuleBuilder(name);
  }
}
