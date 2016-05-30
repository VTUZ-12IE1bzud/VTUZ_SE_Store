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
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import io.realm.RealmResults;
import ru.annin.store.R;
import ru.annin.store.domain.model.UnitModel;
import ru.annin.store.presentation.common.BaseViewHolder;
import ru.annin.store.presentation.ui.adapter.UnitListAdapter;

/**
 * <p>ViewHolder экрана "Номенклатура".</p>
 *
 * @author Pavel Annin.
 */
public class DetailNomenclatureViewHolder extends BaseViewHolder {

    // View's
    private final Toolbar toolbar;
    private final TextInputLayout tilName;
    private final EditText edtName;
    private Spinner spUnit;
    private final TextView txtUnitListEmpty;

    // Adapter's
    private UnitListAdapter unitAdapter;

    // Listener's
    private OnClickListener listener;

    public DetailNomenclatureViewHolder(@NonNull View view) {
        super(view);
        toolbar = (Toolbar) vRoot.findViewById(R.id.toolbar);
        tilName = (TextInputLayout) vRoot.findViewById(R.id.til_name);
        edtName = (EditText) vRoot.findViewById(R.id.edt_name);
        spUnit = (Spinner) vRoot.findViewById(R.id.sp_unit);
        txtUnitListEmpty = (TextView) vRoot.findViewById(R.id.txt_empty_unit_list);

        unitAdapter = new UnitListAdapter(view.getContext());
        // Setup
        toolbar.setNavigationOnClickListener(onNavigationClickListener);
        toolbar.inflateMenu(R.menu.menu_store_detail);
        toolbar.setOnMenuItemClickListener(onMenuItemClickListener);
        spUnit.setAdapter(unitAdapter);
        spUnit.setEmptyView(txtUnitListEmpty);
    }


    public DetailNomenclatureViewHolder errorName(String text) {
        tilName.setErrorEnabled(text != null);
        tilName.setError(text);
        return this;
    }

    public DetailNomenclatureViewHolder showName(String text) {
        edtName.setText(text);
        return this;
    }


    public DetailNomenclatureViewHolder selectUnit(String unitId) {
        spUnit.setSelection(unitAdapter.getPosition(unitId));
        return this;
    }

    public DetailNomenclatureViewHolder showUnits(RealmResults<UnitModel> units) {
        unitAdapter.updateData(units);
        return this;
    }

    public void setOnClickListener(OnClickListener listener) {
        this.listener = listener;
    }

    private final View.OnClickListener onNavigationClickListener = v -> {
        if (listener != null) {
            listener.onNavigationBackClick();
        }
    };

    private String getName() {
        return edtName.getText().toString();
    }

    private final Toolbar.OnMenuItemClickListener onMenuItemClickListener = item -> {
        if (listener != null) {
            switch (item.getItemId()) {
                case R.id.action_save:
                    int position = spUnit.getSelectedItemPosition();
                    listener.onSaveClick(getName(), unitAdapter.getId(position));
                    return true;
                default:
                    break;
            }
        }
        return false;
    };

    public interface OnClickListener {
        void onNavigationBackClick();
        void onSaveClick(String name, String unitId);
    }
}
