package ru.annin.store.presentation.ui.adapter;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

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
}