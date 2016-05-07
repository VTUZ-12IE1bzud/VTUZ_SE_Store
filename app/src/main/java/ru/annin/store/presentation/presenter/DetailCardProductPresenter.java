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
import ru.annin.store.presentation.ui.view.DetailCardProductView;
import ru.annin.store.presentation.ui.viewholder.DetailCardProductViewHolder;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Presenter экрана "Карточка товара".
 *
 * @author Pavel Annin.
 */
public class DetailCardProductPresenter extends BasePresenter {

    private DetailCardProductViewHolder mViewHolder;
    private DetailCardProductView mView;
    private CompositeSubscription mSubscription;

    private final DataRepository dataRepository;

    private boolean isCreated;
    private String cardProductId;
    private String unitId;

    @Inject
    public DetailCardProductPresenter(DataRepository dataRepository) {
        this.dataRepository = dataRepository;
        mSubscription = new CompositeSubscription();
    }

    public void onInitialization() {
        isCreated = true;
        final Subscription subUnits = dataRepository.listUnits()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(model -> {
                    if (mViewHolder != null) {
                        mViewHolder.showUnits(model);
                    }
                });
        mSubscription.add(subUnits);
    }

    public void onInitialization(String cardProductId) {
        isCreated = false;
        final Subscription subCardProduct = dataRepository.getCardProductById(cardProductId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(model -> {
                    this.cardProductId = model.getId();
                    this.unitId = model.getUnit().getId();
                    if (mViewHolder != null) {
                        mViewHolder.showName(model.getName())
                                .selectUnit(unitId);
                    }
                });
        mSubscription.add(subCardProduct);

        final Subscription subUnits = dataRepository.listUnits()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(model -> {
                    if (mViewHolder != null) {
                        mViewHolder.showUnits(model)
                                .selectUnit(unitId);
                    }
                });
        mSubscription.add(subUnits);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mSubscription.unsubscribe();
    }

    public void setViewHolder(DetailCardProductViewHolder viewHolder) {
        mViewHolder = viewHolder;
        if (mViewHolder != null) {
            mViewHolder.setOnClickListener(onHolderListener);
        }
    }

    public void setView(DetailCardProductView view) {
        mView = view;
    }

    private boolean isValidation(String name, String unitId) {
        boolean isValidation = true;

        String msgEmpty = "";
        if (mView != null) {
            msgEmpty = mView.getString(R.string.error_unit_field_empty);
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

        if (TextUtils.isEmpty(unitId)) {
            isValidation = false;
        }

        return isValidation;
    }

    private final DetailCardProductViewHolder.OnClickListener onHolderListener = new DetailCardProductViewHolder.OnClickListener() {
        @Override
        public void onNavigationBackClick() {
            if (mView != null) {
                mView.onFinish();
            }
        }

        @Override
        public void onSaveClick(String name, String unitId) {
            if (isValidation(name, unitId)) {
                final boolean result = isCreated
                        ? dataRepository.createCardProduct(name, unitId)
                        : dataRepository.saveCardProduct(cardProductId, name, unitId);
                if (result) {
                    if (mView != null) {
                        mView.onFinish();
                    }
                }
            }
        }
    };
}
