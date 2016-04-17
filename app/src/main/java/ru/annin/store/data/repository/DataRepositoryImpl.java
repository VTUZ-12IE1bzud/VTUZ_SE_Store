/*
 * Copyright 2016, Pavel Annin
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ru.annin.store.data.repository;

import android.support.annotation.NonNull;
import android.util.Log;

import java.util.UUID;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.realm.Realm;
import io.realm.RealmResults;
import ru.annin.store.data.util.RealmUtil;
import ru.annin.store.domain.model.StoreModel;
import ru.annin.store.domain.model.UnitModel;
import ru.annin.store.domain.repository.DataRepository;
import rx.Observable;

/**
 * Реализация репозитория данных.
 *
 * @author Pavel Annin.
 */
@Singleton
public class DataRepositoryImpl implements DataRepository {

    private static final String TAG = "[Data Repository]";

    private final Realm mRealm;

    @Inject
    public DataRepositoryImpl() {
        mRealm = RealmUtil.getRealm();
    }

    @Override
    public Observable<RealmResults<StoreModel>> listStores() {
        return mRealm.where(StoreModel.class)
                .findAllSortedAsync(StoreModel.FIELD_NAME)
                .asObservable()
                .filter(RealmResults::isLoaded);
    }

    @Override
    public Observable<StoreModel> getStoreById(String storeId) {
        return mRealm.where(StoreModel.class)
                .equalTo(UnitModel.FIELD_ID, storeId)
                .findFirst()
                .asObservable();
    }

    @Override
    public boolean createStore(@NonNull String name) {
        try {
            mRealm.beginTransaction();
            StoreModel store = mRealm.createObject(StoreModel.class);
            store.setId(UUID.randomUUID().toString());
            store.setName(name);
            mRealm.commitTransaction();
            return true;
        } catch (RuntimeException e) {
            Log.e(TAG, "Ошибка: ", e);
            mRealm.cancelTransaction();
        }
        return false;
    }

    @Override
    public boolean saveStore(@NonNull String storeId, @NonNull String name) {
        try {
            mRealm.beginTransaction();
            StoreModel store = mRealm.where(StoreModel.class)
                    .equalTo(StoreModel.FIELD_ID, storeId)
                    .findFirst();
            store.setName(name);
            mRealm.commitTransaction();
            return true;
        } catch (RuntimeException e) {
            Log.e(TAG, "Ошибка: ", e);
            mRealm.cancelTransaction();
        }
        return false;
    }

    @Override
    public boolean canStoreRemoved(@NonNull String storeId) {
        return true;
    }

    @Override
    public boolean removeStore(@NonNull String storeId) {
        try {
            StoreModel store = mRealm.where(StoreModel.class)
                    .equalTo(StoreModel.FIELD_ID, storeId)
                    .findFirst();
            mRealm.beginTransaction();
            store.removeFromRealm();
            mRealm.commitTransaction();
            return true;
        } catch (RuntimeException e) {
            Log.e(TAG, "Ошибка: ", e);
            mRealm.cancelTransaction();
        }
        return false;
    }

    @Override
    public Observable<RealmResults<UnitModel>> listUnits() {
        return mRealm.where(UnitModel.class)
                .findAllSortedAsync(UnitModel.FIELD_NAME)
                .asObservable()
                .filter(RealmResults::isLoaded);
    }

    @Override
    public Observable<UnitModel> getUnitById(String unitId) {
        return mRealm.where(UnitModel.class)
                .equalTo(UnitModel.FIELD_ID, unitId)
                .findFirst()
                .asObservable();
    }

    @Override
    public boolean createUnit(@NonNull String name, @NonNull String symbol, @NonNull String description) {
        try {
            mRealm.beginTransaction();
            UnitModel unit = mRealm.createObject(UnitModel.class);
            unit.setId(UUID.randomUUID().toString());
            unit.setName(name);
            unit.setSymbol(symbol);
            unit.setDescription(description);
            mRealm.commitTransaction();
            return true;
        } catch (RuntimeException e) {
            Log.e(TAG, "Ошибка: ", e);
            mRealm.cancelTransaction();
        }
        return false;
    }

    @Override
    public boolean saveUnit(@NonNull String unitId, @NonNull String name, @NonNull String symbol, @NonNull String description) {
        try {
            mRealm.beginTransaction();
            UnitModel unit = mRealm.where(UnitModel.class)
                    .equalTo(UnitModel.FIELD_ID, unitId)
                    .findFirst();
            unit.setName(name); 
            unit.setSymbol(symbol);
            unit.setDescription(description);
            mRealm.commitTransaction();
            return true;
        } catch (RuntimeException e) {
            Log.e(TAG, "Ошибка: ", e);
            mRealm.cancelTransaction();
        }
        return false;
    }

    @Override
    public boolean canUnitRemoved(@NonNull String unitId) {
        return true;
    }

    @Override
    public boolean removeUnit(@NonNull String unitId) {
        try {
            UnitModel unit = mRealm.where(UnitModel.class)
                    .equalTo(UnitModel.FIELD_ID, unitId)
                    .findFirst();
            mRealm.beginTransaction();
            unit.removeFromRealm();
            mRealm.commitTransaction();
            return true;
        } catch (RuntimeException e) {
            Log.e(TAG, "Ошибка: ", e);
            mRealm.cancelTransaction();
        }
        return false;
    }
}
