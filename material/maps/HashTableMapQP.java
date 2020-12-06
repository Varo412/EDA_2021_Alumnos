package material.maps;

/**
 * @param <K> The hey
 * @param <V> The stored value
 */
public class HashTableMapQP<K, V> extends AbstractHashTableMap<K, V> {

    public HashTableMapQP(int size) {
        super(size);
    }

    public HashTableMapQP() {
        super();
    }

    public HashTableMapQP(int p, int cap) {
        super(p, cap);
    }

    private final int C1 = 3;
    private final int C2 = 7;

    @Override
    protected int offset(K key, int i) {
        return C1 * i + C2 * i * i;
    }

}
