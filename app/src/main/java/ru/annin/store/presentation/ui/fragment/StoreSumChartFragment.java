package ru.annin.store.presentation.ui.fragment;


import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;

import ru.annin.store.R;
import ru.annin.store.data.repository.InvoiceRepositoryImpl;
import ru.annin.store.data.repository.StoreRepositoryImpl;
import ru.annin.store.domain.model.InvoiceModel;
import ru.annin.store.domain.model.ProductModel;
import ru.annin.store.domain.repository.InvoiceRepository;
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

public class StoreSumChartFragment extends Fragment {

    // Repository
    private StoreRepository storeRepository;
    private InvoiceRepository invoiceRepository;

    // View
    private PieChart pieChart;

    private CompositeSubscription subscription;

    public static StoreSumChartFragment newInstance() {
        return new StoreSumChartFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        storeRepository = new StoreRepositoryImpl();
        invoiceRepository = new InvoiceRepositoryImpl();
        subscription = new CompositeSubscription();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_store_sum_chart, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        pieChart = (PieChart) view.findViewById(chart);
        pieChart.setDescription(null);
    }

    @Override
    public void onStart() {
        super.onStart();
        List<String> store = new ArrayList<>();
        List<Float> sumStore = new ArrayList<>();
        Subscription sub = storeRepository.getAll()
                .flatMap(Observable::from)
                .doOnNext(model -> store.add(model.getName()))
                .flatMap(model -> invoiceRepository.getByStore(model.getId()))
                .map(model -> {
                    Float sum = 0.0f;
                    for (InvoiceModel invoice : model) {
                        for (ProductModel product : invoice.getProducts()) {
                            float price = product.getPrice();
                            float amount = product.getAmount();
                            sum += price * amount;
                        }
                    }
                    return sum;
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(aFloat -> {
                    sumStore.add(aFloat);
                    ArrayList<String> xVals = new ArrayList<>();
                    ArrayList<Entry> entries = new ArrayList<>();

                    for(int i = 0; i < sumStore.size(); i++) {
                        xVals.add(store.get(i));
                        entries.add(new Entry(sumStore.get(i), i));
                    }

                    PieDataSet ds1 = new PieDataSet(entries, null);
                    ds1.setColors(ColorTemplate.MATERIAL_COLORS);
                    ds1.setSliceSpace(2f);
                    ds1.setValueTextColor(Color.WHITE);
                    ds1.setValueTextSize(12f);

                    PieData d = new PieData(xVals, ds1);
                    pieChart.setData(d);
                    pieChart.invalidate();
                });
        subscription.add(sub);
    }

    @Override
    public void onStop() {
        super.onStop();
        subscription.unsubscribe();
    }
}
