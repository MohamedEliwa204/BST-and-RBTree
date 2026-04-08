package org.example.core;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BSTreeTest {
    private BSTree bsTree;

    @BeforeEach
    public void setUp(){
        bsTree = new BSTree();
        RBTree.VALIDATE = true;
    }

    @Test
    public void testEmptyTree(){
        assertEquals(0, bsTree.size());
        assertFalse(bsTree.contains(10));
    }

    @Test
    public void InsertAndSize(){
        assertTrue(bsTree.insert(10));
        assertTrue(bsTree.insert(50));
        assertTrue(bsTree.insert(100));
        assertEquals(3, bsTree.size());

    }

    @Test
    public void InsertDuplicate(){
        assertTrue(bsTree.insert(10));
        assertFalse(bsTree.insert(10));
    }

    @Test
    public void Contains(){
        assertTrue(bsTree.insert(10));
        assertTrue(bsTree.insert(50));
        assertTrue(bsTree.insert(100));
        assertTrue(bsTree.contains(10));
        assertTrue(bsTree.contains(50));
        assertTrue(bsTree.contains(100));
        assertFalse(bsTree.contains(20));
    }

    @Test
    public void DeleteLeafNode(){
        assertTrue(bsTree.insert(10));
        assertTrue(bsTree.insert(5));
        assertTrue(bsTree.insert(15));
        assertTrue(bsTree.delete(5));
        assertFalse(bsTree.contains(5));
        assertEquals(2, bsTree.size());
    }

    @Test
    public void DeleteNodeWithSingleChild(){
        assertTrue(bsTree.insert(10));
        assertTrue(bsTree.insert(5));
        assertTrue(bsTree.insert(15));
        assertTrue(bsTree.insert(7));
        assertTrue(bsTree.delete(5));
        assertFalse(bsTree.contains(5));
        assertEquals(3, bsTree.size());
    }

    @Test
    public void DeleteNodeWithTwoChildren(){
        assertTrue(bsTree.insert(10));
        assertTrue(bsTree.insert(5));
        assertTrue(bsTree.insert(15));
        assertTrue(bsTree.insert(7));
        assertTrue(bsTree.insert(20));
        assertTrue(bsTree.insert(17));

        assertTrue(bsTree.delete(10));
        assertFalse(bsTree.contains(10));
        assertEquals(5, bsTree.size());
    }

    @Test
    public void SequentialInsert(){
        for (int i = 0; i <= 1000; i++) {
            bsTree.insert(i);
        }
        assertEquals(1001, bsTree.size());

        assertTrue(bsTree.height() == 1001);
    }

    @Test
    public void DeleteNonExistingNode(){
        bsTree.insert(5);
        bsTree.insert(50);
        bsTree.insert(20);

        assertFalse(bsTree.delete(10));
        assertEquals(3, bsTree.size());
    }

    @Test
    public void Height(){
        assertTrue(bsTree.insert(10));
        assertTrue(bsTree.insert(5));
        assertTrue(bsTree.insert(15));

        assertEquals(2, bsTree.height());
    }
}