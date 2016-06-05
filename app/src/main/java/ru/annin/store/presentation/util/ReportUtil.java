package ru.annin.store.presentation.util;

import android.os.Environment;
import android.support.annotation.NonNull;

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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.UUID;

import io.realm.RealmList;
import io.realm.RealmResults;
import ru.annin.store.domain.model.InvoiceModel;
import ru.annin.store.domain.model.ProductModel;

/**
 * Created by Pavel on 02.06.2016.
 */
public class ReportUtil {


    public static boolean createInvoicesReport(@NonNull String storeName,
                                               @NonNull RealmResults<InvoiceModel> models) {
        StringBuilder builder = new StringBuilder();
        builder.append("<h1><p align=\"center\">Товарные накладные</p></h1>")
                .append("<h2><p align=\"center\">(" + storeName + ")</p></h2>")
                .append("<table border=\"1\" align=\"center\" width=\"100%\">")
                .append("<tr><th>№</th><th>Наименование</th><th>Дата</th><th>Сумма</th></tr>");
        Float sumInvoice = 0.0f;
        for (int i = 0; i < models.size(); ++i) {
            InvoiceModel model = models.get(i);
            float sum = 0.0f;
            final RealmList<ProductModel> products = model.getProducts();
            if (products != null) {
                for (ProductModel product : products) {
                    float amount = product.getAmount();
                    float price = product.getPrice();
                    sum += price * amount;
                }
                sumInvoice += sum;
            }
            builder.append("<tr>" +
                    "<td>" + (i + 1) + "</td>" +
                    "<td>" + model.getName() + "</td>" +
                    "<td>" + SimpleDateFormat.getDateInstance(DateFormat.MEDIUM).format(model.getDate()) + "</td>" +
                    "<td align=\"right\">" + Float.toString(sum) + " р. </td>" +
                    "</tr>");
        }
        builder.append("<tr>" +
                "<td colspan=\"4\">" + Float.toString(sumInvoice) + " р. </td>" +
                "</tr>");
        builder.append("</table>");
        return createPDF(builder.toString(), UUID.randomUUID().toString() + ".pdf");
    }

    public static boolean createInvoiceReport(@NonNull String storeName,
                                               @NonNull InvoiceModel model) {
        StringBuilder builder = new StringBuilder();
        builder.append("<h1><p align=\"center\">Товарная накладная " + model.getName() + "</p></h1>")
                .append("<h2><p align=\"center\">(" + storeName + ")</p></h2>")
                .append("<table border=\"1\" align=\"center\" width=\"100%\">")
                .append("<tr><th>№</th><th>Наименование</th><th>Цена</th><th>Количество</th><th>Сумма</th></tr>");
        Float sumInvoice = 0.0f;
        for (int i = 0; i < model.getProducts().size(); ++i) {
            ProductModel product = model.getProducts().get(i);
            float sum = 0.0f;
            float amount = product.getAmount();
            float price = product.getPrice();
            sum += price * amount;
            sumInvoice += sum;
            builder.append("<tr>" +
                    "<td>" + (i + 1) + "</td>" +
                    "<td>" + product.getNomenclature().getName() + "</td>" +
                    "<td>" + Float.toString(price) + " р. </td>" +
                    "<td>" + Float.toString(amount) + " р. </td>" +
                    "<td>" + Float.toString(sum) + " р. </td>" +
                    "</tr>");
        }
        builder.append("<tr>" +
                "<td colspan=\"4\" align=\"right\">" + Float.toString(sumInvoice) + " р. </td>" +
                "</tr>");
        builder.append("</table>");
        return createPDF(builder.toString(), UUID.randomUUID().toString() + ".pdf");
    }

    private static boolean createPDF(String rawHTML, String fileName) {
        File path = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC), "Отчеты");
        if (!path.exists()) {
            path.mkdir();
        }
        File file = new File(path, fileName);

        try {

            Document document = new Document();
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(file));
            document.open();

            String htmlText = Jsoup.clean(rawHTML, Whitelist.relaxed());
            InputStream inputStream = new ByteArrayInputStream(htmlText.getBytes());

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

    static class MyFont implements FontProvider {
        private static final String FONT_PATH = "/system/fonts/DroidSans.ttf";
        private static final String FONT_ALIAS = "my_font";

        public MyFont() {
            FontFactory.register(FONT_PATH, FONT_ALIAS);
        }

        @Override
        public Font getFont(String fontname, String encoding, boolean embedded,
                            float size, int style, BaseColor color) {

            return FontFactory.getFont(FONT_ALIAS, BaseFont.IDENTITY_H,
                    BaseFont.EMBEDDED, size, style, color);
        }

        @Override
        public boolean isRegistered(String name) {
            return name.equals(FONT_ALIAS);
        }
    }

}


