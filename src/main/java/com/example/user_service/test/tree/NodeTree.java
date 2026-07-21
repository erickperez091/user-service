package com.example.user_service.test.tree;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Queue;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NodeTree<T extends Comparable<? super T>> {

    private Node<T> root;

    public void add(T element) {
        this.root = this.add(element, root);
    }

    public void remove(T element) {
        this.root = this.remove(element, root);
    }

    public Optional<Node<T>> find(T element) {
        return Optional.of(this.findElement(element, root));
    }

    public boolean contains(T element) {
        return this.findElement(element, root) != null;
    }

    public void printPreOrder() {
        System.out.print("PreOrder: ");
        printPreOrder(this.root);
        System.out.println();
    }

    public void printInOrder() {
        System.out.print("InOrder: ");
        this.printInOrder(this.root);
        System.out.println();
    }

    public void printPostOrder() {
        System.out.print("PostOrder: ");
        printPostOrder(this.root);
        System.out.println();
    }

    public List<List<T>> getLevels() {
        List<List<T>> levels = new ArrayList<>();
        return this.getLevels(this.root, new LinkedList<>(), levels);
    }

    private Node<T> add(T element, Node<T> currentNode) {
        if (currentNode == null) {
            return new Node<>(element, null, null);
        } else if (element.compareTo(currentNode.getElement()) < 0) {
            currentNode.setLeftChild(add(element, currentNode.getLeftChild()));
        } else if (element.compareTo(currentNode.getElement()) > 0) {
            currentNode.setRightChild(add(element, currentNode.getRightChild()));
        }
        return currentNode;
    }

    private Node<T> remove(T element, Node<T> currentNode) {
        if (currentNode == null) return null;

        if (element.compareTo(currentNode.getElement()) < 0) {
            currentNode.setLeftChild(remove(element, currentNode.getLeftChild()));
            return currentNode;
        }

        if (element.compareTo(currentNode.getElement()) > 0) {
            currentNode.setRightChild(remove(element, currentNode.getRightChild()));
            return currentNode;
        }

        if (currentNode.getLeftChild() == null && currentNode.getRightChild() == null) {
            return null;
        }
        if (currentNode.getLeftChild() == null) {
            return currentNode.getRightChild();
        }

        if (currentNode.getRightChild() == null) {
            return currentNode.getLeftChild();
        }

        Node<T> minNode = findMin(currentNode.getRightChild());
        currentNode.setElement(minNode.getElement());
        currentNode.setRightChild(remove(minNode.getElement(), currentNode.getRightChild()));
        return currentNode;
    }

    private Node<T> findElement(T element, Node<T> currentNode) {
        if (currentNode == null) return null;
        if (currentNode.getElement().equals(element)) return currentNode;
        if (element.compareTo(currentNode.getElement()) < 0) {
            return findElement(element, currentNode.getLeftChild());
        }
        if (element.compareTo(currentNode.getElement()) > 0) {
            return findElement(element, currentNode.getRightChild());
        }
        return null;
    }

    private void printPreOrder(Node<T> currentNode) {
        if (currentNode == null) {
            return;
        }
        System.out.printf("Value: %s -> ", currentNode.getElement());
        printPreOrder(currentNode.getLeftChild());
        printPreOrder(currentNode.getRightChild());
    }

    private void printInOrder(Node<T> currentNode) {
        if (currentNode == null) {
            return;
        }
        printInOrder(currentNode.getLeftChild());
        System.out.printf("Value: %s -> ", currentNode.getElement());
        printInOrder(currentNode.getRightChild());
    }

    private void printPostOrder(Node<T> currentNode) {
        if (currentNode == null) {
            return;
        }
        printPostOrder(currentNode.getLeftChild());
        printPostOrder(currentNode.getRightChild());
        System.out.printf("Value: %s -> ", currentNode.getElement());
    }

    private List<List<T>> getLevels(Node<T> currentNode, Queue<Node<T>> queue, List<List<T>> levels) {
        if (currentNode == null) return levels;
        queue.add(currentNode);
        while (!queue.isEmpty()) {
            int queueSize = queue.size();
            List<T> level = new ArrayList<>();
            for (int i = 0; i < queueSize; i++) {
                Node<T> tmpNode = queue.poll();
                assert tmpNode != null;
                level.add(tmpNode.getElement());
                if (tmpNode.getLeftChild() != null) {
                    queue.add(tmpNode.getLeftChild());
                }
                if (tmpNode.getRightChild() != null) {
                    queue.add(tmpNode.getRightChild());
                }
            }
            levels.add(level);
        }
        return levels;
    }

    private Node<T> findMin(Node<T> currentNode) {
        while (currentNode.getLeftChild() != null) {
            currentNode = currentNode.getLeftChild();
        }
        return currentNode;
    }

}
