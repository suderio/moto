package sandbox;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static sandbox.CompositeRule.allMatch;
import static sandbox.CompositeRule.anyMatch;
import static sandbox.CompositeRule.exactMatch;
import static sandbox.CompositeRule.minMatch;
import static sandbox.CompositeRule.noneMatch;

public class RuleTest {

  @Test
  public void test() {
    Rule r1 = new SimpleRule<Integer>("a", a -> a < 10);
    Rule s1 = SimpleRule.rule("sa").smallerThan(10);
    Rule r2 = new SimpleRule<Integer>("a", a -> a >= 0);
    Rule r3 = new SimpleRule<Integer>("b", b -> b > 0);
    Rule r4 = new SimpleRule<Integer>("b", b -> Stream.of(-1, 0, 1).anyMatch(i -> i == b));
    Rule r5 = new SimpleRule<String>("c", c -> Stream.of("A", "B", "C").anyMatch(i -> i.equals(c)));
    Rule c1 = allMatch("C", r1, r2, r3, r4);
    Rule c2 = anyMatch("C", r1, r2, r3, r4);
    Rule c3 = noneMatch("C", r1, r2, r3, r4, r5);
    Rule c4 = exactMatch("C", 2, r1, r2, r3, r4);
    Rule c5 = minMatch("C", 2, r1, r2, r3, r4, r5);
    Map<String, List<Object>> params = new HashMap<>();

    assertEquals(r1.toString(), "a");
    assertEquals(c1.toString(), "C");
    
    params.put("a", Arrays.asList(30));
    assertFalse(r1.test(params));
    
    params.put("a", Arrays.asList("30"));
    assertFalse(r1.test(params));
    
    params.put("b", Arrays.asList(30));
    assertTrue(r3.test(params));
    
    params.put("a", Arrays.asList(9));
    params.put("b", Arrays.asList(1));
    
    assertTrue(c1.test(params));
    assertTrue(c2.test(params));
    assertFalse(c3.test(params));
    assertFalse(c4.test(params));
    assertTrue(c5.test(params));
    
    assertTrue(r1.paramsOk(params).contains("a"));
    assertTrue(r2.paramsOk(params).contains("a"));
    assertTrue(r3.paramsOk(params).contains("b"));
    assertTrue(r4.paramsOk(params).contains("b"));
    assertFalse(r5.paramsOk(params).contains("c"));
    
    assertFalse(r1.paramsNotOk(params).contains("a"));
    assertFalse(r2.paramsNotOk(params).contains("a"));
    assertFalse(r3.paramsNotOk(params).contains("b"));
    assertFalse(r4.paramsNotOk(params).contains("b"));
    assertFalse(r5.paramsNotOk(params).contains("c"));
    
    assertTrue(c1.allParams().containsAll(Arrays.asList("a","b")));
    assertFalse(c1.allParams().containsAll(Arrays.asList("a","b","c")));
    assertTrue(c3.allParams().containsAll(Arrays.asList("a","b","c")));
    
    assertFalse(c1.paramsMissing(params).contains("a"));
    assertFalse(c2.paramsMissing(params).contains("b"));
    assertTrue(c3.paramsMissing(params).contains("c"));
  }

}
