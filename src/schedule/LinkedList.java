package schedule;

/**
 * LinkedList represents a list of generic objects
 * 
 * @author Phillip Ngo
 * @version 1.0
 * 
 * @param <T> the class to be put into the list
 */
public final class LinkedList<T> {
    
    //Fields -----------------------------------------------------------------------------
    
    private Node firstNode;
    private int numberOfEntries;

    //Constructors -----------------------------------------------------------------------
    
    /**
     * Default constructor instantiates fields
     */
    public LinkedList() {
        firstNode = null;
        numberOfEntries = 0;
    }

    //Getter Methods ---------------------------------------------------------------------
    
    /**
     * Returns the data at the specified location in the list
     * 
     * @param index the index of the entry to be returned
     * @return the data of the entry
     */
    public T get(int index) {
        if (index > numberOfEntries || index < 0) {
            return null;
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
    
    //Methods ----------------------------------------------------------------------------
    
    /** 
     * Adds a new entry to this list.
     * 
     * @param newEntry  The object to be added as a new entry.
     * @return  True. 
     */
    public void add(T newEntry) {
        if (newEntry == null) {
            throw new IllegalArgumentException();
        }
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
        if (contains(obj) < 0) {
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
                for (int i = 0; i < contains(obj) - 1; i++) {
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
     */
    public LinkedList<T> createCopy() {
        LinkedList<T> copy = new LinkedList<T>();
        for (int i = numberOfEntries - 1; i >= 0; i--) {
            copy.add(this.get(i));
        }
        return copy;
    }
    
    /**
     * Checks if the specified entry is in the list
     * 
     * @param obj entry to be searched
     * @return the index of the item, -1 if not found
     */
    public int contains(T obj) {
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
    
    //Private Class -------------------------------------------------------------------------
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
} 
