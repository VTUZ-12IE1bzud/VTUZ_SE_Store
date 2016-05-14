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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ru.annin.store.R;
import ru.annin.store.domain.model.ReceiptProductInvoiceModel;

/**
 * <p> Адаптер товарных накладных. </p>
 *
 * @author Pavel Annin.
 */
public class ReceiptProductInvoiceAdapter extends RealmRecyclerAdapter<ReceiptProductInvoiceModel, ReceiptProductInvoiceAdapter.ViewHolder> {

    // Listener's
    private OnClickListener listener;

    public ReceiptProductInvoiceAdapter(boolean automaticUpdate) {
        super(automaticUpdate);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_receipt_product_invoice, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final ReceiptProductInvoiceModel model = mRealmResults.get(position);
        if (model != null && model.isValid()) {
            holder.showDate(model.getDate())
                    .showName(model.getName())
                    .showProductName(model.getCardProduct().getName())
                    .showProductAmount(model.getAmount(), model.getCardProduct().getUnit().getSymbol())
                    .showProductPrice(model.getPrice())
                    .showProductSum(model.getAmount() * model.getPrice())
                    .showStore(model.getStore().getName());
        }
    }

    public ReceiptProductInvoiceModel getItem(int position) {
        return mRealmResults.get(position);
    }

    public void setOnClickListener(OnClickListener listener) {
        this.listener = listener;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        // View's
        @Bind(R.id.txt_date)
        TextView txtDate;
        @Bind(R.id.txt_name)
        TextView txtName;
        @Bind(R.id.txt_product_name)
        TextView txtProductName;
        @Bind(R.id.txt_product_amount)
        TextView txtProductAmount;
        @Bind(R.id.txt_product_price)
        TextView txtProductPrice;
        @Bind(R.id.txt_product_sum)
        TextView txtProductSum;
        @Bind(R.id.txt_store)
        TextView txtStore;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public ViewHolder showDate(Date date) {
            final String format = SimpleDateFormat.getTimeInstance(DateFormat.SHORT).format(date);
            txtDate.setText(format);
            return this;
        }

        public ViewHolder showName(String text) {
            txtName.setText(text);
            return this;
        }

        public ViewHolder showProductName(String text) {
            txtProductName.setText(text);
            return this;
        }

        public ViewHolder showProductAmount(double number, String unitSymbol) {
            final String format = txtProductAmount.getResources().getString(
                    R.string.msg_receipt_product_invoice_amount_format, number, unitSymbol);
            txtProductAmount.setText(format);
            return this;
        }

        public ViewHolder showProductPrice(double number) {
            final String format = txtProductPrice.getResources().getString(
                    R.string.msg_receipt_product_invoice_price_format, number);
            txtProductPrice.setText(format);
            return this;
        }

        public ViewHolder showProductSum(double number) {
            final String format = txtProductSum.getResources().getString(
                    R.string.msg_receipt_product_invoice_sum_format, number);
            txtProductSum.setText(format);
            return this;
        }

        public ViewHolder showStore(String text) {
            txtStore.setText(text);
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
        void onItemClick(ReceiptProductInvoiceModel model);
    }
}
