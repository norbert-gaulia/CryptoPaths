/***************************************************************************
 *  FILE: Asset.java
 *  AUTHOR: Bilal Farah
 *  PURPOSE: To hold the asset information for a particular asset e.g. "ETH"
 *  LAST MOD: 01/10/20
 *  REQUIRES: NONE
 ***************************************************************************/
import java.io.Serializable;
public class Assets implements Serializable{
    private static final long serialVersionUID = 1L;
    // Class fields
    private String asset;
    private String lastPrice;
    private String symbol;
    private boolean visited;

    //constructor
    public Assets(String asset, String lastPrice, String symbol){
        this.asset = asset;
        this.lastPrice = lastPrice;
        this.symbol = symbol;
        this.visited = false;
    }

    //Accessors
    //getAsset
    public String getAsset(){
        return this.asset;
    }

    //getLastPrice
    public String getLastPrice(){
        return this.lastPrice;
    }

    //getSymbol
    public String getSymbol(){
        return this.symbol;
    }

    // toString
    public String toString() {
        return "Asset: " + this.asset + "\nSymbol: " + this.symbol + "\nlastPrice: " +this.lastPrice;
    }
    //getVisited
    public boolean getVisited(){
        return this.visited;
    }

    //Mutators
    public void setLastPrice(String lastPrice){
        this.lastPrice = lastPrice;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    //setVisited
    public void setVisited(boolean state){
        this.visited = state;
    }

    
}
