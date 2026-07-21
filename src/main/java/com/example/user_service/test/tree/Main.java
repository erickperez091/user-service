package com.example.user_service.test.tree;

import java.util.Optional;

public class Main {

    public static void main(String[] args) {

        NodeTree<Integer> nodeTree = new NodeTree<>();

        nodeTree.add(5);
        nodeTree.add(10);
        nodeTree.add(3);
        nodeTree.add(6);
        nodeTree.add(4);
        nodeTree.add(2);
        nodeTree.add(12);
        nodeTree.add(3);

        Optional<Node<Integer>> nodeTree2 = nodeTree.find(3);
        Optional<Node<Integer>> nodeTree3 = nodeTree.find(12);
        System.out.println(nodeTree2.get());
        System.out.println(nodeTree3.get());
        System.out.println(nodeTree.getLevels());
        //nodeTree.remove(4);
        System.out.println( nodeTree.getRoot());
        nodeTree.printPreOrder();
        nodeTree.printInOrder();
        nodeTree.printPostOrder();

        NodeTree<Person> personTree = new NodeTree<>();
        personTree.add(new Person("John", 30));
        personTree.add(new Person("Jane", 25));
        personTree.add(new Person("Bob", 40));
        personTree.add(new Person("Alice", 28));
        personTree.add(new Person("Erick", 35));
        System.out.println(personTree.getRoot());

    }
}
