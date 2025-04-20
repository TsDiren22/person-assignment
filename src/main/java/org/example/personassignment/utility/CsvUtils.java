package org.example.personassignment.utility;

import org.example.personassignment.entity.Person;

import java.util.Base64;
import java.util.List;

public class CsvUtils {
    private CsvUtils() {
    }

    public static String personsToCsv(List<Person> persons) {
        StringBuilder csvBuilder = new StringBuilder();

        csvBuilder.append("ID,Name,Birth Date,Parent1 ID,Parent2 ID,Partner ID\n");

        for (Person person : persons) {
            csvBuilder.append(person.getId()).append(",")
                    .append(escapeSpecialCharacters(person.getName())).append(",")
                    .append(person.getBirthDate()).append(",")
                    .append(person.getParent1() != null ? person.getParent1().getId() : "").append(",")
                    .append(person.getParent2() != null ? person.getParent2().getId() : "").append(",")
                    .append(person.getPartner() != null ? person.getPartner().getId() : "")
                    .append("\n");
        }

        return csvBuilder.toString();
    }

    public static String encodeToBase64(String content) {
        return Base64.getEncoder().encodeToString(content.getBytes());
    }

    private static String escapeSpecialCharacters(String value) {
        if (value == null) {
            return "";
        }

        if (value.contains(",") || value.contains("\"") || value.contains("\n")) {
            return "\"" + value.replace("\"", "\"\"") + "\"";
        }
        return value;
    }
}