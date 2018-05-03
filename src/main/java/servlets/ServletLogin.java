package servlets;

import dbService.model.DaoMeteo;
import dbService.model.MeteoUnit;
import dbService.model.User;
import dbService.view.UseDao;
import templates.PageGenerator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Class <Name class>.
 *
 * @author Alexey Rastorguev (rastorguev00@gmail.com)
 * @version 0.1
 * @since 01.05.2018
 */
public class ServletLogin extends HttpServlet {
    private DaoMeteo daoMeteo = DaoMeteo.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html;charset=utf-8");
        resp.getWriter().print(PageGenerator.instance().getPage("login.html", new HashMap<>()));
        resp.setStatus(HttpServletResponse.SC_OK);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html;charset=utf-8");
        String login = req.getParameter("login");
        String pass = req.getParameter("pass");
        Map<String, User> map =  new UseDao().viewUser(daoMeteo.getLoginAndPass(login, pass));
        if (map.isEmpty()) {
            resp.getWriter().print(PageGenerator.instance().getPage("ErrorLogin.html", new HashMap<>()));
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        } else {
            User user = map.get("account");
            if (pass.equals(user.getPass())) {
                resp.getWriter().print(PageGenerator.instance().getPage("page.html", new UseDao().viewMeteo(daoMeteo.getIdMonth(user.getMeteo_station_id() + "",""))));
                resp.setStatus(HttpServletResponse.SC_OK);
            }

        }




    }
}
