package com.racconanalytics.nlpservicejava.service;

import org.springframework.stereotype.Service;

@Service
public class PromptBuilder {

    public String buildInferencePrompt(String query, int keywordsCount, int expandedQueriesCount) {
        return "You are an expert in social networks. Analyze the following user search query and respond in the SAME language as the query.\n\n" +
                "User query: \"" + query + "\"\n\n" +
                "+ You need to infer keywords/hashtags and similar queries that users might also search for, based on the original query.\n\n" +
                "Return ONLY a valid raw JSON object with no additional text, no markdown, no code blocks, no explanations. The JSON must follow exactly this structure:\n" +
                "{\n" +
                "\t\t\"keywords\": [/* " + keywordsCount + " relevant keywords related to the query */],\n" +
                "\t\t\"expanded_queries\": [/* " + expandedQueriesCount + " alternative ways to express the same query */],\n" +
                "\t\t\"language\": \"the detected language of the query\"\n" +
                "}\n\n" +
                "Goals:\n" +
                "\t* Keywords must be useful for analysis of trends and user intent in social networks.\n" +
                "\t* Expanded queries must be alternative reformulations of the original query that a user might also search for.\n" +
                "\t* Keywords will be used for filling the fields \"keywords\" and \"hashtags\" in social networks.\n\n" +
                "Rules:\n" +
                "- Language of keywords and expanded queries must match the detected language of the original query\n" +
                "- NO combine languages in one same keyword or one same expanded query, each must be in the detected language of the original query\n" +
                "- keywords must be relevant short terms (max 3 words), topics and concepts related to the query\n" +
                "- expanded_queries must be alternative reformulations of the original query\n" +
                "- keywords and expanded_queries must be gramatically correct and natural-sounding in the detected language\n" +
                "- Return exactly " + keywordsCount + " keywords and exactly " + expandedQueriesCount + " expanded_queries\n" +
                "- NO additional text outside the JSON\n" +
                "- NO markdown formatting, code blocks, explanations, or ''' at the beginning or end, ONLY the raw JSON object as specified above.\n" +
                "- The first character of the response must be \"{\" and the last character must be \"}\" to ensure it's a valid JSON object.";
    }
}