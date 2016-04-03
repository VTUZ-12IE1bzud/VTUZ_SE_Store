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

package ru.annin.store.data.util;

import android.content.Context;
import android.support.annotation.NonNull;

import io.realm.DynamicRealm;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmMigration;
import io.realm.RealmSchema;

/**
 *
 *
 * @author Pavel Annin.
 */
public class RealmUtil {

    // Version's
    public static final int VERSION_1 = 0x01;

    public static final int CURRENT_VERSION = VERSION_1;

    private static final String DB_NAME = "store.realm";

    public static void initialize(@NonNull final Context ctx) {
        final RealmConfiguration configuration = new RealmConfiguration.Builder(ctx)
                .name(DB_NAME)
                .schemaVersion(CURRENT_VERSION)
                .migration(new Migration())
                .build();
        Realm.setDefaultConfiguration(configuration);
    }

    public static Realm getRealm() {
        return Realm.getDefaultInstance();
    }

    private static class Migration implements RealmMigration {

        @Override
        public void migrate(DynamicRealm realm, long oldVersion, long newVersion) {
            final RealmSchema schema = realm.getSchema();
            // Ignore
        }
    }

}
