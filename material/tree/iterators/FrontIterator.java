package material.tree.iterators;

import material.Position;
import material.tree.Tree;

import java.util.ArrayDeque;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Queue;

/**
 * Front iteartor for trees.
 *
 * @param <T>
 * @author jvelez, JD. Quintana
 */
public class FrontIterator<T> implements Iterator<Position<T>> {

    private Queue<Position<T>> nodeQueue;
    private Tree<T> tree;
//    private HashSet<Position<T>> set;


    public FrontIterator(Tree<T> tree) {
        this.nodeQueue = new ArrayDeque();
        this.tree = tree;
//        this.set = new HashSet<>(tree.size());
        if (!tree.isEmpty()) {
            this.nodeQueue.add(tree.root());
        }
    }


    public FrontIterator(Tree<T> tree, Position<T> node) {
        this.nodeQueue = new ArrayDeque();
        this.tree = tree;
//        this.set = new HashSet<>(tree.size());
        nodeQueue.add(node);
    }

    @Override
    public boolean hasNext() {
        return !this.nodeQueue.isEmpty();
    }

    /**
     * This method visits the nodes of a tree by following a breath-first search
     */
    @Override
    public Position<T> next() {
        Position<T> p = nodeQueue.poll();
        if (tree.isLeaf(p)) {
            return p;
        }
        for (Position<T> c : tree.children(p)) {
            nodeQueue.add(c);
        }
        return next();
    }

    @Override
    public void remove() throws RuntimeException {
        throw new RuntimeException("You should not call this method.");
    }
}
