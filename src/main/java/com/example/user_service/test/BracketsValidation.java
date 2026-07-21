package com.example.user_service.test;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Map;

public class BracketsValidation {

    private static final Map<Character, Character> bracketsMap = Map.of('}', '{', ']', '[', ')', '(');

    private boolean isBracketsValid(String brackets) {
        Deque<Character> stack = new ArrayDeque<>();

        for (int i = 0; i < brackets.length(); i++) {
            char element = brackets.charAt(i);
            if(!bracketsMap.containsKey(element)){
                stack.push(element);
            } else{
                if (stack.isEmpty()) return false;
                char bracket = stack.pop();
                char valueToCompare = bracketsMap.get(element);
                if(bracket != valueToCompare) return false;
            }
        }
        return stack.isEmpty();
    }

    public static void main(String[] args) {

        BracketsValidation bracketsValidation = new BracketsValidation();

        String brackets_1 = "()[]{}";
        String brackets_2 = "([{}])";
        String brackets_3 = "(){";
        String brackets_4 = "[{]}";
        System.out.println("Brackets list: \"" + brackets_1 + "\" " + (bracketsValidation.isBracketsValid(brackets_1) ? "is balanced" : "is not balanced"));
        System.out.println("Brackets list: \"" + brackets_2 + "\" " + (bracketsValidation.isBracketsValid(brackets_2) ? "is balanced" : "is not balanced"));
        System.out.println("Brackets list: \"" + brackets_3 + "\" " + (bracketsValidation.isBracketsValid(brackets_3) ? "is balanced" : "is not balanced"));
        System.out.println("Brackets list: \"" + brackets_4 + "\" " + (bracketsValidation.isBracketsValid(brackets_4) ? "is balanced" : "is not balanced"));

    }


}
