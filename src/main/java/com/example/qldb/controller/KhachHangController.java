package com.example.qldb.controller;

import com.example.qldb.entity.*;
import com.example.qldb.repository.*;
import com.example.qldb.service.impl.AccountImpl;
import com.example.qldb.service.impl.UserServiceImpl;

import org.jvnet.staxex.BinaryText;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
public class KhachHangController {
    @Autowired private UserServiceImpl userService;
    @Autowired private AccountImpl accountService;
    @Autowired private KhachHangRepository khachHangRepository;
    @Autowired private DatBanRepository datBanRepository;
    @Autowired private BanAnRepository banAnRepository;
    @Autowired private ChonMonRepository chonMonRepository;
    @Autowired private MonAnRepository monAnRepository;
    @Autowired private HoaDonRepository hoaDonRepository;
    @Autowired private QuanLyRepository quanLyRepository;

    @RequestMapping(value = "/KH_dsDatBan")
    public String KH_dsDatBan(@RequestParam("idKH") Integer idKH, Model model){
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
        return "KH_dsDatBan";
    }

    @RequestMapping(value = "KH_insertDatBan")
    public String KH_insertDatBan(@RequestParam("idKH") Integer idKH, Model model){
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

        return "KH_insertDatBan";
    }

    @RequestMapping(value = "KH_insertDatBan_save")
    public String KH_insertDatBan_save(@RequestParam("idKH") Integer idKH, DatBan datBan, RedirectAttributes redirect){
        Optional<KhachHang> khachHang = khachHangRepository.findById(idKH);
        System.out.println("Khách hàng: " + khachHang.toString());
        User user = khachHang.get().getUser();
        System.out.println("User: " + user.toString());
        System.out.println("Đặt bàn: " + datBan.toString());
        if (datBan.getSoLuong() <=0){
            redirect.addFlashAttribute("messageDatBan", "Bạn không được nhập số lượng <0 !");
            return "redirect:/KH_insertDatBan?idKH=" + idKH;
        }
        if (datBan.getNgayDatBan()==null){
            redirect.addFlashAttribute("messageDatBan", "Bạn không được để trống ngày đặt bàn!");
            return "redirect:/KH_insertDatBan?idKH=" + idKH;
        }
        if (datBan.getGioDatBan().equals("")){
            redirect.addFlashAttribute("messageDatBan", "Bạn không được để trống giờ đặt bàn!");
            return "redirect:/KH_insertDatBan?idKH=" + idKH;
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
            return "redirect:/KH_insertDatBan?idKH=" + idKH;
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
            return "redirect:/KH_dsDatBan?idKH=" + idKH;
        } else {
            String messageDatBan = "Số lượng bàn còn lại: " + soLuong;
            redirect.addFlashAttribute("messageDatBan", "Số lượng bàn còn lại: " + soLuong);
            return "redirect:/KH_insertDatBan?idKH=" + idKH;
        }
    }

    @RequestMapping(value = "KH_editDatBan")
    public String KH_editDatBan(@RequestParam("idDB") Integer idDB, Model model){
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

        return "KH_editDatBan";
    }

    @RequestMapping(value = "KH_editDatBan_save")
    public String KH_editDatBan_save(@RequestParam("idDB") Integer idDB, DatBan datBan, RedirectAttributes redirect) {
        if (datBan.getSoLuong() <=0){
            redirect.addFlashAttribute("messageDatBan", "Bạn không được nhập số lượng <0 !");
            return "redirect:/KH_editDatBan?idDB=" + idDB;
        }
        if (datBan.getNgayDatBan()==null){
            redirect.addFlashAttribute("messageDatBan", "Bạn không được để trống ngày đặt bàn!");
            return "redirect:/KH_editDatBan?idDB=" + idDB;
        }
        if (datBan.getGioDatBan().equals("")){
            redirect.addFlashAttribute("messageDatBan", "Bạn không được để trống giờ đặt bàn!");
            return "redirect:/KH_editDatBan?idDB=" + idDB;
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
            return "redirect:/KH_editDatBan?idDB=" + idDB;
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
            return "redirect:/KH_dsDatBan?idKH=" + khachHangRepository.findKhachHangByUser(datBan.getUser()).getIdKhachHang();
        } else {
            String messageDatBan = "Số lượng bàn còn lại: " + soLuong;
            redirect.addFlashAttribute("messageDatBan", "Số lượng bàn còn lại: " + soLuong);
            return "redirect:/KH_editDatBan?idDB=" + idDB;
        }
    }
//  Quản lý món ăn
    @RequestMapping(value = "KH_dsMonAn")
    public String KH_dsMonAn(@RequestParam("idDB") Integer idDB, Model model){
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
        return "KH_dsMonAn";
    }
    @RequestMapping(value = "KH_editMonAn", method = RequestMethod.GET)
    public String KH_editMonAn(@RequestParam("idDB") Integer idDB,Model model){
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
        return "KH_editMonAn";
    }

    @RequestMapping(value = "KH_editMonAn_save")
    public String KH_editMonAn_save(@RequestParam("idDB") Integer idDB, ListChonMon listChonMon, BindingResult result){
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
        return "redirect:/KH_editMonAn?idDB=" + idDB;
    }

    @RequestMapping(value = "KH_deleteMonAn")
    public String KH_deleteMonAn(@RequestParam("idDB") Integer idDB, @RequestParam("idMA") Integer idMA){
        chonMonRepository.delete(chonMonRepository.findChonMonByDatBan_IdDatBanAndAndMonAn_IdMonAn(idDB, idMA));
        return "redirect:/KH_editMonAn?idDB=" + idDB;
    }
    @RequestMapping(value = "KH_deleteAllMonAn")
    public String QL_deleteAllMonAnKH(@RequestParam("idDB") Integer idDB){
        List<ChonMon> chonMonList = chonMonRepository.findAllByDatBan_IdDatBan(idDB);
        for (int i=0; i< chonMonList.size(); i++){
            chonMonRepository.delete(chonMonList.get(i));
        }
        System.out.println("Đã xóa thành công!!");
        return "redirect:/KH_dsMonAn?idDB=" + idDB;
    }
    @RequestMapping(value = "KH_xemHoaDon")
    public String KH_xemHoaDon(@RequestParam("idDB") Integer idDB, Model model){
        List<ChonMon> chonMonList = chonMonRepository.findAllByDatBan_IdDatBan(idDB);
        int tongTien = 0;
        for (int i=0; i< chonMonList.size(); i++){
            tongTien += chonMonList.get(i).getSoLuong()*chonMonList.get(i).getMonAn().getGiaTien();
        }
        System.out.println("Tổng tiền trước giảm giá: " + tongTien);
        Optional<DatBan> datBan = datBanRepository.findById(idDB);
        KhachHang khachHang = khachHangRepository.findKhachHangByUser(datBan.get().getUser());
        HoaDon hoaDon = hoaDonRepository.findHoaDonByDatBan_IdDatBan(idDB);
        model.addAttribute("hoaDon", hoaDon);
        model.addAttribute("tongTien", tongTien);
        model.addAttribute("khachHang", khachHang);
        model.addAttribute("dsMonAn", chonMonList);
        return "KH_xemHoaDon";
    }
}
