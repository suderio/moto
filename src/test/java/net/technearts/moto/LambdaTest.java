package net.technearts.moto;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

public class LambdaTest {

  @Test
  public void simpleTest() {
    Lambda<BigDecimal> sub = Lambda.builder("sub", BigDecimal.class).param("a", BigDecimal.class)
        .param("b", BigDecimal.class).returns("a - b");
    Map<String, Object> params = new HashMap<>();
    params.put("a", new BigDecimal(7));
    params.put("b", new BigDecimal(3));
    BigDecimal result = sub.evaluate(params, null);
    assertEquals(new BigDecimal(4), result);

    params = new HashMap<>();
    params.put("a", new BigDecimal(1));
    params.put("b", new BigDecimal(1));
    result = sub.evaluate(params, null);
    assertEquals(new BigDecimal(0), result);
  }

  @Test
  public void dependencyTest() {
    Lambda<BigDecimal> sum = Lambda.builder("sum", BigDecimal.class).param("a", BigDecimal.class)
        .param("b", BigDecimal.class).returns("a + b");
    Lambda<BigDecimal> sub = Lambda.builder("sub", BigDecimal.class).uses(sum).param("a", BigDecimal.class)
        .param("b", BigDecimal.class).returns("a - b");
    Lambda<BigDecimal> formula = Lambda.builder("formula", BigDecimal.class)
        .param("a", BigDecimal.class).param("b", BigDecimal.class).uses(sum, sub).returns("sum(a, b) - sub(a, b)");
    Map<String, Object> params = new HashMap<>();
    params.put("a", new BigDecimal(7));
    params.put("b", new BigDecimal(3));
    BigDecimal result = formula.evaluate(params, null);
    assertEquals(new BigDecimal(6), result);
  }

}
