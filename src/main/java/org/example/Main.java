package org.example;

import org.example.core.BSTree;
import org.example.core.RBTree;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        //TIP Press <shortcut actionId="ShowIntentionActions"/> with your caret at the highlighted text
        // to see how IntelliJ IDEA suggests fixing it.
        RBTree tree = new RBTree();
        BSTree tree2 = new BSTree();
        for (int i = 1; i <= 10000; i++) {
            int randomVal = (int) (Math.random() * 100000);
            tree2.insert(randomVal);
            tree.insert(randomVal);
        }
    }
}