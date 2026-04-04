package org.example.core;

public class Node {
    private int value;
    private Node left;
    private Node right;
    // for RBTree
    private boolean isRed;
    private Node parent;

    public Node(int value){
        this.value = value;
        this.left = null;
        this.right = null;
        this.parent = null;
        this.isRed = true;
    }

    public int getValue(){
        return value;
    }
    public Node getLeft() {
        return left;
    }

    public Node getRight() {
        return right;
    }

    public boolean isRed() {
        return isRed;
    }

    public Node getParent() {
        return parent;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public void setLeft(Node left) {
        this.left = left;
    }

    public void setRight(Node right) {
        this.right = right;
    }

    public void setRed(boolean red) {
        isRed = red;
    }

    public void setParent(Node parent) {
        this.parent = parent;
    }
}
