import java.util.List;

public class Path {
    private Double overallExchange = 1.0;
    private List<String> assetList = null;
    private List<String> priceList = null;

    //constructor
    public Path(List<String> assetList,List<String> priceList){
        this.assetList = assetList;
        this.priceList = priceList;
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
