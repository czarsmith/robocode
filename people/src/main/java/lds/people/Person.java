package lds.people;

import java.util.List;

public class Person {
  private String givenName;
  private String surname;
  private List<Position> positions;
  private String email;
  private String phone;
  private String unitNumber;

  public String getGivenName() {
    return givenName;
  }
  public void setGivenName(String givenName) {
    this.givenName = givenName;
  }
  public String getSurname() {
    return surname;
  }
  public void setSurname(String surname) {
    this.surname = surname;
  }
  public List<Position> getPositions() {
    return positions;
  }
  public void setPositions(List<Position> positions) {
    this.positions = positions;
  }
  public String getEmail() {
    return email;
  }
  public void setEmail(String email) {
    this.email = email;
  }
  public String getPhone() {
    return phone;
  }
  public void setPhone(String phone) {
    this.phone = phone;
  }
  public String getUnitNumber() {
    return unitNumber;
  }
  public void setUnitNumber(String unitNumber) {
    this.unitNumber = unitNumber;
  }
}
