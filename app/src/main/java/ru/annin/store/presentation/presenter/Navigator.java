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

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;

import javax.inject.Inject;
import javax.inject.Singleton;

import ru.annin.store.presentation.presenter.activity.AboutActivity;

/**
 * Класс, обеспечивающий навигацию по приложению.
 *
 * @author Pavel Annin.
 */
@Singleton
public class Navigator {

    @Inject
    public Navigator() {
        // Empty
    }

    /** Перейти на экран "О программе". */
    public void navigate2About(@NonNull final Context ctx) {
        final Intent intent = new Intent(ctx, AboutActivity.class);
        ctx.startActivity(intent);
    }
}
