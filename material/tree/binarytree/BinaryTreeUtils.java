package material.tree.binarytree;

import material.Position;
import material.tree.binarytree.BinaryTree;

import java.util.Iterator;

public class BinaryTreeUtils<E> {
    private final BinaryTree<E> tree;

    public BinaryTreeUtils(BinaryTree<E> tree) {
        this.tree = tree;
    }

    /**
     * Given a tree the method returns a new tree where all left children
     * become right children and vice versa
     */
    public BinaryTree<E> mirror() {
        return mirrorAux(this.tree.root());
    }

    private BinaryTree<E> mirrorAux(Position<E> p) {
        LinkedBinaryTree<E> res = new LinkedBinaryTree<>();
        res.addRoot(p.getElement());
        if (this.tree.hasRight(p))
            res.attachLeft(res.root(), mirrorAux(this.tree.right(p)));
        if (this.tree.hasLeft(p))
            res.attachRight(res.root(), mirrorAux(this.tree.left(p)));
        return res;
    }

    /**
     * Determines whether the element e is the tree or not
     */
    public boolean contains(E e) {
        Iterator<Position<E>> it = this.tree.iterator();
        while (it.hasNext())
            if (it.next().getElement() == e)
                return true;
        return false;
    }

    /**
     * Determines the level of a node in the tree (root is located at level 1)
     */
    public int levelIterative(Position<E> p) {
        Position<E> aux = p;
        int c = 1;
        while (aux != this.tree.root()) {
            aux = tree.parent(aux);
            c++;
        }
        return c;
    }

    public int level(Position<E> p) {
        return (p == this.tree.root()) ? 1 : level(this.tree.parent(p)) + 1;
    }

}
