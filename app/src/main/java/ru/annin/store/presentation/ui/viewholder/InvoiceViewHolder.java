package ru.annin.store.presentation.ui.viewholder;

import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;

import io.realm.RealmResults;
import ru.annin.store.R;
import ru.annin.store.domain.model.InvoiceModel;
import ru.annin.store.presentation.common.BaseViewHolder;
import ru.annin.store.presentation.ui.adapter.InvoiceAdapter;

/**
 * <p>ViewHolder экрана "Товарная накладная".</p>
 *
 * @author Pavel Annin, 2016.
 */
public class InvoiceViewHolder extends BaseViewHolder {

    // View's
    private final Toolbar vToolbar;
    private final RecyclerView rcList;
    private final FloatingActionButton fabAdd;
    private final View vEmpty;

    // Adapter's
    private final InvoiceAdapter adapter;

    // Listener's
    private OnInteractionListener listener;

    public InvoiceViewHolder(@NonNull View view) {
        super(view);
        vToolbar = (Toolbar) vRoot.findViewById(R.id.toolbar);
        rcList = (RecyclerView) vRoot.findViewById(android.R.id.list);
        fabAdd = (FloatingActionButton) vRoot.findViewById(R.id.fab_add);
        vEmpty = vRoot.findViewById(android.R.id.empty);

        // Setup
        adapter = new InvoiceAdapter(false);
        adapter.setViewEmpty(vEmpty);
        adapter.setOnClickListener(model -> {
            if (listener != null) listener.onItemClick(model);
        });
        rcList.setAdapter(adapter);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(onItemTouchHelper);
        itemTouchHelper.attachToRecyclerView(rcList);
        vToolbar.setNavigationOnClickListener(aVoid -> {
            if (listener != null) listener.onBackClick();
        });
        vToolbar.inflateMenu(R.menu.menu_invoice);
        vToolbar.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()) {
                case R.id.action_doc:
                    if (listener != null) {
                        listener.onReportClick();
                    }
                    return true;
            }
            return false;
        });
        fabAdd.setOnClickListener(aVoid -> {
            if (listener != null) listener.onAddClick();
        });
    }

    public InvoiceViewHolder showInvoice(RealmResults<InvoiceModel> model) {
        adapter.updateRealmResults(model);
        return this;
    }

    public InvoiceViewHolder showMessage(@NonNull String msg) {
        Snackbar.make(vRoot, msg, Snackbar.LENGTH_LONG).show();
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
                listener.onRemoveReceiverClick(adapter.getItem(position).getId());
            }
        }
    };

    public interface OnInteractionListener {
        void onBackClick();

        void onAddClick();

        void onItemClick(InvoiceModel model);

        void onRemoveReceiverClick(String receiverId);

        void onReportClick();
    }
}
