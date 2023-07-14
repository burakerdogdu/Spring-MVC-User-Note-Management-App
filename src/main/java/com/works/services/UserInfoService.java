package com.works.services;

import com.works.props.User;
import com.works.services.utils.DB;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class UserInfoService {
    public List<User> users(int uid) {
    List<User> ls = new ArrayList<>();
    DB db = new DB();
    try {
        String sql = "select * from users where uid = ?";
        PreparedStatement pre = db.connect().prepareStatement(sql);
        pre.setInt(1,uid);
        ResultSet rs = pre.executeQuery();
        while(rs.next()) {
            User u = new User();
            u.setUid( rs.getInt("uid") );
            u.setName( rs.getString("name") );
            u.setSurname( rs.getString("surname") );
            u.setEmail( rs.getString("email") );
            u.setPassword(rs.getString("password"));
            u.setStatus(rs.getBoolean("status"));
            u.setAge(rs.getInt("age"));
            u.setDate( rs.getString("date") );
            ls.add(u);
        }
    }catch (Exception ex) {
        System.err.println("Users Error : " + ex);
    }finally {
        db.close();
    }
        return ls;
}
}
