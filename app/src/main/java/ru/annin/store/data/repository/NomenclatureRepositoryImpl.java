package ru.annin.store.data.repository;

import android.support.annotation.NonNull;

import java.util.UUID;

import io.realm.RealmResults;
import ru.annin.store.data.util.RealmUtil;
import ru.annin.store.domain.model.NomenclatureModel;
import ru.annin.store.domain.model.ProductModel;
import ru.annin.store.domain.model.UnitModel;
import ru.annin.store.domain.repository.NomenclatureRepository;
import rx.Observable;

/**
 * <p>Реализация репозитория "Номенклатура".</p>
 *
 * @author Pavel Annin, 2016.
 */
public class NomenclatureRepositoryImpl implements NomenclatureRepository {

    @NonNull
    @Override
    public Observable<RealmResults<NomenclatureModel>> getAll() {
        return RealmUtil.getRealm().where(NomenclatureModel.class)
                .findAllSortedAsync(NomenclatureModel.FIELD_NAME)
                .asObservable()
                .filter(RealmResults::isLoaded);
    }

    @NonNull
    @Override
    public Observable<NomenclatureModel> getById(@NonNull String id) {
        return RealmUtil.getRealm().where(NomenclatureModel.class)
                .equalTo(NomenclatureModel.FIELD_ID, id)
                .findFirst()
                .asObservable();
    }

    @Override
    public void asyncCreate(@NonNull String name, @NonNull String unitId) {
        RealmUtil.getRealm().executeTransactionAsync(realm -> {
            NomenclatureModel model = realm.createObject(NomenclatureModel.class);
            model.setId(UUID.randomUUID().toString());
            model.setName(name);
            UnitModel unit = realm.where(UnitModel.class)
                    .equalTo(UnitModel.FIELD_ID, unitId)
                    .findFirst();
            model.setUnit(unit);
        });
    }

    @Override
    public void asyncSave(@NonNull String id, @NonNull String name, @NonNull String unitId) {
        RealmUtil.getRealm().executeTransactionAsync(realm -> {
            NomenclatureModel model = realm.where(NomenclatureModel.class)
                    .equalTo(NomenclatureModel.FIELD_ID, id)
                    .findFirst();
            if (model != null && model.isValid()) {
                model.setName(name);
                UnitModel unit = realm.where(UnitModel.class)
                        .equalTo(UnitModel.FIELD_ID, unitId)
                        .findFirst();
                model.setUnit(unit);
            }
        });
    }

    @Override
    public boolean canRemoved(@NonNull String id) {
        return RealmUtil.getRealm().where(ProductModel.class)
                .equalTo(ProductModel.FIELD_NOMENCLATURE + "." + NomenclatureModel.FIELD_ID, id)
                .count() == 0;
    }

    @Override
    public void asyncRemove(@NonNull String id) {
        RealmUtil.getRealm().executeTransactionAsync(realm -> {
            NomenclatureModel model = realm.where(NomenclatureModel.class)
                    .equalTo(NomenclatureModel.FIELD_ID, id)
                    .findFirst();
            if (model != null && model.isValid()) {
                model.deleteFromRealm();
            }
        });
    }
}
