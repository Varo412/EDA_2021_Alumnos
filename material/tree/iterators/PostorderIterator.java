package material.tree.iterators;

import material.Position;
import material.tree.Tree;

import java.util.*;
import java.util.function.Predicate;

/**
 * Generic preorder iterator for trees.
 *
 * @param <E>
 * @author A. Duarte, J. Vélez, J. Sánchez-Oro, JD. Quintana
 */
public class PostorderIterator<E> implements Iterator<Position<E>> {

    // left,right,root
    private Deque<Position<E>> nodeQueue;
    private Tree<E> tree;
    private Predicate<Position<E>> predicate;
    private Set<Position<E>> set;

    public PostorderIterator(Tree<E> tree) {
        this.nodeQueue = new ArrayDeque<>();
        this.tree = tree;
        this.predicate = null;
        this.set = new HashSet<>();
        if (!tree.isEmpty())
            nodeQueue.add(tree.root());

    }

    public PostorderIterator(Tree<E> tree, Position<E> start) {
        this.nodeQueue = new ArrayDeque<>();
        this.tree = tree;
        this.predicate = null;
        this.set = new HashSet<>();
        nodeQueue.add(start);
    }

    public PostorderIterator(Tree<E> tree, Position<E> start, Predicate<Position<E>> predicate) {
        this.nodeQueue = new ArrayDeque<>();
        this.tree = tree;
        this.predicate = predicate;
        this.set = new HashSet<>();
        nodeQueue.add(start);
    }


    @Override
    public boolean hasNext() {
        return !this.nodeQueue.isEmpty();
    }

    @Override
    public Position<E> next() {
        Position<E> p = this.nodeQueue.poll();
        if (set.contains(p)) {
            set.remove(p);
            if (this.predicate == null || this.predicate.test(p))
                return p;
            else
                return next();
        }
        Stack<Position<E>> stack = new Stack<>();
        for (Position<E> c : tree.children(p)) {
            stack.push(c);
        }
        this.nodeQueue.addFirst(p);
        while (!stack.isEmpty()) {
            this.nodeQueue.addFirst(stack.pop());
        }
        set.add(p);
        return next();
    }

    @Override
    public void remove() {
        throw new RuntimeException("Not yet implemented");
    }

}
