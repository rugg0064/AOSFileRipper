import java.io.IOException;
import java.io.FileInputStream;
import java.io.BufferedInputStream;
import java.io.File;
import java.util.ArrayList;

public class AOSVXLRipper
{
    public static void main(String[] args)
    {
        if(args.length > 0)
        {
            System.out.println(args[0]);
            File f = new File(args[0]);
            System.out.println(f);
            try
            {
                BufferedInputStream bis = new BufferedInputStream(new FileInputStream(f));
                
                //ArrayList<Span> spans = new ArrayList<Span>();
                ArrayList<ArrayList<ArrayList<Span>>> spans = new ArrayList<ArrayList<ArrayList<Span>>>();
                for(int x = 0; x < 512; x++)
                {
                    spans.add(new ArrayList<ArrayList<Span>>());
                    for(int y = 0; y < 512; y++)
                    {
                        spans.get(x).add(new ArrayList<Span>());
                    }
                }

                int x = 0;
                int y = 0;

                int asd = 0;

                int nextByte = 0;
                while( (nextByte = bis.read()) != -1)
                {
                    Span s = new Span();
                    s.n = nextByte;
                    s.s = bis.read();
                    s.e = bis.read();
                    s.a = bis.read();

                    ArrayList<Color> colors = s.colors;
                    if(s.n == 0)
                    {
                        for(int i = 0; i < (s.e - s.s) + 1; i++)
                        {
                            Color color = new Color();
                            color.r = bis.read();
                            color.g = bis.read();
                            color.b = bis.read();
                            color.a = bis.read();
                            colors.add(color);
                        }

                        x++;
                        if(x >= 512)
                        {
                            x -= 512;
                            y++;
                        }
                        asd++;
                        System.out.println(asd);
                        System.out.print(String.format("(%d,%d)|", x, y));
                        System.out.println(s);
                    }
                    else
                    {
                        for(int i = 0; i < s.n-1; i++)
                        {
                            Color color = new Color();
                            color.r = bis.read();
                            color.g = bis.read();
                            color.b = bis.read();
                            color.a = bis.read();
                            colors.add(color);
                        }
                    }
                    spans.get(x).get(y).add(s);
                }
            }
            catch(IOException e)
            {
                System.out.printf("Error: %s%n", e);
            }
        }
        else
        {
            System.out.println("No file supplied");
        }
    }
}