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
        if (VALIDATE){
            validator.check(this);
        }
        return true;
    }

    @Override
    public boolean delete(int v) {
        return super.delete(v);
    }


    private boolean isRed(Node node) {
        if (node == null) {
            return false;
        }
        return node.isRed();
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
                    if(z == z.getParent().getLeft()){
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
        if (x.getParent() == null){
            root = y;
        } else if (x.getParent().getLeft() == x) {
            x.getParent().setLeft(y);
        }else {
            x.getParent().setRight(y);
        }
        x.setRight(y.getLeft());
        if (y.getLeft() != null){
            y.getLeft().setParent(x);
        }
        y.setLeft(x);
        x.setParent(y);
    }

    private void rightRotate(Node x) {
        Node y = x.getLeft();
        y.setParent(x.getParent());
        if (x.getParent() == null){
            root = y;
        }else if(x.getParent().getLeft() == x){
            x.getParent().setLeft(y);
        }else {
            x.getParent().setRight(y);
        }
        x.setLeft(y.getRight());
        if (y.getRight() != null){
            y.getRight().setParent(x);
        }
        y.setRight(x);
        x.setParent(y);
    }
}
