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
public class PreorderIterator<E> implements Iterator<Position<E>> {
    //raiz, izq y der

    private ArrayDeque<Position<E>> nodeQueue;
    private Tree<E> tree;
    private Predicate<Position<E>> predicate;


    public PreorderIterator(Tree<E> tree) {
        nodeQueue = new ArrayDeque<>();
        this.tree = tree;
        this.predicate = null;
        if (tree.root() != null) {
            nodeQueue.add(tree.root());
        }
    }


    public PreorderIterator(Tree<E> tree, Position<E> start) {
        nodeQueue = new ArrayDeque<>();
        this.tree = tree;
        nodeQueue.add(start);
        this.predicate = null;
    }

    public PreorderIterator(Tree<E> tree, Position<E> start, Predicate<Position<E>> predicate) {
        nodeQueue = new ArrayDeque<>();
        this.tree = tree;
        this.predicate = predicate;
        nodeQueue.add(start);
    }


    @Override
    public boolean hasNext() {
        return nodeQueue.size() != 0;
    }

    @Override
    public Position<E> next() {
        Position<E> node = nodeQueue.poll();
        Stack<Position<E>> stack = new Stack<>();
        for (Position<E> n : tree.children(node)) {
            if (this.predicate == null || this.predicate.test(n))
                stack.push(n);
        }
        while (!stack.isEmpty()) {
            nodeQueue.addFirst(stack.pop());
        }
        return node;
    }

    private void lookForward() {
        throw new RuntimeException("Not yet implemented");
    }

    private void pushChildrenInReverseOrder(Tree<E> tree, Position<E> pop) {
        throw new RuntimeException("Not yet implemented");
    }

}
