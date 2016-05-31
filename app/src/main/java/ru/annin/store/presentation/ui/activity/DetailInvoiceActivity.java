package ru.annin.store.presentation.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import ru.annin.store.R;
import ru.annin.store.data.repository.InvoiceRepositoryImpl;
import ru.annin.store.data.repository.ProductRepositoryImpl;
import ru.annin.store.data.repository.SettingsRepositoryImpl;
import ru.annin.store.data.repository.StoreRepositoryImpl;
import ru.annin.store.presentation.common.BaseActivity;
import ru.annin.store.presentation.presenter.DetailInvoicePresenter;
import ru.annin.store.presentation.ui.alert.DetailProductAlert;
import ru.annin.store.presentation.ui.view.DetailInvoiceView;
import ru.annin.store.presentation.ui.viewholder.DetailInvoiceViewHolder;

/**
 * <p>Экран "Товарной накладной".</p>
 *
 * @author Pavel Annin, 2016.
 */
public class DetailInvoiceActivity extends BaseActivity<DetailInvoicePresenter>
        implements DetailInvoiceView {

    public static final String EXTRA_RECEIVER_PRODUCT_ID = "ru.annin.store.extra.receiver_product_id";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_invoice);
        DetailInvoiceViewHolder viewHolder = new DetailInvoiceViewHolder(findViewById(R.id.main_container));
        presenter = new DetailInvoicePresenter(new InvoiceRepositoryImpl(),
                new StoreRepositoryImpl(),
                new ProductRepositoryImpl(),
                new SettingsRepositoryImpl(this));
        presenter.setViewHolder(viewHolder);
        presenter.setView(this);

        Intent intent = getIntent();
        if (intent != null) {
            String action = intent.getAction();
            Bundle bundle = intent.getExtras();
            if (TextUtils.equals(Intent.ACTION_EDIT, action) && bundle != null
                    && bundle.containsKey(EXTRA_RECEIVER_PRODUCT_ID)) {
                String receiverProductId = bundle.getString(EXTRA_RECEIVER_PRODUCT_ID);
                presenter.onInitialization(receiverProductId);
            } else {
                presenter.onInitialization();
            }
        } else {
            presenter.onInitialization();
        }
    }

    @Override
    public void onFinish() {
        finish();
    }

    @Override
    public void onCreate(DetailProductAlert.OnInteractionListener listener) {
        DetailProductAlert.newInstance(listener)
                .show(getFragmentManager(), DetailProductAlert.TAG);
    }

    @Override
    public void onOpen(String productId, DetailProductAlert.OnInteractionListener listener) {
        DetailProductAlert.newInstance(productId, listener)
                .show(getFragmentManager(), DetailProductAlert.TAG);
    }
}