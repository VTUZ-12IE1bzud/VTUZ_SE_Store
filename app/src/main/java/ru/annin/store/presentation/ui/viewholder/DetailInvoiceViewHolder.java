package ru.annin.store.presentation.ui.viewholder;

import android.app.DatePickerDialog;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.EditText;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import io.realm.RealmResults;
import ru.annin.store.R;
import ru.annin.store.domain.model.ProductModel;
import ru.annin.store.presentation.common.BaseViewHolder;
import ru.annin.store.presentation.ui.adapter.ProductAdapter;

/**
 * <p>ViewHolder экрана "Приходная накладная".</p>
 *
 * @author Pavel Annin, 2016.
 */
public class DetailInvoiceViewHolder extends BaseViewHolder {

    // View's
    private final Toolbar vToolbar;
    private final FloatingActionButton fabAdd;
    private final TextInputLayout tilStore;
    private final EditText edtStore;
    private final TextInputLayout tilDate;
    private final EditText edtDate;
    private final TextInputLayout tilNameInvoice;
    private final EditText edtNameInvoice;
    private final RecyclerView rcList;
    private final View vEmpty;

    // Adapter's
    private final ProductAdapter adapter;

    // Listener's
    private OnInteractionListener listener;

    public DetailInvoiceViewHolder(@NonNull View view) {
        super(view);
        vToolbar = (Toolbar) vRoot.findViewById(R.id.toolbar);
        fabAdd = (FloatingActionButton) vRoot.findViewById(R.id.fab_add);
        tilStore = (TextInputLayout) vRoot.findViewById(R.id.til_store);
        edtStore = (EditText) vRoot.findViewById(R.id.edt_store);
        tilDate = (TextInputLayout) vRoot.findViewById(R.id.til_date);
        edtDate = (EditText) vRoot.findViewById(R.id.edt_date);
        tilNameInvoice = (TextInputLayout) vRoot.findViewById(R.id.til_name_invoice);
        edtNameInvoice = (EditText) vRoot.findViewById(R.id.edt_name_invoice);
        rcList = (RecyclerView) vRoot.findViewById(android.R.id.list);
        vEmpty = vRoot.findViewById(android.R.id.empty);

        // Setup
        adapter = new ProductAdapter(false);
        adapter.setViewEmpty(vEmpty);
        adapter.setOnClickListener(model -> {if (listener != null) listener.onEditProduct(model.getId());});
        vToolbar.inflateMenu(R.menu.menu_invoice_detail);
        rcList.setAdapter(adapter);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(onItemTouchHelper);
        itemTouchHelper.attachToRecyclerView(rcList);
        vToolbar.setNavigationOnClickListener(v -> {if (listener != null) listener.onBackClick();});
        vToolbar.setOnMenuItemClickListener(item -> {
            if (listener != null) {
                switch (item.getItemId()) {
                    case R.id.action_doc:
                        listener.onReportClick();
                        return true;
                    case R.id.action_save:
                        listener.onSaveClick(edtNameInvoice.getText().toString());
                        return true;
                    default: break;
                }
            }
            return false;
        });
        fabAdd.setOnClickListener(v -> {if (listener != null) listener.onAddClick();});
        edtDate.setOnClickListener(v -> {if (listener != null) listener.onDateClick();});
    }

    public DetailInvoiceViewHolder enableAnimation(boolean enabled) {
        tilStore.setHintAnimationEnabled(enabled);
        tilDate.setHintAnimationEnabled(enabled);
        tilNameInvoice.setHintAnimationEnabled(enabled);
        return this;
    }

    public DetailInvoiceViewHolder showMovementTo(String text) {
        edtStore.setText(text);
        return this;
    }

    public DetailInvoiceViewHolder showDate(Date date) {
        String format = SimpleDateFormat.getDateInstance(DateFormat.LONG).format(date);
        edtDate.setText(format);
        return this;
    }

    public DetailInvoiceViewHolder showNameInvoice(String text) {
        edtNameInvoice.setText(text);
        return this;
    }

    public DetailInvoiceViewHolder errorNameInvoice(String text) {
        tilNameInvoice.setError(text);
        return this;
    }

    public DetailInvoiceViewHolder showDatePicker(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        DatePickerDialog dialog = new DatePickerDialog(vRoot.getContext(),
                (view, year, monthOfYear, dayOfMonth) -> {
                    if (listener != null) {
                        Calendar calendar = Calendar.getInstance();
                        calendar.setTimeInMillis(0);
                        calendar.set(Calendar.YEAR, year);
                        calendar.set(Calendar.MONTH, monthOfYear);
                        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        listener.onDateSelect(calendar.getTime());
                    }
                },
                cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
        dialog.show();
        return this;
    }

    public DetailInvoiceViewHolder showProducts(RealmResults<ProductModel> models) {
        adapter.updateRealmResults(models);
        return this;
    }

    public DetailInvoiceViewHolder showReportAction() {
        vToolbar.getMenu().findItem(R.id.action_doc).setVisible(true);
        return this;
    }

    public DetailInvoiceViewHolder hideReportAction() {
        vToolbar.getMenu().findItem(R.id.action_doc).setVisible(false);
        return this;
    }

    public void setOnInteractionListener(OnInteractionListener listener) {
        this.listener = listener;
    }

    private final ItemTouchHelper.SimpleCallback onItemTouchHelper = new ItemTouchHelper.SimpleCallback(0,
            ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
        @Override
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
            final int position = viewHolder.getAdapterPosition();
            adapter.notifyItemRemoved(position);
            if (listener != null) {
                listener.onRemoveProduct(adapter.getItem(position).getId());
            }
        }
    };

    public DetailInvoiceViewHolder showMessage(String text) {
        Snackbar.make(vRoot, text, Snackbar.LENGTH_LONG).show();
        return this;
    }

    public interface OnInteractionListener {
        void onBackClick();
        void onSaveClick(String invoice);
        void onAddClick();
        void onEditProduct(String productId);
        void onRemoveProduct(String productId);
        void onDateClick();
        void onDateSelect(Date date);
        void onReportClick();
    }
}
