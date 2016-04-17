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

package ru.annin.store.presentation.ui.viewholder;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.RealmResults;
import ru.annin.store.R;
import ru.annin.store.domain.model.StoreModel;
import ru.annin.store.presentation.common.BaseViewHolder;
import ru.annin.store.presentation.ui.adapter.StoreAdapter;

/**
 * ViewHolder экрана "Склады".
 *
 * @author Pavel Annin.
 */
public class StoreViewHolder extends BaseViewHolder {

    // View's
    @Bind(R.id.main_container)
    View mainView;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.list_store)
    RecyclerView listStore;
    @Bind(R.id.txt_empty)
    TextView txtEmpty;

    // Adapter's
    private final StoreAdapter adapter;

    // Listener's
    private OnClickListener listener;
    private ItemTouchHelper itemTouchHelper;

    public StoreViewHolder(@NonNull View view) {
        super(view);
        ButterKnife.bind(this, view);
        // Setup
        adapter = new StoreAdapter(true);
        itemTouchHelper = new ItemTouchHelper(onItemTouchHelper);
        toolbar.setNavigationOnClickListener(onNavigationClickListener);
        adapter.setViewEmpty(txtEmpty);
        listStore.setItemAnimator(new DefaultItemAnimator());
        listStore.setAdapter(adapter);
        itemTouchHelper.attachToRecyclerView(listStore);
        adapter.setOnClickListener(onItemClickListener);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    public StoreViewHolder showStores(final RealmResults<StoreModel> stores) {
        adapter.updateRealmResults(stores);
        return this;
    }

    public StoreViewHolder insertItem(int position) {
        adapter.notifyItemInserted(position);
        return this;
    }

    public StoreViewHolder showMessage(@StringRes int text) {
        Snackbar.make(mainView, text, Snackbar.LENGTH_LONG).show();
        return this;
    }

    public void setOnClickListener(final OnClickListener listener) {
        this.listener = listener;
    }

    @OnClick(R.id.fab_add)
    void onCreateClick() {
        if (listener != null) {
            listener.onCreateStoreClick();
        }

    }

    private final View.OnClickListener onNavigationClickListener = v -> {
        if (listener != null) {
            listener.onNavBackClick();
        }
    };

    private final ItemTouchHelper.SimpleCallback onItemTouchHelper = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
        @Override
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
            final int position = viewHolder.getAdapterPosition();
            adapter.notifyItemRemoved(position);
            if (listener != null) {
                listener.onRemoveItem(adapter.getItem(position), position);
            }
        }

        @Override
        public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
            if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
                View itemView = viewHolder.itemView;
                Paint paint = new Paint();
                if (dX > 0) {
                    paint.setColor(mResources.getColor(R.color.accent));
                    c.drawRect((float) itemView.getLeft(), (float) itemView.getTop(), dX, (float) itemView.getBottom(), paint);

                } else {
                    paint.setColor(mResources.getColor(R.color.accent));
                    c.drawRect((float) itemView.getRight() + dX, (float) itemView.getTop(), (float) itemView.getRight(), (float) itemView.getBottom(), paint);
                }
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        }
    };

    private final StoreAdapter.OnClickListener onItemClickListener = model -> {
        if (listener != null) {
            listener.onItemClick(model);
        }
    };

    public interface OnClickListener {
        void onCreateStoreClick();
        void onRemoveItem(StoreModel store, int position);
        void onItemClick(StoreModel store);
        void onNavBackClick();
    }
}
