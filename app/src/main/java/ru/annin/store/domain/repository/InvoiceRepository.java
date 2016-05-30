package ru.annin.store.domain.repository;

import android.support.annotation.NonNull;

import io.realm.RealmResults;
import ru.annin.store.domain.model.InvoiceModel;
import rx.Observable;

/**
 * <p>Интерфейс репозитория "Товарная накладная".</p>
 *
 * @author Pavel Annin, 2016.
 */
public interface InvoiceRepository {

    @NonNull
    Observable<RealmResults<InvoiceModel>> getByStore(@NonNull String id);

    @NonNull
    Observable<InvoiceModel> getById(@NonNull String id);

    void asyncCreate(@NonNull String name, @NonNull String unitId);

    void asyncSave(@NonNull String id, @NonNull String name, @NonNull String unitId);

    void asyncRemove(@NonNull String id);
}
