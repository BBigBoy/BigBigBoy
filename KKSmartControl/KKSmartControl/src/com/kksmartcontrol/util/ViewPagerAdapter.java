/**   
* @Title: ViewPagerAdapter.java 
* @Package com.konka.Home 
* @Description: TODO(用一句话描述该文件做什么) 
* @author llb   
* @date 2013-3-29 下午2:22:33 
* @version V1.0   
*/
package com.kksmartcontrol.util;

import java.util.List;

import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

/** 
 * @ClassName: ViewPagerAdapter 
 * @Description: TODO(这里用一句话描述这个类的作用) 
 * @author llb
 * @date 2013-3-29 下午2:22:33 
 *  
 */
public class ViewPagerAdapter extends PagerAdapter {

	// 界面列表
	private List<View> views;

	public ViewPagerAdapter(List<View> views) {
		this.views = views;
	}

	// 销毁arg1位置的界面
	@Override
	public void destroyItem(View arg0, int arg1, Object arg2) {
		((ViewPager) arg0).removeView(views.get(arg1));
	}

	@Override
	public void finishUpdate(View arg0) {
		// TODO Auto-generated method stub

	}

	// 获得当前界面数
	public int getCount() {
		if (views != null) {
			return views.size();
		}

		return 0;
	}

	// 初始化arg1位置的界面
	public Object instantiateItem(View arg0, int arg1) {

		((ViewPager) arg0).addView(views.get(arg1));

		return views.get(arg1);
	}

	// 判断是否由对象生成界面
	public boolean isViewFromObject(View arg0, Object arg1) {
		return (arg0 == arg1);
	}

	public void restoreState(Parcelable arg0, ClassLoader arg1) {
		// TODO Auto-generated method stub 
	}

	public Parcelable saveState() {
		// TODO Auto-generated method stub
		return null;
	}

	public void startUpdate(View arg0) {
		// TODO Auto-generated method stub 
	}

}