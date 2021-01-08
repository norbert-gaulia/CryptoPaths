CryptoPaths

A console application that uses the official [Binance java API](https://github.com/binance-exchange/binance-java-api) to determine the most profitable exchange paths between currencies or assets. 

The program uses the depth-first seacrh algorithm to retrieve all the paths between a given asset pair and then outputs the paths in the order of profitability.

The eventual plan is to introduce a feature that allows the user to trade using the most profitable path.

Running the program:

javac -cp .;binance-java-api-master.jar;JSON-java-master.jar PathFinder.java

java -cp .;binance-java-api-master.jar;JSON-java-master.jar PathFinder
 