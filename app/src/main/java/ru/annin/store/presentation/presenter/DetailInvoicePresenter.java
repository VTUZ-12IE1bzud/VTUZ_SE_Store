package ru.annin.store.presentation.presenter;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import java.util.Date;

import ru.annin.store.R;
import ru.annin.store.domain.model.ProductModel;
import ru.annin.store.domain.model.StoreModel;
import ru.annin.store.domain.repository.InvoiceRepository;
import ru.annin.store.domain.repository.ProductRepository;
import ru.annin.store.domain.repository.SettingsRepository;
import ru.annin.store.domain.repository.StoreRepository;
import ru.annin.store.presentation.common.BasePresenter;
import ru.annin.store.presentation.ui.alert.DetailProductAlert;
import ru.annin.store.presentation.ui.view.DetailInvoiceView;
import ru.annin.store.presentation.ui.viewholder.DetailInvoiceViewHolder;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.subscriptions.CompositeSubscription;

import static ru.annin.store.presentation.ui.viewholder.DetailInvoiceViewHolder.OnInteractionListener;

/**
 * <p>Presenter экрана "Товарная накладная".</p>
 *
 * @author Pavel Annin, 2016.
 */
public class DetailInvoicePresenter extends BasePresenter<DetailInvoiceViewHolder, DetailInvoiceView> {

    private final CompositeSubscription subscription;

    // Repository's
    private final InvoiceRepository invoiceRepository;
    private final StoreRepository storeRepository;
    private final SettingsRepository settingsRepository;
    private final ProductRepository productRepository;

    // Data's
    private boolean isCreate;
    private Date date;
    private String receiverProductId;
    private String organizationUnitId;

    public DetailInvoicePresenter(@NonNull InvoiceRepository invoiceRepository,
                                  @NonNull StoreRepository storeRepository,
                                  @NonNull ProductRepository productRepository,
                                  @NonNull SettingsRepository settingsRepository) {
        subscription = new CompositeSubscription();
        this.invoiceRepository = invoiceRepository;
        this.storeRepository = storeRepository;
        this.productRepository = productRepository;
        this.settingsRepository = settingsRepository;
        date = new Date();
        isCreate = true;
    }

    public void onInitialization() {
        isCreate = true;
        if (viewHolder != null) {
            viewHolder.enableAnimation(false)
                    .showDate(date);
        }
        Subscription subOrganizationUnit = storeRepository
                .getById(settingsRepository.getSelectStoreId())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(model -> {
                    if (viewHolder != null && model != null && model.isLoaded() && model.isValid()) {
                        organizationUnitId = model.getId();
                        viewHolder.showMovementTo(model.getName());
                    }
                });
        subscription.add(subOrganizationUnit);

        Subscription subReceiverTemp = invoiceRepository
                .getById(InvoiceRepository.TEMP_RECEIVER_PRODUCT_ID)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(model -> {
                    if (viewHolder != null && model != null && model.isLoaded() && model.isValid()) {
                        viewHolder.showNameInvoice(model.getName())
                                .showProducts(model.getProducts().where().findAll());
                    }
                });
        subscription.add(subReceiverTemp);
    }

    public void onInitialization(@NonNull String id) {
        isCreate = false;
        Subscription sub = invoiceRepository.getById(id)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(model -> {
                    if (viewHolder != null && model != null && model.isLoaded() && model.isValid()) {
                        date = model.getDate();
                        receiverProductId = model.getId();
                        viewHolder.enableAnimation(false)
                                .showNameInvoice(model.getName())
                                .showDate(date)
                                .showProducts(model.getProducts().where().findAll());
                        final StoreModel store = model.getStore();
                        if (store != null && store.isValid()) {
                            organizationUnitId = store.getId();
                            viewHolder.showMovementTo(store.getName());
                        }
                    }
                });
        subscription.add(sub);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        subscription.unsubscribe();
    }

    @NonNull
    @Override
    public BasePresenter setViewHolder(@Nullable DetailInvoiceViewHolder vh) {
        super.setViewHolder(vh);
        if (viewHolder != null) {
            viewHolder.setOnInteractionListener(onViewHolderListener);
        }
        return this;
    }

    private boolean isValidation(String invoice) {
        boolean valid = true;

        if (viewHolder != null) {
            viewHolder.errorNameInvoice(null);
        }

        if (TextUtils.isEmpty(invoice)) {
            valid = false;
            if (viewHolder != null && view != null) {
                viewHolder.errorNameInvoice(view.getString(R.string.error_field_empty));
            }
        }
        return valid;
    }

    private final OnInteractionListener onViewHolderListener = new OnInteractionListener() {
        @Override
        public void onBackClick() {
            if (view != null) {
                view.onFinish();
            }
        }

        @Override
        public void onSaveClick(String invoice) {
            if (isValidation(invoice)) {
                if (isCreate) {
                    invoiceRepository.asyncCreateReceiverFromTemp(invoice, organizationUnitId, date);
                } else {
                    invoiceRepository.asyncEdit(receiverProductId, invoice, date);
                }
                onBackClick();
            }
        }

        @Override
        public void onAddClick() {
            if (view != null) {
                view.onCreate(onDetailProductAlertListener);
            }
        }

        @Override
        public void onEditProduct(String productId) {
            if (view != null) {
                view.onOpen(productId, onDetailProductAlertListener);
            }
        }

        @Override
        public void onRemoveProduct(String productId) {
            productRepository.asyncRemove(productId);
        }

        @Override
        public void onDateClick() {
            if (viewHolder != null) {
                viewHolder.showDatePicker(date);
            }
        }

        @Override
        public void onDateSelect(Date d) {
            date = d;
            if (viewHolder != null) {
                viewHolder.showDate(date);
            }
        }
    };

    private final DetailProductAlert.OnInteractionListener onDetailProductAlertListener = new DetailProductAlert.OnInteractionListener() {
        @Override
        public void onSaveProduct(String productId, String nomenclatureId, float amount, float price) {
            productRepository.asyncEdit(productId, nomenclatureId, amount, price);
        }

        @Override
        public void onCreateProduct(String nomenclatureId, float amount, float price) {
            ProductModel product = productRepository.create(nomenclatureId, amount, price);
            if (product != null && product.isValid()) {
                invoiceRepository.asyncAddProduct(isCreate
                        ? InvoiceRepository.TEMP_RECEIVER_PRODUCT_ID
                        : receiverProductId, product.getId());
            }
        }
    };
}
