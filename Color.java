public class Color
{
    public int r,g,b,a;
    public Color()
    {
        r = g = b = a = 0;
    }
    public String toString()
    {
        return String.format("[%03d,%03d,%03d,%03d]", r, g, b, a);
    }
}