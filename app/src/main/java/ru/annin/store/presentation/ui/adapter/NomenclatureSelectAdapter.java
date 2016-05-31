package ru.annin.store.presentation.ui.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import io.realm.RealmResults;
import ru.annin.store.R;
import ru.annin.store.domain.model.NomenclatureModel;
import ru.annin.store.domain.model.UnitModel;

/**
 * <p>Адаптер "Товары".</p>
 *
 * @author Pavel Annin, 2016.
 */
public class NomenclatureSelectAdapter extends ArrayAdapter<NomenclatureModel> {

    private final LayoutInflater inflater;
    private RealmResults<NomenclatureModel> realmResults;

    public NomenclatureSelectAdapter(Context context) {
        super(context, R.layout.item_nomenclature_select);
        inflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_nomenclature_select, parent, false);
        }
        final NomenclatureModel model = realmResults.get(position);
        if (model != null && model.isValid()) {
            final ViewHolder viewHolder = new ViewHolder(convertView);
            viewHolder.showNameProduct(model.getName());
            final UnitModel unit = model.getUnit();
            if (unit != null && unit.isValid()) {
                viewHolder.showUnitProduct(unit.getName(), unit.getSymbol());
            }
        }
        return convertView;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_nomenclature_select, parent, false);
        }
        final NomenclatureModel model = realmResults.get(position);
        if (model != null && model.isValid()) {
            final ViewHolder viewHolder = new ViewHolder(convertView);
            viewHolder.showNameProduct(model.getName());
            final UnitModel unit = model.getUnit();
            if (unit != null && unit.isValid()) {
                viewHolder.showUnitProduct(unit.getName(), unit.getSymbol());
            }
        }
        return convertView;
    }

    @Override
    public long getItemId(int position) {
        final NomenclatureModel model = realmResults.get(position);
        return model != null && model.isValid() ? model.getId().hashCode() : -1;
    }

    @Override
    public int getCount() {
        if (realmResults != null) {
            return realmResults.size();
        }
        return 0;
    }

    public void updateData(RealmResults<NomenclatureModel> data) {
        realmResults = data;
        notifyDataSetChanged();
    }

    @Nullable
    public String getId(int position) {
        if (realmResults != null && position >= 0 && realmResults.size() > position) {
            final NomenclatureModel model = realmResults.get(position);
            if (model.isValid()) {
                return realmResults.get(position).getId();
            }
        }
        return null;
    }

    public int getPosition(String id) {
        if (realmResults != null) {
            for(int i = 0; i < realmResults.size(); ++i) {
                final String dataId = realmResults.get(i).getId();
                if (TextUtils.equals(dataId, id)) {
                    return i;
                }
            }
        }
        return -1;
    }

    private class ViewHolder {

        private final TextView txtNameProduct;
        private final TextView txtUnitProduct;

        public ViewHolder(View view) {
            txtNameProduct = (TextView) view.findViewById(R.id.txt_name_product);
            txtUnitProduct = (TextView) view.findViewById(R.id.txt_unit_product);
        }

        public ViewHolder showNameProduct(String text) {
            txtNameProduct.setText(text);
            return this;
        }

        public ViewHolder showUnitProduct(String name, String symbol) {
            String format = txtUnitProduct.getResources()
                    .getString(R.string.msg_unit_item_name_format, name, symbol);
            txtUnitProduct.setText(format);
            return this;
        }
    }
}
