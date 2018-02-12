/* File: IntStack.java
 * Classes: IntStack
 * Date: 06/30/2017
 * Author: Jin Tang
 */
package hw6;
public class IntStack  {
    
    private int [] A = new int[5]; 
    
    private int next = 0;                        // location of next available unused slot  
    
    // interface methods
    
    public void push(int key) {         // push the key onto the top of the stack 
        A[next++] = key; 
    }
    
    public int pop() {            // remove the top integer and return it -- will cause error if empty! 
        return A[--next];   
    }
    
    public boolean isEmpty() {
        return (next == 0); 
    }
    
    public int size() {                 // how many integers in the stack 
        return next; 
    }
    
    // unit test
    
    public static void main(String [] args) {
        
        IntStack S = new IntStack();        
        
        System.out.println("Pushing 5, 9, 9, -3, 31 then popping and printing them out:"); 
        S.push(5); S.push(9); S.push(9); S.push(-3); S.push(31);
        
        while(!S.isEmpty()) {
            System.out.println(S.pop()); 
        }
        
        // this one will cause an underflow error, since the stack is empty!
        System.out.println("Popping an empty stack will cause an error:");
        System.out.println(S.pop());
        
  /*      
        // This will cause an overflow error
        
        System.out.println("Pushing too many data items will also cause an error:"); 
        S.push(5); S.push(9); S.push(9); S.push(-3); S.push(31); S.push(99);
        
  */
        
    }   
}

// put your class definitions for the exceptions in this file, right here:


