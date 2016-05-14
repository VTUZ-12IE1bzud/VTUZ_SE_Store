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

import ru.annin.store.domain.model.ReceiptProductInvoiceModel;
import ru.annin.store.domain.repository.DataRepository;
import ru.annin.store.presentation.common.BasePresenter;
import ru.annin.store.presentation.ui.view.ReceiptProductInvoiceView;
import ru.annin.store.presentation.ui.viewholder.ReceiptProductInvoiceViewHolder;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.subscriptions.CompositeSubscription;

import static ru.annin.store.presentation.ui.viewholder.ReceiptProductInvoiceViewHolder.OnClickListener;

/**
 * Presenter экрана "Товарная накладная".
 *
 * @author Pavel Annin.
 */
public class ReceiptProductInvoicePresenter extends BasePresenter {

    private ReceiptProductInvoiceViewHolder mViewHolder;
    private ReceiptProductInvoiceView mView;

    private final DataRepository dataRepository;
    private final CompositeSubscription subscriptions;

    @Inject
    public ReceiptProductInvoicePresenter(DataRepository dataRepository){
        this.dataRepository = dataRepository;
        subscriptions = new CompositeSubscription();
    }

    public void onInitialization() {
        final Subscription subscription = dataRepository.listReceiptProductInvoice()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(models -> {
                    if (mViewHolder != null) {
                        mViewHolder.showCardProducts(models);
                    }
                });
        subscriptions.add(subscription);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        subscriptions.unsubscribe();
    }

    public void setViewHolder(ReceiptProductInvoiceViewHolder viewHolder) {
        mViewHolder = viewHolder;
        if (mViewHolder != null) {
            mViewHolder.setOnClickListener(onViewHolderClickListener);
        }
    }

    public void setView(ReceiptProductInvoiceView view) {
        mView = view;
    }

    private final OnClickListener onViewHolderClickListener = new OnClickListener() {

        @Override
        public void onCreateClick() {
            if (mView != null) {
                mView.onCreateReceiptProductInvoiceOpen();
            }
        }

        @Override
        public void onRemoveItem(ReceiptProductInvoiceModel model, int position) {
            dataRepository.removeStore(model.getId());
        }

        @Override
        public void onItemClick(ReceiptProductInvoiceModel model) {
            if (mView != null) {
                mView.onReceiptProductInvoiceOpen(model.getId());
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
