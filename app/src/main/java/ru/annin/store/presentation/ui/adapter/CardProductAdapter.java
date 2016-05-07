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

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ru.annin.store.R;
import ru.annin.store.domain.model.CardProductModel;

/**
 * <p> Адаптер карточек товаров. </p>
 *
 * @author Pavel Annin.
 */
public class CardProductAdapter extends RealmRecyclerAdapter<CardProductModel, CardProductAdapter.ViewHolder> {

    // Listener's
    private OnClickListener listener;

    public CardProductAdapter(boolean automaticUpdate) {
        super(automaticUpdate);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_card_product, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final CardProductModel model = mRealmResults.get(position);
        if (model != null && model.isValid()) {
            holder.showName(model.getName())
                    .showUnit(model.getUnit().getName());
        }
    }

    public CardProductModel getItem(int position) {
        return mRealmResults.get(position);
    }

    public void setOnClickListener(OnClickListener listener) {
        this.listener = listener;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        // View's
        @Bind(R.id.txt_title)
        TextView txtTitle;
        @Bind(R.id.txt_unit)
        TextView txtUnit;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public ViewHolder showName(String name) {
            txtTitle.setText(name);
            return this;
        }

        public ViewHolder showUnit(String unit) {
            txtUnit.setText(unit);
            return this;
        }

        @OnClick(R.id.main_item)
        void onItemClick() {
            if (listener != null) {
                listener.onItemClick(mRealmResults.get(getAdapterPosition()));
            }
        }
    }

    public interface OnClickListener {
        void onItemClick(CardProductModel model);
    }
}
