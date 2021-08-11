import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.HashMap;

public class Subregion
{
    String name;
    double[] latitude;
    double[] longitude;
    int party; // 0: republican 1: democrat 2: independent
    public Subregion(String region, double[] lat, double[] lon, int number) 
    {
        name = region;
        latitude = lat;
        longitude = lon;
        party = number;
    }
    
    public String getName()
    {
        return name;
    }
    
    public double[] getLatitude()
    {
        return latitude;
    }
    
    public double[] getLongitude()
    {
        return longitude;
    }
    
    public int getParty()
    {
        return party;
    }
    
    
    static final int CANVAS_HEIGHT = 600; // always 600


    public static void main(String[] args) throws Exception
    {
        StdDraw.enableDoubleBuffering();
        visualize("NY", "2016");
        StdDraw.show();
    }

    private static void visualize(String region, String votingYear) throws Exception
    {
        String geography = "./input/" + region + ".txt";
        File f = new File(geography);
        Scanner geoScanner = new Scanner(f);
        
        String voting = "./input/" + region + votingYear + ".txt";
        File f2 = new File(voting);
        Scanner votingScanner = new Scanner(f2);
        votingScanner.nextLine();
        
        
        String[] minString = geoScanner.nextLine().split("   ");
        double[] min = new double[2];
        min[0] = Double.parseDouble(minString[0]);
        min[1] = Double.parseDouble(minString[1]);

        String[] maxString = geoScanner.nextLine().split("   ");
        double[] max = new double[2];
        max[0] = Double.parseDouble(maxString[0]);
        max[1] = Double.parseDouble(maxString[1]);
        
        //System.out.println("Long " + max[0] + "   " + min[0]);
        
        double ratio = (min[0] - max[0]) / (min[1] - max[1]);
        //System.out.print( (int) (CANVAS_HEIGHT*ratio));
        
        
        double canvasWidth = (int) (CANVAS_HEIGHT*ratio);
        StdDraw.setCanvasSize( (int) (CANVAS_HEIGHT*ratio), CANVAS_HEIGHT);
        StdDraw.setXscale(0, (int) (CANVAS_HEIGHT*ratio));
        StdDraw.setYscale(0, CANVAS_HEIGHT);
        
        HashMap<String, String> data = new HashMap<String, String>();
        int parts = Integer.parseInt(geoScanner.nextLine());
        
        while(votingScanner.hasNextLine())
        {
            String[] pollNumbers = votingScanner.nextLine().split(",");
            int rep = Integer.parseInt(pollNumbers[1]); // index 0 is region name
            int demo = Integer.parseInt(pollNumbers[2]);
            int indep = Integer.parseInt(pollNumbers[3]);
            
            if(rep > demo && rep > indep)
            {
                data.put(pollNumbers[0], "RED");
            }
            else if(demo > rep && demo > indep)
            {
                data.put(pollNumbers[0], "BLUE");
            }
            else if(indep > rep && indep > demo)
            {
                data.put(pollNumbers[0], "GREEN");
            }
        }
        //System.out.println(data);
        for(int i = 0; i < parts; i++)
        {
            
            geoScanner.nextLine();
            String name = geoScanner.nextLine();
            geoScanner.nextLine();
            int trials = Integer.parseInt(geoScanner.nextLine());
            
            System.out.println(name);
            
            double[] longitude = new double[trials];
            double[] latitude = new double[trials];
            for(int j = 0; j < trials; j++)
            {
                String[] line = geoScanner.nextLine().split("   ");
                longitude[j] = Double.parseDouble(line[0]);
                latitude[j] = Double.parseDouble(line[1]);
            }
            
            for(int k = 0; k < trials; k++)
            {
                double z2x = canvasWidth;
                double x1x = longitude[k] - min[0];
                double x2x = max[0] - min[0];
                
                longitude[k] = (x1x*z2x)/(x2x);
                
                double z2y = CANVAS_HEIGHT;
                double y1y = latitude[k] - min[1];
                double y2y = max[1] - min[1];
                
                latitude[k] = (z2y*y1y)/(y2y);
            }
            
            if(data.containsKey(name))
            {
                String color = data.get(name);
                if(color.equals("RED"))
                {
                    StdDraw.setPenColor(StdDraw.RED);
                }
                else if(color.equals("BLUE"))
                {
                    StdDraw.setPenColor(StdDraw.BLUE);
                }
                else if(color.equals("GREEN"))
                {
                    StdDraw.setPenColor(StdDraw.GREEN);
                }
            }
            StdDraw.filledPolygon(longitude, latitude);
        }
    }
}