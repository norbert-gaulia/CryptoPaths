
/***************************************************************************
*  FILE: GraphIO.java
*  AUTHOR: Bilal Farah
*  PURPOSE: Reading asset and trade data into a graph, writing path data to a file.
*  LAST MOD: 01/11/20
*  REQUIRES: NONE
*  As submitted for practical 4
***************************************************************************/
import java.io.*;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.json.JSONArray;
import com.binance.api.client.BinanceApiClientFactory;
import com.binance.api.client.BinanceApiRestClient;
import com.binance.api.client.domain.market.TickerPrice;
import com.binance.api.client.domain.general.SymbolStatus;
import com.binance.api.client.domain.general.ExchangeInfo;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Stack;

public class GraphIO {
    private final static BinanceApiClientFactory factory = BinanceApiClientFactory.newInstance();
    private final static BinanceApiRestClient client = factory.newRestClient();

    public static void readAsset(String file, Graph graph) {
        Trade trade = null;
        try {
            ExchangeInfo exchangeInfo = client.getExchangeInfo();
            Assets bAsset = null, qAsset = null;
            String quoteAsset, baseAsset;
            for (int i = 0; i < exchangeInfo.getSymbols().size(); i++) {
                // read in each symbol
                quoteAsset = exchangeInfo.getSymbols().get(i).getQuoteAsset();
                baseAsset = exchangeInfo.getSymbols().get(i).getBaseAsset();
                String lastPrice = "";
                // setting up asset info
                bAsset = new Assets(baseAsset, lastPrice, exchangeInfo.getSymbols().get(i).getSymbol());
                qAsset = new Assets(quoteAsset, lastPrice, exchangeInfo.getSymbols().get(i).getSymbol());
                // setting up graph vertices
                graph.addVertex(baseAsset, bAsset);
                graph.addVertex(quoteAsset, qAsset);
                trade = new Trade();
                trade.setBase(baseAsset);
                trade.setQuote(quoteAsset);
                if (exchangeInfo.getSymbols().get(i).getStatus().equals(SymbolStatus.TRADING))
                    graph.addEdge(baseAsset, quoteAsset, exchangeInfo.getSymbols().get(i).getSymbol(), trade);
            }
        } catch (Exception e) {

            UserInterface.displayError(e.getMessage());
        }
    }

    /*
    //readAsset
    // public static Trade readAsset(String file, Graph graph) {
    //     Object obj;
    //     Trade trade = null;
    //     try {
    //         JSONTokener jToken = new JSONTokener(new FileReader(file));

    //         JSONObject jo = new JSONObject(jToken);

    //         JSONArray symbols = jo.getJSONArray("symbols");

    //         JSONObject symbol = (JSONObject) symbols.get(0);
    //         Assets bAsset = null, qAsset = null;
    //         String quoteAsset, baseAsset;
    //         for (int i = 0; i < symbols.length(); i++) {
    //             //read in each symbol
    //             symbol = (JSONObject) symbols.get(i);
    //             quoteAsset = symbol.getString("quoteAsset");
    //             baseAsset = symbol.getString("baseAsset");
    //             String lastPrice = "";
    //             //setting up asset info
    //             bAsset = new Assets(baseAsset, lastPrice, symbol.getString("symbol"));
    //             qAsset = new Assets(quoteAsset, lastPrice, symbol.getString("symbol"));
    //             //setting up graph vertices
    //             graph.addVertex(baseAsset, bAsset);
    //             graph.addVertex(quoteAsset, qAsset);
    //             trade = new Trade();
    //             trade.setBase(baseAsset);
    //             trade.setQuote(quoteAsset);
    //             if(symbol.getString("status").equals("TRADING"))
    //                 graph.addEdge(baseAsset, quoteAsset, symbol.getString("symbol"),trade);
    //         }
    //     } catch (Exception e) {
           
    //         UserInterface.displayError(e.getMessage());
    //     }
    //     return trade;
    // }
    */

    // readTrade
    public static Graph readTradeData(String file, Graph graph) {
        List<TickerPrice> allPrices = client.getAllPrices();
        HashMap<String,String> tradeTree = new HashMap<>();

        //get data via API
        for (int i = 0; i < allPrices.size(); i++) {
            tradeTree.put(allPrices.get(i).getSymbol(), allPrices.get(i).getPrice());
        }
        try {
            //tokenizer
            JSONTokener jToken = new JSONTokener(new FileReader(file));
            //json array
            JSONArray trades = new JSONArray(jToken);
            //json object
            JSONObject trade = null;
            //trade data
            Trade tradeInfo = null;
            System.out.print("Downloading data...");
            
            for (int i = 0; i < trades.length(); i++) {
                try{
                    // read in each trade
                    trade = (JSONObject) trades.get(i);
                    // retrieving price data & setting up the trade info
                    tradeInfo = graph.getTrade(trade.getString("symbol"));
                    tradeInfo.setSymbol(trade.getString("symbol")); 
                    tradeInfo.setPriceChange(trade.getString("priceChange")); 
                    //tradeInfo.setLastPrice(tickerStatistics.getLastPrice()); 
                    //tradeInfo.setLastPrice(trade.getString("lastPrice"));
                    tradeInfo.setLastPrice(tradeTree.get(trade.getString("symbol")));
                    if(trade.getString("symbol").contains("BRL")){
                        // UserInterface.displayScs(""+ tradeTree.get(trade.getString("symbol")));
                        //tradeInfo.setLastPrice("16307.8930");
                    }
                    tradeInfo.setVolume(trade.getString("volume")); 
                    tradeInfo.setPrevClosePrice(trade.getString("prevClosePrice")); 
                    tradeInfo.setCount(trade.getInt("count")); 
                    // setting up edge data
                    graph.updateEdgeData(trade.getString("symbol"), tradeInfo);
                }
                catch (Exception e) {}
            }
            
            System.out.println("Done!");
        } catch (Exception e) {

            UserInterface.displayError(e.getMessage());
        }
        return graph;
    }
    public static void writePath(String tradePair, List<Stack<String>> assetPaths, String file, String directPrice, String date,
            List<Path> pL){
        try {
            FileOutputStream outStream = new FileOutputStream(file);
            PrintWriter writer = new PrintWriter(outStream);
            int pathCount = 1;
            writer.println("All the paths for the pair '"+tradePair+"', Direct Price: " + directPrice + "\n");
            writer.println("Date: " + date + "\n");
            for (Path path : pL) {
                writer.println("Path " + pathCount + ": ");
                writer.print("{");
                for (String asset : path.getAssetList()) {
                    writer.print(" " + asset);
                }
                writer.println(" }");

                writer.print("{");
                for (String price : path.getPriceList()){
                    writer.print(" " + price);
                }
                writer.println(" }");
                writer.println("\nOverall Exchange: " + path.getOverallExchange() + "\n");
                writer.println("-------------------------------------\n");
                pathCount++;
            }/*
            //iterate through the list of paths
            for (Stack<String> object : assetPaths) { 
                //Asset path held in each even queue
                if(i % 2 == 0){
                    if (object instanceof Stack<?>) {
                        Stack<String> q = object;
                        writer.println("Path " + pathCount + ": ");
                        writer.print("{");
                        for (Object object2 : q) {
                            writer.print(" " + object2);
                        }
                        writer.println(" }");
                    }
                } // Price path held in each odd queue
                else{
                    if (object instanceof Stack<?>) {
                        bestPath = object;
                        Stack<String> q = object;
                        writer.print("{");
                        overallExchange = 1;
                        // compute overall exchange, while outputting the price
                        for (Object price : q) {
                            writer.print(" " + price);
                            overallExchange *= Double.parseDouble((String)price);
                        }
                        writer.println(" }");
                        writer.println("\nOverall Exchange: " + overallExchange + "\n");
                        writer.println("-------------------------------------\n");
                        pathCount++;
                    }
                }
                i++;
            }    */         
            writer.close();
            UserInterface.displayScs("The path has been written to '" + file + "'.");
        } catch (IOException e) {
            UserInterface.displayError(e.getMessage());
        }
    }

}
