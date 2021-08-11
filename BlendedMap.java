import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.HashMap;
import java.awt.Color;

public class BlendedMap
{
    static private HashMap<String, HashMap<String, BlendedCounty>> map;//String = statename, String = county name, county = county object
    static final int CANVAS_HEIGHT = 600;
    
    static private String stateNamee;
    static private int yearr;

    public BlendedMap(String stateName1, int year1) throws Exception
    {
        stateNamee = stateName1;
        yearr = year1;
    }
    
    // public static void main(String[] args) throws Exception
    // {
        // StdDraw.enableDoubleBuffering();
        // visualize(stateNamee, yearr);
        // StdDraw.show();
    // }

    public static void visualize(String stateName, int year) throws Exception
    {
        if(stateName.equals("USA-County"))
        {
            map = new HashMap<>();
            String[] states = {"AL", "AR", "AZ", "CA", "CO", "CT", "DC", "DE", "FL", "GA", "IA", "ID", "IL", "IN", "KS", "KY", "LA", "MA", "MD", "ME", "MI", "MN", "MO", "MS", "MT", "NC", "ND", "NE", "NH", "NJ", "NM", "NV", "NY", "OH", "OK", "OR", "PA", "RI", "SC", "SD", "TN", "TX", "UT", "VA", "VT", "WA", "WI", "WV", "WY"};
            for(int i = 0; i < states.length; i++)
            {
                String str = "./input/" + states[i] + year + ".txt";
                File f = new File(str);
                Scanner votingScanner = new Scanner(f);
                HashMap<String, BlendedCounty> data = new HashMap<>();
                //System.out.println("\n" + states[i] + "\n");
                votingScanner.nextLine();
                Color c;
                while(votingScanner.hasNextLine())
                {
                    String[] pollNumbers = votingScanner.nextLine().split(",");
                    double rep = Double.parseDouble(pollNumbers[1]); // index 0 is region name
                    double demo = Double.parseDouble(pollNumbers[2]);
                    double indep = Double.parseDouble(pollNumbers[3]);
                    double totalValue = rep + demo + indep;
                    //System.out.println(pollNumbers[0]);
                    String politicalColor;

                    int red = (int) (255 * rep/totalValue);
                    int blue = (int) (255 * demo/totalValue);
                    int green = (int) (255 * indep/totalValue);

                    c = new Color(red, green, blue);

                    data.put(pollNumbers[0], new BlendedCounty(states[i], c));
                }
                map.put(states[i], data);
                votingScanner.close();
            }
            processGeographicData(stateName);
        }
        else
        {
            String geography = "./input/" + stateName + ".txt";
            File f = new File(geography);
            Scanner geoScanner = new Scanner(f);

            String voting = "./input/" + stateName + year + ".txt";
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

            HashMap<String, Color> data = new HashMap<String, Color>();
            int parts = Integer.parseInt(geoScanner.nextLine());

            Color c;

            while(votingScanner.hasNextLine())
            {
                String[] pollNumbers = votingScanner.nextLine().split(",");
                double rep = Double.parseDouble(pollNumbers[1]); // index 0 is region name
                double demo = Double.parseDouble(pollNumbers[2]);
                double indep = Double.parseDouble(pollNumbers[3]);
                double totalValue = rep + demo + indep;
                //System.out.println(pollNumbers[0]);
                String politicalColor;

                int red = (int) (255 * rep/totalValue);
                int blue = (int) (255 * demo/totalValue);
                int green = (int) (255 * indep/totalValue);

                c = new Color(red, green, blue);
                data.put(pollNumbers[0], c);
            }
            //System.out.println(data);
            for(int i = 0; i < parts; i++)
            {

                geoScanner.nextLine();
                String name = geoScanner.nextLine();
                geoScanner.nextLine();
                int trials = Integer.parseInt(geoScanner.nextLine());

                //System.out.println(name);

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
                    Color color = data.get(name);
                    StdDraw.setPenColor(color);
                }
                else
                {
                    StdDraw.setPenColor(StdDraw.YELLOW);
                }
                StdDraw.filledPolygon(longitude, latitude);
            }
            votingScanner.close();
            geoScanner.close();
        }
        
    }

    public static void processGeographicData(String name) throws Exception
    {
        String str = "./input/" + name + ".txt";
        File f = new File(str);
        Scanner s = new Scanner(f);

        String[] minString = s.nextLine().split("   ");
        double[] min = new double[2];
        min[0] = Double.parseDouble(minString[0]);
        min[1] = Double.parseDouble(minString[1]);

        String[] maxString = s.nextLine().split("   ");
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

        int parts = Integer.parseInt(s.nextLine());

        for(int i = 0; i < parts; i++)
        {
            s.nextLine();
            //s.nextLine();
            //s.nextLine();
            String countyName = s.nextLine();
            String stateName = s.nextLine();
            int trials = Integer.parseInt(s.nextLine());
            double[] longitude = new double[trials];
            double[] latitude = new double[trials];
            for(int j = 0; j < trials; j++)
            {
                String[] line = s.nextLine().split("   ");
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
            if(countyName.contains(" city")) // no "city" counties
                countyName = countyName.substring(0, countyName.length() - 5);
            if(stateName.equals("LA"))
            {
                countyName = countyName.substring(0, countyName.length() - 7);;//county data for Louisiana is wacky; every county in Louisiana has parish on the end
            }
            //System.out.println(countyName);

            Color c;

            if(map.get(stateName) == null)
            {
                c = StdDraw.YELLOW;
            }
            else if(map.get(stateName).get(countyName) == null)
            {
                c = StdDraw.YELLOW;
            }
            else if(map.get(stateName).get(countyName).getParty() == null)
            {
                c = StdDraw.YELLOW;
            }
            else
            {
                c = map.get(stateName).get(countyName).getParty();
            }

            StdDraw.setPenColor(c);

            StdDraw.filledPolygon(longitude, latitude);
        }
    }
}