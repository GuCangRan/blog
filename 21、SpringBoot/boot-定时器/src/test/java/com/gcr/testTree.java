package com.gcr;

import lombok.val;

import java.util.List;
import java.util.TreeMap;

/**
 * Created by GuaiWenWo on 2021/2/25 18:14
 */
public class testTree {

    public static void main(String[] args) {
        BinaryTree bt = new BinaryTree();
        bt.addNode(6);
        bt.addNode(4);
        bt.addNode(8);
        bt.addNode(3);
        bt.addNode(5);
        bt.addNode(7);
        bt.addNode(9);
        String ss = "萨达是打发";
        /**
         6
         4      8
         3   5   7  9
         */

        BinaryTree.Node root = bt.getRoot();
        //深度排序
        bt.traversePreOrder(root);
        System.out.println("=====================");
        bt.depthFirstSort(root);
        System.out.println("=====================");
        bt.traversePostOrder(root);
        System.out.println("=====================");
        List<List<Integer>> lists = bt.levelOrder(root);
        System.out.println(lists);
        System.out.println("=====================");
        System.out.println(bt.maxDepth(root));

        TreeMap dd = new TreeMap<>();
    }


}
