package com.nemo.laptrinhweb.control;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import com.nemo.laptrinhweb.dao.DAO;

import com.nemo.laptrinhweb.entity.Account;
import com.nemo.laptrinhweb.entity.Email;
import com.nemo.laptrinhweb.entity.EmailUtils;

/**
 * Servlet implementation class ForgotPasswordControl
 */
@WebServlet(name = "ForgotPasswordControl", urlPatterns = {"/forgotPassword"})
public class ForgotPasswordControl extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("forgot-password.jsp").forward(request, response);
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
     * response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // TODO Auto-generated method stub
        response.setContentType("text/html;charset=UTF-8");

        try {
            String emailAddress = request.getParameter("email");
            String username = request.getParameter("username");

            DAO dao = new DAO();
            Account account = dao.checkAccountExistByUsernameAndEmail(username, emailAddress);
            if (account == null) {
                request.setAttribute("error", "Email hoac username sai!");
            }
            if (account != null) {
                Email email = new Email();
                email.setFrom("nguyenthaiminh2201@gmail.com"); // Địa chỉ email gửi
                email.setFromPassword("opqnhhczpomhnyie"); // Mật khẩu email gửi
                email.setTo(emailAddress); // Địa chỉ email nhận
                email.setSubject("Forgot Password Function");
                StringBuilder sb = new StringBuilder();
                sb.append("Dear ").append(username).append("<br>");
                sb.append("You are used the forgot password. <br> ");
                sb.append("Your password is <b>").append(account.getPass()).append(" </b> <br>");
                sb.append("Regards<br>");
                sb.append("Administrator");

                email.setContent(sb.toString());
                EmailUtils.sendEmail(email);

                request.setAttribute("mess", "Mat khau da duoc gui den email cua ban!");

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        request.getRequestDispatcher("forgot-password.jsp").forward(request, response);
    }

}
