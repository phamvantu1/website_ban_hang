package com.nemo.laptrinhweb.control;

import java.io.IOException;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import com.nemo.laptrinhweb.dao.DAO;

import com.nemo.laptrinhweb.entity.Account;
import com.nemo.laptrinhweb.entity.Email;
import com.nemo.laptrinhweb.entity.EmailUtils;
import com.nemo.laptrinhweb.entity.Cart;
import com.nemo.laptrinhweb.entity.Product;
import com.nemo.laptrinhweb.entity.SoLuongDaBan;
import com.nemo.laptrinhweb.entity.TongChiTieuBanHang;

/**
 * Servlet implementation class ForgotPasswordControl
 */
@WebServlet(name = "OrderControl", urlPatterns = {"/order"})
public class OrderControl extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        Account a = (Account) session.getAttribute("acc");
        if (a == null) {
            response.sendRedirect("login");
            return;
        }
        int accountID = a.getId();
        DAO dao = new DAO();
        List<Cart> list = dao.getCartByAccountID(accountID);
        List<Product> list2 = dao.getAllProduct();
        double totalMoney = 0;
        for (Cart c : list) {
            for (Product p : list2) {
                if (c.getProductID() == p.getId()) {
                    totalMoney = totalMoney + (p.getPrice() * c.getAmount());
                }
            }
        }
        double totalMoneyVAT = totalMoney + totalMoney * 0.1;

        double tongTienBanHangThem = 0;
        int sell_ID;
        for (Cart c : list) {
            for (Product p : list2) {
                if (c.getProductID() == p.getId()) {
                    tongTienBanHangThem = 0;
                    sell_ID = dao.getSellIDByProductID(p.getId());
                    tongTienBanHangThem = tongTienBanHangThem + (p.getPrice() * c.getAmount());
                    TongChiTieuBanHang t2 = dao.checkTongChiTieuBanHangExist(sell_ID);
                    if (t2 == null) {
                        dao.insertTongChiTieuBanHang(sell_ID, 0, tongTienBanHangThem);
                    } else {
                        dao.editTongBanHang(sell_ID, tongTienBanHangThem);
                    }
                }
            }
        }

        for (Cart c : list) {
            for (Product p : list2) {
                if (c.getProductID() == p.getId()) {
                    SoLuongDaBan s = dao.checkSoLuongDaBanExist(p.getId());
                    if (s == null) {
                        dao.insertSoLuongDaBan(p.getId(), c.getAmount());
                    } else {
                        dao.editSoLuongDaBan(p.getId(), c.getAmount());
                    }
                }
            }
        }

        dao.insertInvoice(accountID, totalMoneyVAT);
        TongChiTieuBanHang t = dao.checkTongChiTieuBanHangExist(accountID);
        if (t == null) {
            dao.insertTongChiTieuBanHang(accountID, totalMoneyVAT, 0);
        } else {
            dao.editTongChiTieu(accountID, totalMoneyVAT);
        }

        request.getRequestDispatcher("DatHang.jsp").forward(request, response);
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
     * response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            String emailAddress = request.getParameter("email");
            String name = request.getParameter("name");
            String phoneNumber = request.getParameter("phoneNumber");
            String deliveryAddress = request.getParameter("deliveryAddress");

            HttpSession session = request.getSession();
            Account a = (Account) session.getAttribute("acc");
            if (a == null) {
                response.sendRedirect("login");
                return;
            }
            int accountID = a.getId();
            DAO dao = new DAO();
            List<Cart> list = dao.getCartByAccountID(accountID);
            List<Product> list2 = dao.getAllProduct();

            double totalMoney = 0;
            for (Cart c : list) {
                for (Product p : list2) {
                    if (c.getProductID() == p.getId()) {
                        totalMoney = totalMoney + (p.getPrice() * c.getAmount());
                    }
                }
            }
            double totalMoneyVAT = totalMoney + totalMoney * 0.1;

            // Gửi email
            //Mail password: opqn hhcz pomh nyie
            Email email = new Email();
            email.setFrom("nguyenthaiminh2201@gmail.com"); // Địa chỉ email gửi
            email.setFromPassword("opqnhhczpomhnyie"); // Mật khẩu email gửi
            email.setTo(emailAddress); // Địa chỉ email nhận
            email.setSubject("Đặt hàng thành công từ Fashion Family"); // Tiêu đề email
            StringBuilder sb = new StringBuilder();
            sb.append("Dear ").append(name).append("<br>");
            sb.append("This is your order from Fashion Family. <br> ");
            sb.append("Your delivery address: <b>").append(deliveryAddress).append(" </b> <br>");
            sb.append("Your phone number: <b>").append(phoneNumber).append(" </b> <br>");
            sb.append("Your orders: <br>");
            for (Cart c : list) {
                for (Product p : list2) {
                    if (c.getProductID() == p.getId()) {
                        sb.append(p.getName()).append(" | ").append("Price:").append(p.getPrice()).append("$").append(" | ").append("Quantity:").append(c.getAmount()).append(" | ").append("Size:").append(c.getSize()).append("<br>");
                    }
                }
            }
            sb.append("Total: ").append(String.format("%.02f", totalMoneyVAT)).append("$").append("<br>");
            sb.append("Thank you for your order at Fashion Family<br>");
            sb.append("Your sincere.");

            email.setContent(sb.toString());
            EmailUtils.sendEmail(email);
            System.out.println(email.getContent());
            request.setAttribute("mess", "Đặt hàng thành công!");
            dao.deleteCartByAccountID(accountID);
        } catch (Exception e) {
            request.setAttribute("error", "Đặt hàng thất bại!");
            e.printStackTrace();
        }

        request.getRequestDispatcher("DatHang.jsp").forward(request, response);
    }

}
