package sandbox;

import java.util.List;
import java.util.Map;
import java.util.Set;

public abstract class Rule {
  public abstract boolean test(Map<String, List<Object>> params);
  public abstract boolean test(Map<String, List<Object>> params, boolean defaultResult);
  public abstract Set<String> paramsOk(Map<String, List<Object>> params);
  public abstract Set<String> paramsOk(Map<String, List<Object>> params, boolean defaultResult);
  public abstract Set<String> paramsNotOk(Map<String, List<Object>> params);
  public abstract Set<String> paramsNotOk(Map<String, List<Object>> params, boolean defaultResult);
  public abstract Set<String> paramsMissing(Map<String, List<Object>> params);
  public abstract Set<String> allParams();

}
