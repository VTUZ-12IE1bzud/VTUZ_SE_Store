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

package ru.annin.store.presentation.ui.viewholder;

import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import ru.annin.store.R;
import ru.annin.store.presentation.common.BaseViewHolder;

/**
 * <p>View Holder экрана "О программе".</p>
 *
 * @author Pavel Annin.
 */
public class AboutViewHolder extends BaseViewHolder {

    // View's
    private final Toolbar toolbar;
    private final TextView txtAppVersion;

    // Listener's
    private OnClickListener mListener;

    public AboutViewHolder(@NonNull final View view) {
        super(view);
        toolbar = (Toolbar) vRoot.findViewById(R.id.toolbar);
        txtAppVersion = (TextView) vRoot.findViewById(R.id.txt_app_version);

        // Setup
        toolbar.setNavigationOnClickListener(onNavigationClickListener);
    }

    /**
     * Отобразить версию и билд приложения.
     *
     * @param appVersion Версия приложения.
     * @param appBuild   Билд приложения.
     */
    public void showAppVersion(@NonNull final String appVersion, int appBuild) {
        if (txtAppVersion != null) {
            txtAppVersion.setText(getString(R.string.title_app_version_format, appVersion, appBuild));
        }
    }

    public void setOnClickListener(final OnClickListener listener) {
        mListener = listener;
    }


    private final View.OnClickListener onNavigationClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (mListener != null) {
                mListener.onNavBackClick();
            }
        }
    };

    public interface OnClickListener {
        void onNavBackClick();
    }
}
