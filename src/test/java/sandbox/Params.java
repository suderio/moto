package sandbox;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class Params extends HashMap<String, List<Object>>{
  
  private static final long serialVersionUID = 3928982821419729682L;

  public void add(String name, Object... objects) {
    this.put(name, Arrays.asList(objects));
  }
}
