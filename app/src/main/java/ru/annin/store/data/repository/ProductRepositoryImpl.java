package ru.annin.store.data.repository;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.UUID;

import io.realm.Realm;
import io.realm.exceptions.RealmException;
import ru.annin.store.data.util.RealmUtil;
import ru.annin.store.domain.model.NomenclatureModel;
import ru.annin.store.domain.model.ProductModel;
import ru.annin.store.domain.repository.ProductRepository;
import rx.Observable;

/**
 * <p>Реализация репозитория "Товар".</p>
 *
 * @author Pavel Annin, 2016.
 */
public class ProductRepositoryImpl implements ProductRepository {


    @NonNull
    @Override
    public Observable<ProductModel> getById(String id) {
        return RealmUtil.getRealm().where(ProductModel.class)
                .equalTo(ProductModel.FIELD_ID, id)
                .findFirst()
                .asObservable();
    }

    @Nullable
    @Override
    public ProductModel create(String nomenclatureId, float amount, float price) {
        Realm realm = RealmUtil.getRealm();
        ProductModel product = null;
        try {
            realm.beginTransaction();
            product = realm.createObject(ProductModel.class);
            product.setId(UUID.randomUUID().toString());
            product.setAmount(amount);
            product.setPrice(price);
            product.setNomenclature(realm.where(NomenclatureModel.class)
                    .equalTo(NomenclatureModel.FIELD_ID, nomenclatureId)
                    .findFirst());
            realm.commitTransaction();
        } catch (RealmException ignore) {
            realm.cancelTransaction();
        }
        return product;
    }

    @Override
    public void asyncEdit(String productId, String nomenclatureId, float amount, float price) {
        RealmUtil.getRealm().executeTransactionAsync(realm -> {
            ProductModel product = realm.where(ProductModel.class)
                    .equalTo(ProductModel.FIELD_ID, productId)
                    .findFirst();
            if (product != null && product.isValid()) {
                product.setAmount(amount);
                product.setPrice(price);
                product.setNomenclature(realm.where(NomenclatureModel.class)
                        .equalTo(NomenclatureModel.FIELD_ID, nomenclatureId)
                        .findFirst());
            }
        });
    }

    @Override
    public void asyncRemove(String Id) {
        RealmUtil.getRealm().executeTransactionAsync(realm -> {
            ProductModel product = realm.where(ProductModel.class)
                    .equalTo(ProductModel.FIELD_ID, Id)
                    .findFirst();
            if (product != null && product.isValid()) {
                product.deleteFromRealm();
            }
        });
    }
}
