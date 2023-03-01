package com.example.qldb.controller;

import com.example.qldb.entity.*;
import com.example.qldb.repository.*;
import com.example.qldb.service.impl.UserServiceImpl;
import org.dom4j.rule.Mode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
public class NhanVienController {
    @Autowired private UserServiceImpl userService;
    @Autowired private QuanLyRepository quanLyRepository;
    @Autowired private KhachHangRepository khachHangRepository;
    @Autowired private NhanVienRepository nhanVienRepository;
    @Autowired private AccountRepository accountRepository;
    @Autowired private DatBanRepository datBanRepository;
    @Autowired private BanAnRepository banAnRepository;
    @Autowired private MonAnRepository monAnRepository;
    @Autowired private ChonMonRepository chonMonRepository;
    @Autowired private HoaDonRepository hoaDonRepository;

    @RequestMapping(value = "NV_dsKH")
    public String NV_dsKH(Model model){
        return "NV_dsKH";
    }

    @RequestMapping(value = "NV_searchKH")
    public String NV_searchKH(@RequestParam("sdt") Optional<String> sdt, Model model, RedirectAttributes redirect){
        if (sdt.isEmpty()){
            return "redirect:/NV_dsKH";
        }
        User user = userService.findUserBySdt(sdt.get());
        KhachHang khachHang = khachHangRepository.findKhachHangByUser(user);
        model.addAttribute("dsKhachHang", khachHang);
        return "NV_dsKH";
    }

    @RequestMapping(value = "NV_searchKHById")
    public String NV_searchKHById(@RequestParam("idKH") Integer idKH, Model model){
        Optional<KhachHang> khachHang = khachHangRepository.findById(idKH);
        model.addAttribute("khachHang", khachHang.get());
        return "NV_searchKH";
    }

    @RequestMapping(value = "NV_dsDatBanKH")
    public String NV_dsDatBanKH(@RequestParam("idKH") Integer idKH, Model model){
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
        return "NV_dsDatBanKH";
    }

    @RequestMapping(value = "NV_confirmDatBan")
    public String NV_confirmDatBan(@RequestParam("idDB") Integer idDB){
        Optional<DatBan> datBan = datBanRepository.findById(idDB);
        datBan.get().setCheckin(true);
        datBanRepository.save(datBan.get());
        KhachHang khachHang = khachHangRepository.findKhachHangByUser(datBan.get().getUser());
        int soLanDB = khachHang.getSoLanDB() + 1;
        khachHang.setSoLanDB(soLanDB);
        khachHangRepository.save(khachHang);
        return "redirect:/NV_dsDatBanKH?idKH=" + khachHang.getIdKhachHang();
    }

    @RequestMapping(value = "NV_insertDatBanKH")
    public String NV_insertDatBan(@RequestParam("idKH") Integer idKH, Model model){
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
        //
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

        return "NV_insertDatBanKH";
    }

    @RequestMapping(value = "NV_insertDatBanKH_save")
    public String NV_insertDatBanKH_save(@RequestParam("idKH") Integer idKH, DatBan datBan, RedirectAttributes redirect){
        Optional<KhachHang> khachHang = khachHangRepository.findById(idKH);
        System.out.println("Khách hàng: " + khachHang.toString());
        User user = khachHang.get().getUser();
        System.out.println("User: " + user.toString());
        System.out.println("Đặt bàn: " + datBan.toString());
        if (datBan.getSoLuong() <=0){
            redirect.addFlashAttribute("messageDatBan", "Bạn không được nhập số lượng <0 !");
            return "redirect:/NV_insertDatBanKH?idKH=" + idKH;
        }
        if (datBan.getNgayDatBan()==null){
            redirect.addFlashAttribute("messageDatBan", "Bạn không được để trống ngày đặt bàn!");
            return "redirect:/NV_insertDatBanKH?idKH=" + idKH;
        }
        if (datBan.getGioDatBan().equals("")){
            redirect.addFlashAttribute("messageDatBan", "Bạn không được để trống giờ đặt bàn!");
            return "redirect:/NV_insertDatBanKH?idKH=" + idKH;
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
            return "redirect:/NV_insertDatBanKH?idKH=" + idKH;
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
            return "redirect:/NV_dsDatBanKH?idKH=" + idKH;
        } else {
            String messageDatBan = "Số lượng bàn còn lại: " + soLuong;
            redirect.addFlashAttribute("messageDatBan", "Số lượng bàn còn lại: " + soLuong);
            return "redirect:/NV_insertDatBanKH?idKH=" + idKH;
        }
    }

    @RequestMapping(value = "NV_editDatBan")
    public String NV_editDatBan(@RequestParam("idDB") Integer idDB, Model model){
        Optional<DatBan> datBan = datBanRepository.findById(idDB);
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

        return "NV_editDatBan";
    }

    @RequestMapping(value = "NV_editDatBan_save")
    public String NV_editDatBan_save(@RequestParam("idDB") Integer idDB, DatBan datBan, RedirectAttributes redirect) {
        if (datBan.getSoLuong() <=0){
            redirect.addFlashAttribute("messageDatBan", "Bạn không được nhập số lượng <0 !");
            return "redirect:/NV_editDatBan?idDB=" + idDB;
        }
        if (datBan.getNgayDatBan()==null){
            redirect.addFlashAttribute("messageDatBan", "Bạn không được để trống ngày đặt bàn!");
            return "redirect:/NV_editDatBan?idDB=" + idDB;
        }
        if (datBan.getGioDatBan().equals("")){
            redirect.addFlashAttribute("messageDatBan", "Bạn không được để trống giờ đặt bàn!");
            return "redirect:/NV_editDatBan?idDB=" + idDB;
        }
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
            return "redirect:/NV_editDatBan?idDB=" + idDB;
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
            datBanRepository.save(datBan);
            redirect.addFlashAttribute("messageChange", "Cập nhật đặt bàn thành công!");
            return "redirect:/NV_dsDatBan?idKH=" + khachHangRepository.findKhachHangByUser(datBan.getUser()).getIdKhachHang();
        } else {
            String messageDatBan = "Số lượng bàn còn lại: " + soLuong;
            redirect.addFlashAttribute("messageDatBan", "Số lượng bàn còn lại: " + soLuong);
            return "redirect:/NV_editDatBan?idDB=" + idDB;
        }
    }

    @RequestMapping(value = "NV_deleteDatBanKH")
    public String NV_deleteDatBanKH(@RequestParam("idDB") Integer idDB, RedirectAttributes redirect){
        Optional<DatBan> datBan = datBanRepository.findById(idDB);
        KhachHang khachHang = khachHangRepository.findKhachHangByUser(datBan.get().getUser());
        datBanRepository.deleteById(idDB);
        redirect.addFlashAttribute("messageChange", "Xóa đặt bàn thành công!");
        return "redirect:/NV_dsDatBanKH?idKH=" + khachHang.getIdKhachHang();

    }

    @RequestMapping(value = "NV_dsMonAnKH")
    public String NV_dsMonAnKH(@RequestParam("idDB") Integer idDB, Model model){
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
        return "NV_dsMonAnKH";
    }
    @RequestMapping(value = "NV_editMonAnKH")
    public String NV_editMonAnKH(@RequestParam("idDB") Integer idDB,Model model){
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
        model.addAttribute("chonMon", new ChonMon());
        model.addAttribute("idDB", idDB);
        return "NV_editMonAnKH";
    }
    @RequestMapping(value = "NV_editMonAnKH_save")
    public String NV_editMonAnKH_save(@RequestParam("idDB") Integer idDB, ListChonMon listChonMon, BindingResult result){
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
        return "redirect:/NV_editMonAnKH?idDB=" + idDB;
    }
    @RequestMapping(value = "NV_deleteMonAnKH")
    public String KH_deleteMonAn(@RequestParam("idDB") Integer idDB, @RequestParam("idMA") Integer idMA){
        chonMonRepository.delete(chonMonRepository.findChonMonByDatBan_IdDatBanAndAndMonAn_IdMonAn(idDB, idMA));
        return "redirect:/NV_editMonAnKH?idDB=" + idDB;
    }
    @RequestMapping(value = "NV_deleteAllMonAnKH")
    public String NV_deleteAllMonAnKH(@RequestParam("idDB") Integer idDB){
        List<ChonMon> chonMonList = chonMonRepository.findAllByDatBan_IdDatBan(idDB);
        for (int i=0; i< chonMonList.size(); i++){
            chonMonRepository.delete(chonMonList.get(i));
        }
        System.out.println("Đã xóa thành công!!");
        return "redirect:/NV_dsMonAnKH?idDB=" + idDB;
    }
    @RequestMapping(value = "NV_xuatHoaDonKH")
    public String NV_xuatHoaDonKH(@RequestParam("idDB") Integer idDB, Model model) throws ParseException {
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
        return "NV_hoaDonKH";
    }
    //QUẢN LÝ ĐẶT BÀN
    @RequestMapping(value = "NV_dsBanAn")
    public String NV_dsBanAn(Model model){
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
        return "NV_dsBanAn";
    }
    @RequestMapping(value = "NV_dsDatBanByDate")
    public String NV_dsDatBanByDate(Model model) throws ParseException {
        Date date = new SimpleDateFormat("yyyy-MM-dd").parse(LocalDate.now().toString());
        System.out.println("Ngày hôm nay: " + date.toString());
        List<DatBan> datBanList = datBanRepository.findAllByNgayDatBan(date);
        System.out.println("List đặt bàn ngày hnay: " + datBanList.toString());
        datBanList = DatBan.sortListDatBanByGioDatBan(datBanList);
        model.addAttribute("dsDatBan", datBanList);
        DatBan datBanCheck = new DatBan();
        datBanCheck.setNgayDatBan(date);
        model.addAttribute("datBanCheck", datBanCheck);
        return "NV_dsDatBanByDate";
    }
    @RequestMapping(value = "NV_searchDatBanByDate")
    public String NV_searchDatBanByDate(DatBan datBan, Model model){
        List<DatBan> datBanList = datBanRepository.findAllByNgayDatBan(datBan.getNgayDatBan());
        System.out.println("List đặt bàn ngày hnay: " + datBanList.toString());
        datBanList = DatBan.sortListDatBanByGioDatBan(datBanList);
        model.addAttribute("dsDatBan", datBanList);
        DatBan datBanCheck = new DatBan();
        datBanCheck.setNgayDatBan(datBan.getNgayDatBan());
        model.addAttribute("datBanCheck", datBanCheck);
        return "NV_dsDatBanByDate";
    }
    @RequestMapping(value = "NV_confirmDatBanByDate")
    public String NV_confirmDatBanByDate(@RequestParam("idDB") Integer idDB){
        Optional<DatBan> datBan = datBanRepository.findById(idDB);
        datBan.get().setCheckin(true);
        datBanRepository.save(datBan.get());
        KhachHang khachHang = khachHangRepository.findKhachHangByUser(datBan.get().getUser());
        int soLanDB = khachHang.getSoLanDB() + 1;
        khachHang.setSoLanDB(soLanDB);
        khachHangRepository.save(khachHang);
        return "redirect:/NV_dsDatBanByDate";
    }
    @RequestMapping(value = "NV_editKH_DatBan")
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
        return "NV_editKH_DatBan";
    }
    @RequestMapping(value = "NV_editKH_DatBan_save")
    public String QL_editKH_DatBan_save(@RequestParam("idDB") Integer idDB, DatBan datBan, RedirectAttributes redirect){
        if (datBan.getSoLuong() <=0){
            redirect.addFlashAttribute("messageDatBan", "Bạn không được nhập số lượng <0 !");
            return "redirect:/NV_editKH_DatBan?idDB=" + idDB;
        }
        if (datBan.getNgayDatBan()==null){
            redirect.addFlashAttribute("messageDatBan", "Bạn không được để trống ngày đặt bàn!");
            return "redirect:/NV_editKH_DatBan?idDB=" + idDB;
        }
        if (datBan.getGioDatBan().equals("")){
            redirect.addFlashAttribute("messageDatBan", "Bạn không được để trống giờ đặt bàn!");
            return "redirect:/NV_editKH_DatBan?idDB=" + idDB;
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
            return "redirect:/NV_editDatBan?idDB=" + idDB;
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
            return "redirect:/NV_dsDatBanByDate";
        } else {
            String messageDatBan = "Số lượng bàn còn lại: " + soLuong;
            redirect.addFlashAttribute("messageDatBan", "Số lượng bàn còn lại: " + soLuong);
            return "redirect:/NV_editDatBan?idDB=" + idDB;
        }
    }
    @RequestMapping(value = "NV_deleteKH_DatBan")
    public String QL_deleteKH_DatBan(@RequestParam("idDB") Integer idDB, RedirectAttributes redirect){
        datBanRepository.deleteById(idDB);
        redirect.addFlashAttribute("messageChange", "Xóa đặt bàn thành công!");
        return "redirect:/NV_dsDatBanByDate";
    }
    @RequestMapping(value = "NV_dsMonAnKHByDate")
    private String NV_dsMonAnKH_ByDate(@RequestParam("idDB") Integer idDB,RedirectAttributes redirect ){
        redirect.addFlashAttribute("byDate", "byDate");
        return "redirect:/NV_dsMonAnKH?idDB=" + idDB;
    }
    //QUẢN LÝ MÓN ĂN
    @RequestMapping(value = "NV_dsMonAn")
    public String NV_dsMonAn(Model model){
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
        return "NV_dsMonAn";
    }
    @RequestMapping(value = "NV_dsMonAnByDate")
    public String NV_dsMonAnByDate(Model model) throws ParseException {
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
        return "NV_dsMonAnByDate";
    }
    @RequestMapping(value = "NV_searchMonAnByDate")
    public String NV_searchMonAnByDate(DatBan datBan, Model model){
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
        return "NV_dsMonAnByDate";
    }
}
