package com.lockscreen;

import android.R.integer;

public class Word {
    
    //private variables
    int _id;
    String _word;
    String _mean;
     
    // Empty constructor
    public Word(){
         
    }
    public Word(int _id){
    	this._id = _id;
    }
    // constructor
    public Word(int id, String _word, String _mean){
        this._id = id;
        this._word = _word;
        this._mean = _mean;
    }
     
    // constructor
    public Word(String _word, String _mean){
        this._word = _word;
        this._mean = _mean;
    }
	public int get_id() {
		return _id;
	}
	public void set_id(int _id) {
		this._id = _id;
	}
	public String get_word() {
		return _word;
	}
	public void set_word(String _word) {
		this._word = _word;
	}
	public String get_mean() {
		return _mean;
	}
	public void set_mean(String _mean) {
		this._mean = _mean;
	}
  
}
