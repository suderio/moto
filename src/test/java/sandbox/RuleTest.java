package sandbox;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import org.junit.Ignore;
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
    Rule r2 = new SimpleRule<Integer>("a", a -> a >= 0);
    Rule r3 = new SimpleRule<Integer>("b", b -> b > 0);
    Rule r4 = new SimpleRule<Integer>("b",
        b -> Stream.of(-1, 0, 1).anyMatch(i -> i == b));
    Rule r5 = new SimpleRule<String>("c",
        c -> Stream.of("A", "B", "C").anyMatch(i -> i.equals(c)));
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

    assertTrue(c1.allParams().containsAll(Arrays.asList("a", "b")));
    assertFalse(c1.allParams().containsAll(Arrays.asList("a", "b", "c")));
    assertTrue(c3.allParams().containsAll(Arrays.asList("a", "b", "c")));

    assertFalse(c1.paramsMissing(params).contains("a"));
    assertFalse(c2.paramsMissing(params).contains("b"));
    assertTrue(c3.paramsMissing(params).contains("c"));
  }

  @Test
  public void testSmaller() {
    Rule x = SimpleRule.rule("x").smallerThan(10);
    Rule y = SimpleRule.rule("y").smallerThan(10L);
    Rule z = SimpleRule.rule("z").smallerOrEqualThan(10);
    Rule w = SimpleRule.rule("w").smallerOrEqualThan(10L);

    Params params = new Params();
    params.add("x", 9);
    params.add("y", 9L);
    params.add("z", 10);
    params.add("w", 10L);

    assertTrue(x.test(params));
    assertTrue(y.test(params));
    assertTrue(z.test(params));
    assertTrue(w.test(params));

    params.add("x", 19);
    params.add("y", 19L);
    params.add("z", 110);
    params.add("w", 110L);

    assertFalse(x.test(params));
    assertFalse(y.test(params));
    assertFalse(z.test(params));
    assertFalse(w.test(params));
  }

  @Test
  public void testBigger() {
    Rule x = SimpleRule.rule("x").biggerThan(10);
    Rule y = SimpleRule.rule("y").biggerThan(10L);
    Rule z = SimpleRule.rule("z").biggerOrEqualThan(10);
    Rule w = SimpleRule.rule("w").biggerOrEqualThan(10L);

    Params params = new Params();
    params.add("x", 19);
    params.add("y", 19L);
    params.add("z", 110);
    params.add("w", 110L);

    assertTrue(x.test(params));
    assertTrue(y.test(params));
    assertTrue(z.test(params));
    assertTrue(w.test(params));

    params.add("x", 9);
    params.add("y", 9L);
    params.add("z", 0);
    params.add("w", 0L);

    assertFalse(x.test(params));
    assertFalse(y.test(params));
    assertFalse(z.test(params));
    assertFalse(w.test(params));
  }

  @Test
  public void testEqual() {
    Rule x = SimpleRule.rule("x").equal(10);
    Rule y = SimpleRule.rule("y").equal(10L);
    Rule z = SimpleRule.rule("z").notEqual(10);
    Rule w = SimpleRule.rule("w").notEqual(10L);

    Params params = new Params();
    params.add("x", 10);
    params.add("y", 10L);
    params.add("z", 11);
    params.add("w", 11L);

    assertTrue(x.test(params));
    assertTrue(y.test(params));
    assertTrue(z.test(params));
    assertTrue(w.test(params));
    
    params.add("x", 11);
    params.add("y", 11L);
    params.add("z", 10);
    params.add("w", 10L);

    assertFalse(x.test(params));
    assertFalse(y.test(params));
    assertFalse(z.test(params));
    assertFalse(w.test(params));
  }
  
  @Test
  public void testIn() {
    Rule x = SimpleRule.rule("x").in(0, 1, 2);
    Rule y = SimpleRule.rule("y").in(0L, 1L, 2L);
    Rule z = SimpleRule.rule("z").notIn(0, 1, 2);
    Rule w = SimpleRule.rule("w").notIn(0L, 1L, 2L);

    Params params = new Params();
    params.add("x", 0);
    params.add("y", 1L);
    params.add("z", 3);
    params.add("w", -1L);

    assertTrue(x.test(params));
    assertTrue(y.test(params));
    assertTrue(z.test(params));
    assertTrue(w.test(params));
    
    params.add("x", 0, 3);
    params.add("y", -1L);
    params.add("z", 0);
    params.add("w", 1L);

    assertFalse(x.test(params));
    assertFalse(y.test(params));
    assertFalse(z.test(params));
    assertFalse(w.test(params));
  }
  
  @Test @Ignore
  public void testContains() {
    Rule x = SimpleRule.rule("x").contains(0, 1, 2);
    Rule y = SimpleRule.rule("y").contains(0L, 1L, 2L);
    Rule z = SimpleRule.rule("z").notContains(0, 1, 2);
    Rule w = SimpleRule.rule("w").notContains(0L, 1L, 2L);

    Params params = new Params();
    params.add("x", 0, 1, 2);
    params.add("y", 0L, 1L, 2L);
    params.add("z", 0);
    params.add("w", 1L);

    assertTrue(x.test(params));
    assertTrue(y.test(params));
    assertTrue(z.test(params));
    assertTrue(w.test(params));
    
    params.add("x", 3);
    params.add("y", -1L);
    params.add("z", 0, 1, 2, 3);
    params.add("w", 0L, 1L, 2L);

    assertFalse(x.test(params));
    assertFalse(y.test(params));
    assertFalse(z.test(params));
    assertFalse(w.test(params));
  }
}
