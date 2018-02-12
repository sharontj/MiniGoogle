/*
 * FileName: TermFrequencyTable.java
 * Author: Jin Tang (sharontj@bu.edu)
 * Date: 06/30/2017
 */
package hw6;

import static java.lang.Math.sqrt;

public class TermFrequencyTable {
	String[] bl = { "the", "a", "to", "with", "up" };
	private final int SIZE = 97;

	Node[] T = new Node[SIZE];
	/*
	 * To calculate the similarity of the two documents, you must take the union
	 * of the two word lists A and B (this is the total vocabulary of the two
	 * documents, minus any "black-listed words"---very common words are often
	 * not used to calculate similarity measures) and then calculate the new
	 * term frequency vector of each document with respect to this total list of
	 * words. For example, if document A is
	 * "the man with the hat ran up to the man with the dog" and document B is
	 * "a man with a hat approached a dog and a man", and the black list is
	 * {"the", "a", "to", "with", "up"}, then we have the following word lists
	 * which record how many times each word occurred in each document, and then
	 * compile a list of the total vocabulary (minus black-listed words): A:
	 * (man,2), (hat,1), (dog,1), (ran,1) B: (man,2), (approached,1), (hat,1),
	 * (dog,1)
	 * 
	 * Total: approached, dog, hat, ran, man Now we must calculate the term
	 * frequency vector for each test, indicating how many times each word in
	 * the total list occurs in each of the two documents (of course, some
	 * entries will be 0):
	 * 
	 * A: [0,1,1,1,2] B: [1,1,1,0,2]
	 * 
	 */

	/*
	 * You should write this class using a separate-chaining table with a table
	 * size which is a relatively small prime around size 100 (the articles are
	 * relatively short and you need to store only the vocabulary of two
	 * articles). Your buckets should be composed of Nodes using something
	 * similar to the following:
	 */
	// bucket node
	private class Node {

		String term;
		int[] termFreq = new int[2];
		// this gives the term frequency in each of two documents for this term
		Node next;

		public Node(String term2, int docNum, Node p) {
			term = term2;
			termFreq[docNum]=1;
			next = p;
		}
	}

	private boolean Inbl(String term) {
		for (int i = 0; i < bl.length; i++) {
			if (term.compareTo(bl[i]) == 0) {
				return true;
			}
		}
		return false;

	}

	int hash(String t) {
		char ch[];
		ch = t.toCharArray();
		// int alength = t.length();

		int i, sum;
		for (sum = 0, i = 0; i < t.length(); i++)
			sum += ch[i];

		return sum % SIZE;
	}

	private boolean Member(String term) {
		int h = hash(term);
		return MemberUtil(term, T[h]);
	}

	private boolean MemberUtil(String term, Node p) {
		if (p == null) {
			return false;
		} else if (term.compareTo(p.term) == 0) {
			return true;
		} else {
			return MemberUtil(term, p.next);
		}
	}

	// insert a term from a document docNum (= 0 or 1) into the table;
	// if the term is not already present, add it to the table with a termFreq
	// of 1 for docNum,
	// i.e., if p is the new node added, then p.termFreq[docNum] = 1.
	// If the term IS already there, just increment the appropriate termFreq
	// value.
/*	public void insert(String term, int docNum) {
		if (!Inbl(term)) {
			if (!Member(term)) {

				T[hash(term)] = insertHelper(term, docNum, T[hash(term)]);
			} else {
				T[hash(term)] = increment(term, docNum, T[hash(term)]);
			}
		}

	}
*/
	public void insert(String term, int docNum) { // insert a into the table using the title
		// of a as the hash key
		if (!Inbl(term)) {
			if (!Member(term)) {		
			T[hash(term)] = new Node(term, docNum,T[hash(term)]);
			}
			else{
				T[hash(term)] = increment(term, docNum, T[hash(term)]);
			}
			}
		
	}

	private Node increment(String term, int docNum, Node p) {
		if (p == null) {
			return p;
		} else if (term.compareTo(p.term) == 0) {
			p.termFreq[docNum]+=1;
			return p;
		} else {
			p.next = increment(term, docNum, p.next);
			return p;
		}
	}

/*	private Node insertHelper(String term, int docNum, Node p) {

		if (p == null) {
			return new Node(term, docNum);
		} else if (p.next == null) {
			p.next = new Node(term, docNum);
			return p;

		} else {
			p.next = insertHelper(term, docNum, p.next);
			return p;
		}
	}*/

	// return the cosine similarity of the terms for the two documents stored in
	// this table;
	/*
	 * private int[] total(int docNum){ int[] A = new int[Integer.MAX_VALUE];
	 * int k=0; for(int i=0;i<SIZE; i++){ while(T[i].term != null){ //A[k++] =
	 * T[i].termFreq[docNum]; //T[i]=T[i].next; } } return A; }
	 * 
	 */

	double dotProduct(double a, double b) {
		return a * b;
	}

	double cosineSimilarity() {
		double ab = 0;
		double aa = 0;
		double bb = 0;
		
		for (int i = 0; i < SIZE; i++) {
			for(Node p=T[i]; p!= null;p=p.next){
				aa += dotProduct(p.termFreq[0], p.termFreq[0]);
				bb += dotProduct(p.termFreq[1], p.termFreq[1]);
				ab += dotProduct(p.termFreq[0], p.termFreq[1]);
				
			}
		}
		double cos = ab / (sqrt(aa) * sqrt(bb));
		return cos;

	}
	/*
	 * 
	 * The basic idea here is to put all the terms in two strings (documents)
	 * into the table, and count how the number of occurrences of each term in
	 * each document. The total list of all words is just the total of all words
	 * in the hash table. At the end, you may return the cosine similarity of
	 * the terms contained in it; if you go through the entire table, the list
	 * of all (non-duplicate) terms is the term list, and the list of the term
	 * frequencies contained in termFreq[] contains the term frequency vectors.
	 * 
	 * In the formula above, the sequence Ai is the sequence of integers found
	 * in p.termFreq[0] for all nodes p, and the sequence Bi is the sequence
	 * found in p.termFreq[1].
	 * 
	 * You need to calculate the product of these integers (for the numerator)
	 * and the squares of each (for the denominator).
	 * 
	 * You must therefore use a method for iterating through all nodes in the
	 * table; you do not need to provide an interface to do this, but you will
	 * need to iterate through all the entries (two for loops should do it!).
	 * 
	 * Write your class in a file TermFrequencyTable.java and provide a unit
	 * test that verifies that it works properly. Test three examples, one with
	 * the exact same terms in each document (e.g., "A B" and "A A B B", the
	 * c.s. should be 1.0), one where there is no common term between the two
	 * documents ("A B" and "C D", the c.s. should be 0.0), and also an example
	 * which produces a c.s. between 0 and 1, perhaps "CS112 HW06" and
	 * "CS112 HW06 HW06" which should be 0.9487.
	 * 
	 * 
	 */

	void printTable() {
		for (int i = 0; i < T.length; i++) {
			System.out.print("i=" + i + ": ");
			for (Node p = T[i]; p != null; p = p.next) {
				System.out.print(p.term + "  a1= "+p.termFreq[0] + "  b1= "+p.termFreq[1]);
				
			}
			System.out.println();
		}
	}

	public static void main(String[] args) {

		String[] a = { "A", "B" };
		String[] b = { "A", "A", "B", "B" };
		String[] c ={"C", "D" };
		String[] e={"CS112", "HW06"};
		String[] f={"CS112", "HW06", "HW06"};
		TermFrequencyTable t = new TermFrequencyTable();
	for (int i = 0; i < a.length; i++) {
			t.insert(a[i], 0);
		}
		/*for (int i = 0; i < b.length; i++) {
			t.insert(b[i], 1);
		}*/
		for (int i = 0; i < c.length; i++) {
			t.insert(c[i], 1);
		}
		/*for (int i = 0; i < e.length; i++) {
			t.insert(e[i], 0);
		}
		for (int i = 0; i < f.length; i++) {
			t.insert(f[i], 1);
		}*/
		t.printTable();
		System.out.printf("%f \n", t.cosineSimilarity());
		
	

	}

}
