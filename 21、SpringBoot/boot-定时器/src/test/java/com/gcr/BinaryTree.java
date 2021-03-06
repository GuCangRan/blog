package com.gcr;

import org.w3c.dom.Node;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * Created by GuaiWenWo on 2021/3/2 16:02
 */
public class BinaryTree {

    private Node root;

    private Node addNode(Node current, int value) {
        if (current == null) {
            return new Node(value);
        }

        if (value < current.val) {
            current.left = addNode(current.left, value);
        } else if (value > current.val) {
            current.right = addNode(current.right, value);
        }
        return current;
    }

    public void addNode(int value) {
        root = addNode(root, value);
    }

    public Node getRoot() {
        return root;
    }

    public void depthFirstSort(Node root) {
        if (root == null) return;
        //System.out.println("深度:左树:" + root.left.val);
        depthFirstSort(root.left);
        System.out.println("中序:" + root.val);
        //System.out.println("深度:右树:" + root.right.val);
        depthFirstSort(root.right);
    }


    public void traversePreOrder(Node root) {
        if (root != null) {
            System.out.println("前序:" + root.val);
            traversePreOrder(root.left);
            traversePreOrder(root.right);
        }
    }

    public void traversePostOrder(Node root) {
        if (root != null) {

            traversePostOrder(root.left);
            traversePostOrder(root.right);
            System.out.println("后序:" + root.val);
        }
    }


    public List<List<Integer>> levelOrder(Node root) {
        List<List<Integer>> ret = new ArrayList<List<Integer>>();
        if (root == null) {
            return ret;
        }

        Queue<Node> queue = new LinkedList<Node>();
        queue.offer(root);
        while (!queue.isEmpty()) {
            List<Integer> level = new ArrayList<Integer>();
            int currentLevelSize = queue.size();
            for (int i = 1; i <= currentLevelSize; ++i) {
                Node node = queue.poll();
                level.add(node.val);
                if (node.left != null) {
                    queue.offer(node.left);
                }
                if (node.right != null) {
                    queue.offer(node.right);
                }
            }
            ret.add(level);
        }

        return ret;
    }

    public int maxDepth(Node root) {
        if (root == null) {
            return 0;
        }
        int leftHeight = maxDepth(root.left);
        int rightHeight = maxDepth(root.right);
        return Math.max(leftHeight, rightHeight) + 1;
        
    }

    class Node {
        int val;
        Node left;
        Node right;

        public Node(int val) {
            this.val = val;
            this.left = null;
            this.right = null;
        }
    }
}
