package lib.kalu.mediaplayer.core.player.video;

import android.annotation.SuppressLint;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import java.util.LinkedList;
import java.util.List;

import lib.kalu.mediaplayer.core.component.ComponentApi;
import lib.kalu.mediaplayer.util.LogUtil;

interface VideoPlayerApiComponent extends VideoPlayerApiBase {

    default void clearAllComponent() {
        try {
            ViewGroup viewGroup = getBaseComponentViewGroup();
            int childCount = viewGroup.getChildCount();
            if (childCount <= 0)
                throw new Exception("not find component");
            viewGroup.removeAllViews();
        } catch (Exception e) {
            LogUtil.log("VideoPlayerApiComponent => clearAllComponent => " + e.getMessage());
        }
    }

    default void clearComponent(java.lang.Class<?> cls) {
        try {
            LinkedList<View> views = new LinkedList<>();
            ViewGroup viewGroup = getBaseComponentViewGroup();
            int childCount = viewGroup.getChildCount();
            if (childCount <= 0)
                throw new Exception("not find component");
            for (int i = 0; i < childCount; i++) {
                View childAt = viewGroup.getChildAt(i);
                if (null == childAt)
                    continue;
                if (!(childAt instanceof ComponentApi))
                    continue;
                if (childAt.getClass() == cls) {
                    views.add(childAt);
                }
            }
            for (View v : views) {
                viewGroup.removeView(v);
            }
        } catch (Exception e) {
            LogUtil.log("VideoPlayerApiComponent => clearComponent => " + e.getMessage());
        }
    }

    @SuppressLint("StaticFieldLeak")
    default void addComponent(ComponentApi component) {
        try {
            if (null == component)
                throw new Exception("componentApi error: null");
            ViewGroup viewGroup = getBaseComponentViewGroup();
            if (null == viewGroup)
                throw new Exception("viewGroup error: null");
            viewGroup.addView((View) component, 0, new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
        } catch (Exception e) {
            LogUtil.log("VideoPlayerApiComponent => addComponent => " + e.getMessage());
        }
    }

    default void addAllComponent(List<ComponentApi> components) {
        try {
            if (null == components || components.size() <= 0)
                throw new Exception("components error: null");
            ViewGroup viewGroup = getBaseComponentViewGroup();
            if (null == viewGroup)
                throw new Exception("viewGroup error: null");
            viewGroup.removeAllViews();
            for (ComponentApi componentApi : components) {
                if (null == componentApi)
                    continue;
                addComponent(componentApi);
            }
        } catch (Exception e) {
            LogUtil.log("VideoPlayerApiComponent => addAllComponent => " + e.getMessage());
        }
    }

    default <T extends ComponentApi> T findComponent(java.lang.Class<?> cls) {
        try {
            ViewGroup viewGroup = getBaseComponentViewGroup();
            int childCount = viewGroup.getChildCount();
            if (childCount <= 0)
                throw new Exception("not find component");
            for (int i = 0; i < childCount; i++) {
                View childAt = viewGroup.getChildAt(i);
                if (null == childAt)
                    continue;
                if (!(childAt instanceof ComponentApi))
                    continue;
                if (childAt.getClass() == cls) {
                    return (T) childAt;
                } else {
                    Class<?>[] interfaces = childAt.getClass().getInterfaces();
                    if (null != interfaces && interfaces.length > 0) {
                        for (Class<?> c : interfaces) {
                            if (c == cls) {
                                return (T) childAt;
                            }
                        }
                    }
                }
            }
            throw new Exception("not find");
        } catch (Exception e) {
            LogUtil.log("VideoPlayerApiComponent => findComponent => " + e.getMessage());
            return null;
        }
    }
}
