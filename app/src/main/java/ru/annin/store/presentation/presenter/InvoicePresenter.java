package ru.annin.store.presentation.presenter;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import io.realm.Realm;
import io.realm.RealmResults;
import ru.annin.store.domain.model.InvoiceModel;
import ru.annin.store.domain.model.StoreModel;
import ru.annin.store.domain.repository.InvoiceRepository;
import ru.annin.store.domain.repository.SettingsRepository;
import ru.annin.store.presentation.common.BasePresenter;
import ru.annin.store.presentation.ui.view.InvoiceView;
import ru.annin.store.presentation.ui.viewholder.InvoiceViewHolder;
import ru.annin.store.presentation.util.ReportUtil;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.subscriptions.CompositeSubscription;

import static ru.annin.store.presentation.ui.viewholder.InvoiceViewHolder.OnInteractionListener;


/**
 * <p>Presenter экрана "Товарная накладная".</p>
 *
 * @author Pavel Annin, 2016.
 */
public class InvoicePresenter extends BasePresenter<InvoiceViewHolder, InvoiceView> {

    private final CompositeSubscription subscription;

    // Repository's
    private final InvoiceRepository invoiceRepository;
    private final SettingsRepository settingsRepository;

    public InvoicePresenter(@NonNull InvoiceRepository invoiceRepository,
                            @NonNull SettingsRepository settingsRepository) {
        this.invoiceRepository = invoiceRepository;
        this.settingsRepository = settingsRepository;
        subscription = new CompositeSubscription();
    }

    public void onInitialization() {
        Subscription sub = invoiceRepository.getByStore(settingsRepository.getSelectStoreId())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(models -> {if (viewHolder != null) viewHolder.showInvoice(models);});
        subscription.add(sub);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        subscription.unsubscribe();
    }

    @Override
    public BasePresenter setViewHolder(@Nullable InvoiceViewHolder vh) {
        super.setViewHolder(vh);
        if (viewHolder != null) {
            viewHolder.setOnInteractionListener(onViewHolderListener);
        }
        return this;
    }

    private final OnInteractionListener onViewHolderListener = new OnInteractionListener() {
        @Override
        public void onBackClick() {
            if (view != null) {
                view.onFinish();
            }
        }

        @Override
        public void onAddClick() {
            if (view != null) {
                view.onCreate();
            }
        }

        @Override
        public void onItemClick(InvoiceModel model) {
            if (view != null) {
                view.onOpen(model.getId());
            }
        }

        @Override
        public void onRemoveReceiverClick(String receiverId) {
            invoiceRepository.asyncRemove(receiverId);
        }

        @Override
        public void onReportClick() {
            String store = Realm.getDefaultInstance()
                    .where(StoreModel.class)
                    .equalTo(StoreModel.FIELD_ID, settingsRepository.getSelectStoreId())
                    .findFirst()
                    .getName();
            RealmResults<InvoiceModel> models = Realm.getDefaultInstance()
                    .where(InvoiceModel.class)
                    .equalTo(InvoiceModel.FIELD_STORE + "." + StoreModel.FIELD_ID, settingsRepository.getSelectStoreId())
                    .notEqualTo(InvoiceModel.FIELD_ID, InvoiceRepository.TEMP_RECEIVER_PRODUCT_ID)
                    .findAllSorted(InvoiceModel.FIELD_DATE);
            boolean b = ReportUtil.createInvoicesReport(store, models);
            if (b) {
                viewHolder.showMessage("Отчет сохранен, в директорию документы");
            } else {
                viewHolder.showMessage("Ошибка сохранения отчета");
            }
        }
    };
}
