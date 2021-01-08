import utils.*;
import java.util.*;

public class App {
    public static void main(String[] args) {
        Graph g = new Graph();
        boolean exit = false;
        Date date = new Date();
        do {
            try{
                Object baseAsset = UserInterface.userInput("Enter the base asset: ");
                Object quoteAsset = UserInterface.userInput("Enter the quote asset: ");
                //read in the data
                GraphIO.readAsset(g);
                GraphIO.readTradeData(g);
                //find the path
                List<Path> pathList = new LinkedList<Path>();
                try{
                    g.DFSPath((String) baseAsset, (String) quoteAsset, pathList);
                }catch(Exception e){UserInterface.displayError(e.getMessage());}
                //comparator to sort the path list
                Comparator<Path> c = new Comparator<Path>() {
                    public int compare(Path a, Path b) {
                        return b.getOverallExchange().compareTo(a.getOverallExchange());
                    }
                };
                //sort the path in desc order
                pathList.sort(c);
                //trade
                String directPrice = g.getPrice(baseAsset + "" + quoteAsset);
                Exchange.trade(pathList, Double.parseDouble(directPrice));
                
                //write paths to file
                GraphIO.writePath(baseAsset + "" + quoteAsset,
                        "All_Paths_" + baseAsset + "" + quoteAsset + ".txt", directPrice, date.toString(), pathList);
                //Overview
                g.writeAssetOverview("Asset_Overview.txt", g);

                g.writeTradeOverview("Trade_Overview.txt", g);

            } catch (Exception e) {
                UserInterface.displayError(e.getLocalizedMessage());
            }
        } while (!exit);
    
    }
}