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

package ru.annin.store.presentation.navigation;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;

import javax.inject.Inject;
import javax.inject.Singleton;

import ru.annin.store.presentation.ui.activity.AboutActivity;
import ru.annin.store.presentation.ui.activity.CardProductActivity;
import ru.annin.store.presentation.ui.activity.DetailCardProductActivity;
import ru.annin.store.presentation.ui.activity.DetailStoreActivity;
import ru.annin.store.presentation.ui.activity.DetailUnitActivity;
import ru.annin.store.presentation.ui.activity.MainActivity;
import ru.annin.store.presentation.ui.activity.ReceiptProductInvoiceActivity;
import ru.annin.store.presentation.ui.activity.StoreActivity;
import ru.annin.store.presentation.ui.activity.UnitActivity;

/**
 * <p> Класс, обеспечивающий навигацию по приложению. </p>
 *
 * @author Pavel Annin.
 */
@Singleton
public class Navigator {

    @Inject
    public Navigator() {
        // Empty
    }

    public void navigate2Main(@NonNull Context ctx) {
        final Intent intent = new Intent(ctx, MainActivity.class);
        ctx.startActivity(intent);
    }

    public void navigate2GitHub(@NonNull Context ctx) {
        final String url = "https://github.com/anninpavel/VTUZ_SoftwareEngineering_Store";
        final Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        ctx.startActivity(intent);
    }

    public void navigate2ReceiptProductInvoice(@NonNull Context ctx) {
        final Intent intent = new Intent(ctx, ReceiptProductInvoiceActivity.class);
        ctx.startActivity(intent);
    }

    public void navigate2CreateReceiptProductInvoice(@NonNull Context ctx) {
        final Intent intent = new Intent(ctx, .class);
        ctx.startActivity(intent);
    }

    public void navigate2OpenReceiptProductInvoice(@NonNull Context ctx, @NonNull String receiptProductInvoiceId) {
        final Intent intent = new Intent(ctx, .class);
        intent.setAction(Intent.ACTION_EDIT);
        intent.putExtra(.EXTRA_CARD_PRODUCT_ID, receiptProductInvoiceId);
        ctx.startActivity(intent);
    }

    public void navigate2CardProducts(@NonNull Context ctx) {
        final Intent intent = new Intent(ctx, CardProductActivity.class);
        ctx.startActivity(intent);
    }

    public void navigate2CreateCardProduct(@NonNull Context ctx) {
        final Intent intent = new Intent(ctx, DetailCardProductActivity.class);
        ctx.startActivity(intent);
    }

    public void navigate2OpenCardProduct(@NonNull Context ctx, @NonNull String cardProductId) {
        final Intent intent = new Intent(ctx, DetailCardProductActivity.class);
        intent.setAction(Intent.ACTION_EDIT);
        intent.putExtra(DetailCardProductActivity.EXTRA_CARD_PRODUCT_ID, cardProductId);
        ctx.startActivity(intent);
    }

    public void navigate2Stores(@NonNull Context ctx) {
        final Intent intent = new Intent(ctx, StoreActivity.class);
        ctx.startActivity(intent);
    }

    public void navigate2CreateStore(@NonNull Context ctx) {
        final Intent intent = new Intent(ctx, DetailStoreActivity.class);
        ctx.startActivity(intent);
    }

    public void navigate2OpenStore(@NonNull Context ctx, @NonNull String unitId) {
        final Intent intent = new Intent(ctx, DetailStoreActivity.class);
        intent.setAction(Intent.ACTION_EDIT);
        intent.putExtra(DetailStoreActivity.EXTRA_STORE_ID, unitId);
        ctx.startActivity(intent);
    }

    public void navigate2Units(@NonNull Context ctx) {
        final Intent intent = new Intent(ctx, UnitActivity.class);
        ctx.startActivity(intent);
    }

    public void navigate2CreateUnit(@NonNull Context ctx) {
        final Intent intent = new Intent(ctx, DetailUnitActivity.class);
        ctx.startActivity(intent);
    }

    public void navigate2OpenUnit(@NonNull Context ctx, @NonNull String unitId) {
        final Intent intent = new Intent(ctx, DetailUnitActivity.class);
        intent.setAction(Intent.ACTION_EDIT);
        intent.putExtra(DetailUnitActivity.EXTRA_UNIT_ID, unitId);
        ctx.startActivity(intent);
    }

    public void navigate2About(@NonNull Context ctx) {
        final Intent intent = new Intent(ctx, AboutActivity.class);
        ctx.startActivity(intent);
    }
}
