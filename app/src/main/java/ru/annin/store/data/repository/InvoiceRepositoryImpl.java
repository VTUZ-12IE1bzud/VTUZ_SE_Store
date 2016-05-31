package ru.annin.store.data.repository;

import android.support.annotation.NonNull;

import java.util.Date;
import java.util.UUID;

import io.realm.RealmList;
import io.realm.RealmResults;
import ru.annin.store.data.util.RealmUtil;
import ru.annin.store.domain.model.InvoiceModel;
import ru.annin.store.domain.model.ProductModel;
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
                .notEqualTo(InvoiceModel.FIELD_ID, TEMP_RECEIVER_PRODUCT_ID)
                .findAllSortedAsync(InvoiceModel.FIELD_DATE)
                .asObservable()
                .filter(RealmResults::isLoaded);
    }

    @NonNull
    @Override
    public Observable<InvoiceModel> getById(@NonNull String id) {
        return RealmUtil.getRealm().where(InvoiceModel.class)
                .equalTo(InvoiceModel.FIELD_ID, id)
                .findFirst()
                .asObservable();
    }

    @Override
    public void asyncCreateReceiverFromTemp(@NonNull String name, @NonNull String storeId, @NonNull Date date) {
        RealmUtil.getRealm().executeTransactionAsync(realm -> {
            InvoiceModel model = realm.createObject(InvoiceModel.class);
            InvoiceModel tempProduct = realm.where(InvoiceModel.class)
                    .equalTo(InvoiceModel.FIELD_ID, TEMP_RECEIVER_PRODUCT_ID)
                    .findFirst();
            model.setId(UUID.randomUUID().toString());
            model.setName(name);
            model.setDate(date);
            model.setStore(realm.where(StoreModel.class)
                    .equalTo(StoreModel.FIELD_ID, storeId)
                    .findFirst());
            model.setProducts(tempProduct.getProducts());
            tempProduct.setProducts(null);
        });
    }

    @Override
    public void asyncEdit(@NonNull String id, @NonNull String name, @NonNull Date date) {
        RealmUtil.getRealm().executeTransactionAsync(realm -> {
            InvoiceModel model = realm.where(InvoiceModel.class)
                    .equalTo(InvoiceModel.FIELD_ID, id)
                    .findFirst();
            if (model != null) {
                model.setName(name);
                model.setDate(date);
            }
        });
    }

    @Override
    public void asyncAddProduct(@NonNull String id, @NonNull String productId) {
        RealmUtil.getRealm().executeTransactionAsync(realm -> {
            InvoiceModel model = realm.where(InvoiceModel.class)
                    .equalTo(InvoiceModel.FIELD_ID, id)
                    .findFirst();
            ProductModel product = realm.where(ProductModel.class)
                    .equalTo(ProductModel.FIELD_ID, productId)
                    .findFirst();
            if (model != null && product != null) {
                RealmList<ProductModel> products = model.getProducts();
                if (products == null) {
                    products = new RealmList<>();
                }
                products.add(product);
            }
        });
    }

    @Override
    public void asyncRemove(@NonNull String id) {
        RealmUtil.getRealm().executeTransactionAsync(realm -> {
            InvoiceModel model = realm.where(InvoiceModel.class)
                    .equalTo(InvoiceModel.FIELD_ID, id)
                    .findFirst();
            if (model != null && model.isValid()) {
                model.deleteFromRealm();
            }
        });
    }
}
