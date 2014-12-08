package com.xlistview.model;

public class PlayListItemModel {

    private String source_Name; // 显示的视频名称
    private String format; // 显示的视频格式
    private String time_Length; // 显示的视频时长
    private String sortLetters; // 显示数据拼音的首字母

    public String getName() {
	return source_Name;
    }

    public void setName(String name) {
	this.source_Name = name;
    }

    public String getFormat() {
	return format;
    }

    public void setFormat(String format) {
	this.format = format;
    }

    public String getTime_Length() {
	return time_Length;
    }

    public void setTime_Length(String time_Length) {
	this.time_Length = time_Length;
    }

    public String getSortLetters() {
	return sortLetters;
    }

    public void setSortLetters(String sortLetters) {
	this.sortLetters = sortLetters;
    }
}