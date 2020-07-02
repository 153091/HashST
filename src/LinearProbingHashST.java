/**
 *  % java LinearProbingHashST.java < input.txt
 Note: LinearProbingHashST.java uses unchecked or unsafe operations.
 Note: Recompile with -Xlint:unchecked for details.
 Size of ST is 10

 A 8
 C 4
 E 12
 H 5
 L 11
 M 9
 P 10
 R 3
 S 0
 X 7
 */


import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

import java.util.NoSuchElementException;

public class LinearProbingHashST<Key, Value> {

    private int initcap = 4; // start capacity of arrays

    private int n; // number of key-value pairs
    private int m; // size of arrays
    private Key[] keys; // array for keys
    private Value[] vals; // array for values

    // with given capacity
    public LinearProbingHashST(int capacity) {
        m = capacity;
        keys = (Key[]) new Object[m]; // no generic array creation
        vals = (Value[]) new Object[m]; // no generic array creation
    }

    // resizing capacity
    public LinearProbingHashST() {
        m = initcap;
        keys = (Key[]) new Object[m]; // no generic array creation
        vals = (Value[]) new Object[m]; // no generic array creation
    }

    // hashCode to index of array
    private int hash(Key key) {
        return (key.hashCode() & 0x7fffffff) % m;
    }

    // resize for arrays
    private void resize(int capacity) {
        LinearProbingHashST<Key, Value> temp = new LinearProbingHashST<Key, Value>(capacity);

        for (int i = 0; i < m; i++)
            if (keys[i] != null)
                temp.put(keys[i], vals[i]);
        keys = temp.keys;
        vals = temp.vals;
        m = temp.m;

    }

    // number of key-value pairs
    public int size() {
        return n;
    }

    // is array empty?
    public boolean isEmpty() {
        return n == 0;
    }

    // get value of given key
    public Value get(Key key) {
        if (key == null) throw new IllegalArgumentException("argument to get() is null");

        for (int i = hash(key); keys[i] != null; i = (i + 1) % m)
            if (key.equals(keys[i])) return vals[i];

        return null;

    }

    // is there value paired with given key?
    public boolean contains(Key key) {
        if (key == null) throw new IllegalArgumentException("argument to contains() is null");

        return get(key) != null;
    }

    // insert key-value pair
    public void put(Key key, Value val) {
        if (key == null) throw new IllegalArgumentException("argument to pey() is null");
        if (val == null) {
            delete(key); return;
        }

        if (n >= m/2) resize(m*2);

        int i;
        if (!contains(key)) n++; // increment of number of key-value pairs
        for (i = hash(key); keys[i] != null; i = (i+1) % m) {
            if (key.equals(keys[i]))
            break;
        }
        keys[i] = key;
        vals[i] = val;

    }

    // delete key-value pair
    public void delete(Key key) {
        if (isEmpty()) throw new NoSuchElementException("ST Underflow");
        if (key == null) throw new IllegalArgumentException("argument to delete() is null");

        if (!contains(key)) return;

        int i = hash(key);

        // find index for that key
        while (!key.equals(keys[i]))
            i = (i + 1) % m;

        // null that key and value
        keys[i] = null;
        vals[i] = null;

        // move to next index
        i = (i + 1) % m;

        while (keys[i] != null) {
            Key reHashkey = keys[i];
            Value reHashVal = vals[i];
            keys[i] = null;
            vals[i] = null;
            n--;                        // decrement of number of key-value pairs (before put())
            put(reHashkey, reHashVal);

            i = (i + 1) % m;
        }

        n--; // decrement of number of key-value pairs

        // halves size of array if it's 12.5% full or less
        if (n > 0 && n <= m/8) resize(m/2);

    }

    // Iterator
    public Iterable<Key> keys() {
        Queue<Key> q = new Queue<>();
        for (int i = 0; i < m; i++)
            if (keys[i] != null) q.enqueue(keys[i]);
        return q;
    }




    public static void main(String[] args) {

        LinearProbingHashST<String, Integer> st = new LinearProbingHashST<>();
        for (int i = 0; !StdIn.isEmpty(); i++) {
            String k = StdIn.readString();
            st.put(k, i);
        }
        StdOut.println("Size of ST is " +st.size());
        StdOut.println();

        for (String s : st.keys())
            StdOut.println(s + " " + st.get(s));
    }
}
