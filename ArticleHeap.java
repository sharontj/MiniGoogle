/*
 * FileName: ArticleHeap.java
 * Author: Jin Tang (sharontj@bu.edu)
 * Date: 06/30/2017
 */
package hw6;

import java.util.*;


public class ArticleHeap implements Iterator<Article> {

	/*
	 * You must write a file ArticleHeap.java which implements a priority queue
	 * for Articles, ordered by the cosine similarity (which is a double field
	 * in the Article class, and has a method compareCS(...), take a look
	 * there).
	 * 
	 */
	private final int SIZE = 10; // initial length of array

	Article[] A = new Article[SIZE];
	private int next = 0; // limit of elements in array

	// standard resize to avoid overflow

	private void resize() {
		Article[] B = new Article[A.length * 2];
		for (int i = 0; i < A.length; ++i)
			B[i] = A[i];
		A = B;
	}

	// methods to move up and down tree as array

	private static int parent(int i) {
		return (i - 1) / 2;
	}

	private static int lchild(int i) {
		return 2 * i + 1;
	}

	private static int rchild(int i) {
		return 2 * i + 2;
	}

	// standard swap, using indices in array

	private void swap(int i, int j) {
		Article temp = A[i];
		A[i] = A[j];
		A[j] = temp;
	}

	// basic data structure methods

	public boolean isEmpty() {
		return (next == 0);
	}

	public int size() {
		return (next);
	}

	// insert an integer into array at next available location
	// and fix any violations of heap property on path up to root

	public void insert(Article k) {

		if (size() == A.length)
			resize();
		A[next++] = k;

		for (int i = next - 1; i > 0 && A[i].compareCS(A[parent(i)]) > 0; i = parent(i)) {
			swap(i, parent(i));
		}

	}

	// The method getMax() in your class should throw a HeapUnderflowException
	// if you call the method when the heap is empty.
	// You MUST create a Unit Test which verifies that your heap works properly
	// and
	// throws this exception in the case of heap underflow.
	// And you will need to call the getMax() inside of a try-catch block.

	// The method getMax() should return the article in the heap with the
	// largest cosineSimilarity field.
	// You should not need to make any changes to the Article class to get this
	// to work.
	public Article getMax() throws HeapUnderflowException{
		if(isEmpty()){
			throw new HeapUnderflowException();
		}
		else{
			--next;
			swap(0, next); // swap root with last element
			int i = 0;
			int maxChild = maxChild(0);

			// while there is a maximum child and element out of order, swap with
			// max child

			// while(maxChild != -1 && A[i] < A[maxChild]) {
			while (maxChild != -1 && A[i].compareCS(A[maxChild]) < 0) {
				swap(i, maxChild);
				i = maxChild;
				maxChild = maxChild(i);

			}

			return A[next];
		}
		
	}


	// return index of maximum child of i or -1 if i is a leaf node (no
	// children)

	private int maxChild(int i) {
		if (lchild(i) >= next)
			return -1;
		if (rchild(i) >= next)
			return lchild(i);
		// else if(A[lchild(i)] > A[rchild(i)])
		else if (A[lchild(i)].compareCS(A[rchild(i)]) > 0)
			return lchild(i);
		else
			return rchild(i);
	}

	// heapsort: if call this while A is being used, will erase everything in A!

	public  void heapSort(Article[] B) {
		ArticleHeap H = new ArticleHeap();
		for (int i = 0; i < B.length; ++i)
			H.insert(B[i]);
		for (int i = H.next - 1; i >= 0; --i)
			try {
				B[i] = H.getMax();
			} catch (HeapUnderflowException e) {			
				e.printStackTrace();
			}
	}

	// debug methods

	public void printHeap() {  //TODO: gaihuilai
		for (int i = 0; i < next; ++i)
			System.out.print(A[i] + " ");
		System.out.println("\t next = " + next);
	}

	private void printHeapAsTree() {
		printHeapTreeHelper(0, "");
	}

	private void printHeapTreeHelper(int i, String indent) {
		if (i < next) {

			printHeapTreeHelper(rchild(i), indent + "   ");
			System.out.println(indent + A[i]);
			printHeapTreeHelper(lchild(i), indent + "   ");
		}
	}

	// Unit Test

	public static void main(String[] args) {
		ArticleHeap H = new ArticleHeap();

		Article a = new Article("a", "dog is our friend");
		a.putCS(0.8);

		Article b = new Article("b", "cat is also a friend");
		b.putCS(0.2);

		Article c = new Article("c ", "single dog is a special creature");
		c.putCS(0.3);
		
		Article d = new Article("d", "nini is a cat");
		d.putCS(0.7);

		// test basic methods
		Article[] S = { a, b, c};
		System.out.println("test00 getmax() the empty heap\n");
		try {
			H.getMax();
		} catch (HeapUnderflowException e) {
			System.out.println("The Heap is empty!");
		}
		
		for (int i = 0; i < S.length; ++i) {
			H.insert(S[i]);
		}

		System.out.println();
		System.out.println("test01 insert\n\n");
		H.printHeap();	
		System.out.println();	
		H.printHeapAsTree();
		
		System.out.println();
		System.out.println("test02 getmax() remove dog\n");
		try {
			System.out.println(H.getMax());
		} catch (HeapUnderflowException e) {
			System.out.println("The Heap is empty!");
		}
		
	
		System.out.println("test03 heap after getmax()\n");
		System.out.println();	
		H.printHeap();
		System.out.println();
		H.printHeapAsTree();
		
		System.out.println("test04:insert d, get d out, insert d again \n");
		H.insert(d);
		try {
			System.out.println(H.getMax());
		} catch (HeapUnderflowException e) {
			System.out.println("The Heap is empty!");
		}
		H.insert(d); 
		System.out.println();
		 H.printHeap(); 
		 System.out.println(); 
		 H.printHeapAsTree();
		  System.out.println();
		 System.out.println();
		 
		 System.out.println("test05:heapsort: a c b \n");
		/* Article[] K = { a, b, c,d };
		 ArticleHeap T = new ArticleHeap();
		 T.heapSort(K);
		 T.printHeap();
		 T.printHeapAsTree();
		 System.out.println();
		 */
		 H.heapSort(S);
		 H.printHeap();

	}

	@Override
	public boolean hasNext() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Article next() {
		// TODO Auto-generated method stub
		return null;
	}
}

class HeapUnderflowException extends Exception {
}