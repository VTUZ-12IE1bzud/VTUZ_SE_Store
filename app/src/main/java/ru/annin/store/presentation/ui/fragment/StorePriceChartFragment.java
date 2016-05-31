package ru.annin.store.presentation.ui.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;

import ru.annin.store.R;
import ru.annin.store.data.repository.InvoiceRepositoryImpl;
import ru.annin.store.data.repository.SettingsRepositoryImpl;
import ru.annin.store.data.repository.StoreRepositoryImpl;
import ru.annin.store.domain.model.ProductModel;
import ru.annin.store.domain.repository.InvoiceRepository;
import ru.annin.store.domain.repository.SettingsRepository;
import ru.annin.store.domain.repository.StoreRepository;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.subscriptions.CompositeSubscription;

import static ru.annin.store.R.id.chart;

/**
 * <p>Экран график "Оборота".</p>
 *
 * @author Pavel Annin, 2016.
 */

public class StorePriceChartFragment extends Fragment {

    // Repository
    private StoreRepository storeRepository;
    private InvoiceRepository invoiceRepository;
    private SettingsRepository settingsRepository;

    // View's
    private BarChart vChart;

    // Data's
    private List<String> chartInvoices;
    private List<IBarDataSet> chartSumPrice;
    private BarData chartData;

    private CompositeSubscription subscription;

    public static StorePriceChartFragment newInstance() {
        return new StorePriceChartFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        storeRepository = new StoreRepositoryImpl();
        invoiceRepository = new InvoiceRepositoryImpl();
        settingsRepository = new SettingsRepositoryImpl(getActivity());
        subscription = new CompositeSubscription();
        chartInvoices = new ArrayList<>();
        chartSumPrice = new ArrayList<>();
        chartData = new BarData(chartInvoices, chartSumPrice);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_store_price_chart, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        vChart = (BarChart) view.findViewById(chart);
        vChart.setDescription(null);
        vChart.setData(chartData);
    }

    @Override
    public void onStart() {
        super.onStart();
        Subscription sub = storeRepository.getById(settingsRepository.getSelectStoreId())
                .flatMap(model -> invoiceRepository.getByStore(model.getId()))
                .flatMap(Observable::from)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(invoice -> {
                    Float sum = 0.0f;
                    for (ProductModel product : invoice.getProducts()) {
                        float price = product.getPrice();
                        float amount = product.getAmount();
                        sum += price * amount;
                    }

                    ArrayList<BarEntry> entries = new ArrayList<>();
                    chartInvoices.add(invoice.getName());
                    entries.add(new BarEntry(sum, chartInvoices.size() - 1));
                    BarDataSet ds = new BarDataSet(entries, invoice.getName());
                    ds.setColors(ColorTemplate.VORDIPLOM_COLORS);
                    chartSumPrice.add(ds);
                    chartData.notifyDataChanged();
                    vChart.notifyDataSetChanged();
                    vChart.invalidate();
                });
        subscription.add(sub);
    }

    @Override
    public void onStop() {
        super.onStop();
        subscription.unsubscribe();
    }
}
