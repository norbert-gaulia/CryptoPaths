import java.util.*;

public class PathFinder {

    public static void main(String[] args) {
        try{
            Graph g = new Graph();
            boolean exit = false;
            Date date = new Date();

            do {
                Object baseAsset = UserInterface.userInput("Enter the base asset: ");
                Object quoteAsset = UserInterface.userInput("Enter the quote asset: ");
                float s = System.nanoTime();
                GraphIO.readAsset("exchangeInfo.json", g);
                GraphIO.readTradeData("24hr.json", g);
                List<Stack<String>> pathList = new LinkedList<>();
                List<Path> pL = new LinkedList<Path>();
                g.DFSPath((String) baseAsset, (String) quoteAsset, pathList, pL);
                float e = System.nanoTime();
                UserInterface.displayMsg("Time taken: " + ((e - s)) + " nanoseconds.");

                // write paths to file
                Comparator<Path> c = new Comparator<>() {
                    public int compare(Path a, Path b) {
                        return b.getOverallExchange().compareTo(a.getOverallExchange());
                    }
                };
                //sort the path
                pL.sort(c);
                for (Path path : pL) {
                    for (String path2 : path.getAssetList()) {
                        //UserInterface.displayMsg(path2);
                    }
                }
                String directPrice = g.getPrice(baseAsset + "" + quoteAsset);
                GraphIO.writePath(baseAsset + "" + quoteAsset, pathList,
                        "All_Paths_" + baseAsset + "" + quoteAsset + ".txt", directPrice, date.toString(),pL);

                //Overview
                g.writeAssetOverview("Asset_Overview.txt", g);

                g.writeTradeOverview("Trade_Overview.txt", g);
            } while (!exit);
        }catch(Exception e){
            UserInterface.displayError(e.getLocalizedMessage());
        }
        
    }
}