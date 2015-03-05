package com.kksmartcontrol.util;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.app.Activity;

public class FragmentUtil {

	/**
	 * 根据tag移除fragment，不带移除动画
	 * 
	 * @param context
	 * @param tag
	 * @return
	 */
	public static Boolean removeFragmentByTag(Context context, String tag) {
		return removeFragmentByTag(context, tag, 0);
	}

	/**
	 * 根据tag移除fragment，带移除动画
	 * 
	 * @param context
	 * @param tag
	 * @param exitTansaction
	 * @return
	 */
	public static Boolean removeFragmentByTag(Context context, String tag,
			int exitTansaction) {
		Fragment fragment = ((Activity) context).getFragmentManager()
				.findFragmentByTag(tag);
		if (fragment != null) {
			FragmentTransaction ft = ((Activity) context).getFragmentManager()
					.beginTransaction();
			ft.setCustomAnimations(0, exitTansaction);
			ft.remove(fragment);
			ft.commit();
			return true;
		}
		return false;
	}

	/**
	 * 到底fragment可见与不可见时，怎么区分
	 * 
	 * @param context
	 * @param tag
	 * @param exitTansaction
	 * @return 有正在显示的控制fragment，则返回true
	 */
	public static Boolean removeVisibleFragmentByTag(Context context,
			String tag, int exitTansaction) {
		Fragment fragment = ((Activity) context).getFragmentManager()
				.findFragmentByTag(tag);
		if (fragment != null && fragment.isVisible()) {
			FragmentTransaction ft = ((Activity) context).getFragmentManager()
					.beginTransaction();
			ft.setCustomAnimations(0, exitTansaction);
			ft.remove(fragment);
			ft.commit();
			return true;
		}
		return false;
	}

	/**
	 * @param context
	 * @param tag
	 * @return 判断有tag指定的Fragment是否存在
	 */
	public static boolean hasFragmentSpecifiedByTag(Context context, String tag) {
		Fragment fragment = ((Activity) context).getFragmentManager()
				.findFragmentByTag(tag);
		if (fragment != null) {
			return true;
		}
		return false;
	}

	/**
	 * @param context
	 * @param tag
	 *            隐藏fragment。 调用这个方法时应该确认这个fragment是存在的
	 */
	public static void hideFragmentByTag(Context context, String tag) {
		Fragment fragment = ((Activity) context).getFragmentManager()
				.findFragmentByTag(tag);
		if (fragment != null) {
			FragmentTransaction ft = ((Activity) context).getFragmentManager()
					.beginTransaction();
			ft.hide(fragment);
			ft.commit();
		}
	}

	/**
	 * @param context
	 * @param tag
	 *            显示fragment。 调用这个方法时应该确认这个fragment是存在的. 如果不存在则返回false
	 */
	public static boolean showExistingFragmentByTag(Context context, String tag) {
		Fragment fragment = ((Activity) context).getFragmentManager()
				.findFragmentByTag(tag);
		if (fragment != null) {
			FragmentTransaction ft = ((Activity) context).getFragmentManager()
					.beginTransaction();
			ft.show(fragment);
			ft.commit();
			return true;
		}
		return false;
	}

	public static void addFragmentWithTag(Context context,
			Class<?> fragmentClass, int containerViewId, String tag,
			int enterTansaction) {
		Fragment fragment = ((Activity) context).getFragmentManager()
				.findFragmentByTag(tag);
		if (fragment == null)
			try {
				fragment = (Fragment) fragmentClass.newInstance();
			} catch (InstantiationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		FragmentTransaction ft = ((Activity) context).getFragmentManager()
				.beginTransaction();
		ft.setCustomAnimations(enterTansaction, 0);
		ft.add(containerViewId, fragment, tag);
		ft.show(fragment);
		ft.commit();
	}
}
