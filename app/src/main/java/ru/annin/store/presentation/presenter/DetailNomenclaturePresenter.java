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
import ru.annin.store.domain.repository.NomenclatureRepository;
import ru.annin.store.domain.repository.UnitRepository;
import ru.annin.store.presentation.common.BasePresenter;
import ru.annin.store.presentation.ui.view.DetailNomenclatureView;
import ru.annin.store.presentation.ui.viewholder.DetailNomenclatureViewHolder;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.subscriptions.CompositeSubscription;

import static ru.annin.store.presentation.ui.viewholder.DetailNomenclatureViewHolder.OnClickListener;

/**
 * <p>Presenter экрана "Номенклатура".</p>
 *
 * @author Pavel Annin.
 */
public class DetailNomenclaturePresenter
        extends BasePresenter<DetailNomenclatureViewHolder, DetailNomenclatureView> {

    // Repository
    private final NomenclatureRepository nomenclatureRepository;
    private final UnitRepository unitRepository;

    // Data's
    private boolean isCreated;
    private String nomenclatureId;
    private String unitId;

    private CompositeSubscription subscription;

    public DetailNomenclaturePresenter(@NonNull NomenclatureRepository nomenclatureRepository,
                                       @NonNull UnitRepository unitRepository) {
        this.nomenclatureRepository = nomenclatureRepository;
        this.unitRepository = unitRepository;
        subscription = new CompositeSubscription();
    }

    public void onInitialization() {
        isCreated = true;
        final Subscription subUnits = unitRepository.getAll()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(model -> {
                    if (viewHolder != null) {
                        viewHolder.showUnits(model);
                    }
                });
        subscription.add(subUnits);
    }

    public void onInitialization(String nomenclatureId) {
        isCreated = false;
        final Subscription subCardProduct = nomenclatureRepository.getById(nomenclatureId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(model -> {
                    this.nomenclatureId = model.getId();
                    this.unitId = model.getUnit().getId();
                    if (viewHolder != null) {
                        viewHolder.showName(model.getName())
                                .selectUnit(unitId);
                    }
                });
        subscription.add(subCardProduct);

        final Subscription subUnits = unitRepository.getAll()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(model -> {
                    if (viewHolder != null) {
                        viewHolder.showUnits(model)
                                .selectUnit(unitId);
                    }
                });
        subscription.add(subUnits);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        subscription.unsubscribe();
    }

    @NonNull
    @Override
    public BasePresenter setViewHolder(@Nullable DetailNomenclatureViewHolder detailNomenclatureViewHolder) {
        super.setViewHolder(detailNomenclatureViewHolder);
        if (viewHolder != null) {
            viewHolder.setOnClickListener(onHolderListener);
        }
        return this;
    }

    private boolean isValidation(String name, String unitId) {
        boolean isValidation = true;

        String msgEmpty = "";
        if (view != null) {
            msgEmpty = view.getString(R.string.error_unit_field_empty);
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

        if (TextUtils.isEmpty(unitId)) {
            isValidation = false;
        }

        return isValidation;
    }

    private final OnClickListener onHolderListener = new OnClickListener() {
        @Override
        public void onNavigationBackClick() {
            if (view != null) {
                view.onFinish();
            }
        }

        @Override
        public void onSaveClick(String name, String unitId) {
            if (isValidation(name, unitId)) {
                if (isCreated) {
                    nomenclatureRepository.asyncCreate(name, unitId);
                } else {
                    nomenclatureRepository.asyncSave(nomenclatureId, name, unitId);
                }
                if (view != null) {
                    view.onFinish();
                }

            }
        }
    };
}
