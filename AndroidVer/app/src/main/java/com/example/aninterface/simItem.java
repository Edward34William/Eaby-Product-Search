package com.example.aninterface;

import java.util.Comparator;

public class simItem {
    private String image;
    private String title;
    private int daysleft;
    private double shipping;
    private double price;
    private String _viewItemURL;

    @Override
    public String toString() {
        return "simItem{" +
                "image='" + image + '\'' +
                ", title='" + title + '\'' +
                ", daysleft=" + daysleft +
                ", shipping=" + shipping +
                ", price=" + price +
                ", _viewItemURL='" + _viewItemURL + '\'' +
                '}';
    }

    public String get_viewItemURL() {
        return _viewItemURL;
    }

    public void set_viewItemURL(String _viewItemURL) {
        this._viewItemURL = _viewItemURL;
    }

    simItem(String image, String title, String _viewItemURL, int daysleft, double shipping, double price){
        this.image = image;
        this.title = title;
        this.daysleft = daysleft;
        this.shipping = shipping;
        this.price = price;
        this._viewItemURL = _viewItemURL;

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

    public int getDaysleft() {
        return daysleft;
    }

    public void setDaysleft(int daysleft) {
        this.daysleft = daysleft;
    }

    public double getShipping() {
        return shipping;
    }

    public void setShipping(double shipping) {
        this.shipping = shipping;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public static Comparator<simItem> itemNameComparatorAsc = new Comparator<simItem>() {

        public int compare(simItem s1, simItem s2) {
            return s1.getTitle().toUpperCase().compareTo(s2.getTitle().toUpperCase());
        }
    };

    public static Comparator<simItem> itemNameComparatorDes = new Comparator<simItem>() {

        public int compare(simItem s1, simItem s2) {
            return s2.getTitle().toUpperCase().compareTo(s1.getTitle().toUpperCase());
        }
    };

    public static Comparator<simItem> itemPriceComparatorAsc = new Comparator<simItem>() {

        public int compare(simItem s1, simItem s2) {
            return ((int)s1.getPrice()*100 - (int)s2.getPrice()*100);
        }
    };

    public static Comparator<simItem> itemPriceComparatorDes = new Comparator<simItem>() {

        public int compare(simItem s1, simItem s2) {
            return ((int)s2.getPrice()*100 - (int)s1.getPrice()*100);
        }
    };

    public static Comparator<simItem> itemDaysComparatorAsc = new Comparator<simItem>() {

        public int compare(simItem s1, simItem s2) {
            return (s1.getDaysleft() - s2.getDaysleft());
        }
    };

    public static Comparator<simItem> itemDaysComparatorDes = new Comparator<simItem>() {

        public int compare(simItem s1, simItem s2) {
            return (s2.getDaysleft() - s1.getDaysleft());
        }
    };


}
