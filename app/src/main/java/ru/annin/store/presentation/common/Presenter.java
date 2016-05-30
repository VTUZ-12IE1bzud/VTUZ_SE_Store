package ru.annin.store.presentation.common;

/**
 * <p>Интерфейс Presenter (MVP).</p>
 *
 * @author Pavel Annin, 2016.
 */
public interface Presenter {

    void onResume();
    void onPause();
    void onDestroy();
}
