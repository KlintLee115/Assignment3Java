package implementations;

import java.io.Serializable;
import utilities.BSTreeADT;
import utilities.Iterator;

import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * Represents a binary search tree (BST) with operations to manipulate and traverse the tree.
 * The BST enforces the property that, for any given node, all elements in the left subtree 
 * are less than the node's element, and all elements in the right subtree are greater.
 * 
 * This class implements the BSTreeADT interface.
 * 
 * @param <E> The type of elements stored in the tree, which must implement Comparable.
 */
public class BSTree<E extends Comparable<? super E>> implements BSTreeADT<E>, Serializable {

    private BSTreeNode<E> root;
    private int size;

    /**
     * Retrieves the root node of the binary search tree.
     *
     * @return the root node of the tree
     * @throws NullPointerException if the tree is empty
     */
    public BSTreeNode<E> getRoot() throws NullPointerException {
        if (root == null) {
            throw new NullPointerException("Tree is empty");
        }
        return root;
    }

    /**
     * Calculates the height of the binary search tree.
     *
     * @return the height of the tree
     */
    public int getHeight() {
        return calculateHeight(root);
    }

    /**
     * Returns the number of elements in the binary search tree.
     *
     * @return the size of the tree
     */
    public int size() {
        return size;
    }

    /**
     * Checks whether the binary search tree is empty.
     *
     * @return true if the tree is empty, false otherwise
     */
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * Clears all elements from the binary search tree. The tree will be empty
     * after this operation.
     */
    public void clear() {
        root = null;
        size = 0;
    }

    /**
     * Removes and returns the node with the minimum value in the binary search
     * tree.
     *
     * @return the node with the minimum value, or null if the tree is empty
     */
    public BSTreeNode<E> removeMin() {
        if (isEmpty()) {
            return null;
        }
        BSTreeNode<E> minNode = findMinNode(root);
        root = removeMinNode(root);
        size--;
        return minNode;
    }

    /**
     * Removes the smallest node from the subtree rooted at the given node.
     *
     * @param node the root of the subtree
     * @return the updated subtree after removing the smallest node
     */
    private BSTreeNode<E> removeMinNode(BSTreeNode<E> node) {
        if (node.getLeft() == null) {
            return node.getRight();
        }
        node.setLeft(removeMinNode(node.getLeft()));
        return node;
    }

    /**
     * Finds the node with the smallest value in the subtree rooted at the given
     * node.
     *
     * @param node the root of the subtree
     * @return the node with the smallest value
     */
    private BSTreeNode<E> findMinNode(BSTreeNode<E> node) {
        BSTreeNode<E> current = node;
        while (current.getLeft() != null) {
            current = current.getLeft();
        }
        return current;
    }

    /**
     * Removes the maximum node from the binary search tree.
     *
     * @return the node with the maximum value, or null if the tree is empty
     */
    public BSTreeNode<E> removeMax() {
        if (isEmpty()) {
            return null;
        }
        BSTreeNode<E> maxNode = findMaxNode(root);
        root = removeMaxNode(root);
        size--;
        return maxNode;
    }

    /**
     * Removes the largest node from the subtree rooted at the given node.
     *
     * @param node the root of the subtree
     * @return the updated subtree after removing the largest node
     */
    private BSTreeNode<E> removeMaxNode(BSTreeNode<E> node) {
        if (node.getRight() == null) {
            return node.getLeft();
        }
        node.setRight(removeMaxNode(node.getRight()));
        return node;
    }

    /**
     * Finds the node with the largest value in the subtree rooted at the given
     * node.
     *
     * @param node the root of the subtree
     * @return the node with the largest value
     */
    private BSTreeNode<E> findMaxNode(BSTreeNode<E> node) {
        BSTreeNode<E> current = node;
        while (current.getRight() != null) {
            current = current.getRight();
        }
        return current;
    }

    /**
     * Adds a new entry to the binary search tree.
     *
     * @param newEntry the element to be added
     * @return true if the element was successfully added, false if it already
     * exists
     * @throws NullPointerException if the new entry is null
     */
    public boolean add(E newEntry) throws NullPointerException {
        if (newEntry == null) {
            throw new NullPointerException("Cannot add null entry to the tree");
        }
        if (root == null) {
            root = new BSTreeNode<>(newEntry);
            size++;
            return true;
        } else {
            return addRecursively(root, newEntry);
        }
    }

    /**
     * Recursively adds a new entry to the appropriate location in the tree.
     *
     * @param node the current node in the tree
     * @param newEntry the element to be added
     * @return true if the element was successfully added, false if it already
     * exists
     */
    private boolean addRecursively(BSTreeNode<E> node, E newEntry) {
        int compareResult = newEntry.compareTo(node.getElement());
        if (compareResult == 0) {
            return false;
        } else if (compareResult < 0) {
            if (node.getLeft() == null) {
                node.setLeft(new BSTreeNode<>(newEntry));
                size++;
                return true;
            } else {
                return addRecursively(node.getLeft(), newEntry);
            }
        } else {
            if (node.getRight() == null) {
                node.setRight(new BSTreeNode<>(newEntry));
                size++;
                return true;
            } else {
                return addRecursively(node.getRight(), newEntry);
            }
        }
    }

    /**
     * Checks if the binary search tree contains the specified entry.
     *
     * @param entry the element to search for
     * @return true if the element is found, false otherwise
     * @throws NullPointerException if the entry is null
     */
    public boolean contains(E entry) throws NullPointerException {
        return search(entry) != null;
    }

    /**
     * Searches for a specific entry in the binary search tree.
     *
     * @param entry the element to search for
     * @return the node containing the element, or null if not found
     * @throws NullPointerException if the entry is null
     */
    public BSTreeNode<E> search(E entry) throws NullPointerException {
        return searchRecursively(root, entry);
    }

    /**
     * Recursively searches for a specific entry in the binary search tree.
     *
     * @param node the current node in the tree
     * @param entry the element to search for
     * @return the node containing the element, or null if not found
     */
    private BSTreeNode<E> searchRecursively(BSTreeNode<E> node, E entry) {
        if (node == null) {
            return null;
        }
        int compareResult = entry.compareTo(node.getElement());
        if (compareResult == 0) {
            return node;
        } else if (compareResult < 0) {
            return searchRecursively(node.getLeft(), entry);
        } else {
            return searchRecursively(node.getRight(), entry);
        }
    }

    /**
     * Calculates the height of the binary search tree from the specified node.
     *
     * @param node the current node in the tree
     * @return the height of the tree rooted at the specified node
     */
    private int calculateHeight(BSTreeNode<E> node) {
        if (node == null) {
            return 0;
        }
        int leftHeight = calculateHeight(node.getLeft());
        int rightHeight = calculateHeight(node.getRight());
        return 1 + Math.max(leftHeight, rightHeight);
    }

    /**
     * Calculates the height of the binary search tree from the specified node.
     *
     * @return the height of the tree rooted at the specified node
     */
    public Iterator<E> inorderIterator() {
        return new InorderIterator(root);
    }

    private class InorderIterator implements Iterator<E> {

        private List<E> elements;
        private int index;

        /**
         * Constructs an InorderIterato with the specified root node. Traverses
         * the tree rooted at the given node in inorder and stores the elements
         * in the list.
         *
         * @param root the root node of the binary search tree
         */
        public InorderIterator(BSTreeNode<E> root) {
            elements = new LinkedList<>();
            index = 0;
            inorderTraversal(root);
        }

        /**
         * Performs an inorder traversal of the binary search tree rooted at the
         * specified node, storing the elements in the list.
         *
         * @param node the current node in the traversal
         */
        private void inorderTraversal(BSTreeNode<E> node) {
            if (node == null) {
                return;
            }
            inorderTraversal(node.getLeft());
            elements.add(node.getElement());
            inorderTraversal(node.getRight());
        }

        /**
         * Checks whether there are more elements to iterate over.
         *
         * @return true if there are more elements, false otherwise
         */
        @Override
        public boolean hasNext() {
            return index < elements.size();
        }

        /**
         * Retrieves the next element in the iteration.
         *
         * @return the next element in the iteration
         * @throws NoSuchElementException if there are no more elements to
         * iterate over
         */
        @Override
        public E next() throws NoSuchElementException {
            if (!hasNext()) {
                throw new NoSuchElementException("No more elements in the tree");
            }
            return elements.get(index++);
        }

        public Iterator<E> preorderIterator() {
            return new PreorderIterator(root);
        }
    }

    private class PreorderIterator implements Iterator<E> {

        private List<E> elements;
        private int index;

        public PreorderIterator(BSTreeNode<E> root) {
            elements = new LinkedList<>();
            index = 0;
            preorderTraversal(root);
        }

        /**
         * Recursively traverses the subtree in preorder and collects the
         * elements.
         *
         * @param node the root node of the subtree to traverse
         */
        private void preorderTraversal(BSTreeNode<E> node) {
            if (node == null) {
                return;
            }
            elements.add(node.getElement());
            preorderTraversal(node.getLeft());
            preorderTraversal(node.getRight());
        }

        /**
         * Checks if there are more elements to iterate over.
         *
         * @return true if there are more elements,false otherwise
         */
        @Override
        public boolean hasNext() {
            return index < elements.size();
        }

        /**
         * Retrieves the next element in the iteration.
         *
         * @return the next element in the iteration
         * @throws NoSuchElementException if there are no more elements to
         * iterate over
         */
        @Override
        public E next() throws NoSuchElementException {
            if (!hasNext()) {
                throw new NoSuchElementException("No more elements in the tree");
            }
            return elements.get(index++);
        }
    }

    public Iterator<E> preorderIterator() {
        return new PreorderIterator(root);
    }

    /**
     * Returns an iterator for traversing the binary search tree in postorder.
     *
     * @return an iterator for postorder traversal
     */
    public Iterator<E> postorderIterator() {
        return new PostorderIterator(root);
    }

    /**
     * Iterator for traversing the elements of the binary search tree in
     * postorder.
     *
     * @generic type E the type of elements stored in the tree
     */
    private class PostorderIterator implements Iterator<E> {

        private List<E> elements;
        private int index;

        /**
         * Constructs a new PostorderIterator for the binary search tree.
         *
         * @param root the root node of the tree to iterate over
         */
        public PostorderIterator(BSTreeNode<E> root) {
            elements = new LinkedList<>();
            index = 0;
            postorderTraversal(root);
        }

        /**
         * Recursively traverses the subtree in postorder and collects the
         * elements.
         *
         * @param node the root node of the subtree to traverse
         */
        private void postorderTraversal(BSTreeNode<E> node) {
            if (node == null) {
                return;
            }
            postorderTraversal(node.getLeft());
            postorderTraversal(node.getRight());
            elements.add(node.getElement());
        }

        /**
         * Checks if there are more elements to iterate over.
         *
         * @return true if there are more elements, false otherwise
         */
        @Override
        public boolean hasNext() {
            return index < elements.size();
        }

        /**
         * Retrieves the next element in the iteration.
         *
         * @return the next element in the iteration
         * @throws NoSuchElementException if there are no more elements to
         * iterate over
         */
        @Override
        public E next() throws NoSuchElementException {
            if (!hasNext()) {
                throw new NoSuchElementException("No more elements in the tree");
            }
            return elements.get(index++);
        }
    }
}
