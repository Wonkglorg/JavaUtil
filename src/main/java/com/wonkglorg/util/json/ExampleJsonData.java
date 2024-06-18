package com.wonkglorg.util.json;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ExampleJsonData {
    @JsonProperty("id")
    private String id;
    @JsonProperty("title")
    private String title;

}
