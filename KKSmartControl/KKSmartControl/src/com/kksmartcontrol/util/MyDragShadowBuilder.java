package com.kksmartcontrol.util;

import android.graphics.Point;
import android.util.Log;
import android.view.View;
import android.view.View.DragShadowBuilder;

public class MyDragShadowBuilder extends DragShadowBuilder {
	@Override
	public void onProvideShadowMetrics(Point shadowSize, Point shadowTouchPoint) {
		// TODO Auto-generated method stub
		final View view = getView();
		if (view != null) {
			shadowSize.set(view.getWidth(), view.getHeight());
			shadowTouchPoint.set(shadowSize.x / 2, shadowSize.y - 26);
			Log.e("MyDragShadowBuilder", "MyDragShadowBuilder------"
					+ shadowSize.y);
		} else {
			Log.e("MyDragShadowBuilder",
					"Asked for drag thumb metrics but no view");
		}
	}

	public MyDragShadowBuilder(View view) {
		super(view);
	}

}
