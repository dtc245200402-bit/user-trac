package com.codegym.controller;

import com.codegym.dao.UserDAO;
import com.codegym.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(name = "UserServlet", urlPatterns = "/users")
public class UserServlet extends HttpServlet {
    private UserDAO dao;

    @Override
    public void init() {
        dao = new UserDAO();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String action = req.getParameter("action");

        try {
            if ("create".equals(action)) {
                show(req, resp, "user/create.jsp", null);
            } else if ("edit".equals(action)) {
                show(req, resp, "user/edit.jsp", dao.selectUser(Integer.parseInt(req.getParameter("id"))));
            } else if ("delete".equals(action)) {
                show(req, resp, "user/delete.jsp", dao.selectUser(Integer.parseInt(req.getParameter("id"))));
            } else if ("search".equals(action)) {
                searchByCountry(req, resp);
            } else if ("sort".equals(action)) {
                req.setAttribute("listUser", dao.sortByName());
                req.setAttribute("sortMode", true);
                req.getRequestDispatcher("user/list.jsp").forward(req, resp);
            } else if ("test-without-tran".equals(action)) {
                testWithoutTran(req, resp);
            } else if ("test-use-tran".equals(action)) {
                testUseTran(req, resp);
            } else if ("test-add-user-transaction".equals(action)) {
                testAddUserTransaction(req, resp);
            } else {
                list(req, resp);
            }
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String action = req.getParameter("action");

        try {
            if ("create".equals(action)) {
                dao.insertUser(new User(req.getParameter("name"), req.getParameter("email"), req.getParameter("country")));
            } else if ("edit".equals(action)) {
                dao.updateUserByProcedure(new User(Integer.parseInt(req.getParameter("id")), req.getParameter("name"),
                        req.getParameter("email"), req.getParameter("country")));
            } else if ("delete".equals(action)) {
                dao.deleteUserByProcedure(Integer.parseInt(req.getParameter("id")));
            }
            resp.sendRedirect(req.getContextPath() + "/users");
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    private void testWithoutTran(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        dao.insertUpdateWithoutTransaction();
        System.out.println("Đã chạy xong hàm testWithoutTran. Hãy kiểm tra database!");
        req.setAttribute("transactionTest", true);
        req.getRequestDispatcher("user/list.jsp").forward(req, resp);
    }

    private void testUseTran(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        dao.insertUpdateUseTransaction();
        System.out.println("Hoàn tất gọi hàm testUseTran!");
        req.setAttribute("transactionTest", true);
        req.getRequestDispatcher("user/list.jsp").forward(req, resp);
    }

    private void testAddUserTransaction(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        try {
            dao.addUserTransaction();
            req.setAttribute("transactionMessage", "Transaction thành công: dữ liệu đã được commit.");
        } catch (Exception e) {
            req.setAttribute("transactionMessage", "Transaction thất bại: dữ liệu đã được rollback. Kiểm tra Console để xem lỗi SQL.");
        }
        req.setAttribute("listUser", dao.selectAllUsersByProcedure());
        req.getRequestDispatcher("user/list.jsp").forward(req, resp);
    }

    private void list(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        req.setAttribute("listUser", dao.selectAllUsersByProcedure());
        req.getRequestDispatcher("user/list.jsp").forward(req, resp);
    }

    private void searchByCountry(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        String country = req.getParameter("country");
        req.setAttribute("searchCountry", country == null ? "" : country);
        req.setAttribute("listUser", dao.searchByCountry(country));
        req.getRequestDispatcher("user/list.jsp").forward(req, resp);
    }

    private void show(HttpServletRequest req, HttpServletResponse resp, String page, User user)
            throws Exception {
        if (user != null) {
            req.setAttribute("user", user);
        }
        req.getRequestDispatcher(page).forward(req, resp);
    }
}
