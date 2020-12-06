package material.maps;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Separate chaining table implementation of hash tables. Note that all
 * "matching" is based on the equals method.
 *
 * @author A. Duarte, J. Vélez, J. Sánchez-Oro, Sergio Pérez
 */
public class HashTableMapSC<K, V> implements Map<K, V> {

    private class HashEntry<T, U> implements Entry<T, U> {

        protected T key;
        protected U value;

        public HashEntry(T k, U v) {
            this.key = k;
            this.value = v;
        }

        @Override
        public U getValue() {
            return this.value;
        }

        @Override
        public T getKey() {
            return this.key;
        }

        public U setValue(U val) {
            U old = this.value;
            this.value = val;
            return old;
        }

        @Override
        public boolean equals(Object o) {
            throw new RuntimeException("Not yet implemented");
        }

        /**
         * Entry visualization.
         */
        @Override
        public String toString() {
            return "(" + key + "," + value + ")";
        }
    }

    private class HashTableMapIterator<T, U> implements Iterator<Entry<T, U>> {

        private Queue<HashEntry<T, U>> queue;
//        private final int n;

        public HashTableMapIterator(List<HashEntry<T, U>>[] map, int numElems) {
//            n = numElems;
            this.queue = new LinkedList();
            for (List<HashEntry<T, U>> e : map) {
                queue.addAll(e);
            }
        }

        @Override
        public boolean hasNext() {
            return this.queue.size() > 0;
        }

        @Override
        public Entry<T, U> next() {
            return this.queue.remove();
        }

        @Override
        public void remove() {
            //NO ES NECESARIO IMPLEMENTARLO
            throw new UnsupportedOperationException("Not implemented.");

        }

        /**
         * Returns the index of the next position starting starting from a given index.
         * (if the parameter is already a valid position then does nothing)
         */
        private int goToNextBucket(int i) {
            //TODO
            throw new RuntimeException("Not yet implemented");
        }
    }

    private class HashTableMapKeyIterator<T, U> implements Iterator<T> {
        public HashTableMapIterator<T, U> it;

        public HashTableMapKeyIterator(HashTableMapIterator<T, U> it) {
            this.it = it;
        }

        @Override
        public T next() {
            return it.next().getKey();
        }

        @Override
        public boolean hasNext() {
            return it.hasNext();
        }

        @Override
        public void remove() {
            //NO ES NECESARIO IMPLEMENTARLO
            throw new RuntimeException("Not yet implemented");
        }
    }

    private class HashTableMapValueIterator<T, U> implements Iterator<U> {
        public HashTableMapIterator<T, U> it;

        public HashTableMapValueIterator(HashTableMapIterator<T, U> it) {
            this.it = it;
        }

        @Override
        public U next() {
            return it.next().getValue();
        }

        @Override
        public boolean hasNext() {
            return it.hasNext();
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException("Not implemented.");
        }
    }

    /**
     * An element position has two parameters: the first one is the position in the array of the allocated element, and
     * the second one is the element position in the inner list.
     *
     * @author Álvaro Rivas
     */
    protected class HashEntryIndex {
        int index[];
        boolean found;

        public HashEntryIndex(int index[], boolean f) {
            this.index = index;
            this.found = f;
        }

        //Easy visualization
        @Override
        public String toString() {
            return "(" + this.index + ", " + this.found + ")";
        }
    }


    private ArrayList<HashEntry<K, V>> bucket[];
    private int prime;
    private int capacity;
    private int n;
    protected long scale, shift; // the shift and scaling factors
    protected final HashEntry<K, V> AVAILABLE = new HashEntry<>(null, null);

    /**
     * Creates a hash table with prime factor 109345121 and capacity 1000.
     */
    public HashTableMapSC() {
        this.capacity = 100;
        this.prime = 109345121;
        this.n = 0;
        this.bucket = new ArrayList[capacity];
        Random rand = new Random();
        this.scale = rand.nextInt(prime - 1) + 1;
        this.shift = rand.nextInt(prime);
    }

    /**
     * Creates a hash table with prime factor 109345121 and given capacity.
     *
     * @param cap initial capacity
     */
    public HashTableMapSC(int cap) {
        this.capacity = cap;
        this.prime = 109345121;
        this.n = 0;
        this.bucket = new ArrayList[capacity];
        Random rand = new Random();
        this.scale = rand.nextInt(prime - 1) + 1;
        this.shift = rand.nextInt(prime);
    }

    /**
     * Creates a hash table with the given prime factor and capacity.
     *
     * @param p   prime number
     * @param cap initial capacity
     */
    public HashTableMapSC(int p, int cap) {
        this.capacity = cap;
        this.prime = p;
        this.n = 0;
        this.bucket = new ArrayList[capacity];
        Random rand = new Random();
        this.scale = rand.nextInt(prime - 1) + 1;
        this.shift = rand.nextInt(prime);
    }

    /**
     * Hash function applying MAD method to default hash code.
     *
     * @param key Key
     * @return the hash value
     */
    protected int hashValue(K key) {
        return (int) (Math.abs(key.hashCode() * this.scale + this.shift) % this.prime) % this.capacity;
    }


    @Override
    public int size() {
        return this.n;
    }

    @Override
    public boolean isEmpty() {
        return this.n == 0;
    }

    @Override
    public V get(K key) {
        HashEntryIndex i = findEntry(key); // helper method for finding a key
        if (!i.found) {
            return null; // there is no value for this key, so return null
        }
        return bucket[i.index[0]].get(i.index[1]).getValue(); // return the found value in this case
    }

    @Override
    public V put(K key, V value) {
        HashEntryIndex i = findEntry(key);
        V old = null;
        if (i.found)
            return bucket[i.index[0]].get(i.index[1]).getValue();
        if (this.n > this.capacity / 2) {
            this.rehash(capacity * 2);
            i = findEntry(key);
        }
        bucket[i.index[0]].add(new HashEntry<>(key, value));
        n++;
        return null;
    }

    @Override
    public V remove(K key) {
        HashEntryIndex i = this.findEntry(key);
        if (i.found) {
            n--;
            return bucket[i.index[0]].remove(i.index[1]).getValue();
        }
        return null;
    }


    @Override
    public Iterator<Entry<K, V>> iterator() {
        return new HashTableMapIterator<K, V>(this.bucket, n);

    }

    @Override
    public Iterable<K> keys() {
        return Arrays.stream(this.bucket).flatMap(Collection::stream).map(HashEntry::getKey).collect(Collectors.toList());


//        LinkedList<K> l = new LinkedList<>();
//        for (List<HashEntry<K, V>> e : this.bucket)
//            l.addAll(e.stream().map(HashEntry::getKey).collect(Collectors.toList()));
//        return l;
    }

    @Override
    public Iterable<V> values() {
        LinkedList<V> l = new LinkedList<>();
        for (List<HashEntry<K, V>> e : this.bucket)
            l.addAll(e.stream().map(HashEntry::getValue).collect(Collectors.toList()));
        return l;
    }

    @Override
    public Iterable<Entry<K, V>> entries() {
        LinkedList<Entry<K, V>> l = new LinkedList<>();
        for (List<HashEntry<K, V>> e : this.bucket)
            l.addAll(e);
        return l;
    }

    /**
     * Determines whether a key is valid.
     *
     * @param k Key
     */
    protected void checkKey(K k) {
        if (k == null) throw new IllegalStateException("Invalid key: null.");
    }


    /**
     * Increase/reduce the size of the hash table and rehashes all the entries.
     */
    protected void rehash(int newCap) {
        this.capacity = newCap;

        //TODO: ¿Por qué los cambiamos?
        Random rand = new Random();
        scale = rand.nextInt(prime - 1) + 1;
        shift = rand.nextInt(prime);

        ArrayList<HashEntry<K, V>>[] resized = new ArrayList[newCap];
        Iterator<Entry<K, V>> it = this.iterator();
        while (it.hasNext()) {
            HashEntry<K, V> next = (HashEntry<K, V>) it.next();
            int index = this.hashValue(next.getKey());
            resized[index].add(next);
        }
        this.bucket = resized;
    }


    /**
     * Searches for the element's entry by its key
     *
     * @param k Key
     * @return the position required
     */
    private HashEntryIndex findEntry(K k) {
        checkKey(k);
        int indexBucket = hashValue(k);
        int indexList = 0;

        while (bucket[indexBucket].get(indexList).getKey() != k && indexList < bucket[indexBucket].size()) {
            if (bucket[indexBucket].get(indexList).getKey() != k)
                return new HashEntryIndex(new int[]{indexBucket, indexList}, true);
            indexList++;
        }
        return new HashEntryIndex(new int[]{indexBucket, indexList}, false);
    }
}
