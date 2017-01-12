package com.foxconn.rfid.theowner.model;

import android.content.Context;

import com.foxconn.rfid.theowner.base.App;

import net.tsz.afinal.FinalDb;
import net.tsz.afinal.annotation.sqlite.Id;
import net.tsz.afinal.annotation.sqlite.Table;

import java.util.List;

/**
 * Created by Administrator on 2016/12/30.
 */
@Table(name = "company_user")
public class CompanyUser {
    private String session;
    private long companyId;
    private int id;
    @Id(column = "userId")
    private int userId;
    private String password;
    private String name;
    private String address;
    private String phone;
    private String email;


    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }


    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public String getSession() {
        return session;
    }

    public void setSession(String session) {
        this.session = session;
    }

    public long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(long companyId) {
        this.companyId = companyId;
    }

    public long getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public static CompanyUser getCurUser(Context context) {


        FinalDb db = FinalDb.create(context, App.DB_NAME, true, App.DB_VERSION, (FinalDb
                .DbUpdateListener) context);
        List<CompanyUser> list = db.findAll(CompanyUser.class);

        if (list.size() > 0) {
            return list.get(0);
        } else {
            return null;
        }
    }

}
