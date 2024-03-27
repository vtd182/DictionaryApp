package org.example.Helper;

import org.example.Models.Word;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.TreeMap;

public class XMLHandler extends DefaultHandler {
    private final TreeMap<String, Word> dictionary;
    private String currentElement;
    private String currentWord;
    private StringBuilder currentMeanings;

    public XMLHandler() {
        dictionary = new TreeMap<>();
        currentElement = "";
        currentWord = "";
        currentMeanings = new StringBuilder();
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) {
        currentElement = qName;
        if ("record".equals(qName)) {
            currentWord = "";
            currentMeanings = new StringBuilder();
        }
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        if ("word".equals(currentElement)) {
            currentWord += new String(ch, start, length);
        } else if ("meaning".equals(currentElement)) {
            currentMeanings.append(new String(ch, start, length));
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) {
        if ("record".equals(qName)) {
            try {
                dictionary.put(currentWord, Word.parseFromString(currentMeanings.toString()));
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Error while parsing word: " + currentWord);
            }
        }
        currentElement = "";
    }

    public TreeMap<String, Word> getDictionary() {
        return dictionary;
    }
}

