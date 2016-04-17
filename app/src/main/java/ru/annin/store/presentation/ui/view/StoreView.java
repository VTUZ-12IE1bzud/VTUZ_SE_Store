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

package ru.annin.store.presentation.ui.view;

import android.support.annotation.NonNull;

/**
 * Представление экрана "Склады".
 *
 * @author Pavel Annin.
 */
public interface StoreView {

    void onCreateStoreOpen();
    void onStoreOpen(@NonNull String storeId);
    void onFinish();

}
