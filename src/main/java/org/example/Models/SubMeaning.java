package org.example.Models;

public class SubMeaning {
    private final String subMeaning;
    private final String definition;

    public SubMeaning(String subMeaning, String definition) {
        this.subMeaning = subMeaning;
        this.definition = definition;
    }

    public String getSubMeaning() {
        return subMeaning;
    }

    public String getDefinition() {
        return definition;
    }
}
