/**
 * @author Gilyazov Marat
 * 11-905
 */

package ru.kpfu.itis.codersfight.server.service;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.StringWriter;

public class GameService {
    public static Integer[] getGameMap(int positionOnMap) {
        if (positionOnMap == 0) {
            return new Integer[]{
                    1, 0, 0,
                    0, 0, 0,
                    0, 0, 2
            };
        } else {
            return new Integer[]{
                    2, 0, 0,
                    0, 0, 0,
                    0, 0, 1
            };
        }
    }

    public static String gameMapToString(Integer[] gameMap) {
        try {

            final StringWriter sw = new StringWriter();
            final ObjectMapper mapper = new ObjectMapper();
            mapper.writeValue(sw, gameMap);

            System.out.println(sw.toString());
            return sw.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }
}
