package org.example.personassignment.utility;

import org.example.personassignment.entity.Person;

import java.time.LocalDate;
import java.util.List;

public class PersonFilterUtils {

    private PersonFilterUtils (){

    }

    public static boolean hasPartner(Person person) {
        return person.getPartner() != null;
    }

    public static boolean hasThreeChildrenWithPartner(Person person, List<Person> allPersons) {
        if (person.getPartner() == null) {
            return false;
        }

        Person partner = person.getPartner();

        long childrenCount = allPersons.stream()
                .filter(p -> hasParents(p, person, partner))
                .count();

        return childrenCount == 3;
    }

    public static boolean hasParents(Person child, Person parent1, Person parent2) {
        return (child.getParent1() != null && child.getParent2() != null) &&
                ((child.getParent1().getId().equals(parent1.getId()) && child.getParent2().getId().equals(parent2.getId())) ||
                        (child.getParent1().getId().equals(parent2.getId()) && child.getParent2().getId().equals(parent1.getId())));
    }

    public static boolean hasOneChildUnder18(Person person, List<Person> allPersons) {
        if (person.getPartner() == null) {
            return false;
        }

        Person partner = person.getPartner();
        LocalDate today = LocalDate.now();

        long childrenUnder18Count = allPersons.stream()
                .filter(p -> hasParents(p, person, partner))
                .filter(p -> isUnder18(p.getBirthDate(), today))
                .count();

        return childrenUnder18Count == 1;
    }

    public static boolean isUnder18(LocalDate birthDate, LocalDate referenceDate) {
        return birthDate.plusYears(18).isAfter(referenceDate);
    }
}