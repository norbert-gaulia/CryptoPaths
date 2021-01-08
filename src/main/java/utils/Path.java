package utils;

import java.util.LinkedList;
import java.util.List;

public class Path {
    private Double overallExchange = 1.0;
    private List<String> assetList = new LinkedList<>();
    private List<String> priceList = new LinkedList<>();

    //constructor
    public Path(List<String> assetList,List<String> priceList){
        this.assetList.addAll(assetList);
        this.priceList.addAll(priceList);
        for (String price : priceList) {
            this.overallExchange *= Double.parseDouble(price);
        }
    }
    public String toString(){
        return "Overall Exchange: " + this.overallExchange;
    }
    public Double getOverallExchange(){
        return this.overallExchange;
    }

    public List<String> getAssetList() {
        return this.assetList;
    }

    public List<String> getPriceList() {
        return this.priceList;
    }
}
