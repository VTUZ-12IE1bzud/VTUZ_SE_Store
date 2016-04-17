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

package ru.annin.store.di.module;

import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import ru.annin.store.StoreApplication;
import ru.annin.store.data.repository.DataRepositoryImpl;
import ru.annin.store.domain.repository.DataRepository;

/**
 * Dagger модуль.
 *
 * @author Pavel Annin.
 */
@Module
public class ApplicationModule {

    private final StoreApplication app;

    public ApplicationModule(StoreApplication app) {
        this.app = app;
    }

    @Provides
    @Singleton
    Context providerApplicationContext() {
        return app;
    }

    @Provides
    @Singleton
    public DataRepository providerDataRepository(DataRepositoryImpl repository) {
        return repository;
    }
}
