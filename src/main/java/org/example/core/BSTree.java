package org.example.core;

import org.example.validation.Validator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BSTree implements Tree {
    private static final Logger log = LoggerFactory.getLogger(BSTree.class);
    protected int size;
    protected Node root;
    public static boolean VALIDATE= true;
    public BSTree() {
        this.size = 0;
        this.root = null;
        log.debug("Initialized empty BSTree");
    }

    @Override
    public boolean insert(int v) {
        log.debug("Attempting to insert value: {}", v);
        Node node = insertHelper(v);
        if (VALIDATE){
            log.debug("Start checking the validity of the tree after inserting {}", v);
            Validator.check(this);
        }
        if (node == null){
            log.warn("Inserting node that already exist: {}", v);
            return false;
        }else {
            log.debug("Node {} inserted successfully", v);
            return true;
        }

    }

    @Override
    public boolean delete(int v) {
        log.debug("Attempting to delete value: {}", v);
        Node x = root;
        Node y = null;
        while (x != null) {
            log.trace("Traversing for delete: current node is {}", x.getValue());
            if (v < x.getValue()) {
                x = x.getLeft();
            } else if (v > x.getValue()) {
                x = x.getRight();
            } else {
                log.debug("Value {} found. Starting structural deletion.", v);
                if (x.getLeft() == null) {
                    log.trace("Delete Case 1/2: Node {} has no left child. Transplating right child.", v);
                    Transplant(x, x.getRight());
                } else if (x.getRight() == null) {
                    log.trace("Delete Case 2: Node {} has no right child. Transplanting left child.", v);
                    Transplant(x, x.getLeft());
                } else {
                    log.trace("Delete Case 3: Node {} has two children. Finding successor.", v);
                    y = Minimum(x.getRight());
                    log.trace("Successor found: {}", y.getValue());
                    if (y.getParent() != x) {
                        log.trace("Successor {} is not direct child. Transplanting successor's right child.", y.getValue());
                        Transplant(y, y.getRight());
                        y.setRight(x.getRight());
                        y.getRight().setParent(y);
                    }
                    Transplant(x, y);
                    y.setLeft(x.getLeft());
                    y.getLeft().setParent(y);
                }
                size--;
                log.debug("Node {} successfully deleted. Current size: {}", v, size);
                if (VALIDATE){
                    log.trace("Validating tree structure after deleting {}", v);
                    Validator.check(this);
                }
                return true;
            }
        }
        log.warn("Deletion failed: Value {} was not found in the tree.", v);
        return false;
    }

    @Override
    public boolean contains(int v) {
        log.debug("Searching for value: {}", v);
        Node x = root;
        while (x != null) {
            if (v < x.getValue()) {
                log.trace("Target {} < current {}. Going left.", v, x.getValue());
                x = x.getLeft();
            } else if (v > x.getValue()) {
                log.trace("Target {} > current {}. Going right.", v, x.getValue());
                x = x.getRight();
            } else {
                log.debug("Value {} found in the tree.", v);
                return true;
            }
        }
        log.debug("Search completed: Value {} is not in the tree.", v);
        return false;
    }

    @Override
    public int[] inOrder() {
        log.debug("Executing in-order traversal to sort the tree.");
        int[] result = new int[size];
        inOrderHelper(root, result, new int[]{0});// 1d array to pass by refernce
        log.debug("In-order traversal complete. Array populated with {} elements.", size);
        return result;
    }

    private void inOrderHelper(Node node, int[] result, int[] idx) {
        if (node == null) {
            return;
        }
        inOrderHelper(node.getLeft(), result, idx);
        result[idx[0]++] = node.getValue();
        inOrderHelper(node.getRight(), result, idx);
    }

    @Override
    public int height() {
        log.debug("Calculating tree height.");
        int h = calculateHeight(root);
        log.debug("Tree height is: {}", h);
        return h;
    }

    private int calculateHeight(Node node) {
        if (node == null) {
            return 0;
        }
        int Hleft = calculateHeight(node.getLeft());
        int Hright = calculateHeight(node.getRight());
        return 1 + Math.max(Hleft, Hright);
    }

    @Override
    public int size() {
        return size;
    }


    protected void Transplant(Node u, Node v) {
        String uVal = (u != null) ? String.valueOf(u.getValue()) : "null";
        String vVal = (v != null) ? String.valueOf(v.getValue()) : "null";

        log.trace("Executing Transplant: Replacing node {} with node {}", uVal, vVal);
        if (u.getParent() == null) {
            root = v;
        } else if (u == u.getParent().getLeft()) {
            u.getParent().setLeft(v);
        } else {
            u.getParent().setRight(v);
        }
        if (v != null) {
            v.setParent(u.getParent());
        }
    }

    protected Node Minimum(Node T) {
        log.trace("Finding minimum node starting from {}", T.getValue());
        while (T.getLeft() != null) {
            T = T.getLeft();
        }
        log.trace("Minimum node found: {}", T.getValue());
        return T;
    }

    protected Node insertHelper(int v){
        Node node = new Node(v);
        Node x = root;
        Node y = null;
        log.trace("Finding the right place to insert {}", v);
        while (x != null) {
            y = x;
            if (node.getValue() < x.getValue()) {
                log.trace("{} is smaller, go left", v);
                x = x.getLeft();
            } else if (node.getValue() == x.getValue()) {
                log.warn("{} already exist!", v);
                return null;
            } else {
                log.trace("{} is bigger, go right", v);
                x = x.getRight();
            }
        }
        node.setParent(y);
        if (y == null) {
            log.trace("insertHelper: Tree is empty. Node {} becomes root.", v);
            root = node;
        } else if (node.getValue() < y.getValue()) {
            log.trace("insertHelper: Inserting {} as left child of {}", v, y.getValue());
            y.setLeft(node);
        } else {
            log.trace("insertHelper: Inserting {} as right child of {}", v, y.getValue());
            y.setRight(node);
        }
        size++;
        return node;
    }

    public Node getRoot() {
        return root;
    }
}
