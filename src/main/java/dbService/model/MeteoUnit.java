package dbService.model;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * модель одной записи метеостанции.
 *
 * @author Alexey Rastorguev (rastorguev00@gmail.com)
 * @version 0.1
 * @since 29.04.2018
 */
public class MeteoUnit {
    private long meteo_station_id;
    private Date date;
    private Double temper;
    private Integer pressure;
    private Integer wind_direction;
    private Integer wind_speed;

    public MeteoUnit(long meteo_station_id, Date date, Double temper, Integer pressure, Integer wind_direction, Integer wind_speed) {
        this.meteo_station_id = meteo_station_id;
        this.date = date;
        this.temper = temper;
        this.pressure = pressure;
        this.wind_direction = wind_direction;
        this.wind_speed = wind_speed;
    }

    public long getMeteo_station_id() {
        return meteo_station_id;
    }

    public Date getDate() {
        return date;
    }

    public Double getTemper() {
        return temper;
    }


    public Integer getPressure() {
        return pressure;
    }


    public Integer getWind_direction() {
        return wind_direction;
    }

    public Integer getWind_speed() {
        return wind_speed;
    }

    public void setMeteo_station_id(long meteo_station_id) {
        this.meteo_station_id = meteo_station_id;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setTemper(Double temper) {
        this.temper = temper;
    }

    public void setPressure(Integer pressure) {
        this.pressure = pressure;
    }

    public void setWind_direction(Integer wind_direction) {
        this.wind_direction = wind_direction;
    }

    public void setWind_speed(Integer wind_speed) {
        this.wind_speed = wind_speed;
    }

    /**
     * метод создает дату
     * @return дата (String)
     */
    public String toStringData() {
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        return new StringBuilder().
                append(calendar.get(Calendar.YEAR)).append("-").
                append(calendar.get(Calendar.MONTH) + 1).append("-").
                append(calendar.get(Calendar.DATE)).append(" ").
                append(calendar.get(Calendar.HOUR_OF_DAY)).append(":").
                append(calendar.get(Calendar.MINUTE)).append(":").
                append(calendar.get(Calendar.SECOND)).toString();
    }
}
