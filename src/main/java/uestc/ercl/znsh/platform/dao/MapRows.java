package uestc.ercl.znsh.platform.dao;

import org.springframework.jdbc.core.RowMapper;
import uestc.ercl.znsh.common.entity.App;
import uestc.ercl.znsh.common.exception.ZNSH_IllegalArgumentException;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by zzs on 2016/11/8.
 */
public class MapRows {


}

//public class AppMapper implements RowMapper<App> {
//
//    @Override
//    public App mapRow(ResultSet rs, int rowNum) throws SQLException {
//
//        App app = new App();
//        app.setPk(rs.getString("pk"));
//        app.setName(rs.getString("name"));
//        app.setAccount(rs.getString("acount"));
//
//        return app;
//    }
//
//}

