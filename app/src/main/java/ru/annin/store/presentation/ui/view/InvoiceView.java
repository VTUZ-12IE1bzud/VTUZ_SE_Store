package ru.annin.store.presentation.ui.view;

/**
 * <p>Представление экрана "Товарная накладная".</p>
 *
 * @author Pavel Annin, 2016.
 */
public interface InvoiceView {
    void onFinish();
    void onCreate();
    void onOpen(String id);
}
