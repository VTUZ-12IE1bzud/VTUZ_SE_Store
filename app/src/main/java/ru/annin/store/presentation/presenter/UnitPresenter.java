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

import io.realm.RealmResults;
import ru.annin.store.R;
import ru.annin.store.domain.interactor.DefaultSubscriber;
import ru.annin.store.domain.interactor.GetUnits;
import ru.annin.store.domain.model.UnitModel;
import ru.annin.store.domain.repository.DataRepository;
import ru.annin.store.presentation.common.BasePresenter;
import ru.annin.store.presentation.ui.view.UnitView;
import ru.annin.store.presentation.ui.viewholder.UnitViewHolder;

/**
 * Presenter экрана "Единицы измерения".
 *
 * @author Pavel Annin.
 */
public class UnitPresenter extends BasePresenter {

    private UnitViewHolder mViewHolder;
    private UnitView mView;

    private final GetUnits getUnits;
    private final DataRepository dataRepository;

    @Inject
    public UnitPresenter(GetUnits getUnits, DataRepository dataRepository){
        this.getUnits = getUnits;
        this.dataRepository = dataRepository;
    }

    public void onInitialization() {
        getUnits.execute(unitsSubscriber);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getUnits.unSubscribe();
    }

    public void setViewHolder(UnitViewHolder viewHolder) {
        mViewHolder = viewHolder;
        if (mViewHolder != null) {
            mViewHolder.setOnClickListener(onViewHolderClickListener);
        }
    }

    public void setView(UnitView view) {
        mView = view;
    }

    private final UnitViewHolder.OnClickListener onViewHolderClickListener = new UnitViewHolder.OnClickListener() {
        @Override
        public void onCreateUnitClick() {
            if (mView != null) {
                mView.onCreateUnitOpen();
            }
        }

        @Override
        public void onRemoveItem(UnitModel unit, int position) {
            if (dataRepository.canUnitRemoved(unit.getId())) {
                dataRepository.removeUnit(unit.getId());
            } else {
                mViewHolder.insertItem(position)
                        .showMessage(R.string.error_unit_used_removed);
            }
        }

        @Override
        public void onItemClick(UnitModel unit) {
            if (mView != null) {
                mView.onUnitOpen(unit.getId());
            }
        }

        @Override
        public void onNavBackClick() {
            if (mView != null) {
                mView.onFinish();
            }
        }
    };

    private final DefaultSubscriber<RealmResults<UnitModel>> unitsSubscriber = new DefaultSubscriber<RealmResults<UnitModel>>() {
        @Override
        public void onNext(RealmResults<UnitModel> unitModels) {
            super.onNext(unitModels);
            if (mViewHolder != null) {
                mViewHolder.showUnits(unitModels);
            }
        }
    };
}
