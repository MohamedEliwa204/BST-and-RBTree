package org.example.core;

public class BSTree implements Tree {
    protected int size;
    protected Node root;

    public BSTree() {
        this.size = 0;
        this.root = null;
    }

    @Override
    public boolean insert(int v) {
        Node node = new Node(v);
        Node x = root;
        Node y = null;
        while (x != null) {
            y = x;
            if (node.getValue() < x.getValue()) {
                x = x.getLeft();
            } else if (node.getValue() == x.getValue()) {
                return false;
            } else {
                x = x.getRight();
            }
        }
        node.setParent(y);
        if (y == null) {
            root = node;
        } else if (node.getValue() < y.getValue()) {
            y.setLeft(node);
        } else {
            y.setRight(node);
        }
        size++;
        return true;
    }

    @Override
    public boolean delete(int v) {
        Node x = root;
        Node y = null;
        while (x != null) {
            if (v < x.getValue()) {
                x = x.getLeft();
            } else if (v > x.getValue()) {
                x = x.getRight();
            } else {
                if (x.getLeft() == null) {
                    Transplant(x, x.getRight());
                } else if (x.getRight() == null) {
                    Transplant(x, x.getLeft());
                } else {
                    y = Minimum(x.getRight());
                    if (y.getParent() != x) {
                        Transplant(y, y.getRight());
                        y.setRight(x.getRight());
                        y.getRight().setParent(y);
                    }
                    Transplant(x, y);
                    y.setLeft(x.getLeft());
                    y.getLeft().setParent(y);
                }
                size--;
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean contains(int v) {
        Node x = root;
        while (x != null) {
            if (v < x.getValue()) {
                x = x.getLeft();
            } else if (v > x.getValue()) {
                x = x.getRight();
            } else {
                return true;
            }
        }
        return false;
    }

    @Override
    public int[] inOrder() {
        int[] result = new int[size];
        inOrderHelper(root, result, new int[]{0}); // 1d array to pass by refernce
        return new int[0];
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
        return calculateHeight(root);
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
        while (T.getLeft() != null) {
            T = T.getLeft();
        }
        return T;
    }
}
