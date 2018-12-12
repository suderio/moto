package net.technearts.moto;

import java.util.Arrays;
import java.util.List;

public abstract class Lambda {
  public abstract Object apply(List<Object> params);

  public abstract List<Object> paramsOk(List<Object> params);

  public abstract List<Object> paramsNotOk(List<Object> params);

  public abstract List<Object> paramsMissing(List<Object> params);

  public Object apply(Object... params) {
    return apply(Arrays.asList(params));
  }

  public List<Object> paramsOk(Object... params) {
    return paramsOk(Arrays.asList(params));
  }

  public List<Object> paramsNotOk(Object... params) {
    return paramsNotOk(Arrays.asList(params));
  }

  public List<Object> paramsMissing(Object... params) {
    return paramsMissing(Arrays.asList(params));
  }

  public abstract List<Class<?>> allParams();
}
