package material.tree.binarytree;

import material.Position;
import material.tree.iterators.InorderBinaryTreeIterator;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ArrayBinaryTree<E> implements BinaryTree<E> {

    private static class ABTNode<E> implements Position<E> {
        private E element;
        private int position;
        private int parent;
        private int leftChild;
        private int rightChild;
        private ArrayBinaryTree<E> myTree;

        public ABTNode(E element, int position, int parent, int leftChild, int rightChild, ArrayBinaryTree<E> myTree) {
            this.element = element;
            this.position = position;
            this.parent = parent;
            this.leftChild = leftChild;
            this.rightChild = rightChild;
            this.myTree = myTree;
        }

        public E getElement() {
            return element;
        }

        public int getPosition() {
            return this.position;
        }

        public void setPosition(int position) {
            this.position = position;
        }

        public void setElement(E element) {
            this.element = element;
        }

        public int getParent() {
            return parent;
        }

        public void setParent(int parent) {
            this.parent = parent;
        }

        public int getLeftChild() {
            return leftChild;
        }

        public void setLeftChild(int leftChild) {
            this.leftChild = leftChild;
        }

        public int getRightChild() {
            return rightChild;
        }

        public void setRightChild(int rightChild) {
            this.rightChild = rightChild;
        }

        public ArrayBinaryTree<E> getMyTree() {
            return myTree;
        }

        public void setMyTree(ArrayBinaryTree<E> myTree) {
            this.myTree = myTree;
        }
    }

    private ABTNode<E>[] tree;
    private int size;
    private int MAX_SIZE;
    private int root;

    public ArrayBinaryTree() {
        this.tree = new ABTNode[16];
        this.size = 0;
        this.MAX_SIZE = 16;
        this.root = -1;
    }

    public ArrayBinaryTree(int capacity) {
        this.tree = new ABTNode[capacity];
        this.size = 0;
        this.MAX_SIZE = capacity;
        this.root = -1;
    }

    @Override
    public Position<E> left(Position<E> p) throws RuntimeException {
        int l = this.checkPosition(p).getLeftChild();
        if (l < 0) throw new RuntimeException("There is no left child");
        return this.tree[l];
    }

    @Override
    public Position<E> right(Position<E> p) throws RuntimeException {
        int r = this.checkPosition(p).getRightChild();
        if (r < 0) throw new RuntimeException("There is no right child");
        return this.tree[r];
    }

    @Override
    public boolean hasLeft(Position<E> p) {
        return this.checkPosition(p).getLeftChild() != -1;
    }

    @Override
    public boolean hasRight(Position<E> p) {
        return this.checkPosition(p).getRightChild() != -1;
    }

    @Override
    public E replace(Position<E> p, E e) {
        ABTNode<E> node = this.checkPosition(p);
        E res = node.getElement();
        node.setElement(e);
        return res;
    }

    @Override
    public Position<E> sibling(Position<E> p) throws RuntimeException {
        int node = this.checkPosition(p).getPosition();
        if (node == this.root) throw new RuntimeException("Root has no sibling");
        int parent = this.tree[node].getParent();
        return (this.left(this.tree[parent]) == this.tree[node]) ? this.right(this.tree[parent]) : this.left(this.tree[parent]);
    }

    @Override
    public Position<E> insertLeft(Position<E> p, E e) throws RuntimeException {
        ABTNode<E> n = this.checkPosition(p);
        if (n.getLeftChild() > -1) throw new RuntimeException("Node already has a left child");
        ABTNode<E> newElement = new ABTNode<>(e, this.size(), n.getPosition(), -1, -1, this);
        if (this.size == this.MAX_SIZE) this.resize();
        this.tree[size++] = newElement;
        n.setLeftChild(newElement.getPosition());
        return newElement;
    }

    @Override
    public Position<E> insertRight(Position<E> p, E e) throws RuntimeException {
        ABTNode<E> n = this.checkPosition(p);
        if (n.getRightChild() > -1) throw new RuntimeException("Node already has a right child");
        ABTNode<E> newElement = new ABTNode<>(e, this.size(), n.getPosition(), -1, -1, this);
        if (this.size == this.MAX_SIZE) this.resize();
        this.tree[size++] = newElement;
        n.setRightChild(newElement.getPosition());
        return newElement;
    }

    @Override
    public E remove(Position<E> p) throws RuntimeException {
        ABTNode<E> n = this.checkPosition(p);
        if (n.getLeftChild() > -1 && n.getRightChild() > -1)
            throw new RuntimeException("Cannot delete nodes with more than one child");
        int i = n.getPosition();
        E res = n.getElement();
        removeAux(i);
        return res;
    }

    private void removeAux(int i) {
        if (this.tree[i].getLeftChild() > 0)
            removeAux(this.tree[i].getLeftChild());
        if (this.tree[i].getRightChild() > 0)
            removeAux(this.tree[i].getRightChild());
        this.size--;
        this.tree[i] = null;
    }

    @Override
    public void swap(Position<E> p1, Position<E> p2) {
        ABTNode<E> n1 = this.checkPosition(p1);
        ABTNode<E> n2 = this.checkPosition(p2);
        ABTNode<E> parent1 = this.tree[n1.getParent()];
        ABTNode<E> parent2 = this.tree[n2.getParent()];
        if (parent1.getLeftChild() == n1.getPosition()) parent1.setLeftChild(n2.getPosition());
        else parent1.setRightChild(n2.getPosition());
        if (parent1.getLeftChild() == n2.getPosition()) parent2.setLeftChild(n2.getPosition());
        else parent2.setRightChild(n1.getPosition());
        n1.setParent(parent2.getPosition());
        n2.setParent(parent1.getPosition());
    }

    @Override
    public BinaryTree<E> subTree(Position<E> v) {
        BinaryTree<E> res = new ArrayBinaryTree<>(MAX_SIZE);
        ABTNode<E> n = this.checkPosition(v);
        res.addRoot(n.getElement());
        if (this.hasLeft(v))
            res.attachLeft(res.root(), subTree(this.tree[n.getLeftChild()]));
        if (this.hasRight(v))
            res.attachRight(res.root(), subTree(this.tree[n.getRightChild()]));
        return res;
    }

    @Override
    public void attachLeft(Position<E> p, BinaryTree<E> tree) throws RuntimeException {
        if (!(tree instanceof ArrayBinaryTree)) throw new RuntimeException("Argument tree uses another implementation");
//        ABTNode<E> node = this.checkPosition(p);
        if (this.hasLeft(p)) throw new RuntimeException("That node already has a left child");
        if (!tree.isEmpty()) {
            this.insertLeft(p, tree.root().getElement());
            attachAux(this.left(p), tree.root(), tree);
        }
    }

    @Override
    public void attachRight(Position<E> p, BinaryTree<E> tree) throws RuntimeException {
        if (!(tree instanceof ArrayBinaryTree)) throw new RuntimeException("Argument tree uses another implementation");
//        ABTNode<E> node = this.checkPosition(p);
        if (this.hasRight(p)) throw new RuntimeException("That node already has a right child");
        if (!tree.isEmpty()) {
            this.insertRight(p, tree.root().getElement());
            attachAux(this.right(p), tree.root(), tree);
        }
    }

    @Override
    public boolean isComplete() {
        int i = 0;
        while (i < this.size) {
            if (this.tree[i] != null && (this.hasLeft(this.tree[i]) ^ this.hasRight(this.tree[i])))
                return false;
            i++;
        }
        return true;
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
        if (this.isEmpty()) throw new RuntimeException("There is no root");
        return this.tree[this.root];
    }

    @Override
    public Position<E> parent(Position<E> v) throws RuntimeException {
        return this.tree[this.checkPosition(v).getParent()];
    }

    @Override
    public Iterable<? extends Position<E>> children(Position<E> v) {
        ABTNode<E> n = this.checkPosition(v);
        List<Position<E>> c = new ArrayList<>(2);
        if (n.getLeftChild() > -1) c.add(this.tree[n.getLeftChild()]);
        if (n.getRightChild() > -1) c.add(this.tree[n.getRightChild()]);
        return c;
    }

    @Override
    public boolean isInternal(Position<E> v) {
        return (this.hasLeft(v) || this.hasRight(v));
    }

    @Override
    public boolean isLeaf(Position<E> v) throws RuntimeException {
        return !this.isInternal(v);
    }

    @Override
    public boolean isRoot(Position<E> v) {
        return this.checkPosition(v).getPosition() == this.root;
    }

    @Override
    public Position<E> addRoot(E e) throws RuntimeException {
        if (this.root > 0)
            throw new RuntimeException("Tree already has a node");
        this.tree[0] = new ABTNode<>(e, 0, -1, -1, -1, this);
        this.root = 0;
        this.size++;
        return this.tree[root];
    }

    @Override
    public void moveSubtree(Position<E> pOrig, Position<E> pDest) throws RuntimeException {
        ABTNode<E> nOrigen = this.checkPosition(pOrig);
        ABTNode<E> nDest = this.checkPosition(pDest);
        if (isAncestor(nOrigen, nDest)) throw new RuntimeException("Target position can't be a sub tree of origin");
        if (nDest.getLeftChild() < 0) {
            nDest.setLeftChild(nOrigen.getPosition());
        } else if (nDest.getLeftChild() < 0) {
            nDest.setRightChild(nOrigen.getPosition());
        } else throw new RuntimeException("Destiny node already has two children");
        nOrigen.setParent(nDest.getPosition());

    }

    private boolean isAncestor(ABTNode<E> anc, ABTNode<E> desc) {
        if (anc == desc) return true;
        if (desc.getParent() > -1) return isAncestor(anc, tree[anc.getParent()]);
        return false;
    }

    @Override
    public Iterator<Position<E>> iterator() {
        return new InorderBinaryTreeIterator<>(this);
    }

    private ABTNode<E> checkPosition(Position<E> p) throws IllegalStateException {
        if (!(p instanceof ABTNode))
            throw new IllegalStateException("The position is invalid");
        ABTNode<E> aux = (ABTNode<E>) p;
        if (this != aux.getMyTree()) {
            throw new IllegalStateException("The node is not from this tree");
        }
        return aux;
    }

    private void resize() {
        int newCapacity = this.MAX_SIZE * 2;
        ABTNode<E>[] newTree = new ABTNode[newCapacity];
        System.arraycopy(this.tree, 0, newTree, 0, this.size);
        this.MAX_SIZE = newCapacity;
        this.tree = newTree;
    }

    private void attachAux(Position<E> dest, Position<E> origin, BinaryTree<E> tree) {
        if (tree.hasLeft(origin)) {
            Position<E> leftChild = tree.left(origin);
            Position<E> newChild = this.insertLeft(dest, leftChild.getElement());
            attachAux(newChild, leftChild, tree);
        }
        if (tree.hasRight(origin)) {
            Position<E> rightChild = tree.right(origin);
            Position<E> newChild = this.insertRight(dest, rightChild.getElement());
            attachAux(newChild, rightChild, tree);
        }
    }
}
