package com.kravchenko.designermanager.processor.url;

import com.kravchenko.designermanager.model.OrderInfo;
import com.kravchenko.designermanager.model.OrderedItem;
import com.kravchenko.designermanager.processor.OrderedItemDocumentGenerator;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class UrlHtmlGenerator implements OrderedItemDocumentGenerator {

    public static final String Oracal = "Ламінована наклейка Oracal";
    public static final String Pvh = "Пластик ПВХ";
    private List<OrderedItem> items;
    private static final Logger LOGGER = LoggerFactory.getLogger(UrlHtmlGenerator.class);
    @Autowired
    private Environment environment;

    @Override
    public void generate(Map<OrderInfo, List<OrderedItem>> infoListMap) {
        String path = environment.getRequiredProperty("generate.doc.path");
        for (OrderInfo info : infoListMap.keySet()) {
            XWPFDocument document = new XWPFDocument();
            items = infoListMap.get(info);
            LOGGER.info("===Inside Generate=");
            LOGGER.info("Size :" + String.valueOf(items.size()));
            try (FileOutputStream out = new FileOutputStream(new File(path + info.getOrderNumber() + ".docx"))) {
                XWPFTable table = document.createTable(items.size(), 6);
                table.setInsideHBorder(XWPFTable.XWPFBorderType.SINGLE, 10, 20, "auto");
                table.setInsideVBorder(XWPFTable.XWPFBorderType.SINGLE, 10, 20, "auto");
                int count = 0;
                for (OrderedItem item : items) {
                    XWPFTableRow tableRow = table.getRow(count);
                    tableRow.getCell(0).setText(Integer.toString(++count));
                    LOGGER.info("Processing " + count);
                    String name;
                    if (item.getMaterial().contains(Oracal)) {
                        name = "Наклейка ламінована" + "\"" + item.getName() + "\"," + item.getRange();
                    } else {
                        Pattern pattern = Pattern.compile("\\d[\\s][м][м]");
                        Matcher mat = pattern.matcher(item.getMaterial());
                        String end = "";
                        if (mat.find())
                            end = item.getMaterial().substring(mat.start(), mat.end());
                        if (item.getMaterial().contains("Світловідбивний")) {
                            name = "Табличка інформаційна" + "\"" + item.getName() + "\"," + item.getRange()
                                    + ",ПВХ " + end + " світловідбивна";
                        } else {
                            name = "Табличка інформаційна" + "\"" + item.getName() + "\"," + item.getRange()
                                    + ",ПВХ " + end + " ламінована";
                        }
                    }
                    tableRow.getCell(1).setText(name);
                    tableRow.getCell(2).setText("шт.");
                    tableRow.getCell(3).setText(String.valueOf(item.getCount()));
                    String amount = item.getAmount().substring(0, item.getAmount().indexOf("грн")).trim();
                    double aDouble = Double.parseDouble(amount);
                    tableRow.getCell(4).setText(String.format("%1$,.2f", aDouble));
                    Double sum = aDouble * item.getCount();
                    tableRow.getCell(5).setText(String.format("%1$,.2f", sum));
                }
                document.write(out);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
