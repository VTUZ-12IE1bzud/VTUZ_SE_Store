package ru.annin.store.domain.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.RealmClass;

/**
 * <p>Модель данных: "Номенклатура".</p>
 *
 * @author Pavel Annin, 2016.
 */
@RealmClass
public class NomenclatureModel extends RealmObject {

    public static final String FIELD_ID = "id";
    public static final String FIELD_NAME = "name";
    public static final String FIELD_UNIT = "unit";

    @PrimaryKey
    private String id;
    private String name;
    private UnitModel unit;

    public NomenclatureModel() {
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

    public UnitModel getUnit() {
        return unit;
    }

    public void setUnit(UnitModel unit) {
        this.unit = unit;
    }
}
