package com.pjcdarker.util.csv;


import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;

import java.io.*;
import java.util.regex.Pattern;

/**
 * @author pjcdarker
 * @Created 2017-05-06.
 * @see http://www.novixys.com/blog/convert-csv-json-java
 */
public class Convert {

    public static void toJson(String csvFile, String jsonFile) {
        JsonFactory fac = new JsonFactory();
        try (BufferedReader in = new BufferedReader(new FileReader(csvFile));
             JsonGenerator gen = fac.createGenerator(new File(jsonFile),
                     JsonEncoding.UTF8)
                     .useDefaultPrettyPrinter()) {

            Pattern pattern = Pattern.compile(",");
            String[] headers = pattern.split(in.readLine());
            gen.writeStartArray();
            String line;
            while ((line = in.readLine()) != null) {
                gen.writeStartObject();
                String[] values = pattern.split(line);
                for (int i = 0; i < headers.length; i++) {
                    String value = i < values.length ? values[i] : null;
                    gen.writeStringField(headers[i], value);
                }
                gen.writeEndObject();
            }
            gen.writeEndArray();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
