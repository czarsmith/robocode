package lds.people;

import java.util.List;

public class Household {
  private String name;
  private String phone;
  private String email;
  private String address;
  private List<Person> members;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getPhone() {
    return phone;
  }

  public void setPhone(String phone) {
    this.phone = phone;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  public List<Person> getMembers() {
    return members;
  }

  public void setMembers(List<Person> members) {
    this.members = members;
  }
}
