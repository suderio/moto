package net.technearts.moto;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class RuleBuilderTest {

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

    params.addAll("x", 0, 3);
    params.add("y", -1L);
    params.add("z", 0);
    params.add("w", 1L);

    assertFalse(x.test(params));
    assertFalse(y.test(params));
    assertFalse(z.test(params));
    assertFalse(w.test(params));
  }

  @Test
  public void testContains() {
    Rule x = SimpleRule.rule("x").contains(0, 1, 2);
    Rule y = SimpleRule.rule("y").contains(0L, 1L, 2L);
    Rule z = SimpleRule.rule("z").notContains(0, 1, 2);
    Rule w = SimpleRule.rule("w").notContains(0L, 1L, 2L);

    Params params = new Params();
    params.add("x", new Integer[] { 0, 1, 2, 3 });
    params.add("y", new Long[] { 0L, 1L, 2L, 3L });
    params.add("z", new Integer[] { 0, 1, 2 });
    params.add("w", new Long[] { 1L, 0L, 2L });

    assertTrue(x.test(params));
    assertTrue(y.test(params));
    assertTrue(z.test(params));
    assertTrue(w.test(params));

    params.add("x", 3);
    params.add("y", -1L);
    params.addAll("z", 0, 1, 2, 3);
    params.addAll("w", 0L, 1L, 3L);

    assertFalse(x.test(params));
    assertFalse(y.test(params));
    assertFalse(z.test(params));
    assertFalse(w.test(params));
  }
}
