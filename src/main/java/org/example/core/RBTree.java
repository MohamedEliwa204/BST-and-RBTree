package org.example.core;

import org.example.validation.Validator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RBTree extends BSTree {
    private static final Logger log = LoggerFactory.getLogger(RBTree.class);

    public RBTree() {
        super();
        log.debug("Initialized empty RBTree");
    }

    @Override
    public boolean insert(int v) {
        log.debug("RBTree: Attempting to insert value {}", v);
        Node z = super.insertHelper(v);
        if (z == null) {
            log.warn("RBTree: Insertion aborted. Value {} already exists.", v);
            return false;
        }
        log.trace("RBTree: Value {} inserted. Starting insertFixup.", v);
        insertFixup(z);
        if (VALIDATE) {
            log.trace("RBTree: Validating structure after inserting {}", v);
            Validator.check(this);
        }
        log.debug("RBTree: Value {} successfully inserted and balanced.", v);
        return true;
    }

    @Override
    public boolean delete(int v) {
        log.debug("RBTree: Attempting to delete value {}", v);
        Node z = root;
        Node y = z;
        Node x = null;
        Node xParent = null;
        boolean yIsRed = isRed(y);
        while (z != null) {
            if (v < z.getValue()) {
                z = z.getLeft();
            } else if (v > z.getValue()) {
                z = z.getRight();
            } else {
                yIsRed = isRed(z);
                log.trace("RBTree: Node to delete ({}) found.", v);
                if (z.getLeft() == null) {
                    log.trace("delete Case 1: No left child. Transplanting right child.");
                    x = z.getRight();
                    xParent = z.getParent();
                    Transplant(z, z.getRight());
                } else if (z.getRight() == null) {
                    log.trace("delete Case 2: No right child. Transplanting left child.");
                    x = z.getLeft();
                    xParent = z.getParent();
                    Transplant(z, z.getLeft());
                } else {
                    log.trace("delete Case 3: Two children. Finding successor.");
                    y = Minimum(z.getRight());
                    yIsRed = y.isRed();
                    x = y.getRight();
                    if (y.getParent() == z) {
                        log.trace("Successor is direct right child.");
                        xParent = y;
                        if (x != null) x.setParent(y);
                    } else {
                        log.trace("Successor is deeper down. Extracting and transplanting.");
                        xParent = y.getParent();
                        Transplant(y, y.getRight());
                        y.setRight(z.getRight());
                        y.getRight().setParent(y);
                    }
                    Transplant(z, y);
                    y.setLeft(z.getLeft());
                    y.getLeft().setParent(y);
                    y.setRed(z.isRed());
                    log.trace("Successor transplanted. Original y color was Red: {}", yIsRed);
                }
                if (!yIsRed) {
                    log.trace("Extracted node was BLACK. Black-height violated! Starting deleteFixUp.");
                    deleteFixUp(x, xParent);
                }else {
                    log.trace("Extracted node was RED. No black-height violation.");
                }
                size--;

                if (VALIDATE) {
                    log.trace("RBTree: Validating structure after deleting {}", v);
                    Validator.check(this);
                }
                log.debug("RBTree: Value {} successfully deleted and balanced.", v);
                return true;
            }

        }
        log.warn("RBTree: Deletion failed. Value {} not found.", v);
        return false;
    }

    private void deleteFixUp(Node x, Node xParent) {
        log.trace("deleteFixUp: Started.");
        Node w;
        while (x != root && isBlack(x)) {
            if (x == xParent.getLeft()) {
                w = xParent.getRight();
                log.trace("deleteFixUp (Left branch): Sibling w is {}", w != null ? w.getValue() : "Nil");
                if (isRed(w)) {
                    log.trace("deleteFixUp Case 1 (Left): Sibling is RED. Recoloring and left-rotating parent.");
                    w.changeColor();
                    xParent.setRed(true);
                    leftRotate(xParent);
                    w = xParent.getRight();
                }
                if (isBlack(rightOf(w)) && isBlack(leftOf(w))) {
                    log.trace("deleteFixUp Case 2 (Left): Sibling's children are both BLACK. Recoloring sibling RED and moving up.");
                    if (w != null) w.setRed(true);
                    x = xParent;
                    xParent = x.getParent();
                } else {
                    if (isRed(leftOf(w)) && isBlack(rightOf(w))) {
                        log.trace("deleteFixUp Case 3 (Left): Sibling's left child is RED. Recoloring and right-rotating sibling.");
                        w.changeColor();
                        leftOf(w).changeColor();
                        rightRotate(w);
                        w = xParent.getRight();
                    }
                    log.trace("deleteFixUp Case 4 (Left): Sibling's right child is RED. Recoloring and left-rotating parent. Terminal case.");
                    w.setRed(isRed(xParent));
                    xParent.setRed(false);
                    if (rightOf(w) != null) rightOf(w).setRed(false);
                    leftRotate(xParent);
                    x = root;
                }
            }else {
                w = xParent.getLeft();
                log.trace("deleteFixUp (Right branch): Sibling w is {}", w != null ? w.getValue() : "Nil");
                if (isRed(w)){
                    log.trace("deleteFixUp Case 1 (Right): Sibling is RED. Recoloring and right-rotating parent.");
                    w.changeColor();
                    xParent.setRed(true);
                    rightRotate(xParent);
                    w  = xParent.getLeft();
                }
                if (isBlack(rightOf(w)) && isBlack(leftOf(w))){
                    log.trace("deleteFixUp Case 2 (Right): Sibling's children are both BLACK. Recoloring sibling RED and moving up.");
                    if (w != null) w.setRed(true);
                    x = xParent;
                    xParent = x.getParent();
                }else {
                    if (isRed(rightOf(w)) && isBlack(leftOf(w))){
                        log.trace("deleteFixUp Case 3 (Right): Sibling's right child is RED. Recoloring and left-rotating sibling.");
                        w.changeColor();
                        rightOf(w).changeColor();
                        leftRotate(w);

                        w = xParent.getLeft();
                    }
                    log.trace("deleteFixUp Case 4 (Right): Sibling's left child is RED. Recoloring and right-rotating parent. Terminal case.");
                    w.setRed(isRed(xParent));
                    xParent.setRed(false);
                    if (leftOf(w) != null) leftOf(w).setRed(false);
                    rightRotate(xParent);
                    x = root;
                }
            }
        }
        if (x != null){
            log.trace("deleteFixUp: Terminal recolor of x to BLACK.");
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
        log.trace("insertFixup: Started for node {}", z.getValue());
        while (isRed(z.getParent())) {
            if (z.getParent().getParent().getLeft() == z.getParent()) {
                Node y = z.getParent().getParent().getRight();
                log.trace("insertFixup (Left branch): Uncle is {}", y != null ? y.getValue() : "Nil");
                if (isRed(y)) {
                    log.trace("insertFixup Case 1 (Left): Uncle is RED. Recoloring parent/uncle to BLACK, grandparent to RED.");
                    z.getParent().changeColor();
                    y.changeColor();
                    z.getParent().getParent().setRed(true);
                    z = z.getParent().getParent();
                } else {
                    if (z.getParent().getRight() == z) {
                        log.trace("insertFixup Case 2 (Left): Uncle is BLACK, node is right child. Left-rotating parent.");
                        z = z.getParent();
                        leftRotate(z);
                    }
                    log.trace("insertFixup Case 3 (Left): Uncle is BLACK, node is left child. Recoloring and right-rotating grandparent.");
                    z.getParent().setRed(false);
                    z.getParent().getParent().setRed(true);
                    rightRotate(z.getParent().getParent());
                }

            } else {
                Node y = z.getParent().getParent().getLeft();
                log.trace("insertFixup (Right branch): Uncle is {}", y != null ? y.getValue() : "Nil");
                if (isRed(y)) {
                    log.trace("insertFixup Case 1 (Right): Uncle is RED. Recoloring parent/uncle to BLACK, grandparent to RED.");
                    z.getParent().changeColor();
                    y.changeColor();
                    z.getParent().getParent().setRed(true);
                    z = z.getParent().getParent();
                } else {
                    if (z == z.getParent().getLeft()) {
                        log.trace("insertFixup Case 2 (Right): Uncle is BLACK, node is left child. Right-rotating parent.");
                        z = z.getParent();
                        rightRotate(z);
                    }
                    log.trace("insertFixup Case 3 (Right): Uncle is BLACK, node is right child. Recoloring and left-rotating grandparent.");
                    z.getParent().setRed(false);
                    z.getParent().getParent().setRed(true);
                    leftRotate(z.getParent().getParent());
                }
            }

        }
        log.trace("insertFixup: Ensuring root is BLACK.");
        root.setRed(false);
    }

    private void leftRotate(Node x) {
        log.trace("LEFT-ROTATE on node: {}", x != null ? x.getValue() : "null");
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
        log.trace("RIGHT-ROTATE on node: {}", x != null ? x.getValue() : "null");
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
