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

import ru.annin.store.domain.repository.SettingsRepository;
import ru.annin.store.domain.repository.StoreRepository;
import ru.annin.store.presentation.common.BasePresenter;
import ru.annin.store.presentation.ui.view.MainView;
import ru.annin.store.presentation.ui.viewholder.MainViewHolder;
import ru.annin.store.presentation.ui.viewholder.MainViewHolder.OnClickListener;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * <p>Presenter главного экрана.</p>
 *
 * @author Pavel Annin.
 */
public class MainPresenter extends BasePresenter<MainViewHolder, MainView> {

    // Repository
    private final StoreRepository storeRepository;
    private final SettingsRepository settingsRepository;

    private final CompositeSubscription subscription;

    public MainPresenter(@NonNull StoreRepository storeRepository,
                         @NonNull SettingsRepository settingsRepository) {
        this.storeRepository = storeRepository;
        this.settingsRepository = settingsRepository;
        subscription = new CompositeSubscription();
    }

    public void onInitialization() {
        Subscription sub = storeRepository.getAll()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(models -> {if (viewHolder != null) viewHolder.showStore(models,
                        settingsRepository.getSelectStoreId());});
        subscription.add(sub);
    }

    /**
     * Получено событие: нажата кнопка назад.
     * @return Если {@code true} выполнить стандартное действие, иначе ничего не выполнять.
     */
    public boolean onBackPress() {
        if (viewHolder != null && viewHolder.isNavigationShowing()) {
            viewHolder.hideNavigation();
            return false;
        }
        return true;
    }

    @NonNull
    @Override
    public BasePresenter setViewHolder(@Nullable MainViewHolder mainViewHolder) {
        super.setViewHolder(mainViewHolder);
        if (viewHolder != null) {
            viewHolder.setOnClickListener(onHolderListener);
        }
        return this;
    }

    private final OnClickListener onHolderListener = new OnClickListener() {

        @Override
        public void onNavInvoiceClick() {
            if (viewHolder != null) {
                viewHolder.hideNavigation();
            }
            if (view != null) {
                view.onInvoiceOpen();
            }
        }

        @Override
        public void onNavNomenclatureClick() {
            if (viewHolder != null) {
                viewHolder.hideNavigation();
            }
            if (view != null) {
                view.onNomenclatureOpen();
            }
        }

        @Override
        public void onNavStoreClick() {
            if (viewHolder != null) {
                viewHolder.hideNavigation();
            }
            if (view != null) {
                view.onStoresOpen();
            }
        }

        @Override
        public void onNavUnitsClick() {
            if (viewHolder != null) {
                viewHolder.hideNavigation();
            }
            if (view != null) {
                view.onUnitsOpen();
            }
        }

        @Override
        public void onNavAboutClick() {
            if (viewHolder != null) {
                viewHolder.hideNavigation();
            }
            if (view != null) {
                view.onAboutOpen();
            }
        }

        @Override
        public void onStoreSelect(String storeId) {
            settingsRepository.saveStoreId(storeId);
        }
    };
}
