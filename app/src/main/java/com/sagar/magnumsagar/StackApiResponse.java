package com.sagar.magnumsagar;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class StackApiResponse {
   @SerializedName("items")
   @Expose
   public List<Item> items;
   @SerializedName("total_count")
   @Expose
   public int total_count;
   @SerializedName("incomplete_results")
   @Expose
   public boolean incomplete_results;

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public int getTotal_count() {
        return total_count;
    }

    public void setTotal_count(int total_count) {
        this.total_count = total_count;
    }

    public boolean isIncomplete_results() {
        return incomplete_results;
    }

    public void setIncomplete_results(boolean incomplete_results) {
        this.incomplete_results = incomplete_results;
    }


}
