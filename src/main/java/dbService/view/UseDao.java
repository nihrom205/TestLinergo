package dbService.view;

import dbService.model.MeteoUnit;
import dbService.model.User;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.json.simple.JSONObject;

import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Класс преобразует данные из БД HashMap, Excel, Json.
 *
 * @author Alexey Rastorguev (rastorguev00@gmail.com)
 * @version 0.1
 * @since 29.04.2018
 */
public class UseDao {
    private Map<String, MeteoUnit> storageMeteo = new HashMap<>();

    /**
     * Метод сохраняет в storageMeteo результаты ResultSet
     * @param rs ResultSet данные из БД
     * @return map
     */
    public Map viewMeteo(ResultSet rs) {
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long count = 0;
        try {
            while (rs.next()) {
                Long meteo_station_id = Long.valueOf(rs.getString("meteo_station_id"));
                Date timestamp = format.parse(rs.getString("read_timestamp"));
                Double temperature = Double.valueOf(rs.getString("temperature"));
                Integer pressure = Integer.valueOf(rs.getString("pressure"));
                Integer wind_direction = Integer.valueOf(rs.getString("wind_direction"));
                Integer wind_speed = Integer.valueOf(rs.getString("wind_speed"));

                storageMeteo.put("" + count++, new MeteoUnit( meteo_station_id, timestamp, temperature, pressure, wind_direction, wind_speed));
            }
            rs.close();
        }catch(Exception e) {
            e.printStackTrace();
        }

        return storageMeteo;
    }

    public Map viewUser(ResultSet rs) {
        Map<String, User> storeAccount = new HashMap<>();
        long count = 0;
        try {
            while (rs.next()) {
                long id = Long.valueOf(rs.getString("id"));
                String login = rs.getString("login");
                String pass = rs.getString("pass");
                String session = rs.getString("session_usr");
                String template = rs.getString("template");
                long meteo_station_id = Long.valueOf(rs.getString("meteo_station_id"));

                storeAccount.put("account", new User(id, login, pass, session, template, meteo_station_id));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return storeAccount;
    }

    /**
     * Метод создания Excel файла
     * @param rs ResultSet данные из БД
     * @return Объект HSSFWorkbook
     */
    public HSSFWorkbook createExcel(ResultSet rs ) {
        storageMeteo = viewMeteo(rs);
        int rowNum = 0;
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("Лист 1");
        Row row = sheet.createRow(rowNum);
        row.createCell(0).setCellValue("Идентификатор метеостанции");
        row.createCell(1).setCellValue("Дата съёма");
        row.createCell(2).setCellValue("Температура");
        row.createCell(3).setCellValue("Атмосферное давление");
        row.createCell(4).setCellValue("Скорость ветра");
        row.createCell(5).setCellValue("Редактировать");

        for (Map.Entry<String,MeteoUnit> entry : storageMeteo.entrySet()) {
            createSheetHeader(sheet, ++rowNum, entry.getValue());
        }
        return workbook;
    }

    /**
     * метод добаление данных в лист Excel
     * @param sheet лист
     * @param rowNum номер строки
     * @param unit объект класса для добавления в строку
     */
    private static void createSheetHeader(HSSFSheet sheet, int rowNum, MeteoUnit unit) {
        Row row = sheet.createRow(rowNum);
        row.createCell(0).setCellValue(unit.getMeteo_station_id());
        row.createCell(1).setCellValue(unit.toStringData());
        row.createCell(2).setCellValue(unit.getTemper());
        row.createCell(3).setCellValue(unit.getPressure());
        row.createCell(4).setCellValue(unit.getWind_direction());
        row.createCell(5).setCellValue(unit.getWind_speed());
    }

    /**
     * метод создания файла json
     * @param rs ResultSet данные из БД
     * @return json объект
     */
    public JSONObject createJson(ResultSet rs) {
        storageMeteo = viewMeteo(rs);
        JSONObject resultJson = new JSONObject();
        JSONObject valueJson = null;
        for (Map.Entry<String,MeteoUnit> entry : storageMeteo.entrySet()) {
            valueJson = new JSONObject();
            valueJson.put("meteo_station_id", entry.getValue().getMeteo_station_id());
            valueJson.put("read_timestamp", entry.getValue().toStringData());
            valueJson.put("temperature", entry.getValue().getTemper());
            valueJson.put("pressure", entry.getValue().getPressure());
            valueJson.put("wind_direction", entry.getValue().getWind_direction());
            valueJson.put("wind_speed", entry.getValue().getWind_speed());
            resultJson.put(entry.getKey(), valueJson);
        }
        return resultJson;
    }

}
