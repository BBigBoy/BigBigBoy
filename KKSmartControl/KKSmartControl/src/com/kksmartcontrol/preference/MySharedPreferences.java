package com.kksmartcontrol.preference;

import android.content.Context;
import android.content.SharedPreferences;

/** 
 * @ClassName: MySharedPreferences 
 *
 */
public class MySharedPreferences {
	public static final String KONKA_PJ_INFO = "konka_pj_info";
	
	public static final String USER_ID = "user_id";
	public static final String IS_OPEN = "is_open";	
	public static final String IS_PREVIEW_OPEN = "is_preview_open";
	
	
	public static void SaveUserId(String userid, Context context){
		if(userid == null  ||  context == null){
			return;
		}
		SharedPreferences setting = context.getSharedPreferences(KONKA_PJ_INFO, 0);
		setting.edit()
		.putString(USER_ID, userid)
		.commit();
	}
	
	public static String GetUserId(Context context){
		if(context == null)
			return null;
		SharedPreferences setting = context.getSharedPreferences(KONKA_PJ_INFO, 0);
		String userId = setting.getString(USER_ID, "");
		if(userId.equals("")){
			return null;
		}
		return userId;
	}
	
	public static void SaveIsOpen(boolean IsOpen, Context context){
		if(context == null){
			return;
		}
		//Debug.debug("MySharedPreferences", "isopen = " + IsOpen);
		SharedPreferences setting = context.getSharedPreferences(KONKA_PJ_INFO, 0);
		setting.edit()
		.putBoolean(IS_OPEN, IsOpen).commit();
		//Debug.debug("SharePreferences","save isopen data ok");
	}
	
	public static boolean GetIsOpen(Context context){
		if(context == null)
			return false;
		SharedPreferences setting = context.getSharedPreferences(KONKA_PJ_INFO, 0);
		boolean IsOpen = setting.getBoolean(IS_OPEN, false);
		
		return IsOpen;
	}
	
	public static void SaveHelpPreviewOpen(boolean IsOpen, Context context){
		if(context == null){
			return;
		}
		//Debug.debug("MySharedPreferences", "isopen = " + IsOpen);
		SharedPreferences setting = context.getSharedPreferences(KONKA_PJ_INFO, 0);
		setting.edit()
		.putBoolean(IS_PREVIEW_OPEN, IsOpen)
		.commit();
		//Debug.debug("SharePreferences","save isopen data ok");
	}
	
	public static boolean GetHelpPreviewOpen(Context context){
		if(context == null)
			return false;
		SharedPreferences setting = context.getSharedPreferences(KONKA_PJ_INFO, 0);
		boolean IsOpen = setting.getBoolean(IS_PREVIEW_OPEN, false);
		//Debug.debug("SharePreferences","get isopen data = " + IsOpen);
		return IsOpen;
	}
}
