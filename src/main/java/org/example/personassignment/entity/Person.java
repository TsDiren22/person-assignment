package org.example.personassignment.entity;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Person {
    public static final long EMPTY_PARENT_1_ID = 999991L;
    public static final long EMPTY_PARENT_2_ID = 999992L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private LocalDate birthDate;
    @ManyToOne
    @JoinColumn(name = "parent1_id")
    private Person parent1;
    @ManyToOne
    @JoinColumn(name = "parent2_id")
    private Person parent2;
    @OneToMany(mappedBy = "parent1", cascade = CascadeType.ALL)
    private List<Person> children = new ArrayList<>();
    @OneToOne
    @JoinColumn(name = "partner_id")
    private Person partner;

    public boolean isRoot() {
        return (parent1 == null || parent1.getId() == EMPTY_PARENT_1_ID) &&
                (parent2 == null || parent2.getId() == EMPTY_PARENT_2_ID);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public Person getParent1() {
        return parent1;
    }

    public void setParent1(Person parent1) {
        this.parent1 = parent1;
    }

    public Person getParent2() {
        return parent2;
    }

    public void setParent2(Person parent2) {
        this.parent2 = parent2;
    }

    public List<Person> getChildren() {
        return children;
    }

    public void setChildren(List<Person> children) {
        this.children = children;
    }

    public Person getPartner() {
        return partner;
    }

    public void setPartner(Person partner) {
        this.partner = partner;
    }
}
