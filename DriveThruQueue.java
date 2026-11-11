import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
/**
 * A generic implementation of a first-in-first-out (FIFO) queue using a linked list.
 */
public class DriveThruQueue<T> {
    /**
     * Represents a single node in the queue.
     */
    private static class Node<T> {
        T data;
        Node<T> next;

        Node(T data) {
            this.data = data;
            this.next = null;
        }
    }
    private int size;
    private Node<T> front;
    private Node<T> rear;
    /**
     * Creates an empty queue.
     */
    public DriveThruQueue() {
        this.front = null;
        this.rear = null;
        this.size = 0;
    }
    /**
     * Adds an element to the end of the queue.
     * @param data the element to be added.
     */
    public void enqueue(T data) {
        if(data == null) {
            throw new IllegalArgumentException("Cannot enqueue null data");
        }
        Node<T> newNode = new Node<>(data);
        if (isEmpty()) {
            this.front = newNode;
            this.rear = newNode;
        } else {
            this.rear.next = newNode;
            this.rear = newNode;
        }
        this.size++;
    }

    /**
     * Removes and returns the element at the front of the queue.
     * @return the element at the front of the queue.
     * @throws NoSuchElementException if the queue is empty.
     */
    public T dequeue() {
        if (isEmpty()) {
            throw new NoSuchElementException("Cannot dequeue from an empty queue");
        }
        T data = this.front.data;
        this.front = this.front.next;
        this.size--;
        if (this.front == null) {
            this.rear = null;
        }
        return data;
    }

    /**
     * Checks if the queue is empty.
     * @return true if the queue is empty, false otherwise.
     */
    public boolean isEmpty() {
        return this.size == 0;
    }

    /**
     * Returns the element at the front of the queue without removing it.
     * @return the element at the front of the queue.
     * @throws NoSuchElementException if the queue is empty.
     */
    public T peek() {
        if (isEmpty()) {
            throw new NoSuchElementException("Cannot peek from an empty queue");
        }
        return this.front.data;
    }

    /**
     * Returns the number of elements in the queue.
     * @return the size of the queue
     */
    public int getSize() {
        return this.size;
    }
/**
 * Checks if the queue contains the specified element.  
 * @param data the element to check for in the queue.
 * @return true if the queue contains the element, false otherwise.
 */
    public boolean contains(T data) {
        Node<T> current = this.front;
        while (current != null) {
            if (current.data.equals(data)) {
                return true;
            }
            current = current.next;
        }
        return false;
    }
    /**
     * Converts the queue to a list of elements.
     * @return a list containing all elements in the queue, in order from front to rear.
     */     
    public List<T> toList() {
        List<T> list = new ArrayList<>();
        Node<T> current = this.front;
        while (current != null) {
            list.add(current.data);
            current = current.next;
        }
        return list;
    }
}