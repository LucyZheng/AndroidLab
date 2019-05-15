package com.example.isszym.actionbar;

/**
 * Created by 64635 on 2019/5/15.
 */

public class WordRec {
    private  String word;
    private String explanation;
    private int level;
    WordRec(){}
    WordRec(String word,String explanation,int level){
        this.word=word;
        this.explanation=explanation;
        this.level=level;
    }
    public String getWord(){return word;}
    public void setWord(String word){this.word=word;}
    public String getExplanation(){return explanation;}
    public void setExplanation(String explanation){this.explanation=explanation;}
    public int getLevel(){return level;}
    public void  setLevel(int level){this.level=level;}

}
