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

import android.text.TextUtils;

import javax.inject.Inject;

import ru.annin.store.R;
import ru.annin.store.domain.repository.DataRepository;
import ru.annin.store.presentation.common.BasePresenter;
import ru.annin.store.presentation.ui.view.DetailStoreView;
import ru.annin.store.presentation.ui.viewholder.DetailStoreViewHolder;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * Presenter экрана "Склад".
 *
 * @author Pavel Annin.
 */
public class DetailStorePresenter extends BasePresenter {

    private DetailStoreViewHolder mViewHolder;
    private DetailStoreView mView;

    private final DataRepository dataRepository;

    private boolean isCreated;
    private String storeId;

    CompositeSubscription subscriptions;

    @Inject
    public DetailStorePresenter(DataRepository dataRepository) {
        this.dataRepository = dataRepository;
        subscriptions = new CompositeSubscription();
    }

    public void onInitialization() {
        isCreated = true;
    }

    public void onInitialization(String storeId) {
        isCreated = false;
        final Subscription subscription = dataRepository.getStoreById(storeId)
                .subscribe(model -> {
                    this.storeId = model.getId();
                    if (mViewHolder != null) {
                        mViewHolder.showName(model.getName());
                    }
                });
        subscriptions.add(subscription);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        subscriptions.unsubscribe();
    }

    public void setViewHolder(DetailStoreViewHolder viewHolder) {
        mViewHolder = viewHolder;
        if (mViewHolder != null) {
            mViewHolder.setOnClickListener(onHolderListener);
        }
    }

    public void setView(DetailStoreView view) {
        mView = view;
    }

    private boolean isValidation(String name) {
        boolean isValidation = true;

        String msgEmpty = "";
        if (mView != null) {
            msgEmpty = mView.getString(R.string.error_store_field_empty);
        }

        if (mViewHolder != null) {
            mViewHolder.errorName(null);
        }

        if (TextUtils.isEmpty(name)) {
            isValidation = false;
            if (mViewHolder != null) {
                mViewHolder.errorName(msgEmpty);
            }
        }

        return isValidation;
    }

    private final DetailStoreViewHolder.OnClickListener onHolderListener = new DetailStoreViewHolder.OnClickListener() {
        @Override
        public void onNavigationBackClick() {
            if (mView != null) {
                mView.onFinish();
            }
        }

        @Override
        public void onSaveClick(String name) {
            if (isValidation(name)) {
                final boolean result = isCreated
                        ? dataRepository.createStore(name)
                        : dataRepository.saveStore(storeId, name);
                if (result) {
                    if (mView != null) {
                        mView.onFinish();
                    }
                }
            }
        }
    };
}
