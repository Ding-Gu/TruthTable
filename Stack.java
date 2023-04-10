package algs;

import java.util.Iterator;

public class Stack<Item> implements Iterable {
    private int N = 0;
    private Stack<Item>.Node root;
    public boolean isEmpty() {
        return this.N == 0;
    }
    public int size() {
        return this.N;
    }
    public void push(Item item) {
        Stack<Item>.Node newNode = new Node(this, item);
        newNode.next = this.root;
        this.root = newNode;
        ++this.N;
    }
    public Item pop() {
        Stack<Item>.Node popNode = this.root;
        this.root = this.root.next;
        --this.N;
        return popNode.item;
    }
    public Iterator iterator() {
        return new ReverArrayIterator();
    }
    private class Node {
        Item item;
        Stack<Item>.Node next;
        Node(Stack var1, Item item) {
            this.item = item;
        }
    }

    private class ReverArrayIterator implements Iterator {
        private ReverArrayIterator() {
        }
        public boolean hasNext() {
            return Stack.this.N > 0;
        }
        public Item next() {
            return Stack.this.pop();
        }
    }
}
