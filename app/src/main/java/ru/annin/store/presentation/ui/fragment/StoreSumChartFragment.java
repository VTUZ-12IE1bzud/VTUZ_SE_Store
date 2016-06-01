package ru.annin.store.presentation.ui.fragment;


import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;
import ru.annin.store.R;
import ru.annin.store.domain.model.InvoiceModel;
import ru.annin.store.domain.model.ProductModel;
import ru.annin.store.domain.model.StoreModel;
import ru.annin.store.domain.repository.InvoiceRepository;

import static ru.annin.store.R.id.chart;

/**
 * <p>Экран график "Оборота".</p>
 *
 * @author Pavel Annin, 2016.
 */

public class StoreSumChartFragment extends BaseChartFragment {

    // Repository

    // View
    private PieChart vChart;
    private TextView vEmpty;

    // Data's

    public static StoreSumChartFragment newInstance() {
        return new StoreSumChartFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_store_sum_chart, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        vChart = (PieChart) view.findViewById(chart);
        vEmpty = (TextView) view.findViewById(R.id.txt_empty);
        vChart.setDescription(null);
        vEmpty.setVisibility(View.GONE);
    }

    @Override
    public void onStart() {
        super.onStart();
        updateChart();
    }

    @Override
    public void updateChart() {
        if (vChart != null && vEmpty != null) {
            vChart.setVisibility(View.GONE);
            vEmpty.setVisibility(View.GONE);

            ArrayList<String> chartLabel = new ArrayList<>();
            ArrayList<Entry> chartValue = new ArrayList<>();
            RealmResults<StoreModel> stores = Realm.getDefaultInstance().where(StoreModel.class)
                    .findAll();
            for (int i = 0; i < stores.size(); ++i) {
                StoreModel store = stores.get(i);
                chartLabel.add(store.getName());
                RealmResults<InvoiceModel> invoices = Realm.getDefaultInstance().where(InvoiceModel.class)
                        .equalTo(InvoiceModel.FIELD_STORE + "." + StoreModel.FIELD_ID, store.getId())
                        .notEqualTo(InvoiceModel.FIELD_ID, InvoiceRepository.TEMP_RECEIVER_PRODUCT_ID)
                        .findAll();
                Float sum = 0.0f;
                for (InvoiceModel invoice : invoices) {
                    for (ProductModel product : invoice.getProducts()) {
                        float price = product.getPrice();
                        float amount = product.getAmount();
                        sum += price * amount;
                    }
                }
                chartValue.add(new Entry(sum, i));
            }
            PieDataSet chartSet = new PieDataSet(chartValue, null);
            chartSet.setColors(ColorTemplate.MATERIAL_COLORS);
            chartSet.setSliceSpace(2f);
            chartSet.setValueTextColor(Color.WHITE);
            chartSet.setValueTextSize(12f);
            PieData chartData = new PieData(chartLabel, chartSet);

            vChart.setData(chartData);
            vChart.notifyDataSetChanged();
            vChart.invalidate();

            if (!chartLabel.isEmpty() && !chartValue.isEmpty()) {
                vChart.setVisibility(View.VISIBLE);
                vEmpty.setVisibility(View.GONE);
            } else {
                vChart.setVisibility(View.GONE);
                vEmpty.setVisibility(View.VISIBLE);
            }
        }
    }
}
