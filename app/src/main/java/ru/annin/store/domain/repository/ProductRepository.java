package ru.annin.store.domain.repository;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import ru.annin.store.domain.model.ProductModel;
import rx.Observable;

/**
 * <p>Интерфейс репозитория "Товар".</p>
 *
 * @author Pavel Annin, 2016.
 */
public interface ProductRepository {

    @NonNull
    Observable<ProductModel> getById(String id);

    @Nullable
    ProductModel create(String nomenclatureId, float amount, float price);

    void asyncEdit(String productId, String nomenclatureId, float amount, float price);

    void asyncRemove(String Id);

}
