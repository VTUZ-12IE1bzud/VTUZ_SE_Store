package ru.annin.store.presentation.common;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * <p>Базовый Presenter (MVP).</p>
 *
 * @author Pavel Annin, 2016.
 */
public abstract class BasePresenter<VH extends BaseViewHolder, V> implements Presenter {

    @Nullable
    protected VH viewHolder;
    @Nullable
    protected V view;

    @Override
    public void onResume() {
        // Empty.
    }

    @Override
    public void onPause() {
        // Empty.
    }

    @Override
    public void onDestroy() {
        // Empty.
    }

    @NonNull
    public BasePresenter setViewHolder(@Nullable VH vh) {
        viewHolder = vh;
        return this;
    }

    @NonNull
    public BasePresenter setView(@Nullable V v) {
        view = v;
        return this;
    }
}
