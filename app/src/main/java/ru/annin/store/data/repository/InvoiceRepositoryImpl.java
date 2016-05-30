package ru.annin.store.data.repository;

import android.support.annotation.NonNull;

import io.realm.RealmResults;
import ru.annin.store.data.util.RealmUtil;
import ru.annin.store.domain.model.InvoiceModel;
import ru.annin.store.domain.model.StoreModel;
import ru.annin.store.domain.repository.InvoiceRepository;
import rx.Observable;

/**
 * <p>Реализация репозитория "Товарная накланая".</p>
 *
 * @author Pavel Annin, 2016.
 */
public class InvoiceRepositoryImpl implements InvoiceRepository {


    @NonNull
    @Override
    public Observable<RealmResults<InvoiceModel>> getByStore(@NonNull String id) {
        return RealmUtil.getRealm().where(InvoiceModel.class)
                .equalTo(InvoiceModel.FIELD_STORE + "." + StoreModel.FIELD_ID, id)
                .findAllSortedAsync(InvoiceModel.FIELD_DATE)
                .asObservable()
                .filter(RealmResults::isLoaded);
    }

    @NonNull
    @Override
    public Observable<InvoiceModel> getById(@NonNull String id) {
        return RealmUtil.getRealm().where(InvoiceModel.class)
                .equalTo(InvoiceModel.FILED_ID, id)
                .findFirst()
                .asObservable();
    }

    @Override
    public void asyncCreate(@NonNull String name, @NonNull String unitId) {

    }

    @Override
    public void asyncSave(@NonNull String id, @NonNull String name, @NonNull String unitId) {

    }

    @Override
    public void asyncRemove(@NonNull String id) {
        RealmUtil.getRealm().executeTransactionAsync(realm -> {
            InvoiceModel model = realm.where(InvoiceModel.class)
                    .equalTo(InvoiceModel.FILED_ID, id)
                    .findFirst();
            if (model != null && model.isValid()) {
                model.deleteFromRealm();
            }
        });
    }
}
