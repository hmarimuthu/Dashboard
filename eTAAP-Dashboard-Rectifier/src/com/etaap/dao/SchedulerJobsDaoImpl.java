package com.etaap.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import com.etaap.db.statements.ETAAPPrepareStatement;
import com.etaap.db.statements.DMLPreparedStatementCallback;
import com.etaap.db.statements.ETAAPPreparedStatementCreator;
import com.etaap.scheduler.SchedulerConstants;
import com.mysql.jdbc.Statement;

public class SchedulerJobsDaoImpl implements SchedulerJobsDao {

	@Autowired
	DataSource dataSource;
	private static final Logger logger = Logger.getLogger(DashboardDaoImpl.class);
	final KeyHolder newlyAddedRecordIds = new GeneratedKeyHolder();
	
	public List getScheduledJobs(Map params) {
		// TODO Auto-generated method stub
		StringBuffer queryStringBuffer = null;
		List queryForList = null;
		try{
			//select sj.pk_jobId,sj.jobName,sj.api_name,sj.job_interval,sj.job_status,sjr.pk_recordId,sjr.fk_jobId,sjr.executionDateValue,sjr.currentdate,sjr.status,sjr.log from schedulerJobs sj,schedulerJobsRecords sjr where sjr.fk_jobId=sj.pk_jobId and sjr.currentdate = (select max(sjr1.currentdate) from schedulerJobsRecords sjr1)
			//select sj.*,sjr.* from schedulerJobs sj left join schedulerJobsRecords sjr on sj.pk_jobId = sjr.fk_jobId and sjr.currentdate = (select max(sjr2.currentdate) from schedulerJobsRecords sjr2);
			//queryStringBuffer = new StringBuffer("select sj.pk_jobId,sj.jobName,sj.api_name,sj.job_interval,sj.job_status,sjr.pk_recordId,sjr.fk_jobId,sjr.executionDateValue,sjr.currentdate,sjr.status,sjr.log from schedulerJobs sj,schedulerJobsRecords sjr where sjr.fk_jobId=sj.pk_jobId and sjr.currentdate = (select max(sjr1.currentdate) from schedulerJobsRecords sjr1)");
			//select sj.*,sj.*,max(sjr.currentdate) from schedulerJobs sj join schedulerJobsRecords sjr on sj.pk_jobId = sjr.fk_jobId group by sj.jobName;
			 queryStringBuffer = new StringBuffer("select sj.*,sjr.*,max(sjr.currentdate) as max_currDate from schedulerJobs sj left join schedulerJobsRecords sjr on sj.pk_jobId = sjr.fk_jobId group by sj.jobName");
			//where sjr.fk_jobId=sj.pk_jobId and sjr.currentdate = (select max(sjr1.currentdate) from schedulerJobsRecords sjr1)
			//queryStringBuffer = new StringBuffer("SELECT sj.*,sjr.* FROM schedulerJobs sj LEFT JOIN schedulerJobsRecords sjr ON  sj.pk_jobid =  sjr.fk_jobid where sjr.currentdate  in (select Max(currentdate) from schedulerJobsRecords sjr1 where sjr1.fk_jobid=sj.pk_jobid group by sj.jobname)");
			if(params!=null && params.size()>0){
				//queryStringBuffer.append(" where");
				boolean isAccessed = false;
				if(params.get("jobName")!=null){
					queryStringBuffer.append(isAccessed == true ? " and" + " sj.jobName = " + params.get("jobName").toString() : " sj.jobName = " + params.get("jobName").toString());
					isAccessed = true;
				}
				if(params.get("api_name")!=null){
					queryStringBuffer.append(isAccessed == true ? " and" + " sj.api_name = " + params.get("api_name").toString() : " sj.api_name = " + params.get("api_name").toString());
					isAccessed = true;
				}
				if(params.get("pk_jobId")!=null){
					queryStringBuffer.append(isAccessed == true ? " and sj.pk_jobId = " + params.get("pk_jobId").toString() : " and sj.pk_jobId = " + params.get("pk_jobId").toString());
				}
			}
			JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
			queryForList = jdbcTemplate.queryForList(queryStringBuffer.toString());
		}catch(Exception e){
			logger.info("ERROR :: SchedulerJobsDaoImpl --> getScheduledJobs() --> " + e.getMessage());
		}
		return queryForList;
	}
	
	public void insertScheduledJobRecords(String fkjobId, String triggerFireTime, String status, String log, String mapId) {
		try {
/*			stringBuffer = new StringBuffer("insert into schedulerJobsRecords values (null,");
			stringBuffer.append("'"+triggerFireTime+"',now(), '");
			stringBuffer.append(status+"', '"+log+"', "+mapId+")");
*/			
			JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
			
/*		    String query="insert into schedulerJobsRecords(pk_recordId, fk_jobId, executionDateValue, currentdate,"
		    		+ "status, log, map_id)"
		    		+ " values(null,?,?,now(),?,?,?)";
*/		    
		    String query="insert into schedulerJobsRecords(fk_jobId, executionDateValue, currentdate,"
		    		+ "status, log, map_id)"
		    		+ " values(?,?,now(),?,?,?)";
		    
		    ArrayList types = new ArrayList();
		    ArrayList values = new ArrayList();
		    
		    types.add(ETAAPPrepareStatement.VALUE_TYPE_BIG_INT);
		    types.add(ETAAPPrepareStatement.VALUE_TYPE_STRING);
		    types.add(ETAAPPrepareStatement.VALUE_TYPE_STRING);
		    types.add(ETAAPPrepareStatement.VALUE_TYPE_STRING);
		    types.add(ETAAPPrepareStatement.VALUE_TYPE_BIG_INT);
		    
		    values.add(Integer.parseInt(fkjobId));
		    values.add(triggerFireTime);
		    values.add(status);
		    values.add(log);
		    values.add(Integer.parseInt(mapId));
		    
		    System.out.println("********&*&*&*&* Values "+values);
		    
		    ETAAPPreparedStatementCreator insertPreparedStatement = new ETAAPPreparedStatementCreator(query, types, values);
		    jdbcTemplate.update(insertPreparedStatement, newlyAddedRecordIds);
		    
		} catch(Exception e) {
			e.printStackTrace();
			logger.info("ERROR :: SchedulerJobsDaoImpl --> getScheduledJobs() --> " + e.getMessage());
		}
	}
	
	public int insertScheduledJobsRecords(Map params) {// TODO Auto-generated method stub
		StringBuffer stringBuffer = null;
		int isUpdateSuccessVal = 0;
		try{
			if(params!=null && params.size()>0){
				//stringBuffer = new StringBuffer("update schedulerJobsRecords set executionDateValue = '" + params.get("triggerFireTime").toString() +"' , status = '"+params.get("triggerStatus").toString()+"' where pk_recordId = " + params.get("unique_key"));
				stringBuffer = new StringBuffer("insert into schedulerJobsRecords values (null,"+params.get("unique_key")+",'"+
				params.get("triggerFireTime").toString()+"',now(),'"+params.get("triggerStatus").toString()+"','"+
						params.get("triggerStatus").toString()+"',"+params.get(SchedulerConstants.SCHEDULER_JOB_MAP_ID).toString()+")");
				JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
				//isUpdateSuccessVal = jdbcTemplate.queryForInt(stringBuffer.toString());
				jdbcTemplate.execute(stringBuffer.toString());
			}
			else{
				System.out.println("updateScheduledJobsRecords   --->  PARAMS -- > cannot be null");
			}
				
		}catch(Exception e){
			e.getMessage();
			isUpdateSuccessVal = 0;
		}
			return isUpdateSuccessVal;
	}
	
	public int insertScheduledJobs(Map params) {// TODO Auto-generated method stub
		StringBuffer queryStringBuffer = null;
		int pk=0;
		try{
			if(params!=null && params.size()>0){
				queryStringBuffer = new StringBuffer("insert into schedulerJobs values (" + params.get("pk") +",'"+params.get("jobName") +"','"+params.get("apiName") +"','"+params.get("interval") +"','"+params.get("status")+"')");
				final String ss = queryStringBuffer.toString();
				JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
				//pk = jdbcTemplate.e
				//pk = jdbcTemplate.queryForInt(queryStringBuffer.toString());
				KeyHolder holder = new GeneratedKeyHolder();
				jdbcTemplate.update(new PreparedStatementCreator() {

					public PreparedStatement createPreparedStatement(
							Connection arg0) throws SQLException {
						// TODO Auto-generated method stub
						PreparedStatement ps = arg0.prepareStatement(ss, Statement.RETURN_GENERATED_KEYS);
						return ps;
					}
					},holder);
					Long newPersonId = holder.getKey().longValue();
					pk = Integer.parseInt(newPersonId+"");
				}
		}catch(Exception e){
			e.getMessage();
		}
		return pk;
	}
	public int updateScheduledJobs(Map params) {
		// TODO Auto-generated method stub
				StringBuffer queryStringBuffer = null;
				int pk=0;
				try{
					if(params!=null && params.size()>0){
						queryStringBuffer = new StringBuffer("update schedulerJobs set api_name = '"+params.get("apiName") +"',job_interval='" + params.get("interval") +"',job_status='"+params.get("status")+"'");
						queryStringBuffer .append(" where pk_jobId = " + params.get("pk"));
						JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
						//pk = jdbcTemplate.queryForLong(queryStringBuffer.toString());
						pk = jdbcTemplate.update(queryStringBuffer.toString());
					}
				}catch(Exception e){
					e.getMessage();
				}
				return pk;
	}
	public int getScheduledJobsStatusBasedOnPk(int pk) {
		// TODO Auto-generated method stub
				int ab=0;
				StringBuffer queryStringBuffer = null;
				try{
					queryStringBuffer = new StringBuffer("select job_status from schedulerJobs where pk_jobId = " + pk);
					JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
					List<Map<String,Object>> list= jdbcTemplate.queryForList(queryStringBuffer.toString());
					if(list!=null && list.size()>0){
						Map a = list.get(0);
						String status = a.get("job_status").toString();
						ab = Integer.parseInt(status);
					}
				}catch(Exception e){
					e.getMessage();
				}
				return ab;
	}
	public List<Map<String, Object>> getScheduledJobsOnPk(int pk) {// TODO Auto-generated method stub
		StringBuffer queryStringBuffer = null;
		List<Map<String,Object>> queryForList = null;
		try{
			queryStringBuffer = new StringBuffer("select * from schedulerJobs where pk_jobId = " + pk);
			JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
			queryForList = jdbcTemplate.queryForList(queryStringBuffer.toString());
			
		}catch(Exception e){
			e.printStackTrace();
		}
		return queryForList;
	}
	
	public List<Map<String, Object>> _getScheduledJobs(Map params) {
		// TODO Auto-generated method stub
		StringBuffer queryStringBuffer = null;
		List queryForList = null;
		try {
			if (params != null && params.size() > 0) {
				JdbcTemplate jdbcTemplate = null;
				if (params.get("jobs") != null) {
					queryStringBuffer = new StringBuffer("select * from schedulerJobs");
					jdbcTemplate = new JdbcTemplate(dataSource);
					queryForList = jdbcTemplate.queryForList(queryStringBuffer.toString());
				} else if (params.get("records") != null) {
					if (params.get("jobId") != null) {
						queryStringBuffer = new StringBuffer("select * from schedulerJobsRecords sjr where sjr.fk_jobId="+params.get("jobId")+" and sjr.currentdate = (select max(sjr1.currentdate) from schedulerJobsRecords sjr1 where sjr1.fk_jobId="+params.get("jobId")+")");
						jdbcTemplate = new JdbcTemplate(dataSource);
						queryForList = jdbcTemplate.queryForList(queryStringBuffer.toString());
					}
				}
			}
		} catch(Exception e) {
			e.printStackTrace();
		}

		return queryForList;
	}
}
