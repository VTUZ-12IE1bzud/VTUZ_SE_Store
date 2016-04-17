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

package ru.annin.store.presentation.common;

import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.view.View;

/**
 * Базовый BaseViewHolder.
 *
 * @author Pavel Annin.
 */
public abstract class BaseViewHolder {

    protected final Resources mResources;

    public BaseViewHolder(@NonNull final View view) {
        mResources = view.getResources();
    }

    public void onDestroyView() {
        // Empty
    }
}
