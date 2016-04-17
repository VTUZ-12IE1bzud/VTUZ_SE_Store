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
import ru.annin.store.domain.interactor.DefaultSubscriber;
import ru.annin.store.domain.interactor.GetUnit;
import ru.annin.store.domain.model.UnitModel;
import ru.annin.store.domain.repository.DataRepository;
import ru.annin.store.presentation.common.BasePresenter;
import ru.annin.store.presentation.ui.view.DetailUnitView;
import ru.annin.store.presentation.ui.viewholder.DetailUnitViewHolder;

/**
 * Presenter экрана единица измерения.
 *
 * @author Pavel Annin.
 */
public class DetailUnitPresenter extends BasePresenter {

    private DetailUnitViewHolder mViewHolder;
    private DetailUnitView mView;

    private final GetUnit getUnit;
    private final DataRepository dataRepository;

    private boolean isCreated;
    private String unitId;

    @Inject
    public DetailUnitPresenter(GetUnit getUnit, DataRepository dataRepository) {
        this.getUnit = getUnit;
        this.dataRepository = dataRepository;
    }

    public void onInitialization() {
        isCreated = true;
    }

    public void onInitialization(String unitId) {
        isCreated = false;
        getUnit.execute(unitId, unitSubscriber);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getUnit.unSubscribe();
    }

    public void setViewHolder(DetailUnitViewHolder viewHolder) {
        mViewHolder = viewHolder;
        if (mViewHolder != null) {
            mViewHolder.setOnClickListener(onHolderListener);
        }
    }

    public void setView(DetailUnitView view) {
        mView = view;
    }

    private boolean isValidation(String name, String symbol, String description) {
        boolean isValidation = true;

        String msgEmpty = "";
        if (mView != null) {
            msgEmpty = mView.getString(R.string.error_unit_field_empty);
        }

        if (mViewHolder != null) {
            mViewHolder.errorName(null)
                    .errorSymbol(null)
                    .errorDescription(null);
        }

        if (TextUtils.isEmpty(name)) {
            isValidation = false;
            if (mViewHolder != null) {
                mViewHolder.errorName(msgEmpty);
            }
        }

        if (TextUtils.isEmpty(symbol)) {
            isValidation = false;
            if (mViewHolder != null) {
                mViewHolder.errorSymbol(msgEmpty);
            }
        }

        if (TextUtils.isEmpty(description)) {
            isValidation = false;
            if (mViewHolder != null) {
                mViewHolder.errorDescription(msgEmpty);
            }
        }

        return isValidation;
    }

    private final DetailUnitViewHolder.OnClickListener onHolderListener = new DetailUnitViewHolder.OnClickListener() {
        @Override
        public void onNavigationBackClick() {
            if (mView != null) {
                mView.onFinish();
            }
        }

        @Override
        public void onSaveClick(String name, String symbol, String description) {
            if (isValidation(name, symbol, description)) {
                final boolean result = isCreated
                        ? dataRepository.createUnit(name, symbol, description)
                        : dataRepository.saveUnit(unitId, name, symbol, description);
                if (result) {
                    if (mView != null) {
                        mView.onFinish();
                    }
                }
            }
        }
    };

    private final DefaultSubscriber<UnitModel> unitSubscriber = new DefaultSubscriber<UnitModel>() {
        @Override
        public void onNext(UnitModel model) {
            super.onNext(model);
            unitId = model.getId();
            if (mViewHolder != null) {
                mViewHolder.showName(model.getName())
                        .showSymbol(model.getSymbol())
                        .showDescription(model.getDescription());
            }
        }
    };
}
