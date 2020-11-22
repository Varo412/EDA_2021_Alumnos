package material.linear;

import material.Position;

public class LinkedQueue<E> implements Queue<E> {

    private static class DNode<E> implements Position<E> {
        private E elem;
        private DNode<E> next;

        public DNode(E elem, DNode<E> next) {
            this.elem = elem;
            this.next = next;
        }

        @Override
        public E getElement() {
            return elem;
        }

        public DNode<E> getNext() {
            return next;
        }

        public void setNext(DNode<E> next) {
            this.next = next;
        }
    }

    private DNode<E> first;
    private DNode<E> last;
    private int size;

    public LinkedQueue() {
        this.first = null;
        this.last = null;
        this.size = 0;
    }

    @Override
    public int size() {
        return this.size;
    }

    @Override
    public boolean isEmpty() {
        return this.size() == 0;
    }

    @Override
    public E front() {
        if (this.size() == 0) {
            throw new RuntimeException("Queue is empty");
        }
        return this.first.getElement();
    }

    @Override
    public void enqueue(E element) {
        DNode<E> node = new DNode<>(element, null);
        if (this.size() > 0) {
            this.last.setNext(node);
        } else {
            this.first = node;
        }

        this.last = node;
        size++;
    }

    @Override
    public E dequeue() {
        if (this.size() == 0) {
            throw new RuntimeException("Queue is empty");
        }
        E deleted = this.first.getElement();
        this.first = this.first.getNext();
        if (--this.size == 0)
            this.last = null;
        return deleted;
    }

}
