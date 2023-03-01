package com.example.qldb.controller;


import com.example.qldb.entity.*;
import com.example.qldb.repository.*;
import com.example.qldb.service.impl.AccountImpl;
import com.example.qldb.service.impl.UserServiceImpl;
import org.dom4j.rule.Mode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.persistence.criteria.CriteriaBuilder;
import java.sql.SQLOutput;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Controller
public class QuanLyController {
    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private AccountImpl accountService;

    @Autowired
    private QuanLyRepository quanLyRepository;
    @Autowired
    private NhanVienRepository nhanVienRepository;
    @Autowired
    private KhachHangRepository khachHangRepository;
    @Autowired
    private DatBanRepository datBanRepository;
    @Autowired private BanAnRepository banAnRepository;
    @Autowired private ChonMonRepository chonMonRepository;
    @Autowired
    private MonAnRepository monAnRepository;
    @Autowired private HoaDonRepository hoaDonRepository;

    //QUẢN LÝ NHÂN VIÊN
    @RequestMapping(value = "/QL_dsNV", method = RequestMethod.GET)
    public String QLNhanVien(Model model) {
        List<NhanVien> nhanVien = (List<NhanVien>) nhanVienRepository.findAll();
        model.addAttribute("dsNhanVien", nhanVien);
        return "QL_dsNV";
    }

    @RequestMapping(value = "searchNV")
    public String searchNV(@RequestParam(name = "hoTen") Optional<String> hoTen, Model model, RedirectAttributes redirect) {
        if (hoTen.isEmpty()) { //nếu search rỗng thì trả về ds NV
            return "redirect:/QL_dsNV";
        } else {

            List<User> users = userService.findUsersByHoTen(hoTen.get());
            List<NhanVien> dsNhanVien = new ArrayList<>();
            if (users == null) {
                dsNhanVien.add(new NhanVien(null, new User(null, "", null, "", "", ""), null, null, null));
                redirect.addFlashAttribute("searchMessage", "Không tìm thấy nhân viên " + hoTen);
                model.addAttribute("dsNhanVien", dsNhanVien);
                return "QL_dsNV";
            }
            for (int i = 0; i < users.size(); i++) {
                NhanVien nhanVien_temp = nhanVienRepository.findNhanVienByUser(users.get(i));
                dsNhanVien.add(nhanVien_temp);
            }
            model.addAttribute("dsNhanVien", dsNhanVien);
            return "QL_dsNV";
        }
    }

    @RequestMapping(value = "/QL_insertNV")
    public String QL_insertNV(Model model) {
        model.addAttribute("nhanVien", new NhanVien());
        return "QL_insertNV";
    }

    @RequestMapping(value = "QL_insertNV_save")
    public String QL_insertNV_save(NhanVien nhanVien, BindingResult result, RedirectAttributes redirect) {
        System.out.println(nhanVien.toString());
        if (result.hasErrors()) {
            System.out.println("Thêm nhân viên không thành công");
            System.out.println("Erorr: " + result);
            redirect.addFlashAttribute("successInsertNV", "Create user faild!");
            return "redirect:/QL_dsNV";
        }
        User user = nhanVien.getUser();
        user.setChucVu("Nhân viên");
        userService.saveUser(user);
        nhanVienRepository.save(nhanVien);
        Account account = new Account(null, user);
        accountService.saveAccount(account);
        System.out.println("Thêm nhân viên thành công");
        redirect.addFlashAttribute("successInsertNV", "Create user successful!");
        return "redirect:/QL_dsNV";
    }

    @RequestMapping(value = "/QL_searchNVById")
    public String Ql_searchNVById(@RequestParam("idNV") Integer idNV, Model model) {
        Optional<NhanVien> nhanVien = nhanVienRepository.findById(idNV);
        if (nhanVien.isPresent()) {
            model.addAttribute("nhanVien", nhanVien.get());
        }
        return "Ql_searchNV";
    }
    @RequestMapping(value = "/QL_editNV")
    public String QL_editNV(@RequestParam("idNV") Integer idNV, Model model){
        Optional<NhanVien> nhanVien = nhanVienRepository.findById(idNV);
        model.addAttribute("nhanVien", nhanVien.get());
        return "QL_editNV";
    }

    @RequestMapping(value = "/QL_editNV_save")
    public String QL_editNV_save(NhanVien nhanVien, BindingResult result, RedirectAttributes redirect){
        if (result.hasErrors()){
            System.out.println("Đã xảy ra lỗi");
        }
        System.out.println("Nhân viên sửa: " + nhanVien.toString());
        userService.saveUser(nhanVien.getUser());
        nhanVienRepository.save(nhanVien);
        System.out.println("Chỉnh sửa nhân viên thành công");
        return "redirect:QL_searchNVById?idNV=" + nhanVien.getIdNhanVien();
    }

    @RequestMapping(value = "QL_deleteNV")
    public String QL_deleteNV(@RequestParam("idNV") Integer idNV){
        Optional<NhanVien> nhanVien = nhanVienRepository.findById(idNV);
        userService.deleteUser(nhanVien.get().getUser().getIdUser());
        return "redirect:/QL_dsNV";
    }

    //QUẢN LÝ KHÁCH HÀNG
    @RequestMapping(value = "/QL_dsKH", method = RequestMethod.GET)
    public String QLKhachHang(Model model) {
        List<KhachHang> khachHang = (List<KhachHang>) khachHangRepository.findAll();
        model.addAttribute("dsKhachHang", khachHang);
        return "QL_dsKH";
    }

    @RequestMapping(value = "QL_searchKH")
    public String searchKH(@RequestParam(name = "hoTen") Optional<String> hoTen, Model model, RedirectAttributes redirect) {
        if (hoTen.isEmpty()) { //nếu search rỗng thì trả về ds KH
            return "redirect:/QL_dsKH";
        } else {
            List<User> users = userService.findUsersByHoTen(hoTen.get());
            List<KhachHang> dsKhachHang = new ArrayList<>();
            if (users == null) {
                redirect.addFlashAttribute("searchMessage", "Không tìm thấy nhân viên " + hoTen);
                model.addAttribute("dsKhachHang", dsKhachHang);
                return "redirect:/QL_dsKH";
            }
            for (int i = 0; i < users.size(); i++) {
                KhachHang khachHang_temp = khachHangRepository.findKhachHangByUser(users.get(i));
                dsKhachHang.add(khachHang_temp);
            }
            model.addAttribute("dsKhachHang", dsKhachHang);
            return "QL_dsKH";
        }
    }

    @RequestMapping(value = "/QL_searchKHById")
    public String Ql_searchKHById(@RequestParam("idKH") Integer idKH, Model model) {
        Optional<KhachHang> khachHang = khachHangRepository.findById(idKH);
        System.out.println("Khách hàng by Id: " + khachHang);
        if (khachHang.isPresent()) {
            model.addAttribute("khachHang", khachHang.get());
        }
        return "Ql_searchKH";
    }

    @RequestMapping(value = "/QL_editKH")
    public String QL_editKH(@RequestParam("idKH") Integer idKH, Model model){
        Optional<KhachHang> khachHang = khachHangRepository.findById(idKH);
        model.addAttribute("user", khachHang.get().getUser());
        model.addAttribute("idKhachHang", idKH);
        return "QL_editKH";
    }

    @RequestMapping(value = "/QL_editKH_save")
    public String QL_editKH_save(@RequestParam("idKH") Integer idKH, User user, BindingResult result){
        if (result.hasErrors()){
            System.out.println("Xảy ra lỗi khi chỉnh sửa KH by Id");
            return "redirect:/QL_searchKHById?idKH=" + idKH;
        }
        userService.saveUser(user);
        System.out.println("Chỉnh sửa User by Id thành công");
        return "redirect:/QL_searchKHById?idKH=" + idKH;
    }

    @RequestMapping(value = "/QL_deleteKH")
    public String QL_deleteKH(@RequestParam("idKH") Integer idKH){
        Optional<KhachHang> khachHang = khachHangRepository.findById(idKH);
        userService.deleteUser(khachHang.get().getUser().getIdUser());
        return "redirect:/QL_dsKH";
    }

    @RequestMapping(value = "QL_dsDatBanKH")
    public String QL_dsDatBanKH(@RequestParam("idKH") Integer idKH, Model model){
        Optional<KhachHang> khachHang = khachHangRepository.findById(idKH);
        if (khachHang.isEmpty()){
            System.out.println("không tìm thấy khách hàng");
        }
        System.out.println("Khách hàng:" + khachHang.toString());
        User user = khachHang.get().getUser();
        List<DatBan> dsDatBan = datBanRepository.findAllByUserOrOrderByNgayDatBan(user);
        System.out.println("List bàn ăn:" + dsDatBan.toString());
        model.addAttribute("khachHang", khachHang.get());
        model.addAttribute("dsDatBan", dsDatBan);
        return "QL_dsDatBanKH";
    }

    @RequestMapping(value = "QL_confirmDatBan")
    public String QL_confirmDatBan(@RequestParam("idDB") Integer idDB){
        Optional<DatBan> datBan = datBanRepository.findById(idDB);
        datBan.get().setCheckin(true);
        datBanRepository.save(datBan.get());
        KhachHang khachHang = khachHangRepository.findKhachHangByUser(datBan.get().getUser());
        int soLanDB = khachHang.getSoLanDB() + 1;
        khachHang.setSoLanDB(soLanDB);
        khachHangRepository.save(khachHang);
        return "redirect:/QL_dsDatBanKH?idKH=" + khachHang.getIdKhachHang();
    }

    @RequestMapping(value = "QL_insertDatBanKH")
    public String QL_insertDatBan(@RequestParam("idKH") Integer idKH, Model model){
        Optional<KhachHang> khachHang = khachHangRepository.findById(idKH);
        List<BanAn> banAnList = banAnRepository.findByActiveTrue();
        //lấy dữ liệu giờ làm việc của nhà hàng từ entity quản lý
        Iterable<QuanLy> listQuanLy = quanLyRepository.findAll();
        QuanLy quanLy = listQuanLy.iterator().next();
        List<String> gioLamViec = new ArrayList<>();
        gioLamViec.add(quanLy.getGioBDSang());
        gioLamViec.add(quanLy.getGioKTSang());
        gioLamViec.add(quanLy.getGioBDToi());
        gioLamViec.add(quanLy.getGioKTToi());
        System.out.println("List giờ làm việc: " + gioLamViec.toString());
        System.out.println("Giờ làm việc trưa: (" + gioLamViec.get(0) + " - " + gioLamViec.get(1) + ")");
        System.out.println("Giờ làm việc tối: (" + gioLamViec.get(2) + " - " + gioLamViec.get(3) + ")");
        List<Integer> loaiBanAn = banAnRepository.findLoaiBanAn();
        loaiBanAn.sort(((o1, o2) -> o1 - o2)); // sắp xếp loại bàn ăn tăng dần

        model.addAttribute("datBan", new DatBan());
        model.addAttribute("dsLoaiBanAn", loaiBanAn);
        model.addAttribute("khachHang", khachHang.get());
        model.addAttribute("gioBDLamViec1", gioLamViec.get(0));
        model.addAttribute("gioKTLamViec1", gioLamViec.get(1));
        model.addAttribute("gioBDLamViec2", gioLamViec.get(2));
        model.addAttribute("gioKTLamViec2", gioLamViec.get(3));

        return "QL_insertDatBanKH";
    }

    @RequestMapping(value = "QL_insertDatBanKH_save")
    public String QL_insertDatBanKH_save(@RequestParam("idKH") Integer idKH, DatBan datBan, RedirectAttributes redirect){
        Optional<KhachHang> khachHang = khachHangRepository.findById(idKH);
        System.out.println("Khách hàng: " + khachHang.toString());
        User user = khachHang.get().getUser();
        System.out.println("User: " + user.toString());
        System.out.println("Đặt bàn: " + datBan.toString());
        if (datBan.getSoLuong() <=0){
            redirect.addFlashAttribute("messageDatBan", "Bạn không được nhập số lượng <0 !");
            return "redirect:/QL_insertDatBanKH?idKH=" + idKH;
        }
        if (datBan.getNgayDatBan()==null){
            redirect.addFlashAttribute("messageDatBan", "Bạn không được để trống ngày đặt bàn!");
            return "redirect:/QL_insertDatBanKH?idKH=" + idKH;
        }
        if (datBan.getGioDatBan().equals("")){
            redirect.addFlashAttribute("messageDatBan", "Bạn không được để trống giờ đặt bàn!");
            return "redirect:/QL_insertDatBanKH?idKH=" + idKH;
        }
        datBan.setUser(khachHang.get().getUser());
        List<BanAn> banAnList = banAnRepository.findByActiveTrue();
        //lấy dữ liệu giờ làm việc của nhà hàng từ entity quản lý
        Iterable<QuanLy> listQuanLy = quanLyRepository.findAll();
        QuanLy quanLy = listQuanLy.iterator().next();
        List<String> gioLamViec = new ArrayList<>();
        gioLamViec.add(quanLy.getGioBDSang());
        gioLamViec.add(quanLy.getGioKTSang());
        gioLamViec.add(quanLy.getGioBDToi());
        gioLamViec.add(quanLy.getGioKTToi());
        //
        System.out.println("List giờ làm việc: " + gioLamViec.toString());
        //so sánh giờ đặt bàn và giờ bàn ăn
        BanAn banAnCheck;
        if (BanAn.compareTime(datBan.getGioDatBan(), gioLamViec.get(0)) <0 || (BanAn.compareTime(datBan.getGioDatBan(), gioLamViec.get(1)) >= 0) && (BanAn.compareTime(datBan.getGioDatBan(), gioLamViec.get(2)) <=0) || (BanAn.compareTime(datBan.getGioDatBan(), gioLamViec.get(3)) >=0) ){
            redirect.addFlashAttribute("messageDatBan", "Bạn đã chọn thời gian quá giờ phục vụ!!!");
            return "redirect:/QL_insertDatBanKH?idKH=" + idKH;
        } else if ( BanAn.compareTime(datBan.getGioDatBan(), gioLamViec.get(0)) >=0 &&  BanAn.compareTime(datBan.getGioDatBan(), gioLamViec.get(1)) <0){
            System.out.println("Đặt bàn sáng");
            banAnCheck = banAnRepository.findByGioBDAndGioKTAndActiveIsTrueAndLoaiBanAn(gioLamViec.get(0), gioLamViec.get(1), datBan.getBanAn().getLoaiBanAn());
            System.out.println("Bàn ăn check: " + banAnCheck.toString());

        } else {
            System.out.println("Đặt bàn chiều");
            banAnCheck = banAnRepository.findByGioBDAndGioKTAndActiveIsTrueAndLoaiBanAn(gioLamViec.get(2), gioLamViec.get(3), datBan.getBanAn().getLoaiBanAn());
            System.out.println("Bàn ăn check: " + banAnCheck.toString());
        }
        List<DatBan> listDatBan = datBanRepository.findAllByBanAn_IdBanAnAndNgayDatBan(banAnCheck.getIdBanAn(), datBan.getNgayDatBan());
        int soLuong = banAnCheck.getSoLuongBanAn();
        for (int i=0; i< listDatBan.size(); i++){
            soLuong -= listDatBan.get(i).getSoLuong();
        }
        if (datBan.getSoLuong() <= soLuong){
            datBan.setBanAn(banAnCheck);
            datBan.setActive(true);
            System.out.println("Đặt bàn save: " + datBan.toString());
            datBanRepository.save(datBan);
            return "redirect:/QL_dsDatBanKH?idKH=" + idKH;
        } else {
            String messageDatBan = "Số lượng bàn còn lại: " + soLuong;
            redirect.addFlashAttribute("messageDatBan", "Số lượng bàn còn lại: " + soLuong);
            return "redirect:/QL_insertDatBanKH?idKH=" + idKH;
        }
    }

    @RequestMapping(value = "QL_editDatBan")
    public String QL_editDatBan(@RequestParam("idDB") Integer idDB, Model model){
        Optional<DatBan> datBan = datBanRepository.findById(idDB);
        System.out.println("Đặt bàn: " + datBan.get().toString());
        List<BanAn> banAnList = banAnRepository.findByActiveTrue();
        KhachHang khachHang = khachHangRepository.findKhachHangByUser(datBan.get().getUser());
        //lấy dữ liệu giờ làm việc của nhà hàng từ entity quản lý
        Iterable<QuanLy> listQuanLy = quanLyRepository.findAll();
        QuanLy quanLy = listQuanLy.iterator().next();
        List<String> gioLamViec = new ArrayList<>();
        gioLamViec.add(quanLy.getGioBDSang());
        gioLamViec.add(quanLy.getGioKTSang());
        gioLamViec.add(quanLy.getGioBDToi());
        gioLamViec.add(quanLy.getGioKTToi());
        System.out.println("List giờ làm việc: " + gioLamViec.toString());
        System.out.println("Giờ làm việc trưa: (" + gioLamViec.get(0) + " - " + gioLamViec.get(1) + ")");
        System.out.println("Giờ làm việc tối: (" + gioLamViec.get(2) + " - " + gioLamViec.get(3) + ")");
        List<Integer> loaiBanAn = banAnRepository.findLoaiBanAn();
        loaiBanAn.sort(((o1, o2) -> o1 - o2)); // sắp xếp loại bàn ăn tăng dần
        model.addAttribute("dsLoaiBanAn", loaiBanAn);
        model.addAttribute("gioBDLamViec1", gioLamViec.get(0));
        model.addAttribute("gioKTLamViec1", gioLamViec.get(1));
        model.addAttribute("gioBDLamViec2", gioLamViec.get(2));
        model.addAttribute("gioKTLamViec2", gioLamViec.get(3));
        model.addAttribute("datBan", datBan.get());
        model.addAttribute("khachHang", khachHang);

        return "QL_editDatBan";
    }
    @RequestMapping(value = "QL_editDatBan_save")
    public String QL_editDatBan_save(@RequestParam("idDB") Integer idDB, DatBan datBan, RedirectAttributes redirect) {
        if (datBan.getSoLuong() <=0){
            redirect.addFlashAttribute("messageDatBan", "Bạn không được nhập số lượng <0 !");
            return "redirect:/QL_editDatBan?idDB=" + idDB;
        }
        if (datBan.getNgayDatBan()==null){
            redirect.addFlashAttribute("messageDatBan", "Bạn không được để trống ngày đặt bàn!");
            return "redirect:/QL_editDatBan?idDB=" + idDB;
        }
        if (datBan.getGioDatBan().equals("")){
            redirect.addFlashAttribute("messageDatBan", "Bạn không được để trống giờ đặt bàn!");
            return "redirect:/QL_editDatBan?idDB=" + idDB;
        }
        Optional<DatBan> datBan_check = datBanRepository.findById(idDB);
        List<BanAn> banAnList = banAnRepository.findByActiveTrue();
        //lấy dữ liệu giờ làm việc của nhà hàng từ entity quản lý
        Iterable<QuanLy> listQuanLy = quanLyRepository.findAll();
        QuanLy quanLy = listQuanLy.iterator().next();
        List<String> gioLamViec = new ArrayList<>();
        gioLamViec.add(quanLy.getGioBDSang());
        gioLamViec.add(quanLy.getGioKTSang());
        gioLamViec.add(quanLy.getGioBDToi());
        gioLamViec.add(quanLy.getGioKTToi());
        System.out.println("List giờ làm việc: " + gioLamViec.toString());
        //so sánh giờ đặt bàn và giờ bàn ăn
        BanAn banAnCheck;
        if (BanAn.compareTime(datBan.getGioDatBan(), gioLamViec.get(0)) <0 || (BanAn.compareTime(datBan.getGioDatBan(), gioLamViec.get(1)) >= 0) && (BanAn.compareTime(datBan.getGioDatBan(), gioLamViec.get(2)) <=0) || (BanAn.compareTime(datBan.getGioDatBan(), gioLamViec.get(3)) >=0) ){
            redirect.addFlashAttribute("messageDatBan", "Bạn đã chọn thời gian quá giờ phục vụ!!!");
            return "redirect:/QL_editDatBan?idDB=" + idDB;
        } else if ( BanAn.compareTime(datBan.getGioDatBan(), gioLamViec.get(0)) >=0 &&  BanAn.compareTime(datBan.getGioDatBan(), gioLamViec.get(1)) <0){
            banAnCheck = banAnRepository.findByGioBDAndGioKTAndActiveIsTrueAndLoaiBanAn(gioLamViec.get(0), gioLamViec.get(1), datBan.getBanAn().getLoaiBanAn());
            System.out.println("Bàn ăn check: " + banAnCheck.toString());

        } else {
            banAnCheck = banAnRepository.findByGioBDAndGioKTAndActiveIsTrueAndLoaiBanAn(gioLamViec.get(2), gioLamViec.get(3), datBan.getBanAn().getLoaiBanAn());
            System.out.println("Bàn ăn check: " + banAnCheck.toString());
        }
        List<DatBan> listDatBan = datBanRepository.findAllByBanAn_IdBanAnAndNgayDatBan(banAnCheck.getIdBanAn(), datBan.getNgayDatBan());
        int soLuong = banAnCheck.getSoLuongBanAn();
        for (int i=0; i< listDatBan.size(); i++){
            soLuong -= listDatBan.get(i).getSoLuong();
        }
        if (datBan.getSoLuong() <= soLuong){
            datBan.setBanAn(banAnCheck);
            System.out.println("Đặt bàn save: " + datBan.toString());
            datBan.setCheckin(datBan_check.get().isCheckin());
            datBanRepository.save(datBan);
            redirect.addFlashAttribute("messageChange", "Cập nhật đặt bàn thành công!");
            return "redirect:/QL_dsDatBanKH?idKH=" + khachHangRepository.findKhachHangByUser(datBan.getUser()).getIdKhachHang();
        } else {
            String messageDatBan = "Số lượng bàn còn lại: " + soLuong;
            redirect.addFlashAttribute("messageDatBan", "Số lượng bàn còn lại: " + soLuong);
            return "redirect:/QL_editDatBan?idDB=" + idDB;
        }
    }

    @RequestMapping(value = "QL_deleteDatBanKH")
    public String QL_deleteDatBanKH(@RequestParam("idDB") Integer idDB, RedirectAttributes redirect){
        Optional<DatBan> datBan = datBanRepository.findById(idDB);
        KhachHang khachHang = khachHangRepository.findKhachHangByUser(datBan.get().getUser());
        datBanRepository.deleteById(idDB);
        redirect.addFlashAttribute("messageChange", "Xóa đặt bàn thành công!");
        return "redirect:/QL_dsDatBanKH?idKH=" + khachHang.getIdKhachHang();

    }

    @RequestMapping(value = "QL_dsMonAnKH")
    public String QL_dsMonAnKH(@RequestParam("idDB") Integer idDB, Model model){
        List<ChonMon> chonMonList = chonMonRepository.findAllByDatBan_IdDatBan(idDB);
        List<ChonMon> dsMonAnKV = new ArrayList<>();
        List<ChonMon> dsMonAnMC = new ArrayList<>();
        List<ChonMon> dsMonAnTM = new ArrayList<>();
        for (int i=0; i< chonMonList.size(); i++){
            if (chonMonList.get(i).getMonAn().getLoaiMonAn().equals("Khai vị")) dsMonAnKV.add(chonMonList.get(i));
            else if (chonMonList.get(i).getMonAn().getLoaiMonAn().equals("Tráng miệng")) dsMonAnTM.add(chonMonList.get(i));
            else dsMonAnMC.add(chonMonList.get(i));
        }
        model.addAttribute("dsMonAnKV", dsMonAnKV);
        model.addAttribute("dsMonAnMC", dsMonAnMC);
        model.addAttribute("dsMonAnTM", dsMonAnTM);
        Optional<DatBan> datBan = datBanRepository.findById(idDB);
        KhachHang khachHang = khachHangRepository.findKhachHangByUser(datBan.get().getUser());
        model.addAttribute("idKH", khachHang.getIdKhachHang());
        model.addAttribute("datBan", datBan.get());
        HoaDon hoaDon = hoaDonRepository.findHoaDonByDatBan_IdDatBan(idDB);
        model.addAttribute("hoaDon", hoaDon);
        return "QL_dsMonAnKH";
    }
    @RequestMapping(value = "QL_editMonAnKH")
    public String QL_editMonAnKH(@RequestParam("idDB") Integer idDB,Model model){
        Optional<DatBan> datBan_check = datBanRepository.findById(idDB);
        List<DatBan> datBanByDate = datBanRepository.findAllByNgayDatBan(datBan_check.get().getNgayDatBan());
        //add all món ăn đang active (kiểm tra các bàn có đặt món và trừ đi lấy số lượng còn lại)
        List<MonAn> listMonAn = monAnRepository.findAllByActiveTrue();
        for (int i=0; i< datBanByDate.size(); i++){
            List<ChonMon> chonMonList_check = chonMonRepository.findAllByDatBan_IdDatBan(datBanByDate.get(i).getIdDatBan());
            for (int j=0; j< chonMonList_check.size(); j++){
                for (int k=0; k< listMonAn.size(); k++){
                    if (listMonAn.get(k).getIdMonAn() == chonMonList_check.get(j).getMonAn().getIdMonAn()){
                        int soLuongConLai = listMonAn.get(k).getSoLuongMonAn() - chonMonList_check.get(j).getSoLuong();
                        listMonAn.get(k).setSoLuongMonAn(soLuongConLai);
                    }
                }
            }
        }
        System.out.println("List món ăn: " + listMonAn.toString());
        ListChonMon dsMonAnKV = new ListChonMon();
        ListChonMon dsMonAnMC = new ListChonMon();
        ListChonMon dsMonAnTM = new ListChonMon();
        for (int i=0; i< listMonAn.size(); i++){
            if (listMonAn.get(i).getLoaiMonAn().equals("Khai vị")) dsMonAnKV.getChonMonList().add(new ChonMon(listMonAn.get(i)));
            else if (listMonAn.get(i).getLoaiMonAn().equals("Tráng miệng")) dsMonAnTM.getChonMonList().add(new ChonMon(listMonAn.get(i)));
            else dsMonAnMC.getChonMonList().add(new ChonMon(listMonAn.get(i)));
        }
        model.addAttribute("dsMonAnKV", dsMonAnKV);
        model.addAttribute("dsMonAnMC", dsMonAnMC);
        model.addAttribute("dsMonAnTM", dsMonAnTM);
        //add món ăn đã chọn
        List<ChonMon> chonMonList = chonMonRepository.findAllByDatBan_IdDatBan(idDB);
        List<ChonMon> dsChonMonAnKV = new ArrayList<>();
        List<ChonMon> dsChonMonAnMC = new ArrayList<>();
        List<ChonMon> dsChonMonAnTM = new ArrayList<>();
        for (int i=0; i< chonMonList.size(); i++){
            if (chonMonList.get(i).getMonAn().getLoaiMonAn().equals("Khai vị")) dsChonMonAnKV.add(chonMonList.get(i));
            else if (chonMonList.get(i).getMonAn().getLoaiMonAn().equals("Tráng miệng")) dsChonMonAnTM.add(chonMonList.get(i));
            else dsChonMonAnMC.add(chonMonList.get(i));
        }
        model.addAttribute("dsChonMonAnKV", dsChonMonAnKV);
        model.addAttribute("dsChonMonAnMC", dsChonMonAnMC);
        model.addAttribute("dsChonMonAnTM", dsChonMonAnTM);
        model.addAttribute("idDB", idDB);
        HoaDon hoaDon = hoaDonRepository.findHoaDonByDatBan_IdDatBan(idDB);
        model.addAttribute("hoaDon", hoaDon);
        return "QL_editMonAnKH";
    }
    @RequestMapping(value = "QL_editMonAnKH_save")
    public String QL_editMonAnKH_save(@RequestParam("idDB") Integer idDB, ListChonMon listChonMon, BindingResult result){
        System.out.println("List chọn món check: " + listChonMon.getChonMonList().toString());
        List<ChonMon> listChonMon_save = new ArrayList<>();
        for (int i=0; i<listChonMon.getChonMonList().size(); i++){
            if (listChonMon.getChonMonList().get(i).getSoLuong() <=0){

            } else if (listChonMon.getChonMonList().get(i).getSoLuong() > listChonMon.getChonMonList().get(i).getMonAn().getSoLuongMonAn()){

            } else {
                listChonMon_save.add(listChonMon.getChonMonList().get(i));
            }
        }
        Optional<DatBan> datBan = datBanRepository.findById(idDB);
        for (int i=0; i< listChonMon_save.size(); i++){
            ChonMonKey chonMonKey = new ChonMonKey(idDB, listChonMon_save.get(i).getMonAn().getIdMonAn());
            listChonMon_save.get(i).setIdChonMon(chonMonKey);
            Optional<MonAn> monAn = monAnRepository.findById(listChonMon_save.get(i).getMonAn().getIdMonAn());
            listChonMon_save.get(i).setMonAn(monAn.get());
            listChonMon_save.get(i).setDatBan(datBan.get());
            chonMonRepository.save(listChonMon_save.get(i));
        }
        System.out.println("Chọn món save:" + listChonMon_save.toString());
        return "redirect:/QL_editMonAnKH?idDB=" + idDB;
    }
    @RequestMapping(value = "QL_deleteMonAnKH")
    public String QL_deleteMonAn(@RequestParam("idDB") Integer idDB, @RequestParam("idMA") Integer idMA){
        chonMonRepository.delete(chonMonRepository.findChonMonByDatBan_IdDatBanAndAndMonAn_IdMonAn(idDB, idMA));
        return "redirect:/QL_editMonAnKH?idDB=" + idDB;
    }
    @RequestMapping(value = "QL_deleteAllMonAnKH")
    public String QL_deleteAllMonAnKH(@RequestParam("idDB") Integer idDB){
        List<ChonMon> chonMonList = chonMonRepository.findAllByDatBan_IdDatBan(idDB);
        for (int i=0; i< chonMonList.size(); i++){
            chonMonRepository.delete(chonMonList.get(i));
        }
        System.out.println("Đã xóa thành công!!");
        return "redirect:/QL_dsMonAnKH?idDB=" + idDB;
    }
    @RequestMapping(value = "QL_xuatHoaDonKH")
    public String QL_xuatHoaDonKH(@RequestParam("idDB") Integer idDB, Model model) throws ParseException {
        List<ChonMon> chonMonList = chonMonRepository.findAllByDatBan_IdDatBan(idDB);
        int tongTien = 0;
        for (int i=0; i< chonMonList.size(); i++){
            tongTien += chonMonList.get(i).getSoLuong()*chonMonList.get(i).getMonAn().getGiaTien();
        }
        System.out.println("Tổng tiền trước giảm giá: " + tongTien);
        Optional<DatBan> datBan = datBanRepository.findById(idDB);
        KhachHang khachHang = khachHangRepository.findKhachHangByUser(datBan.get().getUser());
        tongTien -= tongTien* khachHang.getGiamGia();
        System.out.println("Tổng tiền sau giảm giá: " + tongTien);
        Date date = new SimpleDateFormat("yyyy-MM-dd").parse(LocalDate.now().toString());
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm");
        String time = LocalTime.now().format(dateTimeFormatter);
        System.out.println("Ngày in: " + date.toString());
        System.out.println("Giờ in: " + time);
        datBan.get().setActive(false);
        datBanRepository.save(datBan.get());
        HoaDon hoaDon = hoaDonRepository.findHoaDonByDatBan_IdDatBan(idDB);
        Iterable<QuanLy> listQuanLy = quanLyRepository.findAll();
        QuanLy quanLy = listQuanLy.iterator().next();
        long doanhThu = 0;
        if (hoaDon == null){
            HoaDon hoaDon_save = new HoaDon();
            hoaDon_save.setNgayHoaDon(date);
            hoaDon_save.setGioHoaDon(time);
            hoaDon_save.setDatBan(datBan.get());
            hoaDon_save.setTongTien(tongTien);
            doanhThu = quanLy.getDoanhThu() + tongTien;
            hoaDonRepository.save(hoaDon_save);
            System.out.println("Hóa đơn save: "  + hoaDon_save.toString());
            model.addAttribute("hoaDon", hoaDon_save);
        } else {
            doanhThu = quanLy.getDoanhThu() - hoaDon.getTongTien() + tongTien;
            hoaDon.setTongTien(tongTien);
            hoaDonRepository.save(hoaDon);
            System.out.println("Hóa đơn save: "  + hoaDon.toString());
            model.addAttribute("hoaDon", hoaDon);
        }
        quanLy.setDoanhThu(doanhThu);
        quanLyRepository.save(quanLy);
        model.addAttribute("tongTien", tongTien);
        model.addAttribute("khachHang", khachHang);
        model.addAttribute("dsMonAn", chonMonList);
        return "QL_hoaDonKH";
    }
    @RequestMapping(value = "QL_confirmdsMonAnKH")
    public String QL_confirmdsMonAnKH(@RequestParam("idDB") Integer idDB) throws ParseException {
        List<ChonMon> chonMonList = chonMonRepository.findAllByDatBan_IdDatBan(idDB);
        int tongTien = 0;
        for (int i=0; i< chonMonList.size(); i++){
            tongTien += chonMonList.get(i).getSoLuong()*chonMonList.get(i).getMonAn().getGiaTien();
        }
        System.out.println("Tổng tiền trước giảm giá: " + tongTien);
        Optional<DatBan> datBan = datBanRepository.findById(idDB);
        KhachHang khachHang = khachHangRepository.findKhachHangByUser(datBan.get().getUser());
        tongTien -= tongTien* khachHang.getGiamGia();
        System.out.println("Tổng tiền sau giảm giá: " + tongTien);
        Date date = new SimpleDateFormat("yyyy-MM-dd").parse(LocalDate.now().toString());
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm");
        String time = LocalTime.now().format(dateTimeFormatter);
        System.out.println("Ngày in: " + date.toString());
        System.out.println("Giờ in: " + time);
        HoaDon hoaDon = hoaDonRepository.findHoaDonByDatBan_IdDatBan(idDB);
        QuanLy quanLy = quanLyRepository.findQuanLyByUser(MainController.loginUser);
        long doanhThu = 0;
        if (hoaDon == null){
            hoaDon.setNgayHoaDon(date);
            hoaDon.setGioHoaDon(time);
            hoaDon.setDatBan(datBan.get());
            doanhThu = quanLy.getDoanhThu() + tongTien;
        } else {
            doanhThu = quanLy.getDoanhThu() - hoaDon.getTongTien() + tongTien;
        }
        datBan.get().setActive(false);
        datBanRepository.save(datBan.get());
        hoaDon.setTongTien(tongTien);
        hoaDonRepository.save(hoaDon);
        quanLy.setDoanhThu(doanhThu);
        quanLyRepository.save(quanLy);
        return "redirect:/QL_dsMonAnKH?idDB=" + idDB;
    }

    //QUẢN LÝ BÀN ĂN
    @RequestMapping(value = "QL_dsBanAn")
    public String QL_dsBanAn(Model model){
        List<BanAn> banAnList = banAnRepository.findByActiveTrue();
        //lấy dữ liệu giờ làm việc của nhà hàng từ entity quản lý
        Iterable<QuanLy> listQuanLy = quanLyRepository.findAll();
        QuanLy quanLy = listQuanLy.iterator().next();
        List<String> gioLamViec = new ArrayList<>();
        gioLamViec.add(quanLy.getGioBDSang());
        gioLamViec.add(quanLy.getGioKTSang());
        gioLamViec.add(quanLy.getGioBDToi());
        gioLamViec.add(quanLy.getGioKTToi());
        List<BanAn> dsBanAn = BanAn.sortListBanAn(banAnList, gioLamViec);
        model.addAttribute("dsBanAn", dsBanAn);
        return "QL_dsBanAn";
    }
    @RequestMapping(value = "QL_insertBanAn")
    public String QL_insertBanAn(Model model){
        model.addAttribute("banAn", new BanAn());
        return "QL_insertBanAn";
    }
    @RequestMapping(value = "QL_insertBanAn_save")
    public String QL_insertBanAn_save(BanAn banAn, BindingResult result, RedirectAttributes redirect){
        if (banAn.getLoaiBanAn()<=0){
            redirect.addFlashAttribute("messageBanAn", "Bạn không thể nhập loại bàn <=0!");
            return "redirect:/QL_insertBanAn";
        }
        if (banAn.getGioBD().equals("") || banAn.getGioKT().equals("")){
            redirect.addFlashAttribute("messageBanAn", "Bạn không được để trống thời gian!");
            return "redirect:/QL_insertBanAn";
        }
        if (banAn.getSoLuongBanAn()<=0){
            redirect.addFlashAttribute("messageBanAn", "Bạn không thể nhập số lượng <=0!");
            return "redirect:/QL_insertBanAn";
        }
        //lấy dữ liệu giờ làm việc của nhà hàng từ entity quản lý
        Iterable<QuanLy> listQuanLy = quanLyRepository.findAll();
        QuanLy quanLy = listQuanLy.iterator().next();
        List<String> gioLamViec = new ArrayList<>();
        gioLamViec.add(quanLy.getGioBDSang());
        gioLamViec.add(quanLy.getGioKTSang());
        gioLamViec.add(quanLy.getGioBDToi());
        gioLamViec.add(quanLy.getGioKTToi());
        //
        System.out.println("Bàn ăn save: " + banAn.toString());
        if (BanAn.compareTime(banAn.getGioBD(), gioLamViec.get(0)) < 0 || (BanAn.compareTime(banAn.getGioBD(), gioLamViec.get(1)) > 0 && BanAn.compareTime(banAn.getGioBD(), gioLamViec.get(2)) <0) || BanAn.compareTime(banAn.getGioBD(), gioLamViec.get(3)) >0 ){
            redirect.addFlashAttribute("messageBanAn", "Bạn đã nhập giờ BĐ không trong thời gian phục vụ!");
            return "redirect:/QL_insertBanAn";

        }
        if (BanAn.compareTime(banAn.getGioKT(), gioLamViec.get(0)) < 0 || (BanAn.compareTime(banAn.getGioKT(), gioLamViec.get(1)) > 0 && BanAn.compareTime(banAn.getGioKT(), gioLamViec.get(2)) <0) || BanAn.compareTime(banAn.getGioKT(), gioLamViec.get(3)) >0 ){
            redirect.addFlashAttribute("messageBanAn", "Bạn đã nhập giờ KT không trong thời gian phục vụ!");
            return "redirect:/QL_insertBanAn";

        }
        banAn.setActive(true);
        banAnRepository.save(banAn);
        redirect.addFlashAttribute("messageChange", "Thêm bàn ăn thành công!");
        return "redirect:/QL_dsBanAn";
    }
    @RequestMapping(value = "QL_editBanAn")
    public String QL_editBanAn(@RequestParam("idBA") Integer idBA, Model model){
        Optional<BanAn> banAn = banAnRepository.findById(idBA);
        model.addAttribute("banAn", banAn);
        return "QL_editBanAn";
    }
    @RequestMapping(value = "QL_editBanAn_save")
    public String QL_editBanAn_save(BanAn banAn, BindingResult result, RedirectAttributes redirect){
        Optional<BanAn> banAn_old = banAnRepository.findById(banAn.getIdBanAn());
        System.out.println("Bàn ăn old: " + banAn_old.toString());
        System.out.println("Bàn ăn new: " + banAn.toString());
        if (banAn_old.get().isGiongNhau(banAn)){
            System.out.println("Giống nhau");
            return "redirect:/QL_dsBanAn";
        } else {
            System.out.println("Khác nhau");
        }
        if (banAn.getLoaiBanAn()<=0){
            redirect.addFlashAttribute("messageBanAn", "Bạn không thể nhập loại bàn <=0!");
            return "redirect:/QL_editBanAn?idBA=" + banAn.getIdBanAn();
        }
        if (banAn.getGioBD().equals("") || banAn.getGioKT().equals("")){
            redirect.addFlashAttribute("messageBanAn", "Bạn không được để trống thời gian!");
            return "redirect:/QL_editBanAn?idBA=" + banAn.getIdBanAn();
        }
        if (banAn.getSoLuongBanAn()<=0){
            redirect.addFlashAttribute("messageBanAn", "Bạn không thể nhập số lượng <=0!");
            return "redirect:/QL_editBanAn?idBA=" + banAn.getIdBanAn();
        }
        //lấy dữ liệu giờ làm việc của nhà hàng từ entity quản lý
        Iterable<QuanLy> listQuanLy = quanLyRepository.findAll();
        QuanLy quanLy = listQuanLy.iterator().next();
        List<String> gioLamViec = new ArrayList<>();
        gioLamViec.add(quanLy.getGioBDSang());
        gioLamViec.add(quanLy.getGioKTSang());
        gioLamViec.add(quanLy.getGioBDToi());
        gioLamViec.add(quanLy.getGioKTToi());
        //
        if (BanAn.compareTime(banAn.getGioBD(), gioLamViec.get(0)) < 0 || (BanAn.compareTime(banAn.getGioBD(), gioLamViec.get(1)) > 0 && BanAn.compareTime(banAn.getGioBD(), gioLamViec.get(2)) <0) || BanAn.compareTime(banAn.getGioBD(), gioLamViec.get(3)) >0 ){
            redirect.addFlashAttribute("messageBanAn", "Bạn đã nhập giờ BĐ không trong thời gian phục vụ!");
            return "redirect:/QL_editBanAn?idBA=" + banAn.getIdBanAn();

        }
        if (BanAn.compareTime(banAn.getGioKT(), gioLamViec.get(0)) < 0 || (BanAn.compareTime(banAn.getGioKT(), gioLamViec.get(1)) > 0 && BanAn.compareTime(banAn.getGioKT(), gioLamViec.get(2)) <0) || BanAn.compareTime(banAn.getGioKT(), gioLamViec.get(3)) >0 ){
            redirect.addFlashAttribute("messageBanAn", "Bạn đã nhập giờ KT không trong thời gian phục vụ!");
            return "redirect:/QL_editBanAn?idBA=" + banAn.getIdBanAn();

        }
        //chuyển bàn ăn cũ về trạng thái dừng hoạt động
        banAn_old.get().setActive(false);
        banAnRepository.save(banAn_old.get());
        //thêm bàn ăn mới vào DB
        BanAn banAn_new = new BanAn(null, banAn.getLoaiBanAn(), banAn.getGioBD(), banAn.getGioKT(), banAn.getSoLuongBanAn(), true);
        banAnRepository.save(banAn_new);
        return "redirect:/QL_dsBanAn";
    }
    @RequestMapping(value = "QL_deleteBanAn")
    public String QL_deleteBanAn(@RequestParam("idBA") Integer idBA){
        Optional<BanAn> banAn = banAnRepository.findById(idBA);
        banAn.get().setActive(false);
        banAnRepository.save(banAn.get());
        return "redirect:/QL_dsBanAn";
    }
    @RequestMapping(value = "QL_dsDatBanByDate")
    public String QL_dsDatBanByDate(Model model) throws ParseException {
        Date date = new SimpleDateFormat("yyyy-MM-dd").parse(LocalDate.now().toString());
        System.out.println("Ngày hôm nay: " + date.toString());
        List<DatBan> datBanList = datBanRepository.findAllByNgayDatBan(date);
        System.out.println("List đặt bàn ngày hnay: " + datBanList.toString());
        datBanList = DatBan.sortListDatBanByGioDatBan(datBanList);
        model.addAttribute("dsDatBan", datBanList);
        DatBan datBanCheck = new DatBan();
        datBanCheck.setNgayDatBan(date);
        model.addAttribute("datBanCheck", datBanCheck);
        return "QL_dsDatBanByDate";
    }
    @RequestMapping(value = "QL_searchDatBanByDate")
    public String QL_searchDatBanByDate(DatBan datBan, Model model){
        List<DatBan> datBanList = datBanRepository.findAllByNgayDatBan(datBan.getNgayDatBan());
        System.out.println("List đặt bàn ngày hnay: " + datBanList.toString());
        datBanList = DatBan.sortListDatBanByGioDatBan(datBanList);
        model.addAttribute("dsDatBan", datBanList);
        DatBan datBanCheck = new DatBan();
        datBanCheck.setNgayDatBan(datBan.getNgayDatBan());
        model.addAttribute("datBanCheck", datBanCheck);
        return "QL_dsDatBanByDate";
    }
    @RequestMapping(value = "QL_confirmDatBanByDate")
    public String QL_confirmDatBanByDate(@RequestParam("idDB") Integer idDB){
        Optional<DatBan> datBan = datBanRepository.findById(idDB);
        datBan.get().setCheckin(true);
        datBanRepository.save(datBan.get());
        KhachHang khachHang = khachHangRepository.findKhachHangByUser(datBan.get().getUser());
        int soLanDB = khachHang.getSoLanDB() + 1;
        khachHang.setSoLanDB(soLanDB);
        khachHangRepository.save(khachHang);
        return "redirect:/QL_dsDatBanByDate";
    }
    @RequestMapping(value = "QL_editKH_DatBan")
    public String QL_editKH_DatBan(@RequestParam("idDB") Integer idDB, Model model){
        Optional<DatBan> datBan = datBanRepository.findById(idDB);
        System.out.println("Đặt bàn: " + datBan.get().toString());
        List<BanAn> banAnList = banAnRepository.findByActiveTrue();
        KhachHang khachHang = khachHangRepository.findKhachHangByUser(datBan.get().getUser());
        //lấy dữ liệu giờ làm việc của nhà hàng từ entity quản lý
        Iterable<QuanLy> listQuanLy = quanLyRepository.findAll();
        QuanLy quanLy = listQuanLy.iterator().next();
        List<String> gioLamViec = new ArrayList<>();
        gioLamViec.add(quanLy.getGioBDSang());
        gioLamViec.add(quanLy.getGioKTSang());
        gioLamViec.add(quanLy.getGioBDToi());
        gioLamViec.add(quanLy.getGioKTToi());
        System.out.println("List giờ làm việc: " + gioLamViec.toString());
        System.out.println("Giờ làm việc trưa: (" + gioLamViec.get(0) + " - " + gioLamViec.get(1) + ")");
        System.out.println("Giờ làm việc tối: (" + gioLamViec.get(2) + " - " + gioLamViec.get(3) + ")");
        List<Integer> loaiBanAn = banAnRepository.findLoaiBanAn();
        loaiBanAn.sort(((o1, o2) -> o1 - o2)); // sắp xếp loại bàn ăn tăng dần
        model.addAttribute("dsLoaiBanAn", loaiBanAn);
        model.addAttribute("gioBDLamViec1", gioLamViec.get(0));
        model.addAttribute("gioKTLamViec1", gioLamViec.get(1));
        model.addAttribute("gioBDLamViec2", gioLamViec.get(2));
        model.addAttribute("gioKTLamViec2", gioLamViec.get(3));
        model.addAttribute("datBan", datBan.get());
        model.addAttribute("khachHang", khachHang);
        return "QL_editKH_DatBan";
    }
    @RequestMapping(value = "QL_editKH_DatBan_save")
    public String QL_editKH_DatBan_save(@RequestParam("idDB") Integer idDB, DatBan datBan, RedirectAttributes redirect){
        if (datBan.getSoLuong() <=0){
            redirect.addFlashAttribute("messageDatBan", "Bạn không được nhập số lượng <0 !");
            return "redirect:/QL_editKH_DatBan?idDB=" + idDB;
        }
        if (datBan.getNgayDatBan()==null){
            redirect.addFlashAttribute("messageDatBan", "Bạn không được để trống ngày đặt bàn!");
            return "redirect:/QL_editKH_DatBan?idDB=" + idDB;
        }
        if (datBan.getGioDatBan().equals("")){
            redirect.addFlashAttribute("messageDatBan", "Bạn không được để trống giờ đặt bàn!");
            return "redirect:/QL_editKH_DatBan?idDB=" + idDB;
        }
        Optional<DatBan> datBan_check = datBanRepository.findById(idDB);
        List<BanAn> banAnList = banAnRepository.findByActiveTrue();
        //lấy dữ liệu giờ làm việc của nhà hàng từ entity quản lý
        Iterable<QuanLy> listQuanLy = quanLyRepository.findAll();
        QuanLy quanLy = listQuanLy.iterator().next();
        List<String> gioLamViec = new ArrayList<>();
        gioLamViec.add(quanLy.getGioBDSang());
        gioLamViec.add(quanLy.getGioKTSang());
        gioLamViec.add(quanLy.getGioBDToi());
        gioLamViec.add(quanLy.getGioKTToi());
        System.out.println("List giờ làm việc: " + gioLamViec.toString());
        //so sánh giờ đặt bàn và giờ bàn ăn
        BanAn banAnCheck;
        if (BanAn.compareTime(datBan.getGioDatBan(), gioLamViec.get(0)) <0 || (BanAn.compareTime(datBan.getGioDatBan(), gioLamViec.get(1)) >= 0) && (BanAn.compareTime(datBan.getGioDatBan(), gioLamViec.get(2)) <=0) || (BanAn.compareTime(datBan.getGioDatBan(), gioLamViec.get(3)) >=0) ){
            redirect.addFlashAttribute("messageDatBan", "Bạn đã chọn thời gian quá giờ phục vụ!!!");
            return "redirect:/QL_editDatBan?idDB=" + idDB;
        } else if ( BanAn.compareTime(datBan.getGioDatBan(), gioLamViec.get(0)) >=0 &&  BanAn.compareTime(datBan.getGioDatBan(), gioLamViec.get(1)) <0){
            banAnCheck = banAnRepository.findByGioBDAndGioKTAndActiveIsTrueAndLoaiBanAn(gioLamViec.get(0), gioLamViec.get(1), datBan.getBanAn().getLoaiBanAn());
            System.out.println("Bàn ăn check: " + banAnCheck.toString());

        } else {
            banAnCheck = banAnRepository.findByGioBDAndGioKTAndActiveIsTrueAndLoaiBanAn(gioLamViec.get(2), gioLamViec.get(3), datBan.getBanAn().getLoaiBanAn());
            System.out.println("Bàn ăn check: " + banAnCheck.toString());
        }
        List<DatBan> listDatBan = datBanRepository.findAllByBanAn_IdBanAnAndNgayDatBan(banAnCheck.getIdBanAn(), datBan.getNgayDatBan());
        int soLuong = banAnCheck.getSoLuongBanAn();
        for (int i=0; i< listDatBan.size(); i++){
            soLuong -= listDatBan.get(i).getSoLuong();
        }
        if (datBan.getSoLuong() <= soLuong){
            datBan.setBanAn(banAnCheck);
            System.out.println("Đặt bàn save: " + datBan.toString());
            datBan.setCheckin(datBan_check.get().isCheckin());
            datBanRepository.save(datBan);
            redirect.addFlashAttribute("messageChange", "Cập nhật đặt bàn thành công!");
            return "redirect:/QL_dsDatBanByDate";
        } else {
            String messageDatBan = "Số lượng bàn còn lại: " + soLuong;
            redirect.addFlashAttribute("messageDatBan", "Số lượng bàn còn lại: " + soLuong);
            return "redirect:/QL_editDatBan?idDB=" + idDB;
        }
    }
    @RequestMapping(value = "QL_deleteKH_DatBan")
    public String QL_deleteKH_DatBan(@RequestParam("idDB") Integer idDB, RedirectAttributes redirect){
        datBanRepository.deleteById(idDB);
        redirect.addFlashAttribute("messageChange", "Xóa đặt bàn thành công!");
        return "redirect:/QL_dsDatBanByDate";
    }
    @RequestMapping(value = "Ql_dsMonAnKHByDate")
    private String QL_dsMonAnKH_ByDate(@RequestParam("idDB") Integer idDB,RedirectAttributes redirect ){
        redirect.addFlashAttribute("byDate", "byDate");
        return "redirect:/QL_dsMonAnKH?idDB=" + idDB;
    }
    //QUẢN LÝ MÓN ĂN
    @RequestMapping(value = "QL_dsMonAn")
    public String QL_dsMonAn(Model model){
        List<MonAn> dsMonAn = monAnRepository.findAllByActiveTrue();
        List<MonAn> dsMonAnKV = new ArrayList<>();
        List<MonAn> dsMonAnMC = new ArrayList<>();
        List<MonAn> dsMonAnTM = new ArrayList<>();
        for (int i=0; i< dsMonAn.size(); i++){
            if (dsMonAn.get(i).getLoaiMonAn().equals("Khai vị")) dsMonAnKV.add(dsMonAn.get(i));
            else if (dsMonAn.get(i).getLoaiMonAn().equals("Tráng miệng")) dsMonAnTM.add(dsMonAn.get(i));
            else dsMonAnMC.add(dsMonAn.get(i));
        }
        model.addAttribute("dsMonAnKV", dsMonAnKV);
        model.addAttribute("dsMonAnMC", dsMonAnMC);
        model.addAttribute("dsMonAnTM", dsMonAnTM);
        return "QL_dsMonAn";
    }
    @RequestMapping(value = "QL_insertMonAn")
    public String QL_insertMonAn(Model model){
        model.addAttribute("monAn", new MonAn());
        List<String > loaiMonAn = new ArrayList<>();
        loaiMonAn.add("Khai vị");
        loaiMonAn.add("Món chính");
        loaiMonAn.add("Tráng miệng");
        model.addAttribute("dsLoaiMonAn", loaiMonAn);
        return "QL_insertMonAn";
    }
    @RequestMapping(value = "QL_insertMonAn_save")
    public String QL_insertMonAn_save(MonAn monAn, RedirectAttributes redirect){
        if (monAn.getTenMonAn().equals("")){
            redirect.addFlashAttribute("messageMonAn", "Bạn không được để trống tên món ăn!");
            return "redirect:/QL_insertMonAn";
        }
        if (monAn.getGiaTien() <=0){
            redirect.addFlashAttribute("messageMonAn", "Bạn phải nhập giá tiền >0!");
            return "redirect:/QL_insertMonAn";
        }
        if (monAn.getSoLuongMonAn() <=0){
            redirect.addFlashAttribute("messageMonAn", "Bạn phải nhập số lượng >0!");
            return "redirect:/QL_insertMonAn";
        }
        if (monAn.getTenMonAn().equals("")){
            redirect.addFlashAttribute("messageMonAn", "Bạn không được để trống link ảnh!");
            return "redirect:/QL_insertMonAn";
        }
        monAn.setActive(true);
        monAnRepository.save(monAn);
        redirect.addFlashAttribute("messageChange", "Thêm món ăn thành công!");
        return "redirect:/QL_dsMonAn";
    }
    @RequestMapping(value = "QL_editMonAn")
    public String QL_editMonAn(@RequestParam("idMA") Integer idMA, Model model){
        Optional<MonAn> monAn = monAnRepository.findById(idMA);
        model.addAttribute("monAn", monAn.get());
        List<String > loaiMonAn = new ArrayList<>();
        loaiMonAn.add("Khai vị");
        loaiMonAn.add("Món chính");
        loaiMonAn.add("Tráng miệng");
        model.addAttribute("dsLoaiMonAn", loaiMonAn);
        return "QL_editMonAn";
    }
    @RequestMapping(value = "QL_editMonAn_save")
    public String QL_editMonAn_save(MonAn monAn, RedirectAttributes redirect){
        if (monAn.getTenMonAn().equals("")){
            redirect.addFlashAttribute("messageMonAn", "Bạn không được để trống tên món ăn!");
            return "redirect:/QL_editMonAn";
        }
        if (monAn.getGiaTien() <=0){
            redirect.addFlashAttribute("messageMonAn", "Bạn phải nhập giá tiền >0!");
            return "redirect:/QL_editMonAn";
        }
        if (monAn.getSoLuongMonAn() <=0){
            redirect.addFlashAttribute("messageMonAn", "Bạn phải nhập số lượng >0!");
            return "redirect:/QL_editMonAn";
        }
        if (monAn.getTenMonAn().equals("")){
            redirect.addFlashAttribute("messageMonAn", "Bạn không được để trống link ảnh!");
            return "redirect:/QL_editMonAn";
        }
        monAn.setActive(true);
        monAnRepository.save(monAn);
        redirect.addFlashAttribute("messageChange", "Cập nhật món ăn thành công!");
        return "redirect:/QL_dsMonAn";

    }
    @RequestMapping(value = "QL_deleteMonAn")
    public String QL_deleteMonAn(@RequestParam("idMA") Integer idMA){
        MonAn monAn = monAnRepository.findById(idMA).get();
        monAn.setActive(false);
        monAnRepository.save(monAn);
        return "redirect:/QL_dsMonAn";
    }
    @RequestMapping(value = "QL_dsMonAnByDate")
    public String QL_dsMonAnByDate(Model model) throws ParseException {
        Date date = new SimpleDateFormat("yyyy-MM-dd").parse(LocalDate.now().toString());
        System.out.println("Ngày hôm nay: " + date.toString());
        //add new DatBan để tìm kiếm theo ngày
        DatBan datBanCheck = new DatBan();
        datBanCheck.setNgayDatBan(date);
        model.addAttribute("datBanCheck", datBanCheck);
        //
        List<DatBan> datBanByDate = datBanRepository.findAllByNgayDatBan(date);
        //add all món ăn đang active (kiểm tra các bàn có đặt món và trừ đi lấy số lượng còn lại)
        List<MonAn> listMonAn = monAnRepository.findAllByActiveTrue();
        for (int i=0; i< datBanByDate.size(); i++){
            List<ChonMon> chonMonList_check = chonMonRepository.findAllByDatBan_IdDatBan(datBanByDate.get(i).getIdDatBan());
            for (int j=0; j< chonMonList_check.size(); j++){
                for (int k=0; k< listMonAn.size(); k++){
                    if (listMonAn.get(k).getIdMonAn() == chonMonList_check.get(j).getMonAn().getIdMonAn()){
                        int soLuongConLai = listMonAn.get(k).getSoLuongMonAn() - chonMonList_check.get(j).getSoLuong();
                        listMonAn.get(k).setSoLuongMonAn(soLuongConLai);
                    }
                }
            }
        }
        List<MonAn> dsMonAnKV = new ArrayList<>();
        List<MonAn> dsMonAnMC = new ArrayList<>();
        List<MonAn> dsMonAnTM = new ArrayList<>();

        for (int i=0; i< listMonAn.size(); i++){
            if (listMonAn.get(i).getLoaiMonAn().equals("Khai vị")) dsMonAnKV.add(listMonAn.get(i));
            else if (listMonAn.get(i).getLoaiMonAn().equals("Tráng miệng")) dsMonAnTM.add(listMonAn.get(i));
            else dsMonAnMC.add(listMonAn.get(i));
        }
        model.addAttribute("dsMonAnKV", dsMonAnKV);
        model.addAttribute("dsMonAnMC", dsMonAnMC);
        model.addAttribute("dsMonAnTM", dsMonAnTM);
        return "QL_dsMonAnByDate";
    }
    @RequestMapping(value = "QL_searchMonAnByDate")
    public String QL_searchMonAnByDate(DatBan datBan, Model model){
        List<DatBan> datBanList = datBanRepository.findAllByNgayDatBan(datBan.getNgayDatBan());
        System.out.println("List đặt bàn ngày hnay: " + datBanList.toString());
        DatBan datBanCheck = new DatBan();
        datBanCheck.setNgayDatBan(datBan.getNgayDatBan());
        model.addAttribute("datBanCheck", datBanCheck);
        List<DatBan> datBanByDate = datBanRepository.findAllByNgayDatBan(datBan.getNgayDatBan());
        //add all món ăn đang active (kiểm tra các bàn có đặt món và trừ đi lấy số lượng còn lại)
        List<MonAn> listMonAn = monAnRepository.findAllByActiveTrue();
        for (int i=0; i< datBanByDate.size(); i++){
            List<ChonMon> chonMonList_check = chonMonRepository.findAllByDatBan_IdDatBan(datBanByDate.get(i).getIdDatBan());
            for (int j=0; j< chonMonList_check.size(); j++){
                for (int k=0; k< listMonAn.size(); k++){
                    if (listMonAn.get(k).getIdMonAn() == chonMonList_check.get(j).getMonAn().getIdMonAn()){
                        int soLuongConLai = listMonAn.get(k).getSoLuongMonAn() - chonMonList_check.get(j).getSoLuong();
                        listMonAn.get(k).setSoLuongMonAn(soLuongConLai);
                    }
                }
            }
        }
        List<MonAn> dsMonAnKV = new ArrayList<>();
        List<MonAn> dsMonAnMC = new ArrayList<>();
        List<MonAn> dsMonAnTM = new ArrayList<>();

        for (int i=0; i< listMonAn.size(); i++){
            if (listMonAn.get(i).getLoaiMonAn().equals("Khai vị")) dsMonAnKV.add(listMonAn.get(i));
            else if (listMonAn.get(i).getLoaiMonAn().equals("Tráng miệng")) dsMonAnTM.add(listMonAn.get(i));
            else dsMonAnMC.add(listMonAn.get(i));
        }
        model.addAttribute("dsMonAnKV", dsMonAnKV);
        model.addAttribute("dsMonAnMC", dsMonAnMC);
        model.addAttribute("dsMonAnTM", dsMonAnTM);
        return "QL_dsMonAnByDate";
    }

    //QUẢN LÝ DOANH THU
    @RequestMapping(value = "QL_doanhthu")
    public String QL_doanhthu(Model model) throws ParseException {
        Iterable<ChonMon> listchonMon = chonMonRepository.findAll();
        List<MonAn> listMonAn = monAnRepository.findAllByActiveTrue();
        for (int i=0; i< listMonAn.size(); i++){
            listMonAn.get(i).setSoLuongMonAn(0);
        }
        listchonMon.forEach((chonMon)->{
            for (int i=0; i< listMonAn.size(); i++){
                if (listMonAn.get(i).getIdMonAn() == chonMon.getMonAn().getIdMonAn()){
                    listMonAn.get(i).setSoLuongMonAn( listMonAn.get(i).getSoLuongMonAn() + chonMon.getSoLuong() );
                }
            }
        });
        //get quản lý
        Iterable<QuanLy> listQuanLy = quanLyRepository.findAll();
        QuanLy quanLy = listQuanLy.iterator().next();
        MonAn monAnYeuThich = listMonAn.get(0);
        for (int i=0; i< listMonAn.size(); i++){
            if (listMonAn.get(i).getSoLuongMonAn() > monAnYeuThich.getSoLuongMonAn()){
                monAnYeuThich = listMonAn.get(i);
            }
        }
        quanLy.setMonAnDuocYeuThich(monAnYeuThich.getTenMonAn());
        quanLyRepository.save(quanLy);

        model.addAttribute("quanLy", quanLy);
        //
        Date date = new SimpleDateFormat("yyyy-MM-dd").parse(LocalDate.now().toString());
        System.out.println("Ngày hôm nay: " + date.toString());
        //add new DatBan để tìm kiếm theo ngày
        DatBan datBanCheck = new DatBan();
        datBanCheck.setNgayDatBan(date);
        model.addAttribute("datBanCheck", datBanCheck);
        //find all đặt bàn theo ngày ->hóa đơn
        Long doanhThuByDate = 0l;
        List<DatBan> datBanList = datBanRepository.findAllByNgayDatBan(date);
        for (int i=0; i< datBanList.size(); i++){
            HoaDon hoaDon = hoaDonRepository.findHoaDonByDatBan_IdDatBan(datBanList.get(i).getIdDatBan());
            if (hoaDon != null){
                doanhThuByDate += hoaDon.getTongTien();
            }
        }
        model.addAttribute("doanhThuByDate", doanhThuByDate);
        return "QL_doanhthu";
    }
    @RequestMapping(value = "QL_searchDoanhThuByDate")
    public String QL_searchDoanhThuByDate(DatBan datBan, Model model){
        Iterable<QuanLy> listQuanLy = quanLyRepository.findAll();
        QuanLy quanLy = listQuanLy.iterator().next();
        model.addAttribute("quanLy", quanLy);
        //find all đặt bàn theo ngày ->hóa đơn
        Long doanhThuByDate = 0l;
        List<DatBan> datBanList = datBanRepository.findAllByNgayDatBan(datBan.getNgayDatBan());
        for (int i=0; i< datBanList.size(); i++){
            HoaDon hoaDon = hoaDonRepository.findHoaDonByDatBan_IdDatBan(datBanList.get(i).getIdDatBan());
            if (hoaDon != null){
                doanhThuByDate += hoaDon.getTongTien();
            }
        }
        model.addAttribute("datBanCheck", datBan);
        model.addAttribute("doanhThuByDate", doanhThuByDate);
        return "QL_doanhthu";
    }
}
