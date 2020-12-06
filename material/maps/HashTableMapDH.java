package material.maps;

/**
 * @param <K> The hey
 * @param <V> The stored value
 */
public class HashTableMapDH<K, V> extends AbstractHashTableMap<K, V> {

    public HashTableMapDH(int size) {
        super(size);
    }

    public HashTableMapDH() {
        super();
    }

    public HashTableMapDH(int p, int cap) {
        super(p, cap);
    }

    private final int Q = 7;

    @Override
    protected int offset(K key, int i) {
        return d(key) * i;
    }

    private int d(K key) {
        return Q - (hashValue(key) % Q);
    }
}
