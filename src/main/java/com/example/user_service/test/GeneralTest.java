package com.example.user_service.test;


import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class GeneralTest {

    public void print(Object object) {
        System.out.println("Object");
    }

    public void print(String s) {
        System.out.println("String");
    }

    public void countWords(String s){
        String [] words = s.split(" ");
        Map<String, Long> counterMap = new HashMap<>();

        counterMap = Arrays.stream(words).collect(Collectors.groupingBy(word -> word, Collectors.counting()));
        counterMap.forEach((k,v) -> System.out.println(k + ": " + v));
    }

    public static void main(String[] args) {
        GeneralTest test = new GeneralTest();
        test.countWords("java language hi java language program");
        test.print(test);
        test.print(null);
    }
}
