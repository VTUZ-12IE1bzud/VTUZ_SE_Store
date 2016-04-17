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

import javax.inject.Inject;

import ru.annin.store.R;
import ru.annin.store.domain.model.StoreModel;
import ru.annin.store.domain.repository.DataRepository;
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
public class StorePresenter extends BasePresenter {

    private StoreViewHolder mViewHolder;
    private StoreView mView;

    private final DataRepository dataRepository;
    private final CompositeSubscription subscriptions;

    @Inject
    public StorePresenter(DataRepository dataRepository){
        this.dataRepository = dataRepository;
        subscriptions = new CompositeSubscription();
    }

    public void onInitialization() {
        final Subscription subscription = dataRepository.listStores()
                .subscribe(storeModels -> {
                    if (mViewHolder != null) {
                        mViewHolder.showStores(storeModels);
                    }
                });
        subscriptions.add(subscription);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        subscriptions.unsubscribe();
    }

    public void setViewHolder(StoreViewHolder viewHolder) {
        mViewHolder = viewHolder;
        if (mViewHolder != null) {
            mViewHolder.setOnClickListener(onViewHolderClickListener);
        }
    }

    public void setView(StoreView view) {
        mView = view;
    }

    private final StoreViewHolder.OnClickListener onViewHolderClickListener = new StoreViewHolder.OnClickListener() {
        @Override
        public void onCreateStoreClick() {
            if (mView != null) {
                mView.onCreateStoreOpen();
            }
        }

        @Override
        public void onRemoveItem(StoreModel store, int position) {
            if (dataRepository.canStoreRemoved(store.getId())) {
                dataRepository.removeStore(store.getId());
            } else {
                mViewHolder.insertItem(position)
                        .showMessage(R.string.error_store_used_removed);
            }
        }

        @Override
        public void onItemClick(StoreModel store) {
            if (mView != null) {
                mView.onStoreOpen(store.getId());
            }
        }

        @Override
        public void onNavBackClick() {
            if (mView != null) {
                mView.onFinish();
            }
        }
    };
}
