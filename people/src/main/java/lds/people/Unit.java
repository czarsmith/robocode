package lds.people;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class Unit {
  private String name;
  private List<Person> callings;
  
  public String getName() {
    return name;
  }
  public void setName(String name) {
    this.name = name;
  }
  public List<Person> getCallings() {
    return callings;
  }
  public void setCallings(List<Person> callings) {
    this.callings = callings;
  }
}
