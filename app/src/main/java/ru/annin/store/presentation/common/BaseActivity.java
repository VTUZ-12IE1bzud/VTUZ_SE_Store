package ru.annin.store.presentation.common;

import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import ru.annin.store.presentation.navigation.Navigator;


/**
 * <p>Базовая Activity.</p>
 *
 * @author Pavel Annin, 2016.
 */
public abstract class BaseActivity<P extends BasePresenter> extends AppCompatActivity {

    protected final Navigator navigator;
    @Nullable
    protected P presenter;

    public BaseActivity() {
        navigator = new Navigator();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (presenter != null) {
            presenter.onResume();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (presenter != null) {
            presenter.onPause();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (presenter != null) {
            presenter.onDestroy();
        }
    }

}
