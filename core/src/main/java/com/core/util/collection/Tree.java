package com.core.util.collection;

import java.util.*;

/**
 * Created by laizy on 2017/6/7.
 */
public class Tree<T extends TreeNode<T>> extends AbstractCollection<T> {

    public Tree() {
    }

    public Tree(T root) {
        setRoot(root);
    }

    private T root;
    private int size;
    private int modCount = 0;

    public T getRoot() {
        return root;
    }

    public void setRoot(T root) {
        this.root = root;
        if (root != null) {
            size++;
            modCount++;
        }
    }

    /**
     * once replace is true and it is not leaf, TreeNode is not sorted any more
     * @param treeNode the node to insert
     * @param replace replace the old node
     * @return insert success?
     */
    public boolean insert(T treeNode, boolean replace) {
        if (root == null || treeNode == null) {
            return false;
        }
        T parent = findParent(root, treeNode.getPath());
        if (parent != null) {
            if (parent.getPath().equals(treeNode.getPath())) {
                //重复节点
                if (parent != root && replace) {
                    T p = parent.getParent();
                    treeNode.setParent(p);
                    List<T> children = parent.getChildren();
                    for (T child : children) {
                        treeNode.addChild(child);
                    }

                    int index = p.getChildren().indexOf(parent);
                    p.getChildren().set(index, treeNode);
                    if (index > 0) {
                        T pre = p.getChildren().get(index - 1);
                        pre.next = treeNode;
                        treeNode.next = parent.next;
                    } else {
                        treeNode.next = parent.next;
                    }

                    return true;
                }
                return false;
            }
            modCount++;
            List<T> children = parent.getChildren();
            Iterator<T> it = children.iterator();
            TreeNode pre = treeNode;
            while (it.hasNext()) {
                T child = it.next();
                if (child.getPath().startsWith(treeNode.getPath())) {
                    pre.next = child.next;
                    child.next = null;
                    it.remove();
                    child.setParent(treeNode);
                    treeNode.addChild(child);
                } else {
                    pre = child;
                }
            }
            parent.addChild(treeNode);
            treeNode.setParent(parent);
            size++;
            return true;
        }
        return false;
    }

    public boolean insert(T treeNode) {
        return insert(treeNode, false);
    }

    @Override
    public boolean add(T treeNode) {
        return this.insert(treeNode);
    }

    /**
     * if it is not leaf, TreeNode is not sorted any more
     * @param obj: the node to remove
     * @return success?
     */
    @SuppressWarnings("unchecked")
    @Override
    public boolean remove(Object obj) {
        T treeNode = (T) obj;
        if (treeNode.getParent() == null) {
            modCount++;
            this.root = null;
            size--;
            return true;
        } else {
            List<T> brothers = treeNode.getParent().getChildren();
            int removeIndex = brothers.indexOf(treeNode);
            if (removeIndex != -1) {
                modCount++;

                brothers.remove(removeIndex);
                T pre = null;
                if (removeIndex > 0) {
                    pre = brothers.get(removeIndex - 1);
                }

                List<T> children = treeNode.getChildren();
                if (children.size() > 0) {
                    int index = removeIndex;
                    for (T child : children) {
                        child.setParent(treeNode.getParent());
                        if (index == removeIndex) {
                            brothers.add(index, child);
                            if (pre != null) {
                                pre.next = child;
                            }
                        }
                        if (index == removeIndex + children.size() - 1) {
                            child.next = treeNode.next;
                        }
                        index++;
                    }
                } else {
                    if (pre != null) {
                        pre.next = treeNode.next;
                    }
                }
                size--;
                return true;
            }
        }
        return false;
    }

    /**
     * if it is not leaf, TreeNode is not sorted any more
     * @param path: path of the node to remove
     * @return success?
     */
    public boolean delete(String path) {
        T node = find(path);
        return node == null || remove(node);
    }

    public boolean deleteSubTree(String path) {
        T node = find(path);
        if (node == root) {
            clear();
        } else if (node != null) {
            T parent = node.getParent();
            int index = parent.getChildren().indexOf(node);
            if (index > 0) {
                T pre = parent.getChildren().get(index - 1);
                pre.next = node.next;
            }
            parent.getChildren().remove(index);
            int subSize = subTreeSize(node) + 1;
            size -= subSize;
        }
        return true;
    }

    public T find(String path) {
        T node = findParent(root, path);
        if (node.getPath().equals(path)) {
            return node;
        } else {
            return null;
        }
    }

    @Override
    public int size() {
        return this.size;
    }

    @Override
    public void clear() {
        modCount++;
        //todo: 后序遍历清空表?
        this.root = null;
        this.size = 0;
    }

    private int subTreeSize(T treeNode) {
        List<T> children = treeNode.getChildren();
        if (children.size() > 0) {
            int count = children.size();
            for (T child : children) {
                count += subTreeSize(child);
            }
            return count;
        } else {
            return 0;
        }
    }

    public Iterator<T> iteratorIgnoreRoot() {
        Iterator<T> iterator = iterator();
        iterator.next();
        return iterator;
    }

    /**
     * 默认按照前序遍历
     * @return : PreOrderIterator
     */
    @Override
    public Iterator<T> iterator() {
        return preOrderIterator();
    }

    public Iterator<T> preOrderIterator() {
        return new PreOrderIterator();
    }

    public Iterator<T> byLayerIterator() {
        return new ByLayerIterator();
    }

    private void checkForModification(int expectedModCount) {
        if (modCount != expectedModCount) {
            throw new ConcurrentModificationException();
        }
    }
    public static interface TreeIterator<T> extends Iterator<T> {
        void deleteSubTree();
    }
    private final class PreOrderIterator implements TreeIterator<T> {
        private T current = null;
        private T next = root;
        private int cursor = 0;
        private int expectedModCount = modCount;

        @Override
        public boolean hasNext() {
            return cursor < size;
        }

        @Override
        public T next() {
            checkForModification(expectedModCount);
            cursor++;
            current = next;
            T p = next;
            if (p.getChildren().size() > 0) {
                next = p.getChildren().get(0);
            } else {
                while (p != null && p.next == null) {
                    p = p.getParent();
                }
                if (p == null) {
                    next = null;
                } else {
                    next = p.next;
                }
            }
            return current;
        }

        /**
         * if it is not leaf , TreeNode is not sorted any more
         */
        @Override
        public void remove() {
            checkForModification(expectedModCount);
            if (Tree.this.remove(current)) {
                cursor--;
            }
            expectedModCount = modCount;
        }

        @Override
        public void deleteSubTree() {
            int preSize = size;
            if (Tree.this.deleteSubTree(current.getPath())) {
                cursor = cursor - (preSize - size);
            }
        }
    }

    private class ByLayerIterator implements Iterator<T> {

        private T current = null;
        private T next = root;
        private int cursor = 0;
        private int expectedModCount = modCount;
        private Queue<T> nextLayerQueue = new LinkedList<T>();
        @Override
        public boolean hasNext() {
            return cursor < size;
        }

        @Override
        public T next() {
            checkForModification(expectedModCount);
            cursor++;
            current = next;
            T p = next;
            if(next.hasChild()){
                nextLayerQueue.offer(next.getChildren().get(0));
            }
            if (p.next != null) {
                next = p.next;
            } else {
                next = nextLayerQueue.poll();
            }
            return current;
        }

        @Override
        public void remove() {
            checkForModification(expectedModCount);
            if (Tree.this.remove(current)) {
                cursor--;
            }
            expectedModCount = modCount;
        }
    }


    private T findParent(T parent, String path) {
        List<T> children = parent.getChildren();
        for (T treeNode : children) {
            if (path.equals(treeNode.getPath())) {
                return treeNode;
            } else if (path.startsWith(treeNode.getPath())) {
                return findParent(treeNode, path);
            }
        }
        return parent;
    }


    public static final long ROOT_ID = 1l;

}

