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

import ru.annin.store.R;
import ru.annin.store.domain.model.UnitModel;
import ru.annin.store.domain.repository.UnitRepository;
import ru.annin.store.presentation.common.BasePresenter;
import ru.annin.store.presentation.ui.view.UnitView;
import ru.annin.store.presentation.ui.viewholder.UnitViewHolder;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * <p>Presenter экрана "Единицы измерения".</p>
 *
 * @author Pavel Annin.
 */
public class UnitPresenter extends BasePresenter<UnitViewHolder, UnitView> {

    // Repository
    private final UnitRepository unitRepository;

    private final CompositeSubscription subscription;

    public UnitPresenter(@NonNull UnitRepository unitRepository){
        this.unitRepository = unitRepository;
        subscription = new CompositeSubscription();
    }

    public void onInitialization() {
        final Subscription sub = unitRepository.getAll()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(unitModels -> {
                    if (viewHolder != null) {
                        viewHolder.showUnits(unitModels);
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
    public BasePresenter setViewHolder(@Nullable UnitViewHolder unitViewHolder) {
        super.setViewHolder(unitViewHolder);
        if (viewHolder != null) {
            viewHolder.setOnClickListener(onViewHolderClickListener);
        }
        return this;
    }

    private final UnitViewHolder.OnClickListener onViewHolderClickListener = new UnitViewHolder.OnClickListener() {
        @Override
        public void onCreateUnitClick() {
            if (view != null) {
                view.onCreateUnitOpen();
            }
        }

        @Override
        public void onRemoveItem(UnitModel unit, int position) {
            if (unitRepository.canUnitRemoved(unit.getId())) {
                unitRepository.asyncRemoveUnit(unit.getId());
            } else if (viewHolder != null){
                viewHolder.insertItem(position)
                        .showMessage(R.string.error_unit_used_removed);
            }
        }

        @Override
        public void onItemClick(UnitModel unit) {
            if (view != null) {
                view.onUnitOpen(unit.getId());
            }
        }

        @Override
        public void onNavBackClick() {
            if (view != null) {
                view.onFinish();
            }
        }
    };
}
