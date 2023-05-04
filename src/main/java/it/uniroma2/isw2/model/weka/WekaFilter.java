package it.uniroma2.isw2.model.weka;

import weka.filters.Filter;

public class WekaFilter {

    private Filter filter ;
    private String searchMethod ;
    private String directionString ;


    public WekaFilter(Filter filter, String searchMethod, String directionString) {
        this.filter = filter ;
        this.searchMethod = searchMethod ;
        this.directionString = directionString ;
    }

    public Filter getFilter() {
        return filter;
    }

    public void setFilter(Filter filter) {
        this.filter = filter;
    }

    public String getFilterName() {
        return filter.getClass().getSimpleName();
    }

    public String getSearchMethod() {
        return searchMethod;
    }

    public void setSearchMethod(String searchMethod) {
        this.searchMethod = searchMethod;
    }

    public String getDirectionString() {
        return directionString;
    }


}
