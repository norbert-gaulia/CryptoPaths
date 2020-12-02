import java.util.*;

import com.binance.api.client.*;
import com.binance.api.client.domain.account.*;

import static com.binance.api.client.domain.account.NewOrder.marketBuy;

public class PathFinder {

    public static void main(String[] args) {
        try{
            Graph g = new Graph();
            boolean exit = false;
            Date date = new Date();
            BinanceApiClientFactory factory = BinanceApiClientFactory.newInstance(
                "AZCES3ywkryZfiRQ5HRAGp4JqsdoBxeBMqfu0U3oQHDsRj3nLuUAv0qL0MyGc9FU",
                "a0IbpuUV1wzifgO16poUTqL5HenJBqZUgKqsAzTnNtYiXIG64YWH9FEF3EAfJH0q");
            BinanceApiRestClient client = factory.newRestClient();

            do {
                Object baseAsset = UserInterface.userInput("Enter the base asset: ");
                Object quoteAsset = UserInterface.userInput("Enter the quote asset: ");

                //read in the data
                GraphIO.readAsset("exchangeInfo.json", g);
                GraphIO.readTradeData("24hr.json", g);

                //find the path
                List<Path> pathList = new LinkedList<Path>();
                try{
                    g.DFSPath((String) baseAsset, (String) quoteAsset, pathList);
                }catch(Exception e){UserInterface.displayError(e.getMessage());}

                //comparator to sort the path list
                Comparator<Path> c = new Comparator<>() {
                    public int compare(Path a, Path b) {
                        return b.getOverallExchange().compareTo(a.getOverallExchange());
                    }
                };
                //sort the path in desc order
                pathList.sort(c);

                UserInterface.displayMsg("------------");

                //the best path -  will be used to trade
                List<String> bestPath = pathList.get(0).getAssetList();
                List<String> bestPricePath = pathList.get(0).getPriceList();

                String tradeP = "";

                // Account account = client.getAccount();
                // String amountFree = account.getAssetBalance("BNB").getFree();
                // System.out.println(account.getAssetBalance("BNB"));

                int priceCount = 0;
                for (int i = 0; i < bestPath.size(); i++) {
                    if(i < bestPath.size() - 1){
                        tradeP = bestPath.get(i) + "" + bestPath.get(i + 1);
                        // NewOrderResponse newOrderResponse = client
                        //         .newOrder(marketBuy(tradeP, "28").newOrderRespType(NewOrderResponseType.FULL));
                        // List<Trade> fills = newOrderResponse.getFills();
                        // System.out.println(newOrderResponse.getClientOrderId());
                        UserInterface.displayMsg(bestPath.get(i) + "" + bestPath.get(i + 1));
                        UserInterface.displayMsg("Price: " + bestPricePath.get(priceCount));
                    }
                    priceCount++;
                }

                UserInterface.displayMsg("------------");
                
                String directPrice = g.getPrice(baseAsset + "" + quoteAsset);
                GraphIO.writePath(baseAsset + "" + quoteAsset,
                        "All_Paths_" + baseAsset + "" + quoteAsset + ".txt", directPrice, date.toString(), pathList);

                //Overview
                g.writeAssetOverview("Asset_Overview.txt", g);

                g.writeTradeOverview("Trade_Overview.txt", g);
            } while (!exit);
        }catch(Exception e){
            UserInterface.displayError(e.getLocalizedMessage());
        }
        
    }
}