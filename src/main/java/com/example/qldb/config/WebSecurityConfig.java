package com.example.qldb.config;


import com.example.qldb.service.impl.AccountImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired private AccountImpl userDetailsService;



    @Bean
    public BCryptPasswordEncoder passwordEncoder(){
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        return bCryptPasswordEncoder;
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {

        // Set đặt dịch vụ để tìm kiếm User trong Database.
        // Và sét đặt PasswordEncoder.
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());

    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable(); //CSRF ( Cross Site Request Forgery) là kĩ thuật tấn công bằng cách sử dụng quyền chứng thực của người sử dụng đối với 1 website khác

        // Các trang không yêu cầu login như vậy ai cũng có thể vào được admin hay user hoặc guest có thể vào các trang
        http.authorizeRequests().antMatchers("/", "/login", "/logout","/dangky","/saveDangKyUser").permitAll();

        // Trang /userInfo yêu cầu phải login với vai trò ROLE_USER hoặc ROLE_ADMIN.
        // Nếu chưa login, nó sẽ redirect tới trang /login.sau Mình dung hasAnyRole để cho phép ai được quyền vào
        // 2  ROLE_USER và ROLEADMIN thì ta lấy từ database ra cái mà mình chèn vô ở bước 1 (chuẩn bị database)
        http.authorizeRequests().antMatchers("/giaodien","/editUser","/changePassword","/changePassword_save").access("hasAnyRole('ROLE_USER', 'ROLE_ADMIN', 'ROLE_EMPLOYEE')");

        // Trang chỉ dành cho ADMIN
        http.authorizeRequests().antMatchers("/QL_dsNV", "/QL_searchNV", "QL_insertNV", "QL_insertNV_save", "QL_searchNVById", "/QL_editNV", "QL_editNV_save", "/QL_deleteNV").access("hasRole('ROLE_ADMIN')");
        http.authorizeRequests().antMatchers("/QL_dsKH","/QL_searchKH","/QL_searchKHById","/QL_editKH","/QL_editKH_save","/QL_deleteKH","/QL_dsDatBanKH","/QL_confirmDatBan","/QL_insertDatBanKH","/QL_insertDatBanKH_save","/QL_editDatBan","/QL_editDatBan_save","/QL_deleteDatBanKH","/QL_dsMonAnKH","/QL_editMonAnKH","/QL_editMonAnKH_save","/QL_deleteMonAnKH","/QL_deleteAllMonAnKH","/QL_xuatHoaDonKH","/QL_confirmdsMonAnKH" ).access("hasRole('ROLE_ADMIN')");
        http.authorizeRequests().antMatchers("/QL_dsBanAn", "/QL_insertBanAn", "/QL_insertBanAn_save", "/QL_editBanAn", "/QL_editBanAn_save", "/QL_deleteBanAn", "/QL_dsDatBanByDate", "/QL_searchDatBanByDate","/QL_confirmDatBanByDate","/QL_editKH_DatBan","/QL_editKH_DatBan_save","/QL_deleteKH_DatBan","/Ql_dsMonAnKHByDate").access("hasRole('ROLE_ADMIN')");
        http.authorizeRequests().antMatchers("/QL_dsMonAn", "/QL_insertMonAn", "/QL_insertMonAn_save", "/QL_editMonAn", "/QL_editMonAn_save", "/QL_deleteMonAn", "/QL_dsMonAnByDate", "/QL_searchMonAnByDate").access("hasRole('ROLE_ADMIN')");
        http.authorizeRequests().antMatchers("/QL_doanhthu", "/QL_searchDoanhThuByDate").access("hasRole('ROLE_ADMIN')");
        //Trang chỉ dành cho EMPLOYEE
        http.authorizeRequests().antMatchers("/NV_dsKH","/NV_searchKH","/NV_searchKHById","/NV_dsDatBanKH","/NV_confirmDatBan","/NV_insertDatBanKH","/NV_insertDatBanKH_save","/NV_editDatBan","/NV_editDatBan_save","/NV_deleteDatBanKH","/NV_dsMonAnKH","/NV_editMonAnKH","/NV_editMonAnKH_save","/NV_deleteMonAnKH","/NV_deleteAllMonAnKH","/NV_xuatHoaDonKH").access("hasRole('ROLE_EMPLOYEE')");
        http.authorizeRequests().antMatchers("/NV_dsBanAn","/NV_dsDatBanByDate","/NV_searchDatBanByDate","/NV_confirmDatBanByDate","/NV_editKH_DatBan","/NV_editKH_DatBan_save","/NV_deleteKH_DatBan","/NV_dsMonAnKHByDate").access("hasRole('ROLE_EMPLOYEE')");
        http.authorizeRequests().antMatchers("/NV_dsMonAn","/NV_dsMonAnByDate","/NV_searchMonAnByDate").access("hasRole('ROLE_EMPLOYEE')");
        //Trang chỉ dành cho USER
        http.authorizeRequests().antMatchers("/KH_dsDatBan","/KH_insertDatBan","/KH_insertDatBan_save","/KH_editDatBan","/KH_editDatBan_save","/KH_dsMonAn","/KH_editMonAn","/KH_editMonAn_save","/KH_deleteMonAn","/KH_deleteAllMonAn","/KH_xemHoaDon").access("hasRole('ROLE_USER')");
        // Khi người dùng đã login, với vai trò user .
        // Nhưng cố ý  truy cập vào trang admin
        // Ngoại lệ AccessDeniedException sẽ ném ra.
        // Ở đây mình tạo thêm một trang web lỗi tên 403.html (mọi người có thể tạo bất cứ tên nào kh
        http.authorizeRequests().and().exceptionHandling().accessDeniedPage("/403");

        // Cấu hình cho Login Form.
        http.authorizeRequests().and().formLogin()//
                // Submit URL của trang login
                .loginProcessingUrl("/j_spring_security_check") // Bạn còn nhớ bước 3 khi tạo form login thì action của nó là j_spring_security_check giống ở
                .loginPage("/login")//
                .defaultSuccessUrl("/giaodien")//đây Khi đăng nhập thành công thì vào trang này. userAccountInfo sẽ được khai báo trong controller để hiển thị trang view tương ứng
                .failureUrl("/login?error=true")// Khi đăng nhập sai username và password thì nhập lại
                .usernameParameter("username")// tham số này nhận từ form login ở bước 3 có input  name='username'
                .passwordParameter("password")// tham số này nhận từ form login ở bước 3 có input  name='password'
                // Cấu hình cho Logout Page. Khi logout mình trả về trang
                .and().logout().logoutUrl("/logout").logoutSuccessUrl("/logoutSuccessful");

    }



}
