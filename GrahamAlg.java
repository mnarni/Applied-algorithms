import java.util.Scanner;
import java.util.Arrays;
import java.util.Stack;
import java.util.Comparator;
import java.util.ArrayList;
import java.util.List;

public class GrahamAlg
{
    private Stack<Points> stack = new Stack<Points>();
    
    public static void main(String[] args)
    {
        Scanner sc = new Scanner(System.in);
 
        Points[] pts = new Points[50];
        List<Integer> list=new ArrayList<Integer>();
        
        System.out.println("Enter the points:");
        // Reading till an enter key
        while(true)
        {
          String s = sc.nextLine( );
 
            if(s.equals(""))
            {
              break;    
            }
            else
            {
              String[] tokens= s.split(" ");
              for (String p : tokens)
              {
                 list.add(Integer.parseInt(p));
              }
            }
        }
       int i=0, j=0;
       while (i < list.size())
       {
            pts[j]= new Points(list.get(i), list.get(i+1));
            j++;
            i = i+2;
        }
        Points[] points = Arrays.stream(pts)
                .filter(value -> value != null)
                .toArray(size -> new Points[size]);
                
        GrahamAlg graham = new GrahamAlg(points);
        System.out.println("Convex hull points: ");
        //Print the stack
        for (Points p : graham.getConvexHull())
        {
            System.out.println(p);
        }
        sc.close();
    }
 
    public GrahamAlg(Points[] p)
    {
        int num = p.length;
        Points[] points = new Points[num];
        for (int i = 0; i < num; i++)
        {
            points[i] = p[i];
        }
        //Sort for the lowest point
        Arrays.sort(points);
        stack.push(points[0]); 
        //Sort relative to the lowest vertex
        Arrays.sort(points, 1, num, points[0].SortbyAngle);
       
        int pt;
        //Finding the next point in the stack
        for (pt = 2; pt < num; pt++){
            if (Points.crossProduct(points[0], points[1], points[pt]) != 0){
                stack.push(points[pt - 1]);
                break;
            }
        }
       
        for (int i = pt; i < num; i++)
        {
            Points last = stack.pop();
            //Traverse through the stack to check for all clockwise turns
            while (Points.crossProduct(stack.peek(), last, points[i]) <= 0)
            {
                last = stack.pop();
            }
            stack.push(last);
            stack.push(points[i]);
        }
 
    }
 
    public Stack<Points> getConvexHull()
    {
        Stack<Points> hull = new Stack<Points>();
        for (Points p : stack){
            hull.push(p);
        }
        return hull;
    }
 
   
}

class Points implements Comparable<Points>
{
    public final Comparator<Points> SortbyAngle = new SortbyAngle();
    private final int x; 
    private final int y; 
 
    public Points(int x, int y)
    {
        this.x = x;
        this.y = y;
    }
    public int x()
    {
        return x;
    }
    public int y()
    {
        return y;
    }
    public String toString()
    {
        return x + ", " + y;
    }
    public int compareTo(Points p)
    {
        //Comparing for the lowest point
        if (this.y > p.y) return 1;
        else if (this.y < p.y) return -1;
        else if (this.x > p.x) return 1;
        else if (this.x < p.x) return -1;
        else return 0;
    }
    private class SortbyAngle implements Comparator<Points>
    {
        public int compare(Points p1, Points p2)
        {
            return -crossProduct(Points.this, p1, p2); 
        }
    }

    public static int crossProduct(Points p1, Points p2, Points p3)
    {
        int product = (p2.x - p1.x) * (p3.y - p1.y) - (p2.y - p1.y) * (p3.x - p1.x);
        if (product > 0) return 1;
        else if (product < 0) return -1;
        else return 0;
    }
}
