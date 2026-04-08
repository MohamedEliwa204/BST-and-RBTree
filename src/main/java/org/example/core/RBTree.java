package org.example.core;

import org.example.validation.validator;

public class RBTree extends BSTree {


    public RBTree() {
        super();
    }

    @Override
    public boolean insert(int v) {
        Node z = super.insertHelper(v);
        if (z == null) {
            return false;
        }
        insertFixup(z);
        if (VALIDATE) {
            validator.check(this);
        }
        return true;
    }

    @Override
    public boolean delete(int v) {
        Node z = root;
        Node y = z;
        Node x = null;
        Node xParent = null;
        boolean yIsRed = y.isRed();
        while (z != null) {
            if (v < z.getValue()) {
                z = z.getLeft();
            } else if (v > z.getValue()) {
                z = z.getRight();
            } else {
                if (z.getLeft() == null) {
                    x = z.getRight();
                    xParent = z.getParent();
                    Transplant(z, z.getRight());
                } else if (z.getRight() == null) {
                    x = z.getLeft();
                    xParent = z.getParent();
                    Transplant(z, z.getLeft());
                } else {
                    y = Minimum(z.getRight());
                    yIsRed = y.isRed();
                    x = y.getRight();
                    if (y.getParent() == z) {
                        xParent = y;
                        if (x != null) x.setParent(y);
                    } else {
                        xParent = y.getParent();
                        Transplant(y, y.getRight());
                        y.setRight(z.getRight());
                        y.getRight().setParent(y);
                    }
                    Transplant(z, y);
                    y.setLeft(z.getLeft());
                    y.getLeft().setParent(y);
                    y.setRed(z.isRed());
                }
                if (!yIsRed) {
                    deleteFixUp(x, xParent);
                }
                size--;
                if (VALIDATE) {
                    validator.check(this);
                }
                return true;
            }

        }
        return false;
    }

    private void deleteFixUp(Node x, Node xParent) {
        Node w;
        while (x != root && isBlack(x)) {
            if (x == xParent.getLeft()) {
                w = xParent.getRight();
                if (isRed(w)) {
                    w.changeColor();
                    xParent.setRed(true);
                    leftRotate(xParent);
                    w = xParent.getRight();
                }
                if (isBlack(rightOf(w)) && isBlack(leftOf(w))) {
                    if (w != null) w.setRed(true);
                    x = xParent;
                    xParent = x.getParent();
                } else {
                    if (isRed(leftOf(w)) && isBlack(rightOf(w))) {
                        w.changeColor();
                        leftOf(w).changeColor();
                        rightRotate(w);
                        w = xParent.getRight();
                    }
                    w.setRed(isRed(xParent));
                    xParent.setRed(false);
                    if (rightOf(w) != null) rightOf(w).setRed(false);
                    leftRotate(xParent);
                    x = root;
                }
            }else {
                w = xParent.getLeft();
                if (isRed(w)){
                    w.changeColor();
                    xParent.setRed(true);
                    rightRotate(xParent);
                    w  = xParent.getLeft();
                }
                if (isBlack(rightOf(w)) && isBlack(leftOf(w))){
                    if (w != null) w.setRed(true);
                    x = xParent;
                    xParent = x.getParent();
                }else {
                    if (isRed(rightOf(w)) && isBlack(leftOf(w))){
                        w.changeColor();
                        rightOf(w).changeColor();
                        leftRotate(w);

                        w = xParent.getLeft();
                    }
                    w.setRed(isRed(xParent));
                    xParent.setRed(false);
                    if (leftOf(w) != null) leftOf(w).setRed(false);
                    rightRotate(xParent);
                    x = root;
                }
            }
        }
        if (x != null){
            x.setRed(false);
        }
    }

    private boolean isRed(Node node) {
        if (node == null) {
            return false;
        }
        return node.isRed();
    }

    private boolean isBlack(Node node){
        if (node ==  null){
            return true;
        }
        return !node.isRed();
    }

    private void insertFixup(Node z) {
        while (isRed(z.getParent())) {
            if (z.getParent().getParent().getLeft() == z.getParent()) {
                Node y = z.getParent().getParent().getRight();
                if (isRed(y)) {
                    z.getParent().changeColor();
                    y.changeColor();
                    z.getParent().getParent().setRed(true);
                    z = z.getParent().getParent();
                } else {
                    if (z.getParent().getRight() == z) {
                        z = z.getParent();
                        leftRotate(z);
                    }
                    z.getParent().setRed(false);
                    z.getParent().getParent().setRed(true);
                    rightRotate(z.getParent().getParent());
                }

            } else {
                Node y = z.getParent().getParent().getLeft();
                if (isRed(y)) {
                    z.getParent().changeColor();
                    y.changeColor();
                    z.getParent().getParent().setRed(true);
                    z = z.getParent().getParent();
                } else {
                    if (z == z.getParent().getLeft()) {
                        z = z.getParent();
                        rightRotate(z);
                    }
                    z.getParent().setRed(false);
                    z.getParent().getParent().setRed(true);
                    leftRotate(z.getParent().getParent());
                }
            }

        }
        root.setRed(false);
    }

    private void leftRotate(Node x) {
        Node y = x.getRight();
        y.setParent(x.getParent());
        if (x.getParent() == null) {
            root = y;
        } else if (x.getParent().getLeft() == x) {
            x.getParent().setLeft(y);
        } else {
            x.getParent().setRight(y);
        }
        x.setRight(y.getLeft());
        if (y.getLeft() != null) {
            y.getLeft().setParent(x);
        }
        y.setLeft(x);
        x.setParent(y);
    }

    private void rightRotate(Node x) {
        Node y = x.getLeft();
        y.setParent(x.getParent());
        if (x.getParent() == null) {
            root = y;
        } else if (x.getParent().getLeft() == x) {
            x.getParent().setLeft(y);
        } else {
            x.getParent().setRight(y);
        }
        x.setLeft(y.getRight());
        if (y.getRight() != null) {
            y.getRight().setParent(x);
        }
        y.setRight(x);
        x.setParent(y);
    }
    private Node leftOf(Node node){
        if (node == null){
            return null;
        }
        return node.getLeft();
    }

    private Node rightOf(Node node){
        if (node == null){
            return null;
        }
        return node.getRight();
    }
}
