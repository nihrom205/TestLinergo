package dbService.model;

/**
 * Клас пользователя.
 *
 * @author Alexey Rastorguev (rastorguev00@gmail.com)
 * @version 0.1
 * @since 03.05.2018
 */
public class User {
    private long id;
    private String login;
    private String pass;
    private String session;
    private String template;
    private long meteo_station_id;

    public User(long id, String login, String pass, String session, String template, long meteo_station_id) {
        this.id = id;
        this.login = login;
        this.pass = pass;
        this.session = session;
        this.template = template;
        this.meteo_station_id = meteo_station_id;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getSession() {
        return session;
    }

    public void setSession(String session) {
        this.session = session;
    }

    public String getTemplate() {
        return template;
    }

    public void setTemplate(String template) {
        this.template = template;
    }

    public long getMeteo_station_id() {
        return meteo_station_id;
    }

    public void setMeteo_station_id(long meteo_station_id) {
        this.meteo_station_id = meteo_station_id;
    }
}
