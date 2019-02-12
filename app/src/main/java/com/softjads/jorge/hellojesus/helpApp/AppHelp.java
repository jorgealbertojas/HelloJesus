package com.softjads.jorge.hellojesus.helpApp;

import com.softjads.jorge.hellojesus.data.local.help.Help;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;


public class AppHelp {
    @SerializedName("config")
    @Expose
    private List<Help> helps = new ArrayList<Help>();

    public List<Help> getConfigHelp() {
        return helps;
    }

    public void setContentHelp(List<Help> helps) {
        this.helps = helps;
    }
}
