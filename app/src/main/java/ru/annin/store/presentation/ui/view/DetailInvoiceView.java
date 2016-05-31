package ru.annin.store.presentation.ui.view;

import android.support.annotation.NonNull;
import android.support.annotation.StringRes;

import ru.annin.store.presentation.ui.alert.DetailProductAlert;

/**
 * <p>Представление экрана "Приходная накладная".</p>
 *
 * @author Pavel Annin, 2016.
 */
public interface DetailInvoiceView {

    @NonNull
    String getString(@StringRes int resId);
    void onFinish();
    void onCreate(DetailProductAlert.OnInteractionListener listener);
    void onOpen(String productId, DetailProductAlert.OnInteractionListener listener);
}
