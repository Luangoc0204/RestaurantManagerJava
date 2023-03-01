package com.example.qldb.controller;


import com.example.qldb.entity.Account;
import com.example.qldb.entity.KhachHang;
import com.example.qldb.entity.NhanVien;
import com.example.qldb.entity.QuanLy;
import com.example.qldb.repository.AccountRepository;
import com.example.qldb.repository.KhachHangRepository;
import com.example.qldb.repository.NhanVienRepository;
import com.example.qldb.repository.QuanLyRepository;
import com.example.qldb.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.Optional;

@Controller
public class MainController {
    public static com.example.qldb.entity.User loginUser;
    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private UserService userService;

    @Autowired private QuanLyRepository quanLyRepository;

    @Autowired private NhanVienRepository nhanVienRepository;

    @Autowired
    private KhachHangRepository khachHangRepository;

    @RequestMapping(value = "/")
    public String welcomePage(Model model){
        return "homePage";
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String loginPage(Model model) {
        return "dangnhap";
    }

    @RequestMapping(value = "/logoutSuccessful")
    public String logout(){
        return "homePage";
    }

    @RequestMapping(value = "/giaodien", method = RequestMethod.GET)
    public String giaodien(Model model, Principal principal){
        String username = principal.getName();
        System.out.println("User name: " + username);
        User loginedUser = (User) ((Authentication)principal).getPrincipal();
        Account account = accountRepository.findAccountByUsername(username);
        loginUser = account.getUser();
        model.addAttribute("user", loginUser);
        if (loginUser.getChucVu().equals("Quản lý")){
            QuanLy quanLy = quanLyRepository.findQuanLyByUser(loginUser);
            model.addAttribute("quanLy", quanLy);
            return "giaodienQl";
        } else if (loginUser.getChucVu().equals("Nhân viên")){
            NhanVien nhanVien = nhanVienRepository.findNhanVienByUser(loginUser);
            model.addAttribute("nhanVien", nhanVien);
            return "giaodienNV";
        } else {
            KhachHang khachHang = khachHangRepository.findKhachHangByUser(loginUser);
            model.addAttribute("khachHang", khachHang);
            return "giaodienKH";
        }
    }

    @RequestMapping(value = "/dangky", method = RequestMethod.GET)
    public String pageDangKy(Model model){
        com.example.qldb.entity.User user = new com.example.qldb.entity.User();
        user.setChucVu("Khách hàng");
        model.addAttribute("user", user);
        return "dangky";
    }

    @RequestMapping(value = "saveDangKyUser", method = RequestMethod.POST)
    public String saveDangKyUser(com.example.qldb.entity.User user, BindingResult result, RedirectAttributes redirect){
        if (result.hasErrors()) {
            System.out.println("Đăng ký không thành công");
            redirect.addFlashAttribute("successDangKy", "Create user faild!");
            return "redirect:/login";
        }
        if (user.getHoTen().equals("")){
            redirect.addFlashAttribute("successDangKy", "Không được để trống họ tên!");
            return "redirect:/dangky";
        }
        if (user.getSdt().equals("")){
            redirect.addFlashAttribute("successDangKy", "Không được để trống SĐT!");
            return "redirect:/dangky";
        }
        if (user.getSdt().length() != 10){
            redirect.addFlashAttribute("successDangKy", "SĐT phải có 10 chữ số!");
            return "redirect:/dangky";
        }
        if (user.getNgaySinh().toString().equals("")){
            redirect.addFlashAttribute("successDangKy", "Không được để trống ngày sinh!");
            return "redirect:/dangky";
        }
        Account account = accountRepository.findAccountByUsername(user.getSdt());
        if (account != null){
            redirect.addFlashAttribute("successDangKy", "SĐT đã được sử dụng!");
            return "redirect:/dangky";
        }
        //lưu user
        userService.saveUser(user);
        //Lưu account
        Account account_save = new Account(null, user);
        accountRepository.save(account_save);
        //Lưu khách hàng
        KhachHang khachHang = new KhachHang(null, user, 0,0);
        khachHangRepository.save(khachHang);
        redirect.addFlashAttribute("successDangKy", "Create user successfully!");
        return "redirect:/login";
    }

//    CHỈNH SỬA THÔNG TIN NGƯỜI DÙNG
    @RequestMapping(value = "/editUser")
    public String  editUser(@RequestParam("idUser") Integer idUser, Model model){
        Optional<com.example.qldb.entity.User> user = userService.findUserById(idUser);
        System.out.println("User: " + user.toString());
        if (user.isPresent()){
            model.addAttribute("user", user);
        }
        return "editUser";
    }
    @RequestMapping(value = "editUser_save", method = RequestMethod.POST)
    public String editUser_save(com.example.qldb.entity.User user, BindingResult result, RedirectAttributes redirect){
        if (result.hasErrors()) {
            return "redirect:/giaodien";
        }
        if (user.getHoTen().equals("")){
            redirect.addFlashAttribute("successDangKy", "Không được để trống họ tên!");
            return "redirect:/editUser?idUser=" + user.getIdUser();
        }
        if (user.getSdt().equals("")){
            redirect.addFlashAttribute("successDangKy", "Không được để trống SĐT!");
            return "redirect:/editUser?idUser=" + user.getIdUser();
        }
        if (user.getSdt().length() != 10){
            redirect.addFlashAttribute("successDangKy", "SĐT phải có 10 chữ số!");
            return "redirect:/editUser?idUser=" + user.getIdUser();
        }
        if (user.getNgaySinh().toString().equals("")){
            redirect.addFlashAttribute("successDangKy", "Không được để trống ngày sinh!");
            return "redirect:/editUser?idUser=" + user.getIdUser();
        }
        Account account = accountRepository.findAccountByUsername(user.getSdt());
        if (account != null){
            redirect.addFlashAttribute("successDangKy", "SĐT đã được sử dụng!");
            return "redirect:/editUser?idUser=" + user.getIdUser();
        }
        userService.saveUser(user);
        Account account_save = accountRepository.findAccountByUser(user);
        if (account_save.getUsername().equals("admin")){
            redirect.addFlashAttribute("messageChange", "Cập nhật thông tin thành công!");
            return "redirect:/giaodien";
        } else {
            account_save.setUsername(user.getSdt());
            accountRepository.save(account_save);
            redirect.addFlashAttribute("messageChange", "Cập nhật thông tin thành công!" + "\n" + "Username của bạn đã được đổi thành sđt hiện tại!");
            return "redirect:/giaodien";

        }
    }

    @RequestMapping(value = "/changePassword")
    public String changePassword(@RequestParam("idUser") Integer idUser, Model model){
        Optional<com.example.qldb.entity.User> user_temp = userService.findUserById(idUser);
        com.example.qldb.entity.User user = user_temp.get();
        Account account_check = new Account();
        account_check.setIdAccount(1);
        account_check.setUser(user);
        System.out.println("Account gửi đi: " + account_check.toString());

        model.addAttribute("user", user.getIdUser());
        model.addAttribute("account", account_check);

        return "changePassword";
    }
    @RequestMapping(value = "/changePassword_save", method = RequestMethod.POST)
    public String changePassword_save(@RequestParam(value = "idUser", required = false) Integer idUser, Account account, RedirectAttributes redirect){
        System.out.println(account.toString());
        Optional<com.example.qldb.entity.User> user = userService.findUserById(idUser);
        Account account_check = accountRepository.findAccountByUser(user.get());
        System.out.println("Account nhận: " + account.toString());
        System.out.println("Account check save: " + account_check.toString());
        if (account_check.getOldpass().equals(account.getOldpass())){
            account_check.setOldpass(account.getPassword());
            String new_pass = account.changePasswordToEncriptPassword(account.getPassword());
            account_check.setPassword(new_pass);
            accountRepository.save(account_check);
            redirect.addFlashAttribute("messageChange", "Đổi mật khẩu thành công!");
            System.out.println("Đổi password thành công");
            return "redirect:/giaodien";
        } else {
            redirect.addFlashAttribute("messageChangePassword", "Bạn đã nhập sai mật khẩu!!!");
            System.out.println("Đổi password không thành công");
            return "redirect:/changePassword?idUser=" + idUser;
        }
    }
}
