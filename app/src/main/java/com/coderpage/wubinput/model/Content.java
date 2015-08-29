package com.coderpage.wubinput.model;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author abner-l
 * @since 2015-08-29
 */
public class Content {
    private final List<Word> words = new ArrayList<>();

    public void addWord(Word word){
        words.add(word);
    }

    public void removeWord(Word word){
        words.remove(word);
    }

    public List<Word> getWords() {
        return words;
    }

    public int size(){
        return words.size();
    }
}
