package ru.annin.store.data.repository;

import android.support.annotation.NonNull;

import java.util.UUID;

import io.realm.RealmResults;
import ru.annin.store.data.util.RealmUtil;
import ru.annin.store.domain.model.NomenclatureModel;
import ru.annin.store.domain.model.UnitModel;
import ru.annin.store.domain.repository.UnitRepository;
import rx.Observable;

/**
 * <p>Реализация репозитория "Единиц измерения".</p>
 *
 * @author Pavel Annin, 2016.
 */
public class UnitRepositoryImpl implements UnitRepository {

    @NonNull
    @Override
    public Observable<RealmResults<UnitModel>> getAll() {
        return RealmUtil.getRealm().where(UnitModel.class)
                .findAllSortedAsync(UnitModel.FIELD_NAME)
                .asObservable()
                .filter(RealmResults::isLoaded);
    }

    @NonNull
    @Override
    public Observable<UnitModel> getById(@NonNull String id) {
        return RealmUtil.getRealm().where(UnitModel.class)
                .equalTo(UnitModel.FIELD_ID, id)
                .findFirst()
                .asObservable();
    }

    @Override
    public void asyncCreateUnit(@NonNull String name, @NonNull String symbol) {
        RealmUtil.getRealm().executeTransactionAsync(realm -> {
            UnitModel unit = realm.createObject(UnitModel.class);
            unit.setId(UUID.randomUUID().toString());
            unit.setName(name);
            unit.setSymbol(symbol);
        });
    }

    @Override
    public void asyncSaveUnit(@NonNull String id, @NonNull String name, @NonNull String symbol) {
        RealmUtil.getRealm().executeTransactionAsync(realm -> {
            UnitModel unit = realm.where(UnitModel.class)
                    .equalTo(UnitModel.FIELD_ID, id)
                    .findFirst();
            if (unit != null && unit.isValid()) {
                unit.setName(name);
                unit.setSymbol(symbol);
            }
        });
    }

    @Override
    public boolean canUnitRemoved(@NonNull String id) {
        return RealmUtil.getRealm().where(NomenclatureModel.class)
                .equalTo(NomenclatureModel.FIELD_UNIT + "." + UnitModel.FIELD_ID, id)
                .count() == 0;
    }

    @Override
    public void asyncRemoveUnit(@NonNull String id) {
        RealmUtil.getRealm().executeTransactionAsync(realm -> {
            UnitModel unit = realm.where(UnitModel.class)
                    .equalTo(UnitModel.FIELD_ID, id)
                    .findFirst();
            if (unit != null && unit.isValid()) {
                unit.deleteFromRealm();
            }
        });
    }
}
