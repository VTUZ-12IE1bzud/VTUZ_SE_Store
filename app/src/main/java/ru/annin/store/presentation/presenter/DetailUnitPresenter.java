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
import ru.annin.store.domain.repository.UnitRepository;
import ru.annin.store.presentation.common.BasePresenter;
import ru.annin.store.presentation.ui.view.DetailUnitView;
import ru.annin.store.presentation.ui.viewholder.DetailUnitViewHolder;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * <p>Presenter экрана единица измерения.</p>
 *
 * @author Pavel Annin.
 */
public class DetailUnitPresenter extends BasePresenter<DetailUnitViewHolder, DetailUnitView> {

    // Repository
    private final UnitRepository unitRepository;

    // Data's
    private boolean isCreated;
    private String unitId;

    private CompositeSubscription subscription;

    public DetailUnitPresenter(@NonNull UnitRepository unitRepository) {
        this.unitRepository = unitRepository;
        subscription = new CompositeSubscription();
    }

    public void onInitialization() {
        isCreated = true;
    }

    public void onInitialization(String unitId) {
        isCreated = false;
        final Subscription sub = unitRepository.getById(unitId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(model -> {
                    this.unitId = model.getId();
                    if (viewHolder != null) {
                        viewHolder.showName(model.getName())
                                .showSymbol(model.getSymbol());
                    }
                });
        subscription.add(sub);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        subscription.unsubscribe();
    }

    @NonNull
    @Override
    public BasePresenter setViewHolder(@Nullable DetailUnitViewHolder detailUnitViewHolder) {
        super.setViewHolder(detailUnitViewHolder);
        if (viewHolder != null) {
            viewHolder.setOnClickListener(onHolderListener);
        }
        return this;
    }

    private boolean isValidation(String name, String symbol) {
        boolean isValidation = true;

        String msgEmpty = "";
        if (view != null) {
            msgEmpty = view.getString(R.string.error_unit_field_empty);
        }

        if (viewHolder != null) {
            viewHolder.errorName(null)
                    .errorSymbol(null);
        }

        if (TextUtils.isEmpty(name)) {
            isValidation = false;
            if (viewHolder != null) {
                viewHolder.errorName(msgEmpty);
            }
        }

        if (TextUtils.isEmpty(symbol)) {
            isValidation = false;
            if (viewHolder != null) {
                viewHolder.errorSymbol(msgEmpty);
            }
        }

        return isValidation;
    }

    private final DetailUnitViewHolder.OnClickListener onHolderListener = new DetailUnitViewHolder.OnClickListener() {
        @Override
        public void onNavigationBackClick() {
            if (view != null) {
                view.onFinish();
            }
        }

        @Override
        public void onSaveClick(String name, String symbol) {
            if (isValidation(name, symbol)) {
                if (isCreated) {
                    unitRepository.asyncCreateUnit(name, symbol);
                } else {
                    unitRepository.asyncSaveUnit(unitId, name, symbol);
                }
                if (view != null) {
                    view.onFinish();
                }
            }
        }
    };
}
