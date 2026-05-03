package com.racconanalytics.nlpservicejava.dto;

import java.util.List;

public class InferenceResponse {
    private String originalQuery;
    private List<String> keywords;
    private List<String> expandedQueries;
    private String language;

    // Getters and setters
    public String getOriginalQuery() { return originalQuery; }
    public void setOriginalQuery(String originalQuery) { this.originalQuery = originalQuery; }
    public List<String> getKeywords() { return keywords; }
    public void setKeywords(List<String> keywords) { this.keywords = keywords; }
    public List<String> getExpandedQueries() { return expandedQueries; }
    public void setExpandedQueries(List<String> expandedQueries) { this.expandedQueries = expandedQueries; }
    public String getLanguage() { return language; }
    public void setLanguage(String language) { this.language = language; }
}