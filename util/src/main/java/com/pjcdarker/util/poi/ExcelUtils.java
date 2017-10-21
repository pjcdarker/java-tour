package com.pjcdarker.util.poi;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class ExcelUtils {

    private static DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /**
     * 读取
     *
     * @param inputStream
     * @param clazz<T>    泛型<实体类>
     * @param hasTitle    是否有表头
     * @return
     * @throws IOException
     */
    public static <T> List<T> read(InputStream inputStream, Class<T> clazz, boolean hasTitle) {
        List<T> results = new ArrayList<>();
        try {
            Workbook wb = WorkbookFactory.create(inputStream);
            Map<Integer, Field> fieldMap = new HashMap<>();
            Field[] fields = clazz.getDeclaredFields();
            for (Field field : fields) {
                if (field.isAnnotationPresent(MapperCell.class)) {
                    MapperCell mapperCell = field.getAnnotation(MapperCell.class);
                    fieldMap.put(mapperCell.order(), field);
                }
            }

            int firstNum = hasTitle ? 1 : 0;
            for (int sheetNum = 0, total = wb.getNumberOfSheets(); sheetNum < total; sheetNum++) {
                Sheet sheet = wb.getSheetAt(sheetNum);
                for (int rowNum = firstNum, lastRowNum = sheet.getLastRowNum(); rowNum <= lastRowNum; rowNum++) {
                    Row row = sheet.getRow(rowNum);
                    T t = clazz.newInstance();
                    int cellIndex = 0;
                    for (Cell cell : row) {
                        Field field = fieldMap.get(cellIndex);
                        if (field != null) {
                            field.setAccessible(true);
                            setValue(cell, t, field);
                        }
                        ++cellIndex;
                    }
                    results.add(t);
                }
            }

        } catch (InstantiationException | IllegalAccessException | ParseException | InvalidFormatException
                | IOException e) {
            e.printStackTrace();
        }
        return results;
    }

    /**
     * @param outputStream 输入流
     * @param list         数据
     * @param sheetName    sheet名
     * @param isXls        是否xls后缀格式
     * @return
     */
    public static <T> boolean write(OutputStream outputStream, List<T> list, String sheetName, boolean isXls) {
        if (list == null || list.isEmpty()) {
            return false;
        }
        Workbook workbook;
        if (isXls) {
            workbook = new HSSFWorkbook();
        } else {
            workbook = new XSSFWorkbook();
        }
        T object = list.get(0);
        Map<String, Field> fieldMap = new HashMap<>();
        Map<Integer, String> titleMap = new TreeMap<>();

        // field
        Field[] fields = object.getClass().getDeclaredFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(MapperCell.class)) {
                MapperCell mapperCell = field.getAnnotation(MapperCell.class);
                String title = mapperCell.name();
                fieldMap.put(title, field);
                titleMap.put(mapperCell.order(), title);
            }
        }

        try {
            // sheet
            Sheet sheet = workbook.createSheet(sheetName);
            Collection<String> values = titleMap.values();

            String[] titleCells = new String[values.size()];
            values.toArray(titleCells);
            int titleCellLength = titleCells.length;

            // Row
            Row titleRow = sheet.createRow(0);
            for (int i = 0; i < titleCellLength; i++) {
                Cell cell = titleRow.createCell(i);
                cell.setCellValue(titleCells[i]);
            }

            // Cell
            for (int i = 0, length = list.size(); i < length; i++) {
                Row row = sheet.createRow(i + 1);
                for (int j = 0; j < titleCellLength; j++) {
                    Cell cell = row.createCell(j);
                    for (Map.Entry<String, Field> data : fieldMap.entrySet()) {
                        if (data.getKey().equals(titleCells[j])) {
                            Field field = data.getValue();
                            field.setAccessible(true);
                            cell.setCellValue(field.get(list.get(i)).toString());
                            break;
                        }
                    }
                }
            }
            workbook.write(outputStream);
        } catch (IllegalArgumentException | IllegalAccessException | IOException e) {
            e.printStackTrace();
        }
        return true;
    }

    /**
     * 设置属性值
     *
     * @param cell
     * @param o
     * @param field
     * @throws IllegalAccessException
     * @throws ParseException
     */
    private static void setValue(Cell cell, Object o, Field field) throws IllegalAccessException, ParseException {
        switch (cell.getCellType()) {
            case Cell.CELL_TYPE_BLANK:
                break;
            case Cell.CELL_TYPE_BOOLEAN:
                field.setBoolean(o, cell.getBooleanCellValue());
                break;
            case Cell.CELL_TYPE_ERROR:
                field.setByte(o, cell.getErrorCellValue());
                break;
            case Cell.CELL_TYPE_FORMULA:
                field.set(o, cell.getCellFormula());
                break;
            case Cell.CELL_TYPE_NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    if (field.getType().getName().equals(Date.class.getName())) {
                        field.set(o, cell.getDateCellValue());
                    } else {
                        field.set(o, format.format(cell.getDateCellValue()));
                    }
                } else {
                    if (field.getType().isAssignableFrom(Integer.class) || field.getType().getName().equals("int")) {
                        field.setInt(o, (int) cell.getNumericCellValue());
                    } else if (field.getType().isAssignableFrom(Short.class) || field.getType().getName().equals("short")) {
                        field.setShort(o, (short) cell.getNumericCellValue());
                    } else if (field.getType().isAssignableFrom(Float.class) || field.getType().getName().equals("float")) {
                        field.setFloat(o, (float) cell.getNumericCellValue());
                    } else if (field.getType().isAssignableFrom(Byte.class) || field.getType().getName().equals("byte")) {
                        field.setByte(o, (byte) cell.getNumericCellValue());
                    } else if (field.getType().isAssignableFrom(Double.class)
                            || field.getType().getName().equals("double")) {
                        field.setDouble(o, cell.getNumericCellValue());
                    } else if (field.getType().isAssignableFrom(String.class)) {
                        String s = String.valueOf(cell.getNumericCellValue());
                        if (s.contains("E")) {
                            s = s.trim();
                            BigDecimal bigDecimal = new BigDecimal(s);
                            s = bigDecimal.toPlainString();
                        }
                        field.set(o, s);
                    } else {
                        field.set(o, cell.getNumericCellValue());
                    }
                }
                break;
            case Cell.CELL_TYPE_STRING:
                if (Date.class.getName().equals(field.getType().getName())) {
                    field.set(o, format.parse(cell.getRichStringCellValue().getString()));
                } else {
                    field.set(o, cell.getRichStringCellValue().getString());
                }
                break;
            default:
                field.set(o, cell.getStringCellValue());
                break;
        }
    }
}
