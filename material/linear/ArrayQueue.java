package material.linear;

public class ArrayQueue<E> implements Queue<E> {
    private Object[] queue;
    private int firstPosition;
    private int lastPosition;
    private int MAX_SIZE;
    private int size;

    public ArrayQueue() {
        this.MAX_SIZE = 16;
        this.firstPosition = -1;
        this.lastPosition = -1;
        this.queue = new Object[16];
        this.size = 0;
    }

    @Override
    public int size() {
//        return this.lastPosition + 1;
        return this.size;
//        return Math.abs(this.lastPosition - this.firstPosition);
    }

    @Override
    public boolean isEmpty() {
        return this.size() == 0;
    }

    @Override
    public E front() throws RuntimeException {
        return (E) this.queue[this.firstPosition];
    }

    @Override
    public void enqueue(E element) {
        if (this.size() >= this.MAX_SIZE) {
            this.resize();
        }
        if (this.lastPosition + 1 > this.MAX_SIZE) {
            this.lastPosition = 0;
        } else {
            this.lastPosition++;
        }

        this.queue[this.lastPosition] = element;

        if (this.firstPosition == -1) {
            this.firstPosition++;
        }
        this.size++;
    }

    @Override
    public E dequeue() throws RuntimeException {
        if (this.size() == 0) {
            throw new RuntimeException("Queue is empty");
        }
        E dequeued = (E) this.queue[this.firstPosition];
        if (--this.size == 0) {
            this.firstPosition = -1;
            this.lastPosition = -1;
        } else {
            if (this.firstPosition + 1 >= this.MAX_SIZE) {
                this.firstPosition = 0;
            } else {
                this.firstPosition++;
            }
        }
        return dequeued;
    }

    private void resize() {
        int newSize = this.MAX_SIZE * 2;
        Object[] newQueue = new Object[newSize];

        /*Reordering the array*/
        boolean linear = this.lastPosition - this.firstPosition > 0;
        if (linear) {
            System.arraycopy(this.queue, this.firstPosition, newQueue, 0, this.lastPosition + 1);
        } else {
            System.arraycopy(this.queue, this.firstPosition, newQueue, 0, this.MAX_SIZE);
            System.arraycopy(this.queue, this.MAX_SIZE + 1, newQueue, 0, this.lastPosition);
        }
        this.firstPosition = 0;
        this.lastPosition = this.MAX_SIZE - 1;

        /*Not reordering the array
        System.arraycopy(this.queue, this.lastPosition + 1, newQueue, 0, this.MAX_SIZE);*/

        this.MAX_SIZE = newSize;
        this.queue = newQueue;
    }
}
