/**java SeparateChainingHashST.java < input.txt
 Note: SeparateChainingHashST.java uses unchecked or unsafe operations.
 Note: Recompile with -Xlint:unchecked for details.
 L 11
 P 10
 X 7
 H 5
 M 9
 A 8
 E 12
 R 3
 C 4
 S 0
 */

import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

import java.util.NoSuchElementException;

public class SeparateChainingHashST<Key, Value> {

    private final int initcap = 4; // start with this size of array

    private int n; // number of key-value pairs
    private int m; // array of linked-lists symbol table
    private Node[] st;

    // Node class
    private static class Node {
        private Object key;
        private Object val;
        private Node next;

        public Node(Object key, Object val, Node next) {
            this.key = key;
            this.val = val;
            this.next = next;
        }
    }

    // with resize
    public SeparateChainingHashST() {
        m = initcap;
        st = new Node[m];
    }

    // with capacity from client
    public SeparateChainingHashST(int capacity) {
        m = capacity;
        st = new Node[m];
    }

    // return index of array [0..m-1]
    private int hash(Key key) {
        return (key.hashCode() & 0x7fffffff) % m;
    }

    // return number of key-value pairs
    public int size() {
        return n;
    }

    // is array empty?
    public boolean isEmpty() {
        return n == 0;
    }

    // resize for array
    private void resize(int capacity) {
        SeparateChainingHashST<Key, Value> temp = new SeparateChainingHashST<>(capacity);
        for (int i = 0; i<m; i++ )
            for (Node x = st[i]; x != null; x = x.next)
                temp.put((Key) x.key, (Value) x.val);
        this.n  = temp.n;
        this.m  = temp.m;
        this.st = temp.st;
    }

    // return Value of given key
    public Value get(Key key) {
        if (key == null) throw new IllegalArgumentException("argument to get() is null");

        int i = hash(key);

        for (Node x = st[i]; x != null; x = x.next)
            if (key. equals(x.key))
                return (Value) x.val;
            return null;
    }

    // is here a value paired with given key?
    public boolean contains(Key key) {
        if (key == null) throw new IllegalArgumentException("argument to contains() is null");

        return get(key) != null;
    }


    // Inserts key-value pair into the symbol table
    public void put(Key key, Value val) {
        if (key == null) throw new IllegalArgumentException("first argument to put() is null");

        if (val == null) {
            delete(key);
            return;
        }

        if (n >= 10*m) resize(2*m);

        int i = hash(key);
        if (!contains(key)) n++; // increment of number of key-value pairs
        for (Node x = st[i]; x != null; x = x.next)
            if (key.equals(x.key)) {
                x.val = val;
                return;
            }
        st[i] = new Node(key, val, st[i]);
    }

    // delete Key-value pair with given key
    public void delete(Key key) {
        if (isEmpty()) throw new NoSuchElementException("ST underflow");
        if (key == null) throw new IllegalArgumentException("argument to delete() is null");

        int i = hash(key);
        if (contains(key)) n--; // decrement of number of key-value pairs
        st[i] = delete(key, st[i]);

        if (m > initcap && n <= 2*m) resize(m/2);
    }

    private Node delete(Key key, Node x) {
        if (x == null) return null;

        if (!x.key.equals(key)) x.next = delete(key, x.next);
        else return x.next;

        return x;
    }

    // Iterable keys
    public Iterable<Key> keys() {
        Queue<Key> keys = new Queue<>();
        for (int i = 0; i < m; i++)
            for (Node x = st[i]; x != null; x = x.next)
                keys.enqueue((Key) x.key);
        return keys;
    }

    public static void main(String[] args) {
	SeparateChainingHashST<String, Integer> hash = new SeparateChainingHashST<String, Integer>();
	for (int i = 0; !StdIn.isEmpty(); i++) {
	    String key = StdIn.readString();
	    hash.put(key, i);
    }

	for (String k : hash.keys())
        StdOut.println(k+ " " + hash.get(k));
    }
}