package org.example.validation;

import org.example.core.BSTree;
import org.example.core.Node;
import org.example.core.RBTree;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Validator {
    private static final Logger log = LoggerFactory.getLogger(Validator.class);
    public static void check(BSTree tree){
        log.debug("Starting structural validation for standard BST.");
        if (tree.getRoot() == null){
            log.trace("BST is empty. Trivially valid.");
            return;
        }

        if (!isBST(tree.getRoot(), null, null)){
            log.error("VALIDATION FAILED: Tree violates BST property.");
            throw new IllegalStateException("VALIDATION FAILED: Tree violates BST property.");
        }
        log.debug("BST validation successful. Tree structure is intact.");
    }
    public static void check(RBTree tree){
        log.debug("Starting strict validation for Red-Black Tree.");
        if (tree.getRoot() == null){
            log.trace("RBTree is empty. Trivially valid.");
            return;
        }
        log.trace("RBT Check 1: Verifying binary search tree properties...");
        if (!isBST(tree.getRoot(), null, null)){
            log.error("VALIDATION FAILED: Red-Black Tree violates standard BST property.");
            throw new IllegalStateException("VALIDATION FAILED: Tree violates BST property.");
        }

        log.trace("RBT Check 2: Verifying root color...");
        if (tree.getRoot().isRed()){
            log.error("VALIDATION FAILED: The root node ({}) is RED.", tree.getRoot().getValue());
            throw new IllegalStateException("VALIDATION FAILED: Root is red.");
        }

        log.trace("RBT Check 3: Verifying no consecutive RED nodes exist...");
        if (!checkRedColor(tree.getRoot())){
            log.error("VALIDATION FAILED: A RED node has a RED child.");
            throw new IllegalStateException("VALIDATION FAILED: Red node has a red child.");
        }

        log.trace("RBT Check 4: Verifying uniform black-height across all paths...");
        int blackHeight = chackBlackHeight(tree.getRoot());
        if (chackBlackHeight(tree.getRoot()) == -1){
            log.error("VALIDATION FAILED: Paths from a node to descendant leaves contain different numbers of BLACK nodes.");
            throw new IllegalStateException("VALIDATION FAILED: Paths have different black-heights.");
        }
        log.debug("Red-Black Tree validation successful. All properties hold (Black-Height: {}).", blackHeight);
    }

    private static boolean isBST(Node root, Integer min, Integer max){
        if (root == null){
            return true;
        }

        log.trace("isBST Check: Node {} (min allowed: {}, max allowed: {})",
                root.getValue(), min == null ? "-∞" : min, max == null ? "+∞" : max);
        if (min != null && min > root.getValue()){
            log.error("BST Violation: Node {} is smaller than required minimum {}", root.getValue(), min);
            return false;
        }
        if (max != null && max < root.getValue()){
            log.error("BST Violation: Node {} is greater than required maximum {}", root.getValue(), max);
            return false;
        }
        return isBST(root.getLeft(), min, root.getValue()) && isBST(root.getRight(), root.getValue(), max);
    }

    private static boolean checkRedColor(Node node){
        if (node == null){
            return true;
        }
        if (node.isRed()){
            log.trace("checkRedColor: Checking children of RED node {}", node.getValue());
            if (isRedSafe(node.getLeft()) || isRedSafe(node.getRight())){
                log.error("Red-Color Violation: RED node {} has a RED child!", node.getValue());
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
            if (leftH != rightH && leftH != -1 && rightH != -1) {
                log.error("Black-Height Violation at Node {}: Left path black-height is {}, but right path is {}",
                        node.getValue(), leftH, rightH);
            }
            return -1;
        }
        int currentHeight = rightH + (node.isRed() ? 0 : 1);
        log.trace("checkBlackHeight: Node {} (Red: {}) returning black-height {}", node.getValue(), node.isRed(), currentHeight);
        return rightH + (node.isRed() ? 0 : 1);
    }

    private static boolean isRedSafe(Node node) {
        return node != null && node.isRed();
    }
}
