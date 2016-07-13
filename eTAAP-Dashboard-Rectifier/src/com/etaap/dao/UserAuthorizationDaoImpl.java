package com.etaap.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import com.etaap.beans.UserSession;
import com.etaap.security.AuthMD5Algo;

public class UserAuthorizationDaoImpl implements UserAuthorizationDao{

	@Autowired
	DataSource dataSource;

	private static final Logger logger = Logger.getLogger(UserAuthorizationDaoImpl.class);
	

	public String authenticateUser(String userName, String loginPassword) {
		logger.info("Inside UserAuthorizationDaoImpl :: authUser()");
		String query = "select password from users where user_name = ?";
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		String dbPassword = jdbcTemplate.queryForObject(query, String.class,userName);
		if(dbPassword == null){
			return null;
		}
		AuthMD5Algo md5Algo = new AuthMD5Algo();
		return md5Algo.authenticateUserPassword(dbPassword, loginPassword);
	}
	
	public UserSession getUserAuthDetails(String userName, String password) {
		logger.info("Inside UserAuthorizationDaoImpl :: getUserAuthDetails()");
		String query = "SELECT u.user_id, u.user_name, u.fname, u.lname, r.role_id, r.role_name  FROM users u, roles r, user_roles ur where u.user_name = ? AND u.password = ? AND u.user_id = ur.user_id AND r.role_id = ur.role_id";

		try {
			JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
			

			UserSession userSessionObj = jdbcTemplate.queryForObject(query,
					new Object[] { userName, password },
					new RowMapper<UserSession>() {
						public UserSession mapRow(ResultSet rs, int rowNum)
								throws SQLException {
							UserSession session = new UserSession();
							session.setUserId(rs.getInt(1));
							session.setUserName(rs.getString(2));
							session.setFname(rs.getString(3));
							session.setLname(rs.getString(4));
							session.setRoleId(rs.getInt(5));
							session.setRoleName(rs.getString(6));
							return session;
						}
					});
			return userSessionObj;

		} catch (Exception e) {
			return null;
		}
	}

}
