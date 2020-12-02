import java.io.Serializable;
import java.util.*;
import com.binance.api.client.BinanceApiClientFactory;
import com.binance.api.client.BinanceApiRestClient;
import com.binance.api.client.domain.market.TickerStatistics;
/**
 * Author: Bilal Farah Date Created:18/10/2020 Date Modified:21/10/2020
 * Purpose:To hold the trade information from the last 24hrs for a particular
 * pair e.g. "ETHBTC"
 */
public class Trades implements Serializable {
    
    private static final long serialVersionUID = 1L;
    // class fields
    private String baseAsset;
    private String quoteAsset;
    private String symbol;
    private String priceChange;
    private String lastPrice;
    private String prevClosePrice;
    private String volume;
    private int count;
    private boolean visited;
    private final static BinanceApiClientFactory factory = BinanceApiClientFactory.newInstance();
    private final static BinanceApiRestClient client = factory.newRestClient();

    // constructor
    public Trades(){}
    public Trades(String symbol, String priceChange, String lastPrice,String prevClosePrice, String volume, int count) {
        this.symbol = symbol;
        this.priceChange = priceChange;
        this.lastPrice = lastPrice;
        this.prevClosePrice = prevClosePrice;
        this.volume = volume;
        this.count = count;
        this.visited = false;
    }
    //Mutator
    public void setBase(Object baseAsset) {
        this.baseAsset = (String)baseAsset;
    }
    
    public void setQuote(Object quoteAsset) {
        this.quoteAsset = (String) quoteAsset;
    }
    
    public void setLastPrice(Object lastPrice) {
       this.lastPrice = (String) lastPrice;
    }
    
    public void checkLastPrice() {
        double price = 0.0;
        try {
            TickerStatistics tickerStatistics = client.get24HrPriceStatistics(quoteAsset + "" + baseAsset);
            price = 1 / Double.parseDouble(tickerStatistics.getLastPrice());
            if (Double.parseDouble((String) lastPrice) != price) {
                this.lastPrice = String.valueOf(price);
            }
        } catch (Exception e) {
            this.lastPrice = (String) lastPrice;
        }
    }

    public void setSymbol(Object symbol) {
        this.symbol = (String) symbol;
    }
    
    public void setPriceChange(Object priceChange) {
        this.priceChange = (String) priceChange;
    }
    
    public void setVolume(Object volume) {
        this.volume = (String) volume;
    }
    
    public void setPrevClosePrice(Object prevClosePrice) {
        this.prevClosePrice = (String) prevClosePrice;
    }

    public void setCount(int count) {
        this.count = count;
    }
    // Accessors
    // getSymbol
    public String getSymbol() {
        return this.symbol;
    }

    // getPriceChange
    public String getPriceChange() {
        return this.priceChange;
    }

    // getLastPrice
    public String getLastPrice(HashMap<String, Double> priceTree) {
        // double price = 0;
        // if(this.lastPrice.equals(""))
        //     this.lastPrice = "0.0";
        // double lPrice = Double.parseDouble(this.lastPrice);
        // // if last price is 0
        // if (Double.parseDouble(this.lastPrice) < 0.1) {
        //     try {
        //         price = Double.parseDouble(String.valueOf(priceTree.get(quoteAsset + "" + baseAsset)));
        //     } catch (Exception e) {
        //         try {
        //             TickerStatistics tickerStatistics = client.get24HrPriceStatistics(quoteAsset + "" + baseAsset);
        //             price = Double.parseDouble(tickerStatistics.getLastPrice());
        //             price = 1.0 / price;
        //             if(price < Double.parseDouble(this.lastPrice))
        //                 lPrice = price;
        //             else
        //                 lPrice = Double.parseDouble(this.lastPrice);
        //             this.lastPrice = String.valueOf(lPrice);
        //             if ((quoteAsset + "" + baseAsset).equals("ETHTUSD")) {
        //                 UserInterface.displayScs(" " + lPrice);
        //             }
        //             priceTree.put(quoteAsset + "" + baseAsset, price);
        //         } catch (Exception ex) {
        //             price = Double.parseDouble(this.prevClosePrice);
        //         }
        //     }
        // }
        return this.lastPrice;
    }

    //getPrevClosePrice
    public String getPrevClosePrice(){
        return this.prevClosePrice;
    }
    // getVolume
    public String getVolume() {
        return this.volume;
    }

    // getCount
    public int getCount() {
        return this.count;
    }

    // getVisited
    public boolean getVisited() {
        return this.visited;
    }

    // toString
    public String toString() {
        return "Symbol: " + this.symbol + "\npriceChange: " + this.priceChange + "\nlastPrice: " + this.lastPrice
                + "\nprevClosePrice: " + this.prevClosePrice + "\nvolume: " + this.volume + "\ncount: " + this.count;
    }

    //Mutator
    // setVisited
    public void setVisited(boolean state) {
        this.visited = state;
    }
}
