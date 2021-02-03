package utils;

import com.binance.api.client.BinanceApiClientFactory;
import com.binance.api.client.BinanceApiRestClient;
import com.binance.api.client.domain.account.*;
import com.binance.api.client.domain.general.ExchangeInfo;
import com.binance.api.client.domain.general.FilterType;
import com.binance.api.client.domain.general.SymbolFilter;
import com.binance.api.client.domain.general.SymbolInfo;
import com.binance.api.client.domain.TimeInForce;
import static com.binance.api.client.domain.account.NewOrder.limitBuy;
import java.util.List;

public class Exchange {
 private final static BinanceApiClientFactory factory = BinanceApiClientFactory.newInstance(
         "API_KEY",
         "SECRET_KEY");
 private final static BinanceApiRestClient client = factory.newRestClient();

 public static void trade(List<Path> pathList, double directPrice) {
     try{
         Account account = client.getAccount();
         UserInterface.displayMsg("------------");
         // the best path - will be used to trade
         List<String> bestPath, bestPricePath;
         double overallExchange;
         String tradePair, price, amountFree;
         int priceCount = 0;
         // exchange info to keep track of the asset filters
         ExchangeInfo exchangeInfo = client.getExchangeInfo();
         boolean done = false;
         for (Path path : pathList) {
             priceCount = 0;
             bestPath = path.getAssetList();
             bestPricePath = path.getPriceList();
             overallExchange = path.getOverallExchange();
             if (!done) {
                 if (overallExchange >= directPrice) {
                     for (int i = 0; i < bestPath.size(); i++) {
                         if (i < bestPath.size() - 1) {
                             tradePair = bestPath.get(i) + "" + bestPath.get(i + 1);
                             SymbolInfo symbolInfo = exchangeInfo.getSymbolInfo(tradePair);
                             // get price filter data
                             SymbolFilter priceFilter = symbolInfo.getSymbolFilter(FilterType.PRICE_FILTER);
                             double minPrice = Double.valueOf(priceFilter.getMinPrice()),
                                     tickSize = Double.valueOf(priceFilter.getTickSize());
                             // get the trade price
                             price = round(bestPricePath.get(priceCount));
                             // validate price
                             if ((Double.valueOf(price) - minPrice) % tickSize > tickSize) {
                                 do {
                                     double rem = (Double.valueOf(price) - minPrice) % tickSize;
                                     price = String.valueOf((Double.valueOf(price) - rem));
                                 } while ((Double.valueOf(price) - minPrice) % tickSize > tickSize);
                             }

                             amountFree = round(account.getAssetBalance(bestPath.get(i)).getFree());

                             // get quantity filter data
                             SymbolFilter lotSize = symbolInfo.getSymbolFilter(FilterType.LOT_SIZE);
                             double minQty = Double.valueOf(lotSize.getMinQty()),
                                     stepSize = Double.valueOf(lotSize.getStepSize());
                             SymbolFilter minNotional = symbolInfo.getSymbolFilter(FilterType.MIN_NOTIONAL);
                             SymbolFilter marketLot = symbolInfo.getSymbolFilter(FilterType.MARKET_LOT_SIZE);
                             double marketMinQty = Double.valueOf(marketLot.getMinQty()),
                                     marketStepSize = Double.valueOf(marketLot.getStepSize());
                             // get the trade quantity
                             String quantity = round(String.valueOf(Double.valueOf(price) * Double.valueOf(amountFree)));
                             // validate quantity
                             if ((Double.valueOf(quantity) - minQty) % stepSize > stepSize) {
                                 do {
                                     double rem = (Double.valueOf(quantity) - minQty) % stepSize;
                                     price = String.valueOf((Double.valueOf(quantity) - rem));
                                 } while ((Double.valueOf(quantity) - minQty) % stepSize > stepSize);
                             }
                             String minNot = round(Double.toString(Double.valueOf(quantity) * Double.valueOf(price)));

                             // validate min_notional
                             //if (Double.valueOf(minNot) < Double.valueOf(minNotional.getMinNotional()))

                             UserInterface.displayMsg(tradePair);
                             UserInterface.displayMsg("Amount of " + bestPath.get(i) + " available: " + amountFree);
                             UserInterface.displayMsg("Price: " + price);
                             UserInterface.displayMsg("min price: " + minPrice);
                             UserInterface.displayMsg("price step: " + tickSize);
                             UserInterface.displayMsg(
                                     "price filter status: " + (Double.valueOf(price) - minPrice) % tickSize);
                             UserInterface.displayMsg("Min notional: " + minNot);
                             UserInterface.displayMsg("MIN_NOTIONAL: " + minNotional.getMinNotional());
                             UserInterface.displayMsg("Amount available: " + amountFree);
                             UserInterface.displayMsg("Quantity: " + quantity);
                             UserInterface.displayMsg("min Quantity: " + minQty);
                             UserInterface.displayMsg("qty step: " + stepSize);
                             UserInterface.displayMsg("market qty step: " + marketStepSize);
                             UserInterface.displayMsg("market min Quantity: " + marketMinQty);
                             UserInterface
                                     .displayMsg("qty filter stat: " + (Double.valueOf(quantity) - minQty) % stepSize);
                             // trade
                             try {
                                 NewOrderResponse newOrderResponse = client
                                         .newOrder(limitBuy(tradePair, TimeInForce.GTC, quantity, price)
                                                 .newOrderRespType(NewOrderResponseType.FULL));
                                 System.out.println(newOrderResponse);
                                 done = true;
                             } catch (Exception e) {
                                 UserInterface.displayError(e.getMessage());
                                 // ensures that the next path is visited
                                 done = false;
                             }
                             UserInterface.displayMsg("\n----------------------\n");
                         }
                         priceCount++;
                     }
                 } else
                     UserInterface.displayError("No profit!");
             }
         }
         UserInterface.displayMsg("------------");
     }catch(Exception e){
         UserInterface.displayError(e.getLocalizedMessage());
     }
 }

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
         }
     }
     return s2;
 }

}
