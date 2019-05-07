package com.example.aninterface;
import java.util.ArrayList;

public class resultItems {
    private String image = "N/A";
    private String title = "N/A";
    private String zipcode = "N/A";
    private String shipping = "N/A";
    private String condition = "N/A";
    private String price = "N/A";
    private boolean isWish = false;
    private String itemId = null;

    //sold by
    private String storeName = null;
    private int feedbackScore = -1;
    private int popularity = -1;
    private String feedbackStar = null;
    private String gobalShipping = null;
    private String handlingTime = null;
    private String storeURL = null;

    //return
    private String ShippedBy = null;
    private String RefundMe = null;
    private String ReturnWithin = null;
    private String Policy = null;

    //detail
    private ArrayList<String> galleyPhotos = new ArrayList<>();
    private String subtitle = null;
    private String brand = null;
    private ArrayList<String> specifications = new ArrayList<>();
    private ArrayList<String> GoogleImages = new ArrayList<>();


    public resultItems(String image, String title, String zipcode, String shipping, String condition, String price, boolean isWish, String itemId, String GobalShipping, String handlingTime, String storeURL, String storeName, int feedbackScore, int positiveFeedbackPercent, String feedbackRatingStar) {
        this.image = image;
        this.title = title;
        this.zipcode = zipcode;
        this.shipping = shipping;
        this.condition = condition;
        this.price = price;
        this.isWish = isWish;
        this.itemId = itemId;
        this.gobalShipping = GobalShipping;
        this.handlingTime = handlingTime;
        this.storeURL = storeURL;
        this.storeName = storeName;
        this.feedbackScore = feedbackScore;
        this.popularity = positiveFeedbackPercent;
        this.feedbackStar = feedbackRatingStar;
    }

    public int getFeedbackScore() {
        return feedbackScore;
    }

    public void setFeedbackScore(int feedbackScore) {
        this.feedbackScore = feedbackScore;
    }

    public int getPopularity() {
        return popularity;
    }

    public void setPopularity(int popularity) {
        this.popularity = popularity;
    }

    public String getFeedbackStar() {
        return feedbackStar;
    }

    public void setFeedbackStar(String feedbackStar) {
        this.feedbackStar = feedbackStar;
    }

    public String getGobalShipping() {
        return gobalShipping;
    }

    public void setGobalShipping(String gobalShipping) {
        this.gobalShipping = gobalShipping;
    }

    public String getHandlingTime() {
        return handlingTime;
    }

    public void setHandlingTime(String handlingTime) {
        this.handlingTime = handlingTime;
    }

    public String getStoreURL() {
        return storeURL;
    }

    public void setStoreURL(String storeURL) {
        this.storeURL = storeURL;
    }

    public ArrayList<String> getGalleyPhotos() {
        return galleyPhotos;
    }

    public void setGalleyPhotos(ArrayList<String> galleyPhotos) {
        this.galleyPhotos = galleyPhotos;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public ArrayList<String> getSpecifications() {
        return specifications;
    }

    public void setSpecifications(ArrayList<String> specifications) {
        this.specifications = specifications;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getShippedBy() {
        return ShippedBy;
    }

    public void setShippedBy(String shippedBy) {
        ShippedBy = shippedBy;
    }

    public String getRefundMe() {
        return RefundMe;
    }

    public void setRefundMe(String refundMe) {
        RefundMe = refundMe;
    }

    public String getReturnWithin() {
        return ReturnWithin;
    }

    public void setReturnWithin(String returnWithin) {
        ReturnWithin = returnWithin;
    }

    public String getPolicy() {
        return Policy;
    }

    public void setPolicy(String policy) {
        Policy = policy;
    }

    public ArrayList<String> getGoogleImages() {
        return GoogleImages;
    }

    public void setGoogleImages(ArrayList<String> googleImages) {
        GoogleImages = googleImages;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    public String getShipping() {
        return shipping;
    }

    public void setShipping(String shipping) {
        this.shipping = shipping;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public boolean isWish() {
        return isWish;
    }

    public void setWish(boolean wish) {
        isWish = wish;
    }
}
