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

import ru.annin.store.presentation.common.BasePresenter;
import ru.annin.store.presentation.ui.view.MainView;
import ru.annin.store.presentation.ui.viewholder.MainViewHolder;
import ru.annin.store.presentation.ui.viewholder.MainViewHolder.OnClickListener;

/**
 * Presenter главного экрана.
 *
 * @author Pavel Annin.
 */
public class MainPresenter extends BasePresenter {

    private MainViewHolder mViewHolder;
    private MainView mView;

    @Inject
    public MainPresenter() {
        // Empty
    }

    public void onInitialization() {

    }

    /**
     * Получено событие: нажата кнопка назад.
     * @return Если {@code true} выполнить стандартное действие, иначе ничего не выполнять.
     */
    public boolean onBackPress() {
        if (mViewHolder != null && mViewHolder.isNavigationShowing()) {
            mViewHolder.hideNavigation();
            return false;
        }
        return true;
    }

    public void setViewHolder(MainViewHolder viewHolder) {
        mViewHolder = viewHolder;
        if (mViewHolder != null) {
            mViewHolder.setOnClickListener(onHolderListener);
        }
    }

    public void setView(MainView view) {
        mView = view;
    }

    private final OnClickListener onHolderListener = new OnClickListener() {
        @Override
        public void onGitHubClick() {
            mViewHolder.hideNavigation();
            if (mView != null) {
                mView.onGitHubOpen();
            }
        }

        @Override
        public void onReceiptProductInvoiceClick() {
            mViewHolder.hideNavigation();
            if (mView != null) {
                mView.onReceiptProductInvoiceOpen();
            }
        }

        @Override
        public void onCardProductClick() {
            mViewHolder.hideNavigation();
            if (mView != null) {
                mView.onCardProductsOpen();
            }
        }

        @Override
        public void onStoreClick() {
            mViewHolder.hideNavigation();
            if (mView != null) {
                mView.onStoresOpen();
            }
        }

        @Override
        public void onUnitsClick() {
            mViewHolder.hideNavigation();
            if (mView != null) {
                mView.onUnitsOpen();
            }
        }

        @Override
        public void onAboutClick() {
            mViewHolder.hideNavigation();
            if (mView != null) {
                mView.onAboutOpen();
            }
        }
    };
}
