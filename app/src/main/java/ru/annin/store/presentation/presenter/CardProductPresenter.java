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
import ru.annin.store.domain.model.CardProductModel;
import ru.annin.store.domain.repository.DataRepository;
import ru.annin.store.presentation.common.BasePresenter;
import ru.annin.store.presentation.ui.view.CardProductView;
import ru.annin.store.presentation.ui.viewholder.CardProductViewHolder;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * Presenter экрана "Карточки товаров".
 *
 * @author Pavel Annin.
 */
public class CardProductPresenter extends BasePresenter {

    private CardProductViewHolder mViewHolder;
    private CardProductView mView;

    private final DataRepository dataRepository;
    private final CompositeSubscription subscriptions;

    @Inject
    public CardProductPresenter(DataRepository dataRepository){
        this.dataRepository = dataRepository;
        subscriptions = new CompositeSubscription();
    }

    public void onInitialization() {
        final Subscription subscription = dataRepository.listCardProducts()
                .subscribe(storeModels -> {
                    if (mViewHolder != null) {
                        mViewHolder.showCardProducts(storeModels);
                    }
                });
        subscriptions.add(subscription);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        subscriptions.unsubscribe();
    }

    public void setViewHolder(CardProductViewHolder viewHolder) {
        mViewHolder = viewHolder;
        if (mViewHolder != null) {
            mViewHolder.setOnClickListener(onViewHolderClickListener);
        }
    }

    public void setView(CardProductView view) {
        mView = view;
    }

    private final CardProductViewHolder.OnClickListener onViewHolderClickListener = new CardProductViewHolder.OnClickListener() {
           @Override
        public void onCreateClick() {
            if (mView != null) {
                mView.onCreateCardProductOpen();
            }
        }

        @Override
        public void onRemoveItem(CardProductModel model, int position) {
            if (dataRepository.canCardProductRemoved(model.getId())) {
                dataRepository.removeCardProduct(model.getId());
            } else {
                mViewHolder.insertItem(position)
                        .showMessage(R.string.error_card_product_used_removed);
            }
        }

        @Override
        public void onItemClick(CardProductModel model) {
            if (mView != null) {
                mView.onCardProductOpen(model.getId());
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
