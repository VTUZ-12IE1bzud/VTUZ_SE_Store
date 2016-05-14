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

package ru.annin.store.presentation.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import ru.annin.store.R;
import ru.annin.store.presentation.presenter.ReceiptProductInvoicePresenter;
import ru.annin.store.presentation.ui.view.ReceiptProductInvoiceView;
import ru.annin.store.presentation.ui.viewholder.ReceiptProductInvoiceViewHolder;

/**
 * Экран "Товарная накладная"
 *
 * @author Pavel Annin.
 */
public class ReceiptProductInvoiceActivity extends BaseActivity implements ReceiptProductInvoiceView {

    @Inject
    ReceiptProductInvoicePresenter mPresenter;
    @Bind(R.id.main_container)
    View mainView;
    private ReceiptProductInvoiceViewHolder mViewHolder;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receipt_product_invoice);
        getApplicationComponent().inject(this);
        ButterKnife.bind(this);
        mViewHolder = new ReceiptProductInvoiceViewHolder(mainView);
        mPresenter.setViewHolder(mViewHolder);
        mPresenter.setView(this);
        mPresenter.onInitialization();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPresenter.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mPresenter.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.onDestroy();
        mViewHolder.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void onCreateReceiptProductInvoiceOpen() {
        mNavigator.navigate2CreateReceiptProductInvoice(this);
    }

    @Override
    public void onReceiptProductInvoiceOpen(String id) {
        mNavigator.navigate2OpenReceiptProductInvoice(this, id);
    }

    @Override
    public void onFinish() {
        finish();
    }
}
