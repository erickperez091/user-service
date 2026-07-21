package com.example.user_service.test.tree;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Node<T> {
    private T element;
    private Node<T> leftChild;
    private Node<T> rightChild;

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        sb.append("\"Node\": { ");
        sb.append("\"Element\": ").append(element);

        if (leftChild != null) {
            sb.append(", \"LeftChild\": ").append(leftChild);
        } else {
            sb.append(", \"LeftChild\": {}");
        }

        if (rightChild != null) {
            sb.append(", \"RightChild\": ").append(rightChild);
        } else {
            sb.append(", \"RightChild\": {}");
        }

        sb.append("}");
        sb.append("}");

        return sb.toString();
    }
}
