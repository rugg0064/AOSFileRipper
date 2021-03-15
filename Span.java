import java.util.ArrayList;
public class Span
{
    public int n, s, e, a;
    public ArrayList<Color> colors;
    public Span()
    {
        n = s = e = a = 0;
        colors = new ArrayList<Color>();
    }
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        sb.append(String.format("%d, ", n));
        sb.append(String.format("%d, ", s));
        sb.append(String.format("%d, ", e));
        sb.append(String.format("%d", a));
        sb.append("]");
        sb.append(String.format("%n"));
        for(int i = 0; i < colors.size(); i++)
        {
            sb.append(String.format("\t%s%n", colors.get(i)));
        }
        return sb.toString();
    }
}