package org.example.core;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RBTreeTest {
    private RBTree rbTree;

    @BeforeEach
    public void setUp(){
        rbTree = new RBTree();
        RBTree.VALIDATE = true;
    }

    @Test
    public void testEmptyTree(){
        assertEquals(0, rbTree.size());
        assertFalse(rbTree.contains(10));
    }

    @Test
    public void InsertAndSize(){
        assertTrue(rbTree.insert(10));
        assertTrue(rbTree.insert(50));
        assertTrue(rbTree.insert(100));
        assertEquals(3, rbTree.size());

    }

    @Test
    public void InsertDuplicate(){
        assertTrue(rbTree.insert(10));
        assertFalse(rbTree.insert(10));
    }

    @Test
    public void Contains(){
        assertTrue(rbTree.insert(10));
        assertTrue(rbTree.insert(50));
        assertTrue(rbTree.insert(100));
        assertTrue(rbTree.contains(10));
        assertTrue(rbTree.contains(50));
        assertTrue(rbTree.contains(100));
        assertFalse(rbTree.contains(20));
    }

    @Test
    public void DeleteLeafNode(){
        assertTrue(rbTree.insert(10));
        assertTrue(rbTree.insert(5));
        assertTrue(rbTree.insert(15));
        assertTrue(rbTree.delete(5));
        assertFalse(rbTree.contains(5));
        assertEquals(2, rbTree.size());
    }

    @Test
    public void DeleteNodeWithSingleChild(){
        assertTrue(rbTree.insert(10));
        assertTrue(rbTree.insert(5));
        assertTrue(rbTree.insert(15));
        assertTrue(rbTree.insert(7));
        assertTrue(rbTree.delete(5));
        assertFalse(rbTree.contains(5));
        assertEquals(3, rbTree.size());
    }

    @Test
    public void DeleteNodeWithTwoChildren(){
        assertTrue(rbTree.insert(10));
        assertTrue(rbTree.insert(5));
        assertTrue(rbTree.insert(15));
        assertTrue(rbTree.insert(7));
        assertTrue(rbTree.insert(20));
        assertTrue(rbTree.insert(17));

        assertTrue(rbTree.delete(10));
        assertFalse(rbTree.contains(10));
        assertEquals(5, rbTree.size());
    }

    @Test
    public void SequentialInsert(){
        for (int i = 0; i <= 1000; i++) {
            rbTree.insert(i);
        }
        assertEquals(1001, rbTree.size());

        assertTrue(rbTree.height() < 25, "Tree Failed to balance itself!");
    }

    @Test
    public void DeleteNonExistingNode(){
        rbTree.insert(5);
        rbTree.insert(50);
        rbTree.insert(20);

        assertFalse(rbTree.delete(10));
        assertEquals(3, rbTree.size());
    }

    @Test
    public void Height(){
        assertTrue(rbTree.insert(10));
        assertTrue(rbTree.insert(5));
        assertTrue(rbTree.insert(15));

        assertEquals(2, rbTree.height());
    }

}