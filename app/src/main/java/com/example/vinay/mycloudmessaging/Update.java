package com.example.vinay.mycloudmessaging;

import android.support.annotation.NonNull;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Update implements Comparable<Update>{
    private String title;
    private String link;
    private String info;
    private Date date;
    private String stringDate;
    private List<String> categories;

    public Update(){
    }
    public Update(String title, String link, String info, String date, List<String> categories) {
        this.title = title;
        this.link = link;
        this.info = info;
        this.date = parseDate(date);
        this.stringDate = date;
        this.categories = categories;
    }
    public String getTitle() {
        return title;
    }
    public String getLink() {
        return link;
    }
    public String getInfo() {
        return info;
    }
    public Date getDate() { return date;}
    public String getStringDate() { return stringDate; }
    public List<String> getCategories() {
        return categories;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public void setLink(String link) {
        this.link = link;
    }
    public void setInfo(String info) {
        this.info = info;
    }
    public void setDate(String date) { this.stringDate = date; this.date = parseDate(date);}
    public void setCategories(List<String> categories) {
        this.categories = categories;
    }
    public String toString(){
        return this.title+" "+this.link+" "+this.info+ this.stringDate;
    }

    @Override
    public int compareTo(@NonNull Update update) {
        return getDate().compareTo(update.getDate());
    }

    public Date parseDate(String date){
        DateFormat format = new SimpleDateFormat("MMMM d, yyyy", Locale.ENGLISH);
        try {
            return (Date)format.parse(date);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }
    }
}
