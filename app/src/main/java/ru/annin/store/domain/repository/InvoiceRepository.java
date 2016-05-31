package ru.annin.store.domain.repository;

import android.support.annotation.NonNull;

import java.util.Date;

import io.realm.RealmResults;
import ru.annin.store.domain.model.InvoiceModel;
import rx.Observable;

/**
 * <p>Интерфейс репозитория "Товарная накладная".</p>
 *
 * @author Pavel Annin, 2016.
 */
public interface InvoiceRepository {

    String TEMP_RECEIVER_PRODUCT_ID = "temp_receiver_product_id";

    @NonNull
    Observable<RealmResults<InvoiceModel>> getByStore(@NonNull String id);

    @NonNull
    Observable<InvoiceModel> getById(@NonNull String id);

    void asyncCreateReceiverFromTemp(@NonNull String name, @NonNull String storeId, @NonNull Date date);

    void asyncEdit(@NonNull String id, @NonNull String name, @NonNull Date date);

    void asyncAddProduct(@NonNull String id, @NonNull String productId);

    void asyncRemove(@NonNull String id);
}
