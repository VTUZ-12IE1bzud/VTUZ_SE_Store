package ru.annin.store.data.repository;

import android.support.annotation.NonNull;

import java.util.UUID;

import io.realm.RealmResults;
import ru.annin.store.data.util.RealmUtil;
import ru.annin.store.domain.model.InvoiceModel;
import ru.annin.store.domain.model.StoreModel;
import ru.annin.store.domain.repository.StoreRepository;
import rx.Observable;

/**
 * <p>Реализация репозитория "Склад".</p>
 *
 * @author Pavel Annin, 2016.
 */
public class StoreRepositoryImpl implements StoreRepository {

    @NonNull
    @Override
    public Observable<RealmResults<StoreModel>> getAll() {
        return RealmUtil.getRealm().where(StoreModel.class)
                .findAllSorted(StoreModel.FIELD_NAME)
                .asObservable()
                .filter(RealmResults::isLoaded);
    }

    @NonNull
    @Override
    public Observable<StoreModel> getById(@NonNull String id) {
        return RealmUtil.getRealm().where(StoreModel.class)
                .equalTo(StoreModel.FIELD_ID, id)
                .findFirst()
                .asObservable();
    }

    @Override
    public void asyncCreateStore(@NonNull String name) {
        RealmUtil.getRealm().executeTransactionAsync(realm -> {
            StoreModel store = realm.createObject(StoreModel.class);
            store.setId(UUID.randomUUID().toString());
            store.setName(name);
        });
    }

    @Override
    public void asyncSaveStore(@NonNull String id, @NonNull String name) {
        RealmUtil.getRealm().executeTransactionAsync(realm -> {
            StoreModel store = realm.where(StoreModel.class)
                    .equalTo(StoreModel.FIELD_ID, id)
                    .findFirst();
            if (store != null && store.isValid()) {
                store.setName(name);
            }
        });
    }

    @Override
    public boolean canStoreRemoved(@NonNull String id) {
        return RealmUtil.getRealm().where(InvoiceModel.class)
                .equalTo(InvoiceModel.FIELD_STORE + "." + StoreModel.FIELD_ID, id)
                .count() == 0;
    }

    @Override
    public void asyncRemoveStore(@NonNull String id) {
        RealmUtil.getRealm().executeTransactionAsync(realm -> {
            StoreModel store = realm.where(StoreModel.class)
                    .equalTo(StoreModel.FIELD_ID, id)
                    .findFirst();
            if (store != null && store.isValid()) {
                store.deleteFromRealm();
            }
        });
    }
}
