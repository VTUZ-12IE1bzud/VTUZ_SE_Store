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
import android.text.TextUtils;

import ru.annin.store.R;
import ru.annin.store.domain.repository.StoreRepository;
import ru.annin.store.presentation.common.BasePresenter;
import ru.annin.store.presentation.ui.view.DetailStoreView;
import ru.annin.store.presentation.ui.viewholder.DetailStoreViewHolder;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * <p>Presenter экрана "Склад".</p>
 *
 * @author Pavel Annin.
 */
public class DetailStorePresenter extends BasePresenter<DetailStoreViewHolder, DetailStoreView> {

    // Repository
    private final StoreRepository storeRepository;

    // Data's
    private boolean isCreated;
    private String storeId;

    CompositeSubscription subscriptions;

    public DetailStorePresenter(@NonNull StoreRepository storeRepository) {
        this.storeRepository = storeRepository;
        subscriptions = new CompositeSubscription();
    }

    public void onInitialization() {
        isCreated = true;
    }

    public void onInitialization(String storeId) {
        isCreated = false;
        final Subscription subscription = storeRepository.getById(storeId)
                .subscribe(model -> {
                    this.storeId = model.getId();
                    if (viewHolder != null) {
                        viewHolder.showName(model.getName());
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
    public BasePresenter setViewHolder(@Nullable DetailStoreViewHolder detailStoreViewHolder) {
        super.setViewHolder(detailStoreViewHolder);
        if (viewHolder != null) {
            viewHolder.setOnClickListener(onHolderListener);
        }
        return this;
    }

    private boolean isValidation(String name) {
        boolean isValidation = true;

        String msgEmpty = "";
        if (view != null) {
            msgEmpty = view.getString(R.string.error_store_field_empty);
        }

        if (viewHolder != null) {
            viewHolder.errorName(null);
        }

        if (TextUtils.isEmpty(name)) {
            isValidation = false;
            if (viewHolder != null) {
                viewHolder.errorName(msgEmpty);
            }
        }

        return isValidation;
    }

    private final DetailStoreViewHolder.OnClickListener onHolderListener = new DetailStoreViewHolder.OnClickListener() {
        @Override
        public void onNavigationBackClick() {
            if (view != null) {
                view.onFinish();
            }
        }

        @Override
        public void onSaveClick(String name) {
            if (isValidation(name)) {
                if (isCreated) {
                    storeRepository.asyncCreateStore(name);
                } else {
                    storeRepository.asyncSaveStore(storeId, name);
                }
                if (view != null) {
                    view.onFinish();
                }
            }
        }
    };
}
