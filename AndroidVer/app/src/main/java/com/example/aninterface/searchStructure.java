package com.example.aninterface;

public class searchStructure {
    private String keyword;
    private String Category;
    private String Condition;
    private String Shipping;
    private boolean isNearby;
    private int mils;
    private String zipcode;
    private String localZipcode;

    public searchStructure(String keyword, String category, String condition, String shipping, boolean isNearby, int mils, String zipcode, String localZipcode) {
        this.keyword = keyword;
        Category = category;
        Condition = condition;
        Shipping = shipping;
        this.isNearby = isNearby;
        this.mils = mils;
        this.zipcode = zipcode;
        this.localZipcode = localZipcode;
    }

    @Override
    public String toString() {
        return "searchStructure{" +
                "keyword='" + keyword + '\'' +
                ", Category='" + Category + '\'' +
                ", Condition='" + Condition + '\'' +
                ", Shipping='" + Shipping + '\'' +
                ", isNearby=" + isNearby +
                ", mils=" + mils +
                ", zipcode='" + zipcode + '\'' +
                ", localZipcode='" + localZipcode + '\'' +
                '}';
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getCategory() {
        return Category;
    }

    public void setCategory(String category) {
        Category = category;
    }

    public String getCondition() {
        return Condition;
    }

    public void setCondition(String condition) {
        Condition = condition;
    }

    public String getShipping() {
        return Shipping;
    }

    public void setShipping(String shipping) {
        Shipping = shipping;
    }

    public boolean isNearby() {
        return isNearby;
    }

    public void setNearby(boolean nearby) {
        isNearby = nearby;
    }

    public int getMils() {
        return mils;
    }

    public void setMils(int mils) {
        this.mils = mils;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    public String getLocalZipcode() {
        return localZipcode;
    }

    public void setLocalZipcode(String localZipcode) {
        this.localZipcode = localZipcode;
    }
}
