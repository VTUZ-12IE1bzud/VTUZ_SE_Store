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

package ru.annin.store.presentation.presenter;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import ru.annin.store.R;
import ru.annin.store.domain.model.StoreModel;
import ru.annin.store.domain.repository.StoreRepository;
import ru.annin.store.presentation.common.BasePresenter;
import ru.annin.store.presentation.ui.view.StoreView;
import ru.annin.store.presentation.ui.viewholder.StoreViewHolder;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * Presenter экрана "Склады".
 *
 * @author Pavel Annin.
 */
public class StorePresenter extends BasePresenter<StoreViewHolder, StoreView> {

    // Repository
    private final StoreRepository storeRepository;

    private final CompositeSubscription subscriptions;

    public StorePresenter(@NonNull StoreRepository storeRepository){
        this.storeRepository = storeRepository;
        subscriptions = new CompositeSubscription();
    }

    public void onInitialization() {
        final Subscription subscription = storeRepository.getAll()
                .subscribe(storeModels -> {
                    if (viewHolder != null) {
                        viewHolder.showStores(storeModels);
                    }
                });
        subscriptions.add(subscription);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        subscriptions.unsubscribe();
    }

    @NonNull
    @Override
    public BasePresenter setViewHolder(@Nullable StoreViewHolder storeViewHolder) {
        super.setViewHolder(storeViewHolder);
        if (viewHolder != null) {
            viewHolder.setOnClickListener(onViewHolderClickListener);
        }
        return this;
    }

    private final StoreViewHolder.OnClickListener onViewHolderClickListener = new StoreViewHolder.OnClickListener() {
        @Override
        public void onCreateStoreClick() {
            if (view != null) {
                view.onCreateStoreOpen();
            }
        }

        @Override
        public void onRemoveItem(StoreModel store, int position) {
            if (storeRepository.canStoreRemoved(store.getId())) {
                storeRepository.asyncRemoveStore(store.getId());
            } else if (viewHolder != null) {
                viewHolder.insertItem(position)
                        .showMessage(R.string.error_store_used_removed);
            }
        }

        @Override
        public void onItemClick(StoreModel store) {
            if (view != null) {
                view.onStoreOpen(store.getId());
            }
        }

        @Override
        public void onNavBackClick() {
            if (view != null) {
                view.onFinish();
            }
        }
    };
}
