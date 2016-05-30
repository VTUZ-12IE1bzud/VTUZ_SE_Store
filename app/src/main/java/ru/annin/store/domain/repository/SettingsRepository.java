package ru.annin.store.domain.repository;

import android.support.annotation.NonNull;

/**
 * <p>Интерфейс репозитория "Настройки".</p>
 *
 * @author Pavel Annin, 2016.
 */
public interface SettingsRepository {

    boolean isFirstStart();

    void saveFirstStart(boolean firstStart);

    @NonNull
    String getSelectStoreId();

    void saveStoreId(@NonNull String id);
}
