package servlets;

import dbService.model.DaoMeteo;
import dbService.view.UseDao;
import templates.PageGenerator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Class <Name class>.
 *
 * @author Alexey Rastorguev (rastorguev00@gmail.com)
 * @version 0.1
 * @since 01.05.2018
 */
public class ServletEdit extends HttpServlet {
    private DaoMeteo daoMeteo = DaoMeteo.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html;charset=utf-8");
        String id = req.getParameter("id");
        String strDate = req.getParameter("date");
//        req.getRequestDispatcher("/").forward(req, resp);
//        resp.sendRedirect("/");

//        req.getRequestDispatcher("/");
        resp.getWriter().print(PageGenerator.instance().getPage("edit.html", new UseDao().viewMeteo(daoMeteo.getId(id, strDate))));
        resp.setStatus(HttpServletResponse.SC_OK);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        super.doPost(req, resp);
        System.out.println();
    }
}
