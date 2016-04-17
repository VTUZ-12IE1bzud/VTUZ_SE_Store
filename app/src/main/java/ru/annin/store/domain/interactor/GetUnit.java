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

package ru.annin.store.domain.interactor;

import javax.inject.Inject;

import ru.annin.store.domain.repository.DataRepository;
import rx.Observable;
import rx.Subscriber;

/**
 * @author Pavel Annin.
 */
public class GetUnit extends UseCase {

    // Repository
    DataRepository mRepository;

    private String unitId;

    @Inject
    protected GetUnit(DataRepository dataRepository) {
        super();
        mRepository = dataRepository;
    }

    public void execute(String unitId, Subscriber UseCaseSubscriber) {
        this.unitId = unitId;
        super.execute(UseCaseSubscriber);
    }

    @Override
    protected Observable buildUseCaseObservable() {
        return mRepository.getUnitById(unitId);
    }
}
