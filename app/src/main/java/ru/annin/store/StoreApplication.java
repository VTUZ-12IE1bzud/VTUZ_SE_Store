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

package ru.annin.store;

import android.app.Application;

import ru.annin.store.data.repository.SettingsRepositoryImpl;
import ru.annin.store.data.util.RealmUtil;
import ru.annin.store.domain.repository.SettingsRepository;

/**
 * <p> Класс приложения. </p>
 *
 * @author Pavel Annin.
 */
public class StoreApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        SettingsRepository settingsRepository = new SettingsRepositoryImpl(this);
        RealmUtil.initialize(this);
        if (settingsRepository.isFirstStart()) {
            RealmUtil.importDefaultData(this);
            settingsRepository.saveFirstStart(false);
        }
    }
}
