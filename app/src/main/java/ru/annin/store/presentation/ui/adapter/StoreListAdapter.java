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

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import io.realm.RealmResults;
import ru.annin.store.R;
import ru.annin.store.domain.model.StoreModel;

/**
 * <p> Адаптер списка складов. </p>
 *
 * @author Pavel Annin.
 */
public class StoreListAdapter extends ArrayAdapter<StoreModel> {

    private final LayoutInflater inflater;
    private RealmResults<StoreModel> mData;

    public StoreListAdapter(@NonNull Context ctx) {
        super(ctx, R.layout.item_list_store);
        inflater = LayoutInflater.from(ctx);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_list_store, parent, false);
        }
        final StoreModel model = mData.get(position);
        if (model.isValid()) {
            final ViewHolder viewHolder = new ViewHolder(convertView);
            viewHolder.showName(model.getName());
        }
        return convertView;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(android.R.layout.simple_list_item_1, parent, false);
        }
        final StoreModel model = mData.get(position);
        if (model.isValid()) {
            final TextView textView = (TextView) convertView.findViewById(android.R.id.text1);
            textView.setText(model.getName());
        }
        return convertView;
    }

    @Override
    public long getItemId(int position) {
        final StoreModel model = mData.get(position);
        return model.isValid() ? model.getId().hashCode() : -1;
    }

    @Override
    public int getCount() {
        if (mData != null) {
            return mData.size();
        }
        return 0;
    }

    public void updateData(RealmResults<StoreModel> data) {
        mData = data;
        notifyDataSetChanged();
    }

    private class ViewHolder {

        private final TextView txtName;

        public ViewHolder(View view) {
            txtName = (TextView) view.findViewById(R.id.txt_name);
        }

        public ViewHolder showName(String text) {
            txtName.setText(text);
            return this;
        }
    }
}
