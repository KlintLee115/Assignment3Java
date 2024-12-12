package implementations;

import utilities.BSTreeADT;
import utilities.Iterator;

import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;

public class BSTree<E extends Comparable<? super E>> implements BSTreeADT<E> {
    private BSTreeNode<E> root;
    private int size;

    public BSTreeNode<E> getRoot() throws NullPointerException {
        if (root == null)
            throw new NullPointerException("Tree is empty");
        return root;
    }

    public int getHeight() {
        return calculateHeight(root);
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public void clear() {
        root = null;
        size = 0;
    }

    public BSTreeNode<E> removeMin() {
        if (isEmpty())
            return null;
        BSTreeNode<E> minNode = findMinNode(root);
        root = removeMinNode(root);
        size--;
        return minNode;
    }

    private BSTreeNode<E> removeMinNode(BSTreeNode<E> node) {
        if (node.getLeft() == null)
            return node.getRight();
        node.setLeft(removeMinNode(node.getLeft()));
        return node;
    }


    private BSTreeNode<E> findMinNode(BSTreeNode<E> node) {
        BSTreeNode<E> current = node;
        while (current.getLeft() != null)
            current = current.getLeft();
        return current;
    }

    public BSTreeNode<E> removeMax() {
        if (isEmpty())
            return null;
        BSTreeNode<E> maxNode = findMaxNode(root);
        root = removeMaxNode(root);
        size--;
        return maxNode;
    }

    private BSTreeNode<E> removeMaxNode(BSTreeNode<E> node) {
        if (node.getRight() == null)
            return node.getLeft();
        node.setRight(removeMaxNode(node.getRight()));
        return node;
    }

    private BSTreeNode<E> findMaxNode(BSTreeNode<E> node) {
        BSTreeNode<E> current = node;
        while (current.getRight() != null)
            current = current.getRight();
        return current;
    }

    public boolean add(E newEntry) throws NullPointerException {
        if (newEntry == null)
            throw new NullPointerException("Cannot add null entry to the tree");
        if (root == null) {
            root = new BSTreeNode<>(newEntry);
            size++;
            return true;
        } else {
            return addRecursively(root, newEntry);
        }
    }

    private boolean addRecursively(BSTreeNode<E> node, E newEntry) {
        int compareResult = newEntry.compareTo(node.getElement());
        if (compareResult == 0)
            return false;
        else if (compareResult < 0) {
            if (node.getLeft() == null) {
                node.setLeft(new BSTreeNode<>(newEntry));
                size++;
                return true;
            } else
                return addRecursively(node.getLeft(), newEntry);
        } else {
            if (node.getRight() == null) {
                node.setRight(new BSTreeNode<>(newEntry));
                size++;
                return true;
            } else
                return addRecursively(node.getRight(), newEntry);
        }
    }

    public boolean contains(E entry) throws NullPointerException {
        return search(entry) != null;
    }

    public BSTreeNode<E> search(E entry) throws NullPointerException {
        return searchRecursively(root, entry);
    }

    private BSTreeNode<E> searchRecursively(BSTreeNode<E> node, E entry) {
        if (node == null)
            return null;
        int compareResult = entry.compareTo(node.getElement());
        if (compareResult == 0)
            return node;
        else if (compareResult < 0)
            return searchRecursively(node.getLeft(), entry);
        else
            return searchRecursively(node.getRight(), entry);
    }

    private int calculateHeight(BSTreeNode<E> node) {
        if (node == null)
            return 0;
        int leftHeight = calculateHeight(node.getLeft());
        int rightHeight = calculateHeight(node.getRight());
        return 1 + Math.max(leftHeight, rightHeight);
    }


    public Iterator<E> inorderIterator() {
        return new InorderIterator(root);
    }

    private class InorderIterator implements Iterator<E> {
        private List<E> elements;
        private int index;

        /**
         * Constructs an InorderIterato with the specified root node.
         * Traverses the tree rooted at the given node in inorder and stores
         * the elements in the list.
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
            if (node == null)
                return;
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
         * @throws NoSuchElementException if there are no more elements to iterate over
         */
        @Override
        public E next() throws NoSuchElementException {
            if (!hasNext())
                throw new NoSuchElementException("No more elements in the tree");
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
         * Recursively traverses the subtree in preorder and collects the elements.
         *
         * @param node the root node of the subtree to traverse
         */
        private void preorderTraversal(BSTreeNode<E> node) {
            if (node == null)
                return;
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
         * @throws NoSuchElementException if there are no more elements to iterate over
         */
        @Override
        public E next() throws NoSuchElementException {
            if (!hasNext())
                throw new NoSuchElementException("No more elements in the tree");
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
     * Iterator for traversing the elements of the binary search tree in postorder.
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
         * Recursively traverses the subtree in postorder and collects the elements.
         *
         * @param node the root node of the subtree to traverse
         */
        private void postorderTraversal(BSTreeNode<E> node) {
            if (node == null)
                return;
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
         * @throws NoSuchElementException if there are no more elements to iterate over
         */
        @Override
        public E next() throws NoSuchElementException {
            if (!hasNext())
                throw new NoSuchElementException("No more elements in the tree");
            return elements.get(index++);
        }
    }
}
