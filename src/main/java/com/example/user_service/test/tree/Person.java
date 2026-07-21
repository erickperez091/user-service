package com.example.user_service.test.tree;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jspecify.annotations.NonNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Person implements Comparable<Person> {

    private String name;
    private int age;

    @Override
    public int compareTo(@NonNull Person o) {
        return Integer.compare(this.age, o.age);
    }

    @Override
    public String toString() {
        return "{ \"Person\": { " +
                "\"Name\": \"" + name + "\", " +
                "\"Age\": " + age +
                "} }";
    }
}
