package com.works.services;

import com.works.props.User;
import com.works.services.utils.DB;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class UserService {
     final TinkEncDec tinkEncDec;
    DB db = new DB();
    public List<User> users(int p) {
        List<User> ls = new ArrayList<>();

        p=p-1;
        p=p*50;
        try {
            String sql = "select * from users where status = 1 order by uid desc limit ?,50";
            PreparedStatement pre = db.connect().prepareStatement(sql);
            pre.setInt(1,p);
            ResultSet rs = pre.executeQuery();
            while(rs.next()) {
                User u = new User();
                u.setUid( rs.getInt("uid") );
                u.setName( rs.getString("name") );
                u.setSurname( rs.getString("surname") );
                u.setEmail( rs.getString("email") );
                u.setPassword(rs.getString("password"));
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

    public int totalCount(){
        int count=0;
        DB db = new DB();
        try{
            String sql = "select count(uid) as count  from users where status=1";
            PreparedStatement pre = db.connect().prepareStatement(sql);
            ResultSet rs = pre.executeQuery();
            if(rs.next()){
                count=rs.getInt("count");
            }
        }catch (Exception ex){
            System.err.println("Total count error : " + ex);
        }finally {
            db.close();
        }
        return count;
    }

    public int deleteUser(int uid, int dbStatus) {
        int status = 0;
        DB db = new DB();
        try {
            //String sql = "delete from users where uid = ?";
            String sql = "update users set status = ? where uid = ?";
            PreparedStatement pre = db.connect().prepareStatement(sql);
            pre.setInt(1,dbStatus);
            pre.setInt(2,uid);
            status = pre.executeUpdate();
        } catch (Exception ex) {
            System.out.println(ex);
        } finally {
            db.close();
        }
        return status;
    }

    public int userSave (User user){
        DB db = new DB();
        int status = 0;
        try{
        String sql = "insert into users values( null,?,?,?,?,1,?,now() )";
        PreparedStatement pre = db.connect().prepareStatement(sql);
            pre.setString(1, user.getName());
            pre.setString(2, user.getSurname());
            pre.setString(3, user.getEmail());
            String password =  user.getPassword();
            String cryptpass = tinkEncDec.encrypt(password);
            System.out.println(cryptpass);
            pre.setString(4,cryptpass);
            pre.setInt(5,user.getAge());
            status = pre.executeUpdate();
        } catch (Exception ex) {
            System.err.println("Save Exception : " +ex);
        } finally {
            db.close();
        }
        return status;
    }

    public User single( int uid ) {
        DB db = new DB();
        User u = new User();
        try {
            String sql = "select * from users where uid = ?";
            PreparedStatement pre = db.connect().prepareStatement(sql);
            pre.setInt(1,uid);
            ResultSet rs = pre.executeQuery();
            if ( rs.next() ) {
                u.setUid( rs.getInt("uid") );
                u.setName( rs.getString("name") );
                u.setSurname( rs.getString("surname") );
                u.setEmail( rs.getString("email") );
                u.setPassword(rs.getString("password"));
                u.setAge(rs.getInt("age"));
                u.setDate( rs.getString("date") );
            }
        }catch (Exception ex) {
            System.err.println("single Error : " + ex);
        }finally {
            db.close();
        }
        return u;
    }

    public int updateUser(User user) {
        int status = 0;
        DB db = new DB();
        try {
            String sql = "update users set name = ?,surname = ?, email =?,password=?,age=? where uid = ?";
            PreparedStatement pre = db.connect().prepareStatement(sql);
            pre.setString(1,user.getName());
            pre.setString(2,user.getSurname());
            pre.setString(3,user.getEmail());
            pre.setString(4,user.getPassword());
            pre.setInt(5,user.getAge());
            pre.setInt(6,user.getUid());
            status = pre.executeUpdate();
        } catch (Exception ex) {
            System.out.println(ex);
        } finally {
            db.close();
        }
        return status;
    }

    public User loginUser (User user) {

        User u = null;
        try {
            String sql2 = "select * from users where email = ?";
            PreparedStatement pre1 = db.connect().prepareStatement(sql2);
            pre1.setString(1, user.getEmail());
            ResultSet rs1 = pre1.executeQuery();
            String password="";
            if (rs1.next()){
                password = tinkEncDec.decrypt( rs1.getString("password"));
            }
            if (password=="") {
                String sql = "select * from users where email = ?  and password = ?";
                PreparedStatement pre = db.connect().prepareStatement(sql);

                pre.setString(1, user.getEmail());
                pre.setString(2, user.getPassword());
                ResultSet rs = pre.executeQuery();

                if (rs.next()) {
                    u = new User();
                    u.setUid(rs.getInt("uid"));
                    u.setName(rs.getString("name"));
                    u.setSurname(rs.getString("surname"));
                    u.setEmail(rs.getString("email"));
                    u.setPassword(rs.getString("password"));
                    u.setAge(rs.getInt("age"));
                    u.setDate(rs.getString("date"));

                }
            }else if(password!=null && user.getPassword().equals(password)){
                String sql = "select * from users where email = ?";
                PreparedStatement pre = db.connect().prepareStatement(sql);

                pre.setString(1, user.getEmail());

                ResultSet rs2 = pre.executeQuery();

                if (rs2.next()) {
                    u = new User();
                    u.setUid(rs2.getInt("uid"));
                    u.setName(rs2.getString("name"));
                    u.setSurname(rs2.getString("surname"));
                    u.setEmail(rs2.getString("email"));
                    u.setPassword(rs2.getString("password"));
                    u.setAge(rs2.getInt("age"));
                    u.setDate(rs2.getString("date"));

                }
            }


        } catch (Exception ex) {
            System.err.println("Login Error : " + ex);
        } finally {
            db.close();
        }
        return u;


    }

}