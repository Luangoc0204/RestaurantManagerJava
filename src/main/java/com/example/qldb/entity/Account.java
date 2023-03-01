package com.example.qldb.entity;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.persistence.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

@Entity
@Table(name = "account")
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idAccount;

    @OneToOne
    @JoinColumn(name = "idUser")
    private User user;

    @Column(name = "username")
    private String username;

    @Column(name = "password")
    private String password;

    @Column(name = "oldpass")
    private String oldpass;

    @Column(name = "role")
    private String role;

    public Account() {
    }

    public Account(Integer idAccount, String username, String password, String oldpass, String role) {
        this.idAccount = idAccount;

        this.username = username;
        this.password = password;
        this.oldpass = oldpass;
        if (this.user.getChucVu().equals("Quản lý")){
            this.role = "ROLE_ADMIN";
        } else if (this.user.getChucVu().equals("Nhân viên")){
            this.role = "ROLE_EMPLOYEE";
        } else {
            this.role = "ROLE_USER";
        }

    }

    public Account(Integer idAccount, User user, String username, String password, String oldpass, String role) {
        this.idAccount = idAccount;
        this.user = user;
        this.username = username;
        this.password = password;
        this.oldpass = oldpass;
        if (this.user.getChucVu().equals("Quản lý")){
            this.role = "ROLE_ADMIN";
        } else if (this.user.getChucVu().equals("Nhân viên")){
            this.role = "ROLE_EMPLOYEE";
        } else {
            this.role = "ROLE_USER";
        }
    }

    public Account(Integer idAccount, User user) {
        this.idAccount = idAccount;
        this.user = user;
        if (this.user.getChucVu().equals("Quản lý")){
            this.role = "ROLE_ADMIN";
        } else if (this.user.getChucVu().equals("Nhân viên")){
            this.role = "ROLE_EMPLOYEE";
        } else {
            this.role = "ROLE_USER";
        }
        this.setAccount();
        this.setOldPassByPassword();
    }

    public void setAccount(){
        this.username = this.user.getSdt();
        //encript ngaySinh -> save password
        this.password = changePasswordToEncriptPassword(this.changeDateToPassword());
    }
    public String changeDateToPassword(){
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        String dateString = dateFormat.format(this.user.getNgaySinh());
        System.out.println("DateString: " + dateString);
        String date_split[] = dateString.split("-");
        String password = date_split[0] + date_split[1] + date_split[2];
        return password;
    }
    public void setOldPassByPassword(){
        this.oldpass = this.changeDateToPassword();
    }

    public String changePasswordToEncriptPassword(String pass){
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        String encode_pass = bCryptPasswordEncoder.encode(pass);
        return encode_pass;
    }
    public Integer getIdAccount() {
        return idAccount;
    }

    public void setIdAccount(Integer idAccount) {
        this.idAccount = idAccount;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getOldpass() {
        return oldpass;
    }

    public void setOldpass(String oldpass) {
        this.oldpass = oldpass;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return "Account{" +
                "idAccount=" + idAccount +
                ", user=" + user +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", oldpass='" + oldpass + '\'' +
                ", role='" + role + '\'' +
                '}';
    }
}
