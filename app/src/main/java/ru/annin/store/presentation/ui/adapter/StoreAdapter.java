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

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import ru.annin.store.R;
import ru.annin.store.domain.model.StoreModel;

/**
 * <p> Адаптер складов. </p>
 *
 * @author Pavel Annin.
 */
public class StoreAdapter extends RealmRecyclerAdapter<StoreModel, StoreAdapter.ViewHolder> {

    // Listener's
    private OnClickListener listener;

    public StoreAdapter(boolean automaticUpdate) {
        super(automaticUpdate);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_store, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final StoreModel store = mRealmResults.get(position);
        if (store != null && store.isValid()) {
            holder.showName(store.getName());
        }
    }

    public StoreModel getItem(int position) {
        return mRealmResults.get(position);
    }

    public void setOnClickListener(OnClickListener listener) {
        this.listener = listener;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        // View's
        private final TextView txtName;

        public ViewHolder(View itemView) {
            super(itemView);
            txtName =(TextView) itemView.findViewById(R.id.txt_name_store);
            itemView.setOnClickListener(v -> onItemClick());
        }

        public ViewHolder showName(String name) {
            txtName.setText(name);
            return this;
        }

        void onItemClick() {
            if (listener != null) {
                listener.onItemClick(mRealmResults.get(getAdapterPosition()));
            }
        }
    }

    public interface OnClickListener {
        void onItemClick(StoreModel model);
    }
}
