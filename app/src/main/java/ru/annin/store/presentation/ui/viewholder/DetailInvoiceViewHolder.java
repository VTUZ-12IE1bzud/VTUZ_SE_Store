package ru.annin.store.presentation.ui.viewholder;

import android.app.DatePickerDialog;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
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
    private final ProductListAdapter adapter;

    // Listener's
    private OnInteractionListener listener;

    public DetailInvoiceViewHolder(@NonNull View view) {
        super(view);
        vToolbar = (Toolbar) vRoot.findViewById(R.id.toolbar);
        fabAdd = (FloatingActionButton) vRoot.findViewById(R.id.fab_add);
        tilMovementTo = (TextInputLayout) vRoot.findViewById(R.id.til_movement_to);
        edtMovementTo = (EditText) vRoot.findViewById(R.id.edt_movement_to);
        tilNameEmployee = (TextInputLayout) vRoot.findViewById(R.id.til_name_employee);
        edtNameEmployee = (EditText) vRoot.findViewById(R.id.edt_name_employee);
        tilDate = (TextInputLayout) vRoot.findViewById(R.id.til_date);
        edtDate = (EditText) vRoot.findViewById(R.id.edt_date);
        tilNameInvoice = (TextInputLayout) vRoot.findViewById(R.id.til_name_invoice);
        edtNameInvoice = (EditText) vRoot.findViewById(R.id.edt_name_invoice);
        rcList = (RecyclerView) vRoot.findViewById(android.R.id.list);
        vEmpty = vRoot.findViewById(android.R.id.empty);

        // Setup
        adapter = new ProductListAdapter();
        adapter.setViewEmpty(vEmpty);
        adapter.setOnClickListener(model -> {if (listener != null) listener.onEditProduct(model.getId());});
        vToolbar.inflateMenu(R.menu.menu_invoice);
        rcList.setAdapter(adapter);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(onItemTouchHelper);
        itemTouchHelper.attachToRecyclerView(rcList);
        RxToolbar.navigationClicks(vToolbar).subscribe(aVoid -> {if (listener != null) listener.onBackClick();});
        RxToolbar.itemClicks(vToolbar).subscribe(menuItem -> {
            if (listener != null) {
                switch (menuItem.getItemId()) {
                    case R.id.action_save:
                        listener.onSaveClick(edtNameInvoice.getText().toString());
                        break;
                    default: break;
                }
            }
        });
        RxView.clicks(fabAdd).subscribe(aVoid -> {if (listener != null) listener.onAddClick();});
        RxView.clicks(edtDate).subscribe(aVoid -> {if (listener != null) listener.onDateClick();});
    }

    public DetailReceiverProductViewHolder enableAnimation(boolean enabled) {
        tilMovementTo.setHintAnimationEnabled(enabled);
        tilNameEmployee.setHintAnimationEnabled(enabled);
        tilDate.setHintAnimationEnabled(enabled);
        tilNameInvoice.setHintAnimationEnabled(enabled);
        return this;
    }

    public DetailReceiverProductViewHolder showMovementTo(String text) {
        edtMovementTo.setText(text);
        return this;
    }

    public DetailReceiverProductViewHolder showNameEmployee(String surname, String name, String patronymic) {
        String format = getString(R.string.item_employee_name_list_format, surname, name, patronymic);
        edtNameEmployee.setText(format);
        return this;
    }

    public DetailReceiverProductViewHolder showDate(Date date) {
        String format = SimpleDateFormat.getDateInstance(DateFormat.LONG).format(date);
        edtDate.setText(format);
        return this;
    }

    public DetailReceiverProductViewHolder showNameInvoice(String text) {
        edtNameInvoice.setText(text);
        return this;
    }

    public DetailReceiverProductViewHolder errorNameInvoice(String text) {
        tilNameInvoice.setError(text);
        return this;
    }

    public DetailReceiverProductViewHolder showDatePicker(Date date) {
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

    public DetailReceiverProductViewHolder showProducts(RealmResults<ProductModel> models) {
        adapter.updateRealmResults(models);
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

    public interface OnInteractionListener {
        void onBackClick();
        void onSaveClick(String invoice);
        void onAddClick();
        void onEditProduct(String productId);
        void onRemoveProduct(String productId);
        void onDateClick();
        void onDateSelect(Date date);
    }
}
