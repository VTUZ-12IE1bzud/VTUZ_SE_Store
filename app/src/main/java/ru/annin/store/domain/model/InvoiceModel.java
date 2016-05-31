package ru.annin.store.domain.model;

import java.util.Date;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.RealmClass;

/**
 * <p>Модуль данных: "Накладная".</p>
 *
 * @author Pavel Annin, 2016.
 */
@RealmClass
public class InvoiceModel extends RealmObject {

    public static final String FIELD_ID = "id";
    public static final String FILED_NAME = "name";
    public static final String FIELD_DATE = "date";
    public static final String FIELD_STORE = "store";
    public static final String FIELD_PRODUCTS = "products";

    @PrimaryKey
    private String id;
    private String name;
    private Date date;
    private StoreModel store;
    private RealmList<ProductModel> products;

    public InvoiceModel() {
        // Empty
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public StoreModel getStore() {
        return store;
    }

    public void setStore(StoreModel store) {
        this.store = store;
    }

    public RealmList<ProductModel> getProducts() {
        return products;
    }

    public void setProducts(RealmList<ProductModel> products) {
        this.products = products;
    }
}
