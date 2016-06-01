package ru.annin.store.presentation.ui.adapter;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.SparseArray;
import android.view.ViewGroup;

import ru.annin.store.presentation.ui.fragment.StorePriceChartFragment;
import ru.annin.store.presentation.ui.fragment.StoreSumChartFragment;


/**
 * <p></p>
 *
 * @author Pavel Annin, 2016.
 */

public class ChartPagerAdapter extends FragmentStatePagerAdapter {

    public ChartPagerAdapter(@NonNull final FragmentManager fm) {
        super(fm);
    }

    SparseArray<Fragment> registeredFragments = new SparseArray<>();

    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return StorePriceChartFragment.newInstance();
        }
        return StoreSumChartFragment.newInstance();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if (position == 0) {
            return "Накладные";
        }
        return "Оборот";
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Fragment fragment = (Fragment) super.instantiateItem(container, position);
        registeredFragments.put(position, fragment);
        return fragment;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        registeredFragments.remove(position);
        super.destroyItem(container, position, object);
    }

    public Fragment getRegisteredFragment(int position) {
        return registeredFragments.get(position);
    }

    public SparseArray<Fragment> getRegisteredFragments() {
        return registeredFragments;
    }
}