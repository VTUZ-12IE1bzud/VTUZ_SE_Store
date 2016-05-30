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
import ru.annin.store.domain.model.NomenclatureModel;
import ru.annin.store.domain.repository.NomenclatureRepository;
import ru.annin.store.presentation.common.BasePresenter;
import ru.annin.store.presentation.ui.view.NomenclatureView;
import ru.annin.store.presentation.ui.viewholder.NomenclatureViewHolder;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.subscriptions.CompositeSubscription;

import static ru.annin.store.presentation.ui.viewholder.NomenclatureViewHolder.OnClickListener;

/**
 * <p>Presenter экрана "Номенклатура".</p>
 *
 * @author Pavel Annin.
 */
public class NomenclaturePresenter extends BasePresenter<NomenclatureViewHolder, NomenclatureView> {

    // Repository
    private final NomenclatureRepository nomenclatureRepository;

    private final CompositeSubscription subscription;

    public NomenclaturePresenter(@NonNull NomenclatureRepository nomenclatureRepository){
        this.nomenclatureRepository = nomenclatureRepository;
        subscription = new CompositeSubscription();
    }

    public void onInitialization() {
        final Subscription sub = nomenclatureRepository.getAll()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(models -> {
                    if (viewHolder != null) {
                        viewHolder.showNomenclatures(models);
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
    public BasePresenter setViewHolder(@Nullable NomenclatureViewHolder vh) {
        super.setViewHolder(vh);
        if (viewHolder != null) {
            viewHolder.setOnClickListener(onViewHolderClickListener);
        }
        return this;
    }

    private final OnClickListener onViewHolderClickListener = new OnClickListener() {
        @Override
        public void onCreateUnitClick() {
            if (view != null) {
                view.onCreate();
            }
        }

        @Override
        public void onRemoveItem(NomenclatureModel model, int position) {
            if (nomenclatureRepository.canRemoved(model.getId())) {
                nomenclatureRepository.asyncRemove(model.getId());
            } else if (viewHolder != null){
                viewHolder.insertItem(position)
                        .showMessage(R.string.error_nomenclature_used_removed);
            }
        }

        @Override
        public void onItemClick(NomenclatureModel model) {
            if (view != null) {
                view.onOpen(model.getId());
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
