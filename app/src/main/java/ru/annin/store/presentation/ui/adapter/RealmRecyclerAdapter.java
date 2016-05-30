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

package ru.annin.store.presentation.ui.adapter;

import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import io.realm.RealmChangeListener;
import io.realm.RealmObject;
import io.realm.RealmResults;

/**
 * Realm адаптер.
 *
 * @author Pavel Annin.
 */
public abstract class RealmRecyclerAdapter <TRealm extends RealmObject,
        VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> {

    private View viewEmpty;
    protected RealmResults<TRealm> mRealmResults;
    private final RealmChangeListener listener;

    public RealmRecyclerAdapter(boolean automaticUpdate) {
        this.listener = (!automaticUpdate) ? null : new ChangeListener();
        notifyEmptyChanged();
    }

    @Override
    public int getItemCount() {
        if (mRealmResults != null) {
            return mRealmResults.size();
        }
        return 0;
    }

    public void setViewEmpty(@Nullable final View view) {
        viewEmpty = view;
        notifyEmptyChanged();
    }

    public void updateRealmResults(@Nullable RealmResults<TRealm> queryResults) {
        if (listener != null) {
            if (this.mRealmResults != null) {
                this.mRealmResults.removeChangeListener(listener);
            }
            if (queryResults != null) {
                queryResults.addChangeListener(listener);
            }
        }
        this.mRealmResults = queryResults;
        notifyDataSetChanged();
        notifyEmptyChanged();
    }

    public boolean isEmpty() {
        return (mRealmResults != null ? mRealmResults.size() : 0) == 0;
    }

    @Nullable
    public TRealm getItem(int position) {
        if (mRealmResults != null && position >= 0 && position < mRealmResults.size()) {
            return mRealmResults.get(position);
        }
        return null;
    }

    protected void notifyEmptyChanged() {
        if (viewEmpty != null) {
            if (mRealmResults != null && mRealmResults.size() >= 1) {
                viewEmpty.setVisibility(View.GONE);
            } else {
                viewEmpty.setVisibility(View.VISIBLE);
            }
        }
    }

    private final class ChangeListener implements RealmChangeListener {

        @Override
        public void onChange(Object element) {
            notifyDataSetChanged();
            notifyEmptyChanged();
        }
    }
}