package dbService.model;

import com.mysql.cj.api.jdbc.Statement;
import com.mysql.cj.jdbc.Driver;

import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * Класс взаимодействия с БД.
 *
 * @author Alexey Rastorguev (rastorguev00@gmail.com)
 * @version 0.1
 * @since 29.04.2018
 */
public class DaoMeteo {

    private static DaoMeteo daoMeteo = null;
    private Connection connection = null;

    private DaoMeteo() {
        this.connection = getConnectMySQL();
    }

    /**
     * Синглтон
     * @return instance daoMeteo
     */
    public static DaoMeteo getInstance() {
        if (daoMeteo == null) {
            daoMeteo = new DaoMeteo();
        }
        return daoMeteo;
    }



    /**
     * метод соединения с базой и получениея конекта
     * @return соединение с БД
     */
    private Connection getConnectMySQL() {
        if (connection != null) {
            return connection;
        }

        Driver driver = null;
        Connection connection = null;
        Properties properties = new Properties();

        try (FileInputStream fis = new FileInputStream("src/main/resources/config.properties")){
            driver = new com.mysql.cj.jdbc.Driver();
            DriverManager.registerDriver(driver);
            properties.load(fis);

            StringBuilder URL = new StringBuilder();
            URL.append(properties.getProperty("db.type")).append("://").
                    append(properties.getProperty("db.host")).
                    append(":").
                    append(properties.getProperty("db.port")).append("/").
                    append(properties.getProperty("db.base"));

            String config = properties.getProperty("db.config");
            if (config != null) {
                URL.append("?").append(config);
            }
            String USER = properties.getProperty("db.login");
            String PASS = properties.getProperty("db.password");
            connection = DriverManager.getConnection(URL.toString(), USER, PASS);
            if (!connection.isClosed()) {
                System.out.println("Соединение с БД установлено");
            }
            return connection;
        } catch (Exception e) {
            System.out.println("Драйвер не зарегистрировался");
            return null;
        }
    }

    /**
     * Метод возвращает все данные из БД.
     * @return ResultSet
     */
    public ResultSet getAllToDate() {

        Statement statement;
        String selectTable = "SELECT * from MeteoData";
        ResultSet rs = null;

        try {
            statement = (Statement) connection.createStatement();
            rs = statement.executeQuery(selectTable);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rs;
    }

    /**
     * Метод обращается к БД запрашивая по id и месяцу и возвращате результат id за весь месяц.
     * @param id идентификационный номер метеостанции
     * @param strDate дата
     * @return ResultSet
     */
    public ResultSet getIdMonth(String id, String strDate) {
        ResultSet rs = null;
        Statement statement;
        Calendar calendar = null;
        int year;
        int month;
        int countDay;
        String idQuery = "";

        if (id.equals("")) {
            idQuery = "select * from MeteoData where ";
        } else {
            idQuery = "select * from MeteoData where meteo_station_id = " + id +  " AND ";
        }

        if (strDate.equals("")) {
            calendar = new GregorianCalendar();
            year = calendar.get(Calendar.YEAR);
            month = calendar.get(Calendar.MONTH) + 1;
        } else {
            String[] dateStr = strDate.split("-");
            year = Integer.valueOf(dateStr[0]);
            month = Integer.valueOf(dateStr[1]);
            calendar = new GregorianCalendar(year, month, 0);
        }

        countDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);    // возвращает кол-во денй в месяце
        StringBuilder sqlQuery = new StringBuilder();
        sqlQuery.append(idQuery).
                append("DATE(read_timestamp) >= '").
                append(year).
                append("-").
                append(month).
                append("-").
                append("01' AND DATE(read_timestamp) <= '").
                append(year).
                append("-").
                append(month).
                append("-").
                append(countDay).
                append("'");

        try {
            statement = (Statement) connection.createStatement();
            rs = statement.executeQuery(sqlQuery.toString());
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return rs;
    }

    /**
     * метод удаляет 1 строку из БД по id на определенную дату.
     * @param id идентификатор метеостанции
     * @param date дата
     */
    public void deleteUnit(String id, String date) {
        Statement statement;
        StringBuilder strSqlQuery = new StringBuilder();
        strSqlQuery.append("delete from MeteoData where meteo_station_id = ").
                append(id).
                append(" AND UNIX_TIMESTAMP(read_timestamp) >= UNIX_TIMESTAMP('").
                append(date).
                append("')");

        try {
            statement = (Statement) connection.createStatement();
            statement.execute(strSqlQuery.toString());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Обновление информации в БД по id и дате.
     * @param id идентификатор метеостанции
     * @param date дата
     */
    public void updateUnit(String id, String date, String strTemper, String strPress, String strWind_dir, String strWind_speed) {
        Statement statement;

        StringBuilder strSqlQuery = new StringBuilder();
        strSqlQuery.append("update MeteoData set meteo_station_id = ").
                append(id).append(", read_timestamp = '").
                append(date).append("', temperature = ").
                append(strTemper).append(", pressure = ").
                append(strPress).append(", wind_direction = ").
                append(strWind_dir).append(", wind_speed = ").
                append(strWind_speed).append(" where meteo_station_id = ").
                append(id).append(" AND unix_timestamp(read_timestamp) = unix_timestamp('").
                append(date).append("')");

        try {
            statement = (Statement) connection.createStatement();
            statement.execute(strSqlQuery.toString());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Метод возвращает 1 значение из бады по id b по дате
     * @param id идентификатор метеостанции
     * @param strDate дата
     * @return результат
     */
    public ResultSet getId(String id, String strDate) {
        ResultSet rs = null;
        Statement statement;

        StringBuilder sqlQuery = new StringBuilder();
        sqlQuery.append("select * from MeteoData where meteo_station_id =").
                append(id).
                append(" AND UNIX_TIMESTAMP(read_timestamp) >= UNIX_TIMESTAMP('").
                append(strDate).
                append("')");

        try {
            statement = (Statement) connection.createStatement();
            rs = statement.executeQuery(sqlQuery.toString());
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return rs;
    }

    /**
     * Метод запрашивает из БД логин и пароль ползователя
     * @param login логин
     * @param pass пароль
     * @return ResultSet
     */
    public ResultSet getLoginAndPass(String login, String pass) {
        ResultSet rs = null;
        Statement statement;

        StringBuilder sqlQuery = new StringBuilder();
        sqlQuery.append("select * from Users where login = \"").append(login).append("\"");
//        sqlQuery.append("select * from Users where login = \"UserEkb\"");

        try {
            statement = (Statement) connection.createStatement();
            rs = statement.executeQuery(sqlQuery.toString());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rs;
    }
}
