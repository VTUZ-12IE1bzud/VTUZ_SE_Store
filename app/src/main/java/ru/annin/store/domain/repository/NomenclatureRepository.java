package ru.annin.store.domain.repository;

import android.support.annotation.NonNull;

import io.realm.RealmResults;
import ru.annin.store.domain.model.NomenclatureModel;
import rx.Observable;

/**
 * <p>Интерфейс репозитория "Номенклатура".</p>
 *
 * @author Pavel Annin, 2016.
 */
public interface NomenclatureRepository {

    @NonNull
    Observable<RealmResults<NomenclatureModel>> getAll();

    @NonNull
    Observable<NomenclatureModel> getById(@NonNull String id);

    void asyncCreate(@NonNull String name, @NonNull String unitId);

    void asyncSave(@NonNull String id, @NonNull String name, @NonNull String unitId);

    boolean canRemoved(@NonNull String id);

    void asyncRemove(@NonNull String id);
}
