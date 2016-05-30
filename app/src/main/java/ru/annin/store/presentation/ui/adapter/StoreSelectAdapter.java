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
import ru.annin.store.domain.model.StoreModel;

/**
 * <p>Адаптер "Складов".</p>
 *
 * @author Pavel Annin, 2016.
 */
public class StoreSelectAdapter extends ArrayAdapter<StoreModel> {

    private final LayoutInflater inflater;
    private RealmResults<StoreModel> realmResults;

    public StoreSelectAdapter(Context context) {
        super(context, R.layout.item_sore_select);
        inflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_sore_select, parent, false);
        }
        final StoreModel model = realmResults.get(position);
        if (model != null && model.isValid()) {
            final ViewHolder viewHolder = new ViewHolder(convertView);
            viewHolder.showName(model.getName());
        }
        return convertView;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_sore_select_drop, parent, false);
        }
        final StoreModel model = realmResults.get(position);
        if (model != null && model.isValid()) {
            final ViewHolder viewHolder = new ViewHolder(convertView);
            viewHolder.showName(model.getName());
        }
        return convertView;
    }

    @Override
    public long getItemId(int position) {
        final StoreModel model = realmResults.get(position);
        return model != null && model.isValid() ? model.getId().hashCode() : -1;
    }

    @Override
    public int getCount() {
        if (realmResults != null) {
            return realmResults.size();
        }
        return 0;
    }

    public void updateData(RealmResults<StoreModel> data) {
        realmResults = data;
        notifyDataSetChanged();
    }

    @Nullable
    public String getId(int position) {
        if (realmResults != null && position >= 0 && realmResults.size() > position) {
            final StoreModel model = realmResults.get(position);
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

        private final TextView txtName;

        public ViewHolder(View view) {
            txtName = (TextView) view.findViewById(android.R.id.text1);
        }

        public ViewHolder showName(String text) {
            txtName.setText(text);
            return this;
        }
    }
}
