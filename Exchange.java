import com.binance.api.client.domain.account.*;
import com.binance.api.client.domain.general.ExchangeInfo;
import com.binance.api.client.domain.general.FilterType;
import com.binance.api.client.domain.general.SymbolFilter;
import com.binance.api.client.domain.general.SymbolInfo;

import static com.binance.api.client.domain.account.NewOrder.marketBuy;

import java.nio.file.DirectoryStream.Filter;
import java.text.DecimalFormat;
import java.util.List;

import com.binance.api.client.*;
public class Exchange {
    private final static BinanceApiClientFactory factory = BinanceApiClientFactory.newInstance(
            "AZCES3ywkryZfiRQ5HRAGp4JqsdoBxeBMqfu0U3oQHDsRj3nLuUAv0qL0MyGc9FU",
            "a0IbpuUV1wzifgO16poUTqL5HenJBqZUgKqsAzTnNtYiXIG64YWH9FEF3EAfJH0q");
    private final static BinanceApiRestClient client = factory.newRestClient();
    public static void trade(List<Path> pathList, double directPrice){
        Account account = client.getAccount();
        double amountFree;
        UserInterface.displayMsg("------------");

        // the best path - will be used to trade
        List<String> bestPath = pathList.get(0).getAssetList();
        List<String> bestPricePath = pathList.get(0).getPriceList();
        double overallExchange = pathList.get(0).getOverallExchange();
        DecimalFormat df = new DecimalFormat("###.###");
        String tradePair = "";
        double price;
        //exchange info to keep track of the asset filters
        ExchangeInfo exchangeInfo = client.getExchangeInfo();
        int priceCount = 0;

        if(overallExchange > directPrice){
        for (int i = 0; i < bestPath.size(); i++) {
            if (i < bestPath.size() - 1) {
                tradePair = bestPath.get(i) + "" + bestPath.get(i + 1);
                price = Double.parseDouble(df.format(Double.parseDouble(bestPricePath.get(priceCount))));
                amountFree = Double.parseDouble(df.format(Double.parseDouble(account.getAssetBalance(bestPath.get(i)).getFree())));
                double quantity = Double.parseDouble(df.format((amountFree * price)));
                double minNot = quantity * price;

                SymbolInfo symbolInfo = exchangeInfo.getSymbolInfo(tradePair);
                SymbolFilter priceFilter = symbolInfo.getSymbolFilter(FilterType.PRICE_FILTER);
                double minPrice = Double.parseDouble(priceFilter.getMinPrice()), tickSize = Double.parseDouble(priceFilter.getTickSize());
                SymbolFilter lotSize = symbolInfo.getSymbolFilter(FilterType.LOT_SIZE);
                double minQty = Double.parseDouble(lotSize.getMinQty()), stepSize = Double.parseDouble(lotSize.getStepSize());
                SymbolFilter minNotional = symbolInfo.getSymbolFilter(FilterType.MIN_NOTIONAL);
                SymbolFilter marketLot = symbolInfo.getSymbolFilter(FilterType.MARKET_LOT_SIZE);
                double marketMinQty =  Double.parseDouble(marketLot.getMinQty()),marketStepSize = Double.parseDouble(marketLot.getStepSize());

                try {
                    NewOrderResponse newOrderResponse = client
                .newOrder(marketBuy(tradePair,
                String.valueOf(quantity)).newOrderRespType(NewOrderResponseType.FULL));
                List<Trade> fills = newOrderResponse.getFills();
                System.out.println(newOrderResponse.getClientOrderId());
                } catch (Exception e) {
                    UserInterface.displayError(e.getMessage());
                }
                UserInterface.displayMsg(bestPath.get(i) + "" + bestPath.get(i + 1));
                UserInterface.displayMsg("Price: " + price);
                UserInterface.displayMsg("min price: " + minPrice);
                UserInterface.displayMsg("price step: " + tickSize);
                UserInterface.displayMsg("price filter stat: " + (price - minPrice) % tickSize);
                UserInterface.displayMsg("Min notional: " + minNot);
                UserInterface.displayMsg("Amount available: " + amountFree);
                UserInterface.displayMsg("Quantity: " + quantity);
                UserInterface.displayMsg("min Quantity: " + minQty);
                UserInterface.displayMsg("qty step: " + stepSize);
                UserInterface.displayMsg("market qty step: " + marketStepSize);
                UserInterface.displayMsg("market min Quantity: " + marketMinQty);
                UserInterface.displayMsg("qty filter stat: " + (quantity - minQty) % stepSize);
                UserInterface.displayMsg("\n----------------------\n");
            }
            priceCount++;
        }
        }
        else
            UserInterface.displayError("No profit!");

        UserInterface.displayMsg("------------");
    }
}
