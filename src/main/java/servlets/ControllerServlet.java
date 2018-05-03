package servlets;

import dbService.model.DaoMeteo;
import dbService.view.UseDao;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.json.simple.JSONObject;
import templates.PageGenerator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Class <Name class>.
 *
 * @author Alexey Rastorguev (rastorguev00@gmail.com)
 * @version 0.1
 * @since 28.04.2018
 */
public class ControllerServlet extends HttpServlet {

    private DaoMeteo daoMeteo = DaoMeteo.getInstance();

    /**
     * Метод doGet выводи на экран все данные.
     * @param req http запрос
     * @param resp http ответ
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html;charset=utf-8");
        resp.getWriter().print(PageGenerator.instance().getPage("page.html", new UseDao().viewMeteo(daoMeteo.getIdMonth("",""))));
        resp.setStatus(HttpServletResponse.SC_OK);
    }

    /**
     * метод обработки doPost.
     * обрабатывет запросы отправление из формы.
     * выводит на экран, передат ecxel файл и json-файл
     * @param req http запрос
     * @param resp http ответ
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String id = req.getParameter("idMeteo");
        String dateStr = req.getParameter("calendar");
        String show = req.getParameter("show");
        String downlod_excel = req.getParameter("downlod_excel");
        String exit = req.getParameter("exit");

        if (exit != null) {
            resp.setContentType("text/html;charset=utf-8");
            daoMeteo.setSessionMeteoId(id);
            resp.sendRedirect("/");
            resp.setStatus(HttpServletResponse.SC_OK);
            return;
        }

        if (show != null) {
            resp.setContentType("text/html;charset=utf-8");
            resp.getWriter().print(PageGenerator.instance().getPage("page.html", new UseDao().viewMeteo(daoMeteo.getIdMonth(id, dateStr))));
            resp.setStatus(HttpServletResponse.SC_OK);
        } else if (downlod_excel != null){
            StringBuilder strHandler = new StringBuilder();
            strHandler.append("attachment; filename=file-").append(id).append("-").append(dateStr).append(".xls");

            HSSFWorkbook excelFile = new UseDao().createExcel(daoMeteo.getIdMonth(id, dateStr));

            // создание файла excel и передача его в сервлет
            ByteArrayOutputStream outByteStream = new ByteArrayOutputStream();
            excelFile.write(outByteStream);
            byte[] outArray = outByteStream.toByteArray();

            resp.setContentType("application/ms-excel");
            resp.setContentLength(outArray.length);
            resp.setHeader("Content-Disposition", strHandler.toString());
            OutputStream outputStream = resp.getOutputStream();
            outputStream.write(outArray);
            outputStream.flush();
            outputStream.close();

        } else {
            resp.setContentType("application/json");
            resp.addHeader("Content-Disposition", "attachment; filename=file.json");
            //создание json
            JSONObject  jsonObject = new UseDao().createJson(daoMeteo.getIdMonth(id, dateStr));
            // создание файла json и передача в сервлет
            OutputStream outputStream = resp.getOutputStream();
            outputStream.write(jsonObject.toString().getBytes());
            outputStream.flush();
            outputStream.close();

        }
    }
}
