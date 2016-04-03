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

package ru.annin.store.presentation.presenter.presenter;

import android.support.annotation.NonNull;

import javax.inject.Inject;

import ru.annin.store.BuildConfig;
import ru.annin.store.presentation.presenter.common.Presenter;
import ru.annin.store.presentation.presenter.view.AboutView;
import ru.annin.store.presentation.presenter.viewholder.AboutViewHolder;

/**
 * Presenter экрана "О программе".
 *
 * @author Pavel Annin.
 */
public class AboutPresenter implements Presenter  {

    private AboutView mView;
    private AboutViewHolder mViewHolder;

    @Inject
    public AboutPresenter() {
        // Empty
    }

    @Override
    public void onResume() {
        if (mViewHolder != null) {
            mViewHolder.showAppVersion(BuildConfig.VERSION_NAME, BuildConfig.VERSION_CODE);
        }
    }

    @Override
    public void onPause() {
        // Empty
    }

    @Override
    public void onDestroy() {
        // Empty
    }

    public void setView(@NonNull final AboutView view) {
        mView = view;
    }

    public void setViewHolder(@NonNull final AboutViewHolder viewHolder) {
        mViewHolder = viewHolder;
        mViewHolder.setOnClickListener(onViewHolderClickListener);
    }

    private final AboutViewHolder.OnClickListener onViewHolderClickListener = new AboutViewHolder.OnClickListener() {
        @Override
        public void onNavBackClick() {
            if (mView != null) {
                mView.onFinish();
            }
        }
    };
}
