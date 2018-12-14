package net.technearts.moto;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import groovy.lang.Binding;
import groovy.lang.GroovyShell;
import lombok.Data;

@Data
public class Lambda<R> {
  private String name;
  private List<Parameter> parameters = new ArrayList<>();
  private String varargType;
  private String resultType;
  private String script;
  private GroovyShell shell;
  private Binding binding;
  private Set<Lambda<?>> dependencies;

  public Lambda() {
    this.binding = new Binding();
    this.shell = new GroovyShell(binding);
    this.dependencies = new HashSet<>();
  }

  @SuppressWarnings("unchecked")
  public R evaluate(Map<String, Object> params, List<Object> varargs) {
    if (params.size() != parameters.size()) {
      throw new IllegalArgumentException(
          "Parâmetros enviados não correspondem aos definidos na função.");
    }
    StringBuilder sb = new StringBuilder();
    sb.append(this);
    sb.append(name).append('(');
    for (Parameter p : parameters) {
      if (!params.containsKey(p.getName())) {
        throw new IllegalArgumentException("Parâmetro " + p.getName() + " está faltando");
      }
      sb.append("param_").append(p.getName()).append(',');
      binding.setVariable("param_" + p.getName(), params.get(p.getName()));
    }
    sb.setCharAt(sb.length() - 1, ')');
    System.out.println(sb);
    return (R) shell.evaluate(sb.toString());
  }

  public String toString() {
    StringBuilder params = new StringBuilder();
    for (Parameter p : parameters) {
      params.append(p.getType()).append(' ').append(p.getName()).append(", ");
    }
    params.setLength(params.length() - 2);
    String result = "";
    for (Lambda<?> dependency : dependencies) {
      result += dependency;
    }
    result += String.format("%s %s(%s) {\n%s\n}\n", resultType, name, params, script);
    return result;
  }

  public static <T> Lambda<T> builder(String name, Class<T> resultType) {
    Lambda<T> result = new Lambda<>();
    result.setName(name);
    result.setResultType(resultType.getSimpleName());
    return result;
  }

  public <T> Lambda<R> param(String name, Class<T> klazz) {
    Parameter parameter = new Parameter();
    parameter.setName(name);
    parameter.setType(klazz.getSimpleName());
    parameters.add(parameter);
    return this;
  }

  public Lambda<R> returns(String script) {
    this.script = script;
    return this;
  }

  public Lambda<R> uses(Lambda<?>... dependencies) {
    for (Lambda<?> dependency : dependencies) {
      this.dependencies.add(dependency);
    }
    this.dependencies.addAll(dependencies());
    return this;
  }

  private Set<Lambda<?>> dependencies() {
    Set<Lambda<?>> result = new HashSet<>();
    for (Lambda<?> dependency : this.dependencies) {
      result.addAll(dependency.dependencies());
    }
    result.addAll(this.dependencies);
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (!(obj instanceof Lambda)) {
      return false;
    }
    Lambda other = (Lambda) obj;
    boolean sameParameters = false;
    for (Parameter p : parameters) {
      sameParameters
    }
    return Objects.equals(name, other.name) && Objects.equals(parameters, other.parameters);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, parameters);
  }
}


@Data
class Parameter {
  private String name;
  private String type;

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (!(obj instanceof Parameter)) {
      return false;
    }
    Parameter other = (Parameter) obj;
    return Objects.equals(type, other.type);
  }

  @Override
  public int hashCode() {
    return Objects.hash(type);
  }
}

