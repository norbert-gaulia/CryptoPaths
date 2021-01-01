import com.binance.api.client.domain.account.*;
import com.binance.api.client.domain.general.ExchangeInfo;
import com.binance.api.client.domain.general.FilterType;
import com.binance.api.client.domain.general.SymbolFilter;
import com.binance.api.client.domain.general.SymbolInfo;

import static com.binance.api.client.domain.account.NewOrder.marketBuy;

import java.math.BigDecimal;
import java.nio.file.DirectoryStream.Filter;
import java.text.DecimalFormat;
import java.util.List;

import com.binance.api.client.*;
public class Exchange {
    private final static BinanceApiClientFactory factory = BinanceApiClientFactory.newInstance(
            "AZCES3ywkryZfiRQ5HRAGp4JqsdoBxeBMqfu0U3oQHDsRj3nLuUAv0qL0MyGc9FU",
            "a0IbpuUV1wzifgO16poUTqL5HenJBqZUgKqsAzTnNtYiXIG64YWH9FEF3EAfJH0q");
    private final static BinanceApiRestClient client = factory.newRestClient();

    public static String round(String d) {
        String s = d, s2 = "";
        int places = 0;
        boolean right = false;
        for (int i = 0; i < s.length(); i++) {
            if (places >= 2 && s.charAt(i - 1) != '0')
                break;
            s2 += s.charAt(i);
            if (right)
                places++;
            if (s.charAt(i) == '.') {
                right = true;
                if (s.charAt(i + 1) != '0') {
                    s2 += s.charAt(i + 1);
                    break;
                }
            }
        }
        return s2;
    }

    public static void trade(List<Path> pathList, double directPrice){
        Account account = client.getAccount();
        UserInterface.displayMsg("------------");
        // the best path - will be used to trade
        List<String> bestPath = pathList.get(0).getAssetList();
        List<String> bestPricePath = pathList.get(0).getPriceList();
        double overallExchange = pathList.get(0).getOverallExchange();
        DecimalFormat df = new DecimalFormat("###.####");
        String tradePair = "";
        String price, amountFree;
        BigDecimal p, free,q;
        //exchange info to keep track of the asset filters
        ExchangeInfo exchangeInfo = client.getExchangeInfo();
        int priceCount = 0;

        // if(overallExchange > directPrice){
        for (int i = 0; i < bestPath.size(); i++) {
            if (i < bestPath.size() - 1) {
                tradePair = bestPath.get(i) + "" + bestPath.get(i + 1);
                price = round(bestPricePath.get(priceCount));
                amountFree = round(account.getAssetBalance(bestPath.get(i)).getFree());
                p = new BigDecimal(price);
                free = new BigDecimal(amountFree);
                String quantity = round((p.multiply(free)).toPlainString());
                UserInterface.displayMsg(quantity);
                String minNot = round(Double.toString(Double.parseDouble(quantity) * Double.parseDouble(price)));

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
                    .newOrder(marketBuy(tradePair,quantity).newOrderRespType(NewOrderResponseType.FULL));
                    List<Trade> fills = newOrderResponse.getFills();
                    System.out.println(newOrderResponse.getClientOrderId());
                } catch (Exception e) {
                    UserInterface.displayError(e.getMessage());
                }
                UserInterface.displayMsg(bestPath.get(i) + "" + bestPath.get(i + 1));
                UserInterface.displayMsg("Amount of "+ bestPath.get(i)+" available: " + amountFree);
                UserInterface.displayMsg("Price: " + price);
                UserInterface.displayMsg("min price: " + minPrice);
                UserInterface.displayMsg("price step: " + tickSize);
                UserInterface.displayMsg("price filter status: " + (Double.parseDouble(price) - minPrice) % tickSize);
                UserInterface.displayMsg("Min notional: " + minNot);
                UserInterface.displayMsg("MIN_NOTIONAL: " + minNotional.getMinNotional());
                UserInterface.displayMsg("Amount available: " + amountFree);
                UserInterface.displayMsg("Quantity: " + quantity);
                UserInterface.displayMsg("min Quantity: " + minQty);
                UserInterface.displayMsg("qty step: " + stepSize);
                UserInterface.displayMsg("market qty step: " + marketStepSize);
                UserInterface.displayMsg("market min Quantity: " + marketMinQty);
                UserInterface.displayMsg("qty filter stat: " + (Double.parseDouble(quantity) - minQty) % stepSize);
                UserInterface.displayMsg("\n----------------------\n");
            }
            priceCount++;
        }
        // }
        // else
        //     UserInterface.displayError("No profit!");

        UserInterface.displayMsg("------------");
    }
}
