package ru.annin.store.presentation.ui.alert;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;

import ru.annin.store.R;
import ru.annin.store.data.repository.NomenclatureRepositoryImpl;
import ru.annin.store.data.repository.ProductRepositoryImpl;
import ru.annin.store.domain.model.NomenclatureModel;
import ru.annin.store.domain.repository.NomenclatureRepository;
import ru.annin.store.domain.repository.ProductRepository;
import ru.annin.store.presentation.ui.adapter.NomenclatureSelectAdapter;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * <p>Диалог "Товар".</p>
 *
 * @author Pavel Annin, 2016.
 */
public class DetailProductAlert extends DialogFragment {

    public static final String TAG = DetailProductAlert.class.getSimpleName();

    private static final String BUNDLE_PRODUCT_ID = "ru.annin.store.bundle.product_id";

    private CompositeSubscription subscription;

    // View's
    private Spinner spNomenclature;
    private TextInputLayout tilAmount;
    private EditText edtAmount;
    private TextInputLayout tilPrice;
    private EditText edtPrice;

    // Adapter's
    private NomenclatureSelectAdapter adapter;

    // Listener's
    private OnInteractionListener listener;

    // Data's
    private boolean isCreate;
    private String productId;
    private String nomenclatureId;
    private float amount;
    private float price;

    // Repository'es
    private NomenclatureRepository nomenclatureRepository;
    private ProductRepository productRepository;

    public static DetailProductAlert newInstance(OnInteractionListener listener) {
        DetailProductAlert fragment = new DetailProductAlert();
        fragment.listener = listener;
        return fragment;
    }

    public static DetailProductAlert newInstance(String productId, OnInteractionListener listener) {
        Bundle args = new Bundle();
        args.putString(BUNDLE_PRODUCT_ID, productId);
        DetailProductAlert fragment = new DetailProductAlert();
        fragment.setArguments(args);
        fragment.listener = listener;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        subscription = new CompositeSubscription();
        nomenclatureRepository = new NomenclatureRepositoryImpl();
        productRepository = new ProductRepositoryImpl();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final View vRoot = LayoutInflater.from(getActivity()).inflate(R.layout.alert_detail_product, null);

        spNomenclature = (Spinner) vRoot.findViewById(R.id.sp_nomenclature);
        tilAmount = (TextInputLayout) vRoot.findViewById(R.id.til_amount_product);
        edtAmount = (EditText) vRoot.findViewById(R.id.edt_amount_product);
        tilPrice = (TextInputLayout) vRoot.findViewById(R.id.til_price_product);
        edtPrice = (EditText) vRoot.findViewById(R.id.edt_price_product);

        AlertDialog dialog = new AlertDialog.Builder(getActivity())
                .setTitle(R.string.title_alert_product)
                .setView(vRoot)
                .setPositiveButton(R.string.action_alert_product_positive, null)
                .setNegativeButton(R.string.action_alert_product_negative, null)
                .create();
        // Data
        final Bundle args = getArguments();
        if (args != null && args.containsKey(BUNDLE_PRODUCT_ID)) {
            isCreate = false;
            productId = args.getString(BUNDLE_PRODUCT_ID);
        } else {
            isCreate = true;
        }

        // Setup
        adapter = new NomenclatureSelectAdapter(getActivity());
        spNomenclature.setAdapter(adapter);
        tilAmount.setHintAnimationEnabled(false);
        tilPrice.setHintAnimationEnabled(false);

        Subscription subNomenclature = nomenclatureRepository.getAll()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(models -> {
                    adapter.updateData(models);
                    if (!TextUtils.isEmpty(nomenclatureId)) {
                        spNomenclature.setSelection(adapter.getPosition(nomenclatureId));
                    }
                });
        subscription.add(subNomenclature);

        if (!isCreate) {
            Subscription subProduct = productRepository.getById(productId)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(model -> {
                        if (model != null && model.isLoaded() && model.isValid()) {
                            productId = model.getId();
                            amount = model.getAmount();
                            price = model.getPrice();
                            edtAmount.setText(Float.toString(amount));
                            edtPrice.setText(Float.toString(price));
                            final NomenclatureModel nomenclature = model.getNomenclature();
                            if (nomenclature != null && nomenclature.isValid()) {
                                nomenclatureId = nomenclature.getId();
                                spNomenclature.setSelection(adapter.getPosition(nomenclatureId));
                            }
                        }
                    });
            subscription.add(subProduct);
        }

        return dialog;
    }

    @Override
    public void onStart() {
        super.onStart();
        ((AlertDialog) getDialog()).getButton(DialogInterface.BUTTON_POSITIVE)
                .setOnClickListener(v -> {
                    String nomenclatureId = adapter.getId(spNomenclature.getSelectedItemPosition());
                    String amount = edtAmount.getText().toString();
                    String price = edtPrice.getText().toString();
                    if (isValidation(nomenclatureId, amount, price)) {
                        if (listener != null) {
                            float fAmount = Float.valueOf(amount);
                            float fPrice = Float.valueOf(price);
                            if (isCreate) {
                                listener.onCreateProduct(nomenclatureId, fAmount, fPrice);
                            } else {
                                listener.onSaveProduct(productId, nomenclatureId, fAmount, fPrice);
                            }
                        }
                        getDialog().dismiss();
                    }
                });
        ((AlertDialog) getDialog()).getButton(DialogInterface.BUTTON_NEGATIVE)
                .setOnClickListener(v -> getDialog().dismiss());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        subscription.unsubscribe();
    }

    private boolean isValidation(String nomenclatureId, String amount, String price) {
        boolean valid = true;

        if (TextUtils.isEmpty(nomenclatureId)) {
            valid = false;
        }
        if (TextUtils.isEmpty(amount)) {
            valid = false;
        }
        if (TextUtils.isEmpty(price)) {
            valid = false;
        }
        return valid;
    }

    public interface OnInteractionListener {
        void onSaveProduct(String productId, String nomenclatureId, float amount, float price);
        void onCreateProduct(String nomenclatureId, float amount, float price);
    }
}
