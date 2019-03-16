import java.util.*;

public class Test{
    public static void main(String[] args)
    {
        HashMap<String, Integer> typeCounter = new HashMap<String, Integer>();
        Integer h = typeCounter.get("harrypotter");
        if( h != null )
        {
            System.out.println(h);
        }
    }
}