package material.tree.narytree;

import material.Position;
import material.tree.iterators.BFSIterator;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * A linked class for a tree where nodes have an arbitrary number of children.
 *
 * @param <E> the elements stored in the tree
 * @author Raul Cabido, Abraham Duarte, Jose Velez, Jesús Sánchez-Oro
 */
public class LCRSTree<E> implements NAryTree<E> {

    private static class TreeNode<E> implements Position<E> {

        private E element;
        private TreeNode<E> leftChild;
        private TreeNode<E> rightSibling;
        private TreeNode<E> parent;
        private LCRSTree<E> tree;

        public TreeNode(E element, TreeNode<E> leftChild, TreeNode<E> rightSibling, TreeNode<E> parent, LCRSTree<E> myTree) {
            this.element = element;
            this.leftChild = leftChild;
            this.rightSibling = rightSibling;
            this.parent = parent;
            this.tree = myTree;
        }

        @Override
        public E getElement() {
            return this.element;
        }

        public void setElement(E element) {
            this.element = element;
        }

        public TreeNode<E> getLeftChild() {
            return leftChild;
        }

        public void setLeftChild(TreeNode<E> leftChild) {
            this.leftChild = leftChild;
        }

        public TreeNode<E> getRightSibling() {
            return rightSibling;
        }

        public void setRightSibling(TreeNode<E> rightSibling) {
            this.rightSibling = rightSibling;
        }

        public TreeNode<E> getParent() {
            return parent;
        }

        public void setParent(TreeNode<E> parent) {
            this.parent = parent;
        }

        public LCRSTree<E> getTree() {
            return tree;
        }

        public void setTree(LCRSTree<E> tree) {
            this.tree = tree;
        }
    }

    private TreeNode<E> root;
    private int size;

    public void LCRSTree() {
        this.root = null;
        this.size = 0;
    }


    @Override
    public int size() {
        return this.size;
    }

    @Override
    public boolean isEmpty() {
        return this.size == 0;
    }

    @Override
    public Position<E> root() throws RuntimeException {
        if (this.root == null) {
            throw new RuntimeException("The tree is empty");
        }
        return this.root;
    }

    @Override
    public Position<E> parent(Position<E> v) throws RuntimeException {
        TreeNode<E> node = (TreeNode<E>) checkPosition(v);
        if (v == root()) {
            throw new RuntimeException("The node has not parent");
        }
        return node.getParent();
    }

    @Override
    public Iterable<? extends Position<E>> children(Position<E> v) {
        TreeNode<E> node = (TreeNode<E>) checkPosition(v);
        List<TreeNode<E>> childrenList = new LinkedList<>();
        node = node.leftChild;
        if (node != null) {
            childrenList.add(node);
            node = node.rightSibling;
            while (node != null) {
                childrenList.add(node);
                node = node.rightSibling;
            }
        }
        return childrenList;
    }

    @Override
    public boolean isInternal(Position<E> v) {
        return !this.isLeaf(v);
    }

    @Override
    public boolean isLeaf(Position<E> v) throws RuntimeException {
        TreeNode<E> node = (TreeNode<E>) checkPosition(v);
        return node.getLeftChild() == null;
    }

    @Override
    public boolean isRoot(Position<E> v) throws RuntimeException {
        return checkPosition(v) == this.root;
    }

    @Override
    public Position<E> addRoot(E e) throws RuntimeException {
        if (this.root != null) {
            throw new RuntimeException("Tree already has a root");
        }
        TreeNode<E> newRoot = new TreeNode<>(e, null, null, null, this);
        this.root = newRoot;
        this.size++;
        return newRoot;
    }

    @Override
    public Iterator<Position<E>> iterator() {
        return new BFSIterator<>(this);
    }

    @Override
    public E replace(Position<E> p, E e) throws RuntimeException {
        TreeNode<E> n = checkPosition(p);
        E elementReturned = n.getElement();
        n.setElement(e);
        return elementReturned;
    }

    @Override
    public void swapElements(Position<E> p1, Position<E> p2) throws RuntimeException {
        TreeNode<E> n1 = checkPosition(p1);
        TreeNode<E> n2 = checkPosition(p2);
        E aux = n1.getElement();
        n1.setElement(n2.getElement());
        n2.setElement(aux);
    }

    @Override
    public Position<E> add(E element, Position<E> p) throws RuntimeException {
        TreeNode<E> referenceNode = (TreeNode<E>) checkPosition(p);
        TreeNode<E> newNode = new TreeNode<>(element, null, null, referenceNode, this);
        if (referenceNode.leftChild == null) {
            referenceNode.leftChild = newNode;
        } else {
            referenceNode = referenceNode.leftChild;
            while (referenceNode.rightSibling != null) {
                referenceNode = referenceNode.rightSibling;
            }
            referenceNode.rightSibling = newNode;
        }
        this.size++;
        return newNode;
    }

    @Override
    public void remove(Position<E> p) {
        TreeNode<E> n = checkPosition(p);
        Iterator<Position<E>> it;

        // reconectamos hermanos y padre-hijo para poder extraer correctamente el nodo y su respectivo subarbol.
        if (n != this.root) {
            TreeNode<E> sibling = n.getRightSibling();
            // con 'found' nos evitamos seguir recorriendo los hermanos una vez encontrado el deseado
            boolean found = false;
            it = new BFSIterator<>(this, n.getParent());
            while (!found && it.hasNext()) {
                TreeNode<E> aux = (TreeNode<E>) it.next();
                if (aux.getRightSibling() == n) {
                    aux.setRightSibling(sibling);
                    found = true;
                }
            }

            if (n.getParent().getLeftChild() == n && sibling != null) {
                n.getParent().setLeftChild(sibling);
            }
        }

        it = new BFSIterator<>(this, p);

        int count = 0;

        while (it.hasNext()) {
            n = (TreeNode<E>) it.next();
            n.setTree(null);
            count++;
        }
        n.setTree(null);
        if (p == this.root) {
            this.root = null;
        }
        this.size -= count;
    }

    @Override
    public void moveSubtree(Position<E> pOrig, Position<E> pDest) throws RuntimeException {
        TreeNode<E> nOrig = checkPosition(pOrig);
        TreeNode<E> nDest = checkPosition(pDest);
        if (pOrig == this.root) throw new RuntimeException("Root node can't be moved");
        if (pOrig == pDest) throw new RuntimeException("Both positions are the same");

        // Checking there is no infinite recursion
        Iterator<Position<E>> it = new BFSIterator<>(this, pOrig);
        while (it.hasNext()) {
            if (it.next() == pDest) {
                throw new RuntimeException("Target position can't be a sub tree of origin");
            }
        }

        if (nOrig.getParent() != null) {
            it = new BFSIterator<>(this, nOrig.getParent());
            boolean found = false;
            TreeNode<E> aux = nOrig.getParent();
            while (it.hasNext() && !found) {
                aux = (TreeNode<E>) it.next();
                if (aux.getRightSibling() == nOrig) {
                    aux.rightSibling = nOrig.rightSibling;
                }
            }
        }

        nOrig.setParent(nDest);
        nOrig.setRightSibling(null);

        // If pDest has no children: O(1) assigment
        if (nDest.getLeftChild() == null) {
            nDest.setLeftChild(nOrig);

        } else {
            nDest = nDest.getLeftChild();
            while (nDest.getRightSibling() != null) {
                nDest = nDest.getRightSibling();
            }
            nDest.setRightSibling(nOrig);
        }


    }

    private TreeNode<E> checkPosition(Position<E> p)
            throws IllegalStateException {
        if (!(p instanceof TreeNode)) {
            throw new IllegalStateException("The position is invalid");
        }
        TreeNode<E> aux = (TreeNode<E>) p;

        if (aux.getTree() != this) {
            throw new IllegalStateException("The node is not from this tree");
        }
        return aux;
    }
}