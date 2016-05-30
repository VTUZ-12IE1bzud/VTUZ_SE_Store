package ru.annin.store.data.repository;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;

import ru.annin.store.R;
import ru.annin.store.domain.repository.SettingsRepository;

/**
 * <p>Реализация репозитория "Настройки".</p>
 *
 * @author Pavel Annin, 2016.
 */
public class SettingsRepositoryImpl implements SettingsRepository {

    private static final String PREFS_NAME = "SETTINGS";

    private static final String PREFS_FIRST_START = "PREFS_FIRST_START";
    private static final String PREFS_STORE_ID = "PREFS_STORE_ID";

    private final Context ctx;

    public SettingsRepositoryImpl(@NonNull Context context) {
        this.ctx = context;
    }

    @Override
    public boolean isFirstStart() {
        SharedPreferences settings = ctx.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return settings.getBoolean(PREFS_FIRST_START, true);
    }

    @Override
    public void saveFirstStart(boolean firstStart) {
        SharedPreferences settings = ctx.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        settings.edit()
                .putBoolean(PREFS_FIRST_START, firstStart)
                .apply();
    }

    @NonNull
    @Override
    public String getSelectStoreId() {
        SharedPreferences settings = ctx.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return settings.getString(PREFS_STORE_ID, ctx.getString(R.string.default_store_id));
    }

    @Override
    public void saveStoreId(@NonNull String id) {
        SharedPreferences settings = ctx.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        settings.edit()
                .putString(PREFS_STORE_ID, id)
                .apply();
    }
}
