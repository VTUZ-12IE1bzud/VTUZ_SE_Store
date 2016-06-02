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

package ru.annin.store.presentation.ui.activity;

import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.FontProvider;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.XMLWorkerHelper;

import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.nio.charset.Charset;

import ru.annin.store.presentation.common.BaseActivity;

/**
 * <p> Сплеш экран. </p>
 *
 * @author Pavel Annin.
 */
public class SplashActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        String html = "<p>TextTest</p>" +
                "<table border=\"1\">\n" +
                "<tr>\n" +
                "<td>строка1 ячейка1</td>\n" +
                "<td>строка1 ячейка2</td>\n" +
                "<td>строка1 ячейка3</td>\n" +
                "</tr>\n" +
                "<tr>\n" +
                "<td>строка2 ячейка1</td>\n" +
                "<td>строка2 ячейка2</td>\n" +
                "<td>строка2 ячейка3</td>\n" +
                "</tr>\n" +
                "<tr>\n" +
                "<td>строка3 ячейка1</td>\n" +
                "<td>строка3 ячейка2</td>\n" +
                "<td>строка3 ячейка3</td>\n" +
                "</tr>\n" +
                "</table>";
        createPDF(html, "Test3.pdf");

//        navigator.navigate2Main(this);
//        finish();
    }

    public boolean createPDF(String rawHTML, String fileName) {
        File path = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC), "test");
        if (!path.exists()) {
            path.mkdir();
        }
        File file = new File(path, fileName);

        try {

            Document document = new Document();
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(file));
            document.open();

            String htmlText = Jsoup.clean(rawHTML, Whitelist.relaxed());
            InputStream inputStream = new ByteArrayInputStream( htmlText.getBytes());

            // Печатаем документ PDF
            XMLWorkerHelper.getInstance().parseXHtml(writer, document, inputStream,
                    null, Charset.defaultCharset(), new MyFont());

            document.close();
            return true;

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        } catch (DocumentException e) {
            e.printStackTrace();
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public class MyFont implements FontProvider {
        private static final String FONT_PATH = "/system/fonts/DroidSans.ttf";
        private static final String FONT_ALIAS = "my_font";

        public MyFont(){ FontFactory.register(FONT_PATH, FONT_ALIAS); }

        @Override
        public Font getFont(String fontname, String encoding, boolean embedded,
                            float size, int style, BaseColor color){

            return FontFactory.getFont(FONT_ALIAS, BaseFont.IDENTITY_H,
                    BaseFont.EMBEDDED, size, style, color);
        }

        @Override
        public boolean isRegistered(String name) { return name.equals( FONT_ALIAS ); }
    }
}
