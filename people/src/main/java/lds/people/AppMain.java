package lds.people;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.ListUtils;
import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

public class AppMain {
  @SuppressWarnings("serial")
  private static Map<String, String> units = new HashMap<String, String>() {
    {
      put("2132214", "ONMS"); // Omaha Nebraska Millard Stake
      put("2010216", "NOM"); // Nebraska Omaha Mission
      put("2054949", "CH"); // Chalco Hills Ward
      put("220426", "EW"); // Elmwood Ward
      put("2122022", "FC"); // Fieldclub Branch
      put("530867", "GR"); // Gretna Ward
      put("2122014", "HH"); // Harrison Hills Ward
      put("485322", "LV"); // Lakeview Ward
      put("130710", "ML"); // Millard Ward
      put("2143534", "RV"); // Rio Vista Branch (Spanish)
      put("94153", "RB"); // Rockbrook Ward
    }
  };

  private ObjectMapper om;

  private AppMain() {
    om = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
  }

  private void go() throws IOException {
    Arrays.asList("ch", "ew", "fc", "gr", "hh", "lv", "ml", "rb", "rv").stream()
      .flatMap(this::readHouseholds)
      .forEach(this::printHousehold);    
  }

  private Stream<Household> readHouseholds(String unit) {
    try {
      return om.readValue(Household.class.getResource(unit + ".json"),
        new TypeReference<List<Household>>(){}).stream();
    }
    catch (IOException e) {
      e.printStackTrace();
    }
    return null;
  }

  private void printHousehold(Household h) {
    for (Person person : h.getMembers()) {
      if (CollectionUtils.isEmpty(person.getPositions())) {
        List<String> parts = new ArrayList<>();
        parts.add(getUnitName(person.getUnitNumber()));
        parts.add("");
        parts.add(person.getSurname());
        parts.add(person.getGivenName());
        parts.add(StringUtils.defaultString(StringUtils.defaultString(person.getEmail(), h.getEmail())));
        parts.add(StringUtils.defaultString(StringUtils.defaultString(person.getPhone(), h.getPhone())));

        System.out.println(String.join("\t", parts));      
      }
      else {
        for (Position position : ListUtils.emptyIfNull(person.getPositions())) {
          List<String> parts = new ArrayList<>();
          parts.add(getUnitName(position.getUnitNumber()));
          parts.add(position.getPositionTypeName());
          parts.add(person.getSurname());
          parts.add(person.getGivenName());
          parts.add(StringUtils.defaultString(StringUtils.defaultString(person.getEmail(), h.getEmail())));
          parts.add(StringUtils.defaultString(StringUtils.defaultString(person.getPhone(), h.getPhone())));
  
          System.out.println(String.join("\t", parts));
        }
      }
    }
  }

  private String getUnitName(String number) {
    return StringUtils.defaultString(units.get(number), number);
  }
  public static final void main(String[] args) throws IOException { 
    new AppMain().go();
  }
}
