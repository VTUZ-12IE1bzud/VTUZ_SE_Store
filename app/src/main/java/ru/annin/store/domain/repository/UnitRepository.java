package ru.annin.store.domain.repository;

import android.support.annotation.NonNull;

import io.realm.RealmResults;
import ru.annin.store.domain.model.UnitModel;
import rx.Observable;

/**
 * <p>Интерфейс репозитория "Единиц измерения".</p>
 *
 * @author Pavel Annin, 2016.
 */
public interface UnitRepository {

    @NonNull
    Observable<RealmResults<UnitModel>> getAll();

    @NonNull
    Observable<UnitModel> getById(@NonNull String id);

    void asyncCreateUnit(@NonNull String name, @NonNull String symbol);

    void asyncSaveUnit(@NonNull String id, @NonNull String name, @NonNull String symbol);

    boolean canUnitRemoved(@NonNull String id);

    void asyncRemoveUnit(@NonNull String id);
}
