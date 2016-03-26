package fr.chaltiel.doublefragmentviewpager.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

import fr.chaltiel.doublefragmentviewpager.R;
import fr.chaltiel.doublefragmentviewpager.adapters.VerticalPagerAdapter;
import fr.chaltiel.doublefragmentviewpager.pagers.VerticalViewPager;


/**
 * Fragment Parent = mouvement horizontal
 */
public class ParentFragment extends Fragment {
    public final static String TAG = "PARENT_FRAGMENT";
    private final static String MODULO_HORIZ = "modulo_horiz";
    private final static String CHILD_CLASS = "child_class";

    public static ParentFragment newInstance(int horiz, Class<? extends Fragment> childClass) {
//            Log.d("Dan", "VerticalPagerAdapter.GraphFragment (154) - newInstance: horiz=" + horiz + ", vert=" + vert);
        ParentFragment rtn = new ParentFragment();
        Bundle args = new Bundle();
        args.putString(TAG, "ParentFragment : H=" + horiz);
        args.putSerializable(CHILD_CLASS, childClass);
        args.putInt(MODULO_HORIZ, horiz);
        rtn.setArguments(args);
        return rtn;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View layout = inflater.inflate(R.layout.child_viewpager, container, false);
        final VerticalViewPager childVP = (VerticalViewPager) layout.findViewById(R.id.vvp);
//            final VerticalPagerAdapter adapter = mAdapters.get(position);
        int position = getArguments().getInt(MODULO_HORIZ);
        Class childClass = (Class) getArguments().getSerializable(CHILD_CLASS);

        try {
            Method m = childClass.getDeclaredMethod("getVerticalPagerAdapterList", Context.class, FragmentManager.class);

            ArrayList<VerticalPagerAdapter> adapterList = (ArrayList<VerticalPagerAdapter>) m.invoke(null, getContext(), getChildFragmentManager());
//            final VerticalPagerAdapter adapter = Model.getVerticalPagerAdapterList(getContext(), getChildFragmentManager()).get(position);
            final VerticalPagerAdapter adapter = adapterList.get(position);
            childVP.setAdapter(adapter);
        } catch (NoSuchMethodException e) {
            Log.d("Dan", "VerticalPagerAdapter (51) - getItem : ", e);
        } catch (InvocationTargetException e) {
            Log.d("Dan", "VerticalPagerAdapter (53) - getItem : ", e);
        } catch (IllegalAccessException e) {
            Log.d("Dan", "VerticalPagerAdapter (55) - getItem : ", e);
        }

        childVP.setTag(TAG + "_" + position);
        return layout;
    }
}