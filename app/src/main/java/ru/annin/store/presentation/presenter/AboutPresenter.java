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

import javax.inject.Inject;

import ru.annin.store.BuildConfig;
import ru.annin.store.presentation.common.BasePresenter;
import ru.annin.store.presentation.ui.view.AboutView;
import ru.annin.store.presentation.ui.viewholder.AboutViewHolder;

/**
 * BasePresenter экрана "О программе".
 *
 * @author Pavel Annin.
 */
public class AboutPresenter extends BasePresenter {

    private AboutView mView;
    private AboutViewHolder mViewHolder;

    @Inject
    public AboutPresenter() {
        // Empty
    }

    public void onInitialization() {
        if (mViewHolder != null) {
            mViewHolder.showAppVersion(BuildConfig.VERSION_NAME, BuildConfig.VERSION_CODE);
        }
    }

    public void setViewHolder(AboutViewHolder viewHolder) {
        mViewHolder = viewHolder;
        if (mViewHolder != null) {
            mViewHolder.setOnClickListener(onViewHolderClickListener);
        }
    }

    public void setView(AboutView view) {
        mView = view;
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
