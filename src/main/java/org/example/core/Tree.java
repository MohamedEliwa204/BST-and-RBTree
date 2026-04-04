package org.example.core;

public interface Tree {
    public boolean insert(int v);
    public boolean delete(int v);
    public boolean contains(int v);
    public int[] inOrder();
    public int height();
    public int size();
}
