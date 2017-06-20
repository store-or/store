package com.core.util.collection;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by laizy on 2017/6/7.
 */
public abstract class TreeNode<T extends TreeNode> {

    protected T parent;
    protected String path;
    protected List<T> children = new LinkedList<T>();
    T next;

    protected TreeNode(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }

    public List<T> getChildren() {
        return children;
    }

    void addChild(T child) {
        T pre = null;
        if (children.size() < 1) {
            children.add(child);
        } else {
            for (int i = 0; i < children.size(); i++) {
                T treeNode = children.get(i);
                if (compare(treeNode,child) > 0) {
                    if (pre != null) {
                        pre.next = child;
                        child.next = treeNode;
                        children.add(i, child);
                    } else {
                        child.next = treeNode;
                        children.add(i, child);
                    }
                    break;
                } else if (i == children.size() -1) {
                    treeNode.next = child;
                    child.next = null;
                    children.add(child);
                    break;
                }
                pre = treeNode;
            }
        }
    }

    protected abstract int compare(T node0, T node1);

    public boolean hasChild() {
        return this.children.size() > 0;
    }

    public T getParent() {
        return parent;
    }

    void setParent(T parent) {
        this.parent = parent;
    }
}
