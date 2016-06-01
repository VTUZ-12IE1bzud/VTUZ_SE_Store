package ru.annin.store.presentation.ui.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;
import ru.annin.store.R;
import ru.annin.store.data.repository.SettingsRepositoryImpl;
import ru.annin.store.domain.model.InvoiceModel;
import ru.annin.store.domain.model.ProductModel;
import ru.annin.store.domain.model.StoreModel;
import ru.annin.store.domain.repository.InvoiceRepository;
import ru.annin.store.domain.repository.SettingsRepository;

import static ru.annin.store.R.id.chart;

/**
 * <p>Экран график "Оборота".</p>
 *
 * @author Pavel Annin, 2016.
 */

public class StorePriceChartFragment extends BaseChartFragment {

    // Repository
    private SettingsRepository settingsRepository;

    // View's
    private BarChart vChart;
    private TextView vEmpty;

    // Data's
    private List<String> chartInvoices;
    private List<IBarDataSet> chartSumPrice;
    private BarData chartData;

    public static StorePriceChartFragment newInstance() {
        return new StorePriceChartFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        settingsRepository = new SettingsRepositoryImpl(getActivity());
        chartInvoices = new ArrayList<>();
        chartSumPrice = new ArrayList<>();
        chartData = new BarData(chartInvoices, chartSumPrice);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_store_price_chart, container, false);
        vChart = (BarChart) view.findViewById(chart);
        vEmpty = (TextView) view.findViewById(R.id.txt_empty);
        vChart.setDescription(null);
        vChart.setData(chartData);
        vEmpty.setVisibility(View.GONE);
        return view;
    }


    @Override
    public void onResume() {
        super.onResume();
        updateChart();
    }

    @Override
    public void updateChart() {
        if (vChart != null && vEmpty != null) {
            vChart.setVisibility(View.GONE);
            vEmpty.setVisibility(View.GONE);
            chartInvoices.clear();
            chartSumPrice.clear();

            RealmResults<InvoiceModel> invoices = Realm.getDefaultInstance().where(InvoiceModel.class)
                    .equalTo(InvoiceModel.FIELD_STORE + "." + StoreModel.FIELD_ID, settingsRepository.getSelectStoreId())
                    .notEqualTo(InvoiceModel.FIELD_ID, InvoiceRepository.TEMP_RECEIVER_PRODUCT_ID)
                    .findAll();

            for (InvoiceModel model : invoices) {
                Float sum = 0.0f;
                for (ProductModel product : model.getProducts()) {
                    float price = product.getPrice();
                    float amount = product.getAmount();
                    sum += price * amount;
                }

                ArrayList<BarEntry> entries = new ArrayList<>();
                chartInvoices.add(model.getName());
                entries.add(new BarEntry(sum, chartInvoices.size() - 1));
                BarDataSet ds = new BarDataSet(entries, model.getName());
                ds.setColors(ColorTemplate.MATERIAL_COLORS);
                chartSumPrice.add(ds);
            }

            chartData.notifyDataChanged();
            vChart.notifyDataSetChanged();
            vChart.invalidate();

            if (!chartSumPrice.isEmpty() && !chartInvoices.isEmpty()) {
                vChart.setVisibility(View.VISIBLE);
                vEmpty.setVisibility(View.GONE);
            } else {
                vChart.setVisibility(View.GONE);
                vEmpty.setVisibility(View.VISIBLE);
            }
        }
    }
}
