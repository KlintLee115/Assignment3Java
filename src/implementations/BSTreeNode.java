package implementations;

import java.io.Serializable;

public class BSTreeNode<E extends Comparable<? super E>> implements Serializable {
    private E data;
    private BSTreeNode<E> left;
    private BSTreeNode<E> right;

    public BSTreeNode(E data) {
        this.data = data;
        left = null;
        right = null;
    }

    /**
     * Retrieves the data stored in the node.
     *
     * @return the data stored in the node
     */
    public E getElement() {
        return data;
    }

    /**
     * Sets the data stored in the node.
     *
     * @param data the data to be stored in the node
     */
    public void setData(E data) {
        this.data = data;
    }

    /**
     * Retrieves the left child node.
     *
     * @return the left child node
     */
    public BSTreeNode<E> getLeft() {
        return left;
    }

    /**
     * Sets the left child node.
     *
     * @param left the left child node
     */
    public void setLeft(BSTreeNode<E> left) {
        this.left = left;
    }

    /**
     * Retrieves the right child node.
     *
     * @return the right child node
     */
    public BSTreeNode<E> getRight() {
        return right;
    }

    /**
     * Sets the right child node.
     *
     * @param right the right child node
     */
    public void setRight(BSTreeNode<E> right) {
        this.right = right;
    }
}
