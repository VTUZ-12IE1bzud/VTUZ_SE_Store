package ru.annin.store.presentation.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import ru.annin.store.R;
import ru.annin.store.domain.model.NomenclatureModel;
import ru.annin.store.domain.model.ProductModel;
import ru.annin.store.domain.model.UnitModel;

/**
 * <p>Адаптер "Товаров".</p>
 *
 * @author Pavel Annin, 2016.
 */
public class ProductAdapter extends RealmRecyclerAdapter<ProductModel, ProductAdapter.ItemViewHolder> {

    // Listener's
    private OnClickListener listener;

    public ProductAdapter(boolean automaticUpdate) {
        super(automaticUpdate);
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View vItem = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product, parent, false);
        return new ItemViewHolder(vItem);
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {
        final ProductModel model = mRealmResults.get(position);
        if (model != null && model.isValid()) {
            float amount = model.getAmount();
            float price = model.getPrice();
            float sum = amount * price;
            holder.showPriceProduct(price)
                    .showSumProduct(sum);
            final NomenclatureModel nomenclature = model.getNomenclature();
            if (nomenclature != null && nomenclature.isValid()) {
                holder.showNameProduct(nomenclature.getName());
                final UnitModel unit = nomenclature.getUnit();
                if (unit != null && unit.isValid()) {
                    holder.showAmountProduct(amount, unit.getSymbol());
                }
            }
        }
    }

    public void setOnClickListener(OnClickListener listener) {
        this.listener = listener;
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {

        // View's
        private final TextView txtNameProduct;
        private final TextView txtPriceProduct;
        private final TextView txtAmountProduct;
        private final TextView txtSumProduct;


        public ItemViewHolder(View itemView) {
            super(itemView);
            txtNameProduct = (TextView) itemView.findViewById(R.id.txt_name_product);
            txtPriceProduct = (TextView) itemView.findViewById(R.id.txt_price_product);
            txtAmountProduct = (TextView) itemView.findViewById(R.id.txt_amount_product);
            txtSumProduct = (TextView) itemView.findViewById(R.id.txt_sum_product);
            itemView.setOnClickListener(v -> {
                if (listener != null)
                    listener.onClickListener(mRealmResults.get(getAdapterPosition()));
            });
        }

        public ItemViewHolder showNameProduct(String text) {
            txtNameProduct.setText(text);
            return this;
        }

        public ItemViewHolder showPriceProduct(float price) {
            String format = txtPriceProduct.getResources()
                    .getString(R.string.item_product_price_format, Float.toString(price));
            txtPriceProduct.setText(format);
            return this;
        }

        public ItemViewHolder showAmountProduct(float amount, String symbolUnit) {
            String format = txtAmountProduct.getResources()
                    .getString(R.string.item_product_amount_format, Float.toString(amount), symbolUnit);
            txtAmountProduct.setText(format);
            return this;
        }

        public ItemViewHolder showSumProduct(float sum) {
            String format = txtSumProduct.getResources()
                    .getString(R.string.item_product_sum_format, Float.toString(sum));
            txtSumProduct.setText(format);
            return this;
        }
    }

    public interface OnClickListener {
        void onClickListener(ProductModel model);
    }
}
