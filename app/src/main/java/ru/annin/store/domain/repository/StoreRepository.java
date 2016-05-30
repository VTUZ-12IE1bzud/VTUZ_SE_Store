package ru.annin.store.domain.repository;

import android.support.annotation.NonNull;

import io.realm.RealmResults;
import ru.annin.store.domain.model.StoreModel;
import rx.Observable;

/**
 * <p>Интерфейс репозитория "Склад".</p>
 *
 * @author Pavel Annin, 2016.
 */
public interface StoreRepository {

    @NonNull
    Observable<RealmResults<StoreModel>> getAll();

    @NonNull
    Observable<StoreModel> getById(@NonNull String id);

    void asyncCreateStore(@NonNull String name);

    void asyncSaveStore(@NonNull String id, @NonNull String name);

    boolean canStoreRemoved(@NonNull String id);

    void asyncRemoveStore(@NonNull String id);
}
