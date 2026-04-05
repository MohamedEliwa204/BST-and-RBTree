package org.example.validation;

import org.example.core.Node;
import org.example.core.RBTree;

public class validator {


    public static void check(RBTree tree){
        if (tree.getRoot() == null){
            return;
        }

        if (!isBST(tree.getRoot(), null, null)){
            throw new IllegalStateException("VALIDATION FAILED: Tree violates BST property.");
        }

        if (tree.getRoot().isRed()){
            throw new IllegalStateException("VALIDATION FAILED: Root is red.");
        }

        if (!checkRedColor(tree.getRoot())){
            throw new IllegalStateException("VALIDATION FAILED: Red node has a red child.");
        }

        if (chackBlackHeight(tree.getRoot()) == -1){
            throw new IllegalStateException("VALIDATION FAILED: Paths have different black-heights.");
        }
    }

    private static boolean isBST(Node root, Integer min, Integer max){
        if (root == null){
            return true;
        }
        if (min != null && min > root.getValue()){
            return false;
        }
        if (max != null && max < root.getValue()){
            return false;
        }
        return isBST(root.getLeft(), min, root.getValue()) && isBST(root.getRight(), root.getValue(), max);
    }

    private static boolean checkRedColor(Node node){
        if (node == null){
            return true;
        }
        if (node.isRed()){
            if (isRedSafe(node.getLeft()) || isRedSafe(node.getRight())){
                return false;
            }
        }
        return checkRedColor(node.getLeft()) && checkRedColor(node.getRight());
    }

    private static int chackBlackHeight(Node node){
        if (node == null){
            return 1;
        }
        int leftH = chackBlackHeight(node.getLeft());
        int rightH = chackBlackHeight(node.getRight());
        if (leftH == -1 || rightH == -1 || leftH != rightH){
            return -1;
        }
        return rightH + (node.isRed() ? 0 : 1);
    }

    private static boolean isRedSafe(Node node) {
        return node != null && node.isRed();
    }
}
