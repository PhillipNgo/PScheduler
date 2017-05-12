package ds;

import java.util.Iterator;

/**
 * LinkedList represents a list of objects
 * 
 * @author Phillip Ngo
 * @param <T> the type of object to be put into the list
 */
public class LinkedList<T> implements Iterable<T> {
    
    //Fields 
    
    private Node firstNode;
    private int numberOfEntries;

    //Constructors
    
    /**
     * Default constructor instantiates fields
     */
    public LinkedList() {
        clear();
    }

    /**
     * fills the list with the given array
     * 
     * @param arr the array to input
     */
    public LinkedList(T[] arr) throws Exception {
        clear();
        for (T a : arr) {
            this.add(a);
        }
    }
    
    //Getter Methods 
    
    /**
     * Returns the data at the specified location in the list
     * 
     * @param index the index of the entry to be returned
     * @return the data of the entry
     */
    public T get(int index) {
        if (index > numberOfEntries - 1 || index < 0) {
            throw new IndexOutOfBoundsException();
        }
        Node currNode = firstNode;
        for (int i = 0; i < index; i++) {
            currNode = currNode.next;
        }
        return currNode.data;
    }
    
    /** 
     * Gets the number of entries currently in this bag.
     * 
     * @return  The integer number of entries currently in the bag. 
     */
    public int size() {
        return numberOfEntries;
    } 
    
    //Methods
    
    /** 
     * Adds a new entry to this list.
     * 
     * @param newEntry  The object to be added as a new entry.
     * @return  True. 
     */
    public void add(T newEntry) throws Exception {
        Node newNode = new Node(newEntry);
        newNode.next = firstNode;        
        firstNode = newNode;       
        numberOfEntries++;
    }
    
    /** 
     * Retrieves all entries that are in this bag.
     * 
     * @return  A newly allocated array of all the entries in this bag. 
     */
    public T[] toArray() {
        @SuppressWarnings("unchecked")
        T[] arr = (T[])new Object[numberOfEntries]; 
        Node currNode = firstNode;
        for (int i = 0; i < numberOfEntries; i++) {
            arr[i] = currNode.data;
            currNode = currNode.next;
        }
        return arr;
    } 
    
    /** 
     * Sees whether this bag is empty.
     *
     * @return  True if the bag is empty, or false if not. 
     */
    public boolean isEmpty() {
        return numberOfEntries == 0;
    }   

    /** 
     * Removes the specified entry from this bag.
     * 
     * @param obj entry to be removed
     * @return  the removed entry
     */
    public T remove(T obj) {
        int index = indexOf(obj);
        if (index < 0) {
            return null;
        }
        else {
            T data;
            if (obj.equals(firstNode.data)) {
                data = firstNode.data;
                firstNode = firstNode.next;
            }
            else {
                Node currNode = firstNode;
                for (int i = 0; i < index - 1; i++) {
                    currNode = currNode.next;
                }
                data = currNode.next.data;
                currNode.next = currNode.next.next;
            }
            numberOfEntries--;
            return data;
        }
    } 
    
    /**
     * Removes all entries from the list
     */
    public void clear() {
        firstNode = null;
        numberOfEntries = 0;
    }
    
    /**
     * Creates a copy 
     * 
     * @param list the LinkedList to be copied
     * @return a copied LinkList
     * @throws Exception 
     */
    public LinkedList<T> createCopy() throws Exception {
        LinkedList<T> copy = new LinkedList<T>();
        for (int i = numberOfEntries - 1; i >= 0; i--) {
            copy.add(this.get(i));
        }
        return copy;
    }
    
    /**
     * The position in the list of an object
     * 
     * @param obj entry to be searched
     * @return the index of the item, -1 if not found
     */
    public int indexOf(T obj) {
        if (obj == null) {
            return -1;
        }
        Node currNode = firstNode;
        for (int i = 0; i < numberOfEntries; i++) {
            if (currNode.data.equals(obj)) {
                return i;
            }
            currNode = currNode.next;
        }
        return -1;
    } 
    
    /**
     * Invokes the toSting of all objects in the list
     * 
     * @return String with all toStrings
     */ 
    @Override
    public String toString() {
        String s = "";
        for (int i = 0; i < numberOfEntries; i++) {
            s += this.get(i).toString(); 
            if (i != numberOfEntries - 1) {
                s += "\r\n";
            }
        }
        return s;
    }
    
    //Private Node Class 
    /**
     * Node Class
     */
    private class Node {
        
        private T data; 
        private Node next; 
        
        /**
         * Constructor for Node, that takes data
         * 
         * @param dataPortion data to be stored in Node
         */
        private Node(T dataPortion) {
            this(dataPortion, null);    
        }
        
        /**
         * Constructor that takes data and a next node
         * 
         * @param dataPortion data to be stored
         * @param nextNode the next node
         */
        private Node(T dataPortion, Node nextNode) {
            data = dataPortion;
            next = nextNode;    
        }
    }
    
    /**
     * Returns a list iterator
     * @return an iterator at the beginning of the list
     */
    @Override
    public Iterator<T> iterator() {
        return new ListIterator();
    }
    
    /**
     * Iterator inner class for the LinkedList
     */
    private class ListIterator implements Iterator<T> {
        
        Node currNode;
        Node prevNode;
        
        /**
         * Constructor that stores the current and previous node
         */
        public ListIterator() {
            currNode = null;
            prevNode = null;
        }
        
        /**
         * Checks if there is another element after currNode
         */
        @Override
        public boolean hasNext() {
            if (firstNode == null) {
                return false;
            }
            else if (currNode == null) {
                if (prevNode == null) {
                    return true;
                }
                return false;
            }
            else {
                return currNode.next != null;
            }
        }
        
        /**
         * Moves the iterator one step up and returns the value passed over
         */
        @Override
        public T next() {
            if (currNode == null && prevNode == null && firstNode == null) {
                return null;
            }
            else if (currNode == null && firstNode != null) {
                currNode = firstNode;
            }
            else {
                if (currNode != prevNode) {
                    prevNode = currNode;
                }
                currNode = currNode.next;             
            }
            return currNode.data;
        }
        
        /**
         * Removes the last element passed over
         */
        @Override
        public void remove() {
            if (!isEmpty() && currNode != null && currNode != prevNode) {
                if (currNode == firstNode) {
                    firstNode = currNode.next;
                    currNode.next = null;
                    currNode = null;
                }
                else {
                    prevNode.next = currNode.next;
                    currNode.next = null;
                    currNode = prevNode;
                }
                numberOfEntries--;
            }
        }
    }
} 
