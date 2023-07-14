package com.works.services;

import com.works.props.Notes;
import com.works.services.utils.DB;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class NotesService {
    DB db = new DB();
    public List<Notes> notes(){
        List<Notes> ls = new ArrayList<>();
        try{
            String sql ="Select * from notes";
            PreparedStatement pre = db.connect().prepareStatement(sql);
            ResultSet rs = pre.executeQuery();
            while(rs.next())
            {
                Notes notes = new Notes();
                notes.setNid(rs.getInt("nid"));
                notes.setTitle(rs.getString("title"));
                notes.setDetail(rs.getString("detail"));
                ls.add(notes);
            }
        }catch (Exception ex){
            System.err.println("Notes Error : "+ex);
        }finally {
            db.close();
        }
        return ls;
    }
    public int notesSave (Notes notes){
        int status = 0;
        try {
            String sql = "insert into notes values (null,?,?)";
            PreparedStatement pre = db.connect().prepareStatement(sql);
            pre.setString(1,notes.getTitle());
            pre.setString(2,notes.getDetail());
            status= pre.executeUpdate();

        }catch (Exception ex){
            System.err.println("Notes Save Error : "+ex);
        }finally {
            db.close();
        }
        return status;
    }
    public int deleteNote (int nid){
        int status = 0;
        try {
        String sql = "Delete from notes where nid = ?";
        PreparedStatement pre = db.connect().prepareStatement(sql);
        pre.setInt(1,nid);
        status = pre.executeUpdate();
        }catch (Exception ex){
            System.err.println("Delete Notes Error : " + ex);
        }finally {
            db.close();
        }

        return status;
    }

}
