package ru.annin.store.presentation.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import io.realm.RealmList;
import ru.annin.store.R;
import ru.annin.store.domain.model.InvoiceModel;
import ru.annin.store.domain.model.ProductModel;
import ru.annin.store.domain.model.StoreModel;

/**
 * <p>Адаптер "Товарных накладных".</p>
 *
 * @author Pavel Annin, 2016.
 */
public class InvoiceAdapter extends RealmRecyclerAdapter<InvoiceModel, InvoiceAdapter.ItemViewHolder> {

    // Listener's
    private OnClickListener listener;

    public InvoiceAdapter(boolean automaticUpdate) {
        super(automaticUpdate);
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View vItem = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_invoice, parent, false);
        return new ItemViewHolder(vItem);
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {
        final InvoiceModel model = mRealmResults.get(position);
        if (model != null && model.isValid()) {
            holder.showDateInvoice(model.getDate())
                    .showNameInvoice(model.getName());
            final StoreModel store = model.getStore();
            if (store != null && store.isValid()) {
                holder.showStore(store.getName());
            }
            final RealmList<ProductModel> products = model.getProducts();
            if (products != null) {
                float price = products.sum(ProductModel.FIELD_PRICE).floatValue();
                holder.showPrice(price);
            }
        }
    }

    public void setOnClickListener(OnClickListener listener) {
        this.listener = listener;
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {

        // View's
        private final TextView txtDateInvoice;
        private final TextView txtNameInvoice;
        private final TextView txtStore;
        private final TextView txtPrice;

        public ItemViewHolder(View itemView) {
            super(itemView);
            txtDateInvoice = (TextView) itemView.findViewById(R.id.txt_date_invoice);
            txtNameInvoice = (TextView) itemView.findViewById(R.id.txt_name_invoice);
            txtStore = (TextView) itemView.findViewById(R.id.txt_store);
            txtPrice = (TextView) itemView.findViewById(R.id.txt_price);

            // Setup
            itemView.setOnClickListener(v -> {
                if (listener != null) listener.onClick(mRealmResults.get(getAdapterPosition()));
            });
        }

        public ItemViewHolder showDateInvoice(Date date) {
            if (date != null) {
                String format = SimpleDateFormat.getDateInstance(DateFormat.SHORT).format(date);
                txtDateInvoice.setText(format);
            }
            return this;
        }

        public ItemViewHolder showNameInvoice(String text) {
            txtNameInvoice.setText(text);
            return this;
        }

        public ItemViewHolder showStore(String text) {
            txtStore.setText(text);
            return this;
        }

        public ItemViewHolder showPrice(float price) {
            String format = txtPrice.getResources().getString(R.string.item_price_format, price);
            txtPrice.setText(format);
            return this;
        }
    }

    public interface OnClickListener {
        void onClick(InvoiceModel model);
    }
}