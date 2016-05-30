package ru.annin.store.domain.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.RealmClass;

/**
 * <p>Модель данныж: "Товар".</p>
 *
 * @author Pavel Annin, 2016.
 */
@RealmClass
public class ProductModel extends RealmObject {

    public static final String FIELD_ID = "id";
    public static final String FIELD_NOMENCLATURE = "nomenclature";
    public static final String FILED_AMOUNT = "amount";
    public static final String FIELD_PRICE = "price";

    @PrimaryKey
    private String id;
    private NomenclatureModel nomenclature;
    private float amount;
    private float price;

    public ProductModel() {
        // Empty
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public NomenclatureModel getNomenclature() {
        return nomenclature;
    }

    public void setNomenclature(NomenclatureModel nomenclature) {
        this.nomenclature = nomenclature;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }
}
