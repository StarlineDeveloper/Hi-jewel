package com.hijewel.models;

@SuppressWarnings("WeakerAccess")
public class SymbolModel {

    private int SymbolId;
    private String Symbol, Bid, Ask, High, Low, Stock, Status, ProductType, isDisplay, W_display, Source, bidStatus, askStatus;

    public SymbolModel() {
    }

    @SuppressWarnings("unused")
    public SymbolModel(int symbolId, String symbol, String bid, String ask, String high, String low, String stock, String status, String productType, String isDisplay, String w_display, String source) {
        SymbolId = symbolId;
        Symbol = symbol;
        Bid = bid;
        Ask = ask;
        High = high;
        Low = low;
        Stock = stock;
        Status = status;
        ProductType = productType;
        this.isDisplay = isDisplay;
        W_display = w_display;
        Source = source;
    }

    public int getSymbolId() {
        return SymbolId;
    }

    public void setSymbolId(int symbolId) {
        SymbolId = symbolId;
    }

    public String getSymbol() {
        return Symbol;
    }

    public void setSymbol(String symbol) {
        Symbol = symbol;
    }

    public String getBid() {
        return Bid;
    }

    public void setBid(String bid) {
        Bid = bid;
    }

    public String getAsk() {
        return Ask;
    }

    public void setAsk(String ask) {
        Ask = ask;
    }

    public String getHigh() {
        return High;
    }

    public void setHigh(String high) {
        High = high;
    }

    public String getLow() {
        return Low;
    }

    public void setLow(String low) {
        Low = low;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getProductType() {
        return ProductType;
    }

    public void setProductType(String productType) {
        ProductType = productType;
    }

    public String getIsDisplay() {
        return isDisplay;
    }

    public void setIsDisplay(String isDisplay) {
        this.isDisplay = isDisplay;
    }

    public String getW_display() {
        return W_display;
    }

    public void setW_display(String w_display) {
        W_display = w_display;
    }

    public String getSource() {
        return Source;
    }

    public void setSource(String source) {
        Source = source;
    }

    public String getStock() {
        return Stock;
    }

    public void setStock(String stock) {
        Stock = stock;
    }

    public String getBidStatus() {
        return bidStatus;
    }

    public void setBidStatus(String bidStatus) {
        this.bidStatus = bidStatus;
    }

    public String getAskStatus() {
        return askStatus;
    }

    public void setAskStatus(String askStatus) {
        this.askStatus = askStatus;
    }
}
