/*
 * FileName: ArticleTable.java
 * Author: Jin Tang (sharontj@bu.edu)
 * Date: 06/30/2017
 */
package hw6;
import java.util.*;
import java.io.*;

public class ArticleTable implements Iterable<Article> {

	private final int SIZE = 2503;

	Node[] T = new Node[SIZE];

	class Node {

		String title;
		Article art;
		Node next;

		public Node(String t, Article a, Node p) {
			title = t;
			art = a;
			next = p;
		}
	}
	
	public void initialize(Article[] a) {	
		      for(int i = 0; i < a.length; ++i) {
		    	  insert(a[i]);
		      }  	   
	}

	int hash(String t) {
		char ch[];
		ch = t.toCharArray();
		int alength = t.length();

		int i, sum;
		for (sum = 0, i = 0; i < t.length(); i++)
			sum += ch[i];

		return sum % SIZE;
	}

	public void insert(Article a) { // insert a into the table using the title
									// of a as the hash key
		if (!member(a)) {
			T[hash(a.getTitle())] = new Node(a.getTitle(), a, T[hash(a.getTitle())]);
		}
		else{
			System.out.println("already exist!");
		}
	}

 // inserts a new node into the list, and returns the result

	public void delete(String title) {
		if(lookup(title)==null){
			System.out.println("the article does not exist");
		}
		deleteUtil(title, T[hash(title)]);

	}

	private Node deleteUtil(String title, Node p) {
		if (p == null) {
			return p;
		} else if (title.compareTo(p.title)==0){
			return p.next;
			}
		else {
			p.next = deleteUtil(title, p.next);
			return p;
		}

	}

	public boolean member(Article a) {
		return (lookup(a.getTitle()) != null);
	}

	public Article lookup(String title) { 

		return lookupUtil(title, T[hash(title)]);
	}

	private Article lookupUtil(String title, Node p) {
		if (p == null) {
			return null;
		} else if (title.compareTo(p.title) == 0) {
			return p.art;
		} else {
			return lookupUtil(title, p.next);
		}
		

	}
	
	void printTable(){
		for(int i=0;i<T.length;i++){
			System.out.print("i=" + i +": ");
			for(Node p=T[i];p!=null;p=p.next){
				System.out.print(p.art.getTitle()+" ");
			
			}
			System.out.println();
		}
	}

	

	public Iterator<Article> iterator() {
		return new It();
	}

	private class It implements Iterator<Article> {

		private Node cursor; // where in the enumeration we are
		private int c = 0;
		Node[] A = T;
		
		
		public It(){
			while(c<SIZE &&  A[c]== null){
				++c;
			}
			if(c<SIZE){
				cursor=A[c];
			}
		}

		public boolean hasNext() {
			
			return c != SIZE;
		}

		public Article next() {
			Article article = cursor.art;
			cursor = cursor.next;
			if(cursor == null){
			c++;
			while(c<SIZE &&  A[c]== null){
				++c;
			}
			if(c<SIZE){
				cursor=A[c];
			}	
			}
			return article;
		}

	}



	/////////

	public static void main(String[] args) {

		// String dbPath = "articles/";
		//String dbPath = "/Users/sharontj1/Desktop/CS112/Eclipse/hw6/src/hw6/articles/";

		//DatabaseIterator db = new DatabaseIterator(dbPath);
		ArticleTable a = new ArticleTable();
		a.insert(new Article("dog","dog is our friend"));
		a.insert(new Article("dog2","dog is our friend"));
		a.insert(new Article("dog3","dog is our friend"));
		a.insert(new Article("dog4","dog is our friend"));
		a.insert(new Article("dog51","dog is our friend"));
		a.insert(new Article("dog21","dog is our friend"));
		a.insert(new Article("dog31","dog is our friend"));
		a.insert(new Article("dog41","dog is our friend"));
		a.insert(new Article("dog5","dog is our friend"));
		
		//a.printTable();
		//System.out.print(a.lookup("dog"));
		//a.insert(new Article("dog","dog is our friend"));
		//a.insert(new Article("cat","cat is our friend"));
		Iterator<Article> I = a.iterator();
		while(I.hasNext()){
			System.out.println(I.next());
		}
		//Article[] ar=new Article[3];
		//a.initialize(ar);
		

	}

	

}