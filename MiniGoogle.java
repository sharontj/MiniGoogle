/*
 * FileName: Minipedia.java
 *
 * A client program that uses the DatabaseIterator
 * and Article classes, along with additional data
 * structures, to allow a user to create, modify
 * and interact with a encyclopedia database.
 *
 * Author: Jin Tang (sharontj@bu.edu)
 * Date: 06/30/2017
 */
package hw6;

import java.util.*;

public class MiniGoogle implements Iterable<Article> {
	

	/*
	 * take a string, turn it into all lower case, and remove all characters
	 * except for letters, digits, and whitespace // (use
	 * Character.isWhitespace(..) and similar methods)
	 */
	private static String preprocess(String s) {
		// convert it to all lower case
		s = s.toLowerCase();
		String t = "";
		for(int i = 0; i < s.length(); i++) {
			if(Character.isLetter(s.charAt(i)) || Character.isDigit(s.charAt(i)) ||
					Character.isWhitespace(s.charAt(i))) {
				t = t + s.charAt(i);
			}
			
		}
		
		return t;
		

		

	}

	private static final String[] blackList = { "the", "of", "and", "a", "to", "in", "is", "you", "that", "it", "he",
			"was", "for", "on", "are", "as", "with", "his", "they", "i", "at", "be", "this", "have", "from", "or",
			"one", "had", "by", "word", "but", "not", "what", "all", "were", "we", "when", "your", "can", "said",
			"there", "use", "an", "each", "which", "she", "do", "how", "their", "if", "will", "up", "other", "about",
			"out", "many", "then", "them", "these", "so", "some", "her", "would", "make", "like", "him", "into", "time",
			"has", "look", "two", "more", "write", "go", "see", "number", "no", "way", "could", "people", "my", "than",
			"first", "water", "been", "call", "who", "oil", "its", "now", "find", "long", "down", "day", "did", "get",
			"come", "made", "may", "part" };
/*
	// determine if the string s is a member of the blacklist (given at the
	// bottom of this assignment);
	// if so do no process it!
*/	private static boolean blacklisted(String s) {
		for (int i = 0; i < blackList.length; i++) {
			if (s.compareTo(blackList[i]) == 0) {
				return true;
			}
		}
		return false;
	}

	/*// take two strings (e.g., the search phrase and the body of an article) and
	// preprocess each to remove all but letters, digits, and whitespace, and
	// then
	// use the StringTokenizer class to extract each of the terms; create a
	// TermFrequencyTable and
	// insert each of the terms which is NOT in the blacklist into the table
	// with its docNum
	// (String s being document 0 and String t being document 1);
	// finally extract the cosine similarity and return it.
*/
	private static double getCosineSimilarity(String s, String t) {
		String str1 = preprocess(s);
		String str2 = preprocess(t);
		TermFrequencyTable T = new TermFrequencyTable();

		StringTokenizer st1 = new StringTokenizer(str1);
		StringTokenizer st2 = new StringTokenizer(str2);

		while (st1.hasMoreTokens()) {
			String term1 = st1.nextToken();
			if (!blacklisted(term1)) {
				T.insert(term1, 0);
			}
		}
		while (st2.hasMoreTokens()) {
			String term2 = st2.nextToken();
			if (!blacklisted(term2)) {
				T.insert(term2, 1);
			}
		}

		Double cos = T.cosineSimilarity();
		return cos;
	}

	// Take an ArticleTable and search it for articles most similar to
	// the phrase; return a string response that includes the top three
	// as shown in the sample session shown below

	// 3. Insert any articles with a cosinesimilarity greater than 0.001 into
	// the ArticleHeap;

	public static String phraseSearch(String phrase, ArticleTable T)  {

		//TermFrequencyTable t = new TermFrequencyTable();
		//t.insert(phrase, 0);

		Iterator<Article> it = T.iterator();
		ArticleHeap A = new ArticleHeap();

		while (it.hasNext()) {
			Article a = it.next();
			double cs=getCosineSimilarity(phrase,a.getBody());
			//System.out.println(cs);
			if (cs> 0.001) {
				a.putCS(cs);
				A.insert(a);
			}
		}

		//A.printHeap();
		String result = "";
		
		if (A.size() == 0) {
			result = "Nothing matches";
		} 
		for(int i=0;i<3;i++){
			
			Article r=null;
			try {
				r=A.getMax();
				result +=  "Match" + (i+1)  + " with cosine similarity of " + r.getCS() + ": \n"+r.toString();
			} catch (HeapUnderflowException e) {
				// TODO Auto-generated catch block
				System.out.println("Error: Attempt to get max from empty heap");			
				}
			
			
				
			} 
		   
		
		
		return result;

	}

	private static Article[] getArticleList(DatabaseIterator db) {
	    
	    // count how many articles are in the directory
	    int numArticles = db.getNumArticles(); 
	    
	    // now create array to return list of articles
	    Article[] list = new Article[numArticles];
	    for(int i = 0; i < numArticles; i++)
	      list[i] = db.next();
	    
	    return list; 
	  }
	  
	  private static DatabaseIterator setupDatabase(String path) {
	    return new DatabaseIterator(path);
	  }
	  
	  private static void addArticle(Scanner s, ArticleTable D) {
	    System.out.println();
	    System.out.println("Add an article");
	    System.out.println("==============");
	    
	    System.out.print("Enter article title: ");
	    String title = s.nextLine();
	    
	    System.out.println("You may now enter the body of the article.");
	    System.out.println("Press return two times when you are done.");
	    
	    String body = "";
	    String line = "";
	    do {
	      line = s.nextLine();
	      body += line + "\n";
	    } while (!line.equals(""));
	    
	    D.insert(new Article(title, body));
	  }
	  
	  
	  private static void removeArticle(Scanner s, ArticleTable D) {
	    System.out.println();
	    System.out.println("Remove an article");
	    System.out.println("=================");
	    
	    System.out.print("Enter article title: ");
	    String title = s.nextLine();
	    
	    D.delete(title);
	  }
	  
	  private static void titleSearch(Scanner s, ArticleTable D) {
		    System.out.println();
		    System.out.println("Search by article title");
		    System.out.println("=======================");
		    
		    System.out.print("Enter article title: ");
		    String title = s.nextLine();
		    
		    Article a = D.lookup(title);
		    if(a != null)
		      System.out.println(a);
		    else {
		      System.out.println("Article not found!"); 
		      return; 
		    }
		    
		    System.out.println("Press return when finished reading.");
		    s.nextLine();
		  }


		  public static void main(String[] args) {
		
		Scanner user = new Scanner(System.in);

	    String dbPath = "./src/hw6/articles/";

	    
	    DatabaseIterator db = setupDatabase(dbPath);
	    
	    System.out.println("Read " + db.getNumArticles() + 
	                       " articles from disk.");
	 
		
	    
	    ArticleTable L = new ArticleTable(); 
	    Article[] A = getArticleList(db);
	    L.initialize(A);
	 
		  
	    
	 	    int choice = -1;
	    do {
	      System.out.println();
	      System.out.println("Welcome to Minipedia!");
	      System.out.println("=====================");
	      System.out.println("Make a selection from the " +
	                         "following options:");
	      System.out.println();
	      System.out.println("Manipulating the database");
	      System.out.println("-------------------------");
	      System.out.println("    1. add a new article");
	      System.out.println("    2. remove an article");
	      System.out.println();
	      System.out.println("Searching the database");
	      System.out.println("----------------------");
	      System.out.println("    3. search by exact article title");
	      System.out.println("    4. search by phrase");
	      System.out.println();
	      
	      System.out.print("Enter a selection (1-4, or 0 to quit): ");
	      
	      choice = user.nextInt();
	      user.nextLine();
	      
	      switch (choice) {
	        case 0:
	          return;
	          
	        case 1:
	          addArticle(user, L);
	          break;
	          
	        case 2:
	          removeArticle(user, L);
	          break;
	          
	        case 3:
	        	titleSearch(user, L);
	          break;
	          
	        case 4:
	        	String ph=user.nextLine().toString();
	    	    
	    	    System.out.println(phraseSearch(ph,L));
	      
	          
	        default:
	          break;
	      }
	      
	      choice = -1;
	      
	    } while (choice < 0 || choice > 5);

	   
	    
	  }
		 

		@Override
		public Iterator<Article> iterator() {
			// TODO Auto-generated method stub
			return null;
		}


}
