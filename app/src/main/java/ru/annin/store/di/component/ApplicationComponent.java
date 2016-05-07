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

package ru.annin.store.di.component;

import android.content.Context;

import javax.inject.Singleton;

import dagger.Component;
import ru.annin.store.di.module.ApplicationModule;
import ru.annin.store.domain.repository.DataRepository;
import ru.annin.store.presentation.ui.activity.AboutActivity;
import ru.annin.store.presentation.ui.activity.BaseActivity;
import ru.annin.store.presentation.ui.activity.CardProductActivity;
import ru.annin.store.presentation.ui.activity.DetailCardProductActivity;
import ru.annin.store.presentation.ui.activity.DetailStoreActivity;
import ru.annin.store.presentation.ui.activity.DetailUnitActivity;
import ru.annin.store.presentation.ui.activity.MainActivity;
import ru.annin.store.presentation.ui.activity.StoreActivity;
import ru.annin.store.presentation.ui.activity.UnitActivity;

/**
 * <p> Dagger компонент. </p>
 *
 * @author Pavel Annin.
 */
@Singleton
@Component(modules = ApplicationModule.class)
public interface ApplicationComponent {

    void inject(BaseActivity activity);
    void inject(MainActivity activity);
    void inject(AboutActivity activity);
    void inject(UnitActivity activity);
    void inject(DetailUnitActivity activity);
    void inject(CardProductActivity activity);
    void inject(DetailCardProductActivity activity);
    void inject(StoreActivity activity);
    void inject(DetailStoreActivity activity);

    Context context();
    DataRepository dataRepository();
}
