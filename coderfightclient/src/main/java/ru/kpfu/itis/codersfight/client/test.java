/**
 * @author Gilyazov Marat
 * 11-905
 */

package ru.kpfu.itis.codersfight.client;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Arrays;

public class test {
    public static void main(String[] args) throws IOException {
        int[] gameMap=new int[]{
                0,0,0
        };

        final StringWriter sw = new StringWriter();
        final ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(sw, gameMap);

        System.out.println(sw.toString());
        Integer[] a = mapper.readValue(sw.toString(), Integer[].class);
        System.out.println(Arrays.toString(a));
    }
}
