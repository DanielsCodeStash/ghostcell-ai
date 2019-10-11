package com.ghostcell.priomodel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PrioList {

    private List<FactoryPrio> prioLists = new ArrayList<>();

    public void print() {
        String s = "";
        for(int i=0; i < prioLists.size(); i++) {
            boolean isLast = i == prioLists.size() - 1;

            s += prioLists.get(i).toString();

            if(!isLast) {
                s += "\n";
            }
        }
        System.err.println(s);
    }

    public PrioList sort() {
        Collections.sort(prioLists);
        return this;
    }

    public List<FactoryPrio> get() {
        return prioLists;
    }


    public void add(FactoryPrio factoryPrio) {
        prioLists.add(factoryPrio);
    }
}
