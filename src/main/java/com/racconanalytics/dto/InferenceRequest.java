package com.racconanalytics.nlpservicejava.dto;

public class InferenceRequest {
    private String query;
    private int keywordsCount;
    private int expandedQueriesCount;

    // Getters and setters
    public String getQuery() { return query; }
    public void setQuery(String query) { this.query = query; }
    public int getKeywordsCount() { return keywordsCount; }
    public void setKeywordsCount(int keywordsCount) { this.keywordsCount = keywordsCount; }
    public int getExpandedQueriesCount() { return expandedQueriesCount; }
    public void setExpandedQueriesCount(int expandedQueriesCount) { this.expandedQueriesCount = expandedQueriesCount; }
}