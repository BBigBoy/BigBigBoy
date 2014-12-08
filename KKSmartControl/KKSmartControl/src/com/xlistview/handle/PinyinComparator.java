package com.xlistview.handle;

import java.util.Comparator;

import com.xlistview.model.PlayListItemModel;
 
/**
 * 
 * @author Mr.Z
 */
public class PinyinComparator implements Comparator<PlayListItemModel> {

    public int compare(PlayListItemModel o1, PlayListItemModel o2) {
	if (o1.getSortLetters().equals("@") || o2.getSortLetters().equals("#")) {
	    return -1;
	} else if (o1.getSortLetters().equals("#")
		|| o2.getSortLetters().equals("@")) {
	    return 1;
	} else {
	    return o1.getSortLetters().compareTo(o2.getSortLetters());
	}
    }

}
