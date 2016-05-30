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
import android.support.annotation.NonNull;

import ru.annin.store.presentation.ui.activity.AboutActivity;
import ru.annin.store.presentation.ui.activity.DetailNomenclatureActivity;
import ru.annin.store.presentation.ui.activity.DetailStoreActivity;
import ru.annin.store.presentation.ui.activity.DetailUnitActivity;
import ru.annin.store.presentation.ui.activity.InvoiceActivity;
import ru.annin.store.presentation.ui.activity.MainActivity;
import ru.annin.store.presentation.ui.activity.NomenclatureActivity;
import ru.annin.store.presentation.ui.activity.StoreActivity;
import ru.annin.store.presentation.ui.activity.UnitActivity;

/**
 * <p> Класс, обеспечивающий навигацию по приложению. </p>
 *
 * @author Pavel Annin.
 */
public class Navigator {

    public void navigate2Main(@NonNull Context ctx) {
        final Intent intent = new Intent(ctx, MainActivity.class);
        ctx.startActivity(intent);
    }

    public void navigate2Invoice(@NonNull Context ctx) {
        final Intent intent = new Intent(ctx, InvoiceActivity.class);
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

    public void navigate2Nomenclature(@NonNull Context ctx) {
        final Intent intent = new Intent(ctx, NomenclatureActivity.class);
        ctx.startActivity(intent);
    }

    public void navigate2CreateNomenclature(@NonNull Context ctx) {
        final Intent intent = new Intent(ctx, DetailNomenclatureActivity.class);
        ctx.startActivity(intent);
    }

    public void navigate2OpenNomenclature(@NonNull Context ctx, @NonNull String id) {
        final Intent intent = new Intent(ctx, DetailNomenclatureActivity.class);
        intent.setAction(Intent.ACTION_EDIT);
        intent.putExtra(DetailNomenclatureActivity.EXTRA_NOMENCLATURE_ID, id);
        ctx.startActivity(intent);
    }

    public void navigate2About(@NonNull Context ctx) {
        final Intent intent = new Intent(ctx, AboutActivity.class);
        ctx.startActivity(intent);
    }
}