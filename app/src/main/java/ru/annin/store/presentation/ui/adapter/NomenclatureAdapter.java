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

package ru.annin.store.presentation.ui.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import ru.annin.store.R;
import ru.annin.store.domain.model.NomenclatureModel;
import ru.annin.store.domain.model.UnitModel;

/**
 * <p> Адаптер "Номенклатуры". </p>
 *
 * @author Pavel Annin.
 */
public class NomenclatureAdapter extends RealmRecyclerAdapter<NomenclatureModel, NomenclatureAdapter.ViewHolder> {

    // Listener's
    private OnClickListener listener;

    public NomenclatureAdapter(boolean automaticUpdate) {
        super(automaticUpdate);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_nomenclature, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final NomenclatureModel model = mRealmResults.get(position);
        if (model != null && model.isValid()) {
            holder.showName(model.getName());
            UnitModel unit = model.getUnit();
            if (unit != null && unit.isValid()) {
                holder.showUnit(unit.getName(), unit.getSymbol());
            }
        }
    }

    public NomenclatureModel getItem(int position) {
        return mRealmResults.get(position);
    }

    public void setOnClickListener(OnClickListener listener) {
        this.listener = listener;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        // View's
        private final TextView txtName;
        private final TextView txtUnit;

        public ViewHolder(View itemView) {
            super(itemView);
            txtName = (TextView) itemView.findViewById(R.id.txt_name);
            txtUnit = (TextView) itemView.findViewById(R.id.txt_unit);
            itemView.setOnClickListener(v -> onItemClick());
        }

        public ViewHolder showName(String text) {
            txtName.setText(text);
            return this;
        }

        public ViewHolder showUnit(String name, String symbol) {
            String format = txtUnit.getResources().getString(R.string.msg_unit_item_name_format, name, symbol);
            txtUnit.setText(format);
            return this;
        }

        void onItemClick() {
            if (listener != null) {
                listener.onItemClick(mRealmResults.get(getAdapterPosition()));
            }
        }
    }

    public interface OnClickListener {
        void onItemClick(NomenclatureModel model);
    }
}
