package utils;
/***************************************************************************
*  FILE: GraphIO.java
*  AUTHOR: Bilal Farah
*  PURPOSE: Reading asset and trade data into a graph, writing path data to a file.
*  LAST MOD: 01/11/20
*  REQUIRES: NONE
*  As submitted for practical 4
***************************************************************************/
import java.io.*;
import com.binance.api.client.BinanceApiClientFactory;
import com.binance.api.client.BinanceApiRestClient;
import com.binance.api.client.domain.general.SymbolStatus;
import com.binance.api.client.domain.general.ExchangeInfo;
import com.binance.api.client.domain.market.TickerStatistics;
import java.util.List;

public class GraphIO {
    private final static BinanceApiClientFactory factory = BinanceApiClientFactory.newInstance();
    private final static BinanceApiRestClient client = factory.newRestClient();

    public static void readAsset(Graph graph) {
        Trades trade = null;
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
                trade = new Trades();
                trade.setBase(baseAsset);
                trade.setQuote(quoteAsset);
                trade.setSymbol(baseAsset+""+quoteAsset);
                if (exchangeInfo.getSymbols().get(i).getStatus().equals(SymbolStatus.TRADING))
                    graph.addEdge(baseAsset, quoteAsset, exchangeInfo.getSymbols().get(i).getSymbol(), trade);
            }
        } catch (Exception e) {

            UserInterface.displayError(e.getMessage());
        }
    }
    // readTrade
    public static Graph readTradeData(Graph graph) {
        String symbol;
        try {
            Trades tradeInfo = null;
            List<TickerStatistics> tl = client.getAll24HrPriceStatistics();
            System.out.print("Downloading data...");
            for (TickerStatistics tickerStatistics : tl) {
                symbol = tickerStatistics.getSymbol();
                if(graph.hasEdge(symbol) != null){
                    tradeInfo = graph.getTrade(symbol);
                    tradeInfo.setPriceChange(tickerStatistics.getPriceChange());
                    tradeInfo.setLastPrice(tickerStatistics.getLastPrice());
                    tradeInfo.setVolume(tickerStatistics.getVolume());
                    tradeInfo.setPrevClosePrice(tickerStatistics.getPrevClosePrice());
                    tradeInfo.setCount(tickerStatistics.getCount());
                    // setting up edge data
                    graph.updateEdgeData(symbol, tradeInfo);
                }
            }
            System.out.println("Done!");
        } catch (Exception e) {

            UserInterface.displayError(e.getMessage());
        }
        return graph;
    }
    public static void writePath(String tradePair, String file, String directPrice, String date,
                                 List<Path> pathList){
        try {
            FileOutputStream outStream = new FileOutputStream(file);
            PrintWriter writer = new PrintWriter(outStream);
            int pathCount = 1;
            writer.println("All the paths for the pair '"+tradePair+"', Direct Price: " + directPrice + "\n");
            writer.println("Date: " + date + "\n");
            for (Path path : pathList) {
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
            }
            writer.close();
            UserInterface.displayScs("The path has been written to '" + file + "'.");
        } catch (IOException e) {
            UserInterface.displayError(e.getMessage());
        }
    }
}
