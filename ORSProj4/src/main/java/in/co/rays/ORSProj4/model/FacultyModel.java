package in.co.rays.ORSProj4.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import in.co.rays.ORSProj4.bean.CollegeBean;
import in.co.rays.ORSProj4.bean.CourseBean;
import in.co.rays.ORSProj4.bean.FacultyBean;
import in.co.rays.ORSProj4.bean.SubjectBean;
import in.co.rays.ORSProj4.exception.ApplicationException;
import in.co.rays.ORSProj4.exception.DuplicateRecordException;
import in.co.rays.ORSProj4.util.JDBCDataSource;

public class FacultyModel {
				
	//The log**
		public static Logger log=Logger.getLogger(FacultyModel.class);
	//next pk of faculty
	
	
	public Integer nextPk() throws ApplicationException {
		log.debug("Next Pk started");
		
		Connection conn=null;
		int pk=0;
		try {
			conn= JDBCDataSource.getConnection();
			PreparedStatement ps=conn.prepareStatement("select max(id) from st_faculty");
			ResultSet rs=ps.executeQuery();
			while(rs.next())
			{
				pk=rs.getInt(1);
			}
		
		rs.close();
		} catch (Exception e) {
			
			log.error("DataBaseException",e);
			e.printStackTrace();
			throw new ApplicationException("Exception in Getting the PK");
		}
	 finally {
		JDBCDataSource.closeConnection(conn);
	}
	log.debug("Faculty Model nextPK method End");
	return pk + 1;
		
	}
	
		//Add Started 
		public long add(FacultyBean bean) throws ApplicationException, DuplicateRecordException {
			log.debug("model add start here!!");
			int pk = 0;

			
			
			CollegeModel collegeModel = new CollegeModel();	
			CollegeBean collegeBean = collegeModel.findByPK(bean.getCollegeId());
			bean.setCollegeName(collegeBean.getName());
			
			CourseModel courseModel = new CourseModel();	
			CourseBean courseBean = courseModel.findByPk(bean.getCourseId());
			bean.setCourseName(courseBean.getName());
			
			SubjectModel subjectModel = new SubjectModel();
			SubjectBean subjectBean = subjectModel.findByPk(bean.getSubjectId());
			bean.setSubjectName(subjectBean.getSubjectName());
			
			FacultyBean beanExist = findByEmail(bean.getEmailId());
			if (beanExist != null) { 
				  throw new DuplicateRecordException("Email already exists"); 
				  }
			
			
			
			
			Connection conn=null;
			
			try {
				pk=nextPk();
				conn=JDBCDataSource.getConnection();
				conn.setAutoCommit(false);
				PreparedStatement ps=conn.prepareStatement("INSERT INTO st_faculty VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
				ps.setInt(1, pk);
				ps.setLong(2, bean.getCollegeId());
				ps.setLong(3, bean.getSubjectId());
				ps.setLong(4, bean.getCourseId());
				ps.setString(5, bean.getFirstName());
				ps.setString(6, bean.getLastName());
				ps.setString(7, bean.getGender());
				ps.setDate(8, new java.sql.Date(bean.getDob().getTime()));
				ps.setString(9, bean.getEmailId());
				ps.setString(10, bean.getMobileNo());
				ps.setString(11, bean.getCourseName());
				ps.setString(12, bean.getCollegeName());
				ps.setString(13, bean.getSubjectName());	
				ps.setString(14, bean.getCreatedBy());
				ps.setString(15, bean.getModifiedBy());
				ps.setTimestamp(16, bean.getCreatedDatetime());
				ps.setTimestamp(17, bean.getModifiedDatetime());
				int n=ps.executeUpdate();
				if(n>0) {
					System.out.println("Add Success");
				}
				
				conn.commit();
				ps.close();
			} catch (Exception e) {
				log.error("Database Exception..", e);
				e.printStackTrace();
//				try {
//					conn.rollback();
//				} catch (Exception ex) {
//					ex.printStackTrace();
//					throw new ApplicationException("Exception : add rollback exception " + ex.getMessage());
//				}
//				e.printStackTrace();
//				throw new ApplicationException("Exception : Exception in add Faculty");
			} 
			finally {
				JDBCDataSource.closeConnection(conn);
			}
			log.debug("Model add End");
			
			
			return pk;
			
		}
	
	public void delete(FacultyBean bean) throws ApplicationException {
		
	FacultyBean be = findByPk(bean.getId());
	if(be==null)
	{
		System.out.println("record not found");
	}
	else
	{
		Connection conn=null;
		
		try {
			conn=JDBCDataSource.getConnection();
			conn.setAutoCommit(false);
			
			PreparedStatement ps=conn.prepareStatement("delete from st_faculty where id=?");
			ps.setLong(1, bean.getId());
			ps.executeUpdate();
			System.out.println("Faculty Delete Success");
			conn.commit();
		} catch (Exception e) {
			log.error("Database Exception..", e);
			e.printStackTrace();
			
			try {
				conn.rollback();
			} catch (Exception ex) {
				ex.printStackTrace();
				throw new ApplicationException("Exception : add rollback exception " + ex.getMessage());
			}
			e.printStackTrace();
			throw new ApplicationException("Exception : Exception in  Faculty model delete method");
		} finally {
			JDBCDataSource.closeConnection(conn);
		}
	}
		log.debug("Model add End");
		
		
	}
	
	/**
	 * Update a Faculty.
*/
	public void update(FacultyBean bean) throws ApplicationException, DuplicateRecordException {
		log.debug("Model update Started");
		Connection conn = null;
		CollegeModel collegeModel = new CollegeModel();	
		//CollegeBean collegeBean = collegeModel.findByPK(bean.getCollegeId());
	//	bean.setCollegeName(collegeBean.getName());
		
//		CourseModel courseModel = new CourseModel();	
//		CourseBean courseBean = courseModel.findByPk(bean.getCourseId());
//		bean.setCourseName(courseBean.getName());
//		
//		SubjectModel subjectModel = new SubjectModel();
//		SubjectBean subjectBean = subjectModel.findByPk(bean.getSubjectId());
//		bean.setSubjectName(subjectBean.getSubjectName());

		//FacultyBean beanExist = findByEmail(bean.getEmailId());
		// Check if updated EmailId already exist
		//if (beanExist != null && !(beanExist.getId() == bean.getId())) {
			//throw new DuplicateRecordException("EmailId is already exist");
		//}

		
		try {
			conn = JDBCDataSource.getConnection();
			conn.setAutoCommit(false);
			PreparedStatement pstmt = conn.prepareStatement(
"UPDATE ST_FACULTY SET COLLEGE_ID=?,SUBJECT_ID=?,COURSE_ID=?, FIRST_NAME=?, LAST_NAME=?, GENDER=?, DOB=?,  EMAIL_ID=?, MOBILE_NO=? , COURSE_NAME=?,  COLLEGE_NAME=?, SUBJECT_NAME=?,  MODIFIED_BY=? , MODIFIED_DATETIME=? WHERE ID=? ");
			
			pstmt.setLong(1, bean.getCollegeId());
			pstmt.setLong(2, bean.getSubjectId());
			pstmt.setLong(3, bean.getCourseId());
			pstmt.setString(4, bean.getFirstName());
			pstmt.setString(5, bean.getLastName());
			pstmt.setString(6, bean.getGender());
			pstmt.setDate(7, new java.sql.Date(bean.getDob().getTime()));
			pstmt.setString(8, bean.getEmailId());
			pstmt.setString(9, bean.getMobileNo());
			pstmt.setString(10, bean.getCourseName());
			pstmt.setString(11,bean.getCollegeName());
			pstmt.setString(12, bean.getSubjectName());
			//pstmt.setString(13, bean.getCreatedBy());
			pstmt.setString(13, bean.getModifiedBy());
			//pstmt.setTimestamp(15, bean.getCreatedDatetime());
			pstmt.setTimestamp(14, bean.getModifiedDatetime());
			pstmt.setLong(15, bean.getId());

			pstmt.executeUpdate();

			conn.commit();
			pstmt.close();
		} catch (Exception e) {
			e.printStackTrace();
			log.error("DATABASE EXCEPTION ...", e);
			try {
				conn.rollback();
			} catch (Exception ex) {
				throw new ApplicationException("Exception in rollback faculty model .." + ex.getMessage());
			}
			throw new ApplicationException("Exception in faculty model Update Method..");
		} finally {
			JDBCDataSource.closeConnection(conn);
		}
		log.debug("Faculty Model update method End");
	}
	
	/**
	 * Find User by Faculty name.
	 *
	 * @param EmailId            : get parameter
	 * @return bean
	 * @throws ApplicationException the application exception
	 */
	
	public FacultyBean findByEmail(String EmailId) throws ApplicationException {
		
		//System.out.println("faculty add find by name");
		log.debug("Faculty Model findByName method Started");
		StringBuffer sql = new StringBuffer("SELECT * FROM ST_FACULTY WHERE EMAIL_ID=?");
		Connection conn = null;
		FacultyBean bean = null;
		
	//	System.out.println(" faculty  find by name 1");
		try {
			conn = JDBCDataSource.getConnection();
			PreparedStatement pstmt = conn.prepareStatement(sql.toString());
			
		//	System.out.println("prepared");
			pstmt.setString(1, EmailId);
			//System.out.println("resultset"+EmailId);
			ResultSet rs = pstmt.executeQuery();
			//System.out.println(" faculty  find by name 1 while");
			while (rs.next()) {
				bean = new FacultyBean();
				bean.setId(rs.getLong(1));
				bean.setCollegeId(rs.getLong(2));
				bean.setSubjectId(rs.getLong(3));
				bean.setCourseId(rs.getLong(4));	
				bean.setFirstName(rs.getString(5));
				bean.setLastName(rs.getString(6));
				bean.setGender(rs.getString(7));
				bean.setDob(rs.getDate(8));
				bean.setEmailId(rs.getString(9));
				bean.setMobileNo(rs.getString(10));
				bean.setCourseName(rs.getString(11));
				bean.setCollegeName(rs.getString(12));
				bean.setSubjectName(rs.getString(13));
				bean.setCreatedBy(rs.getString(14));
				bean.setModifiedBy(rs.getString(15));
				bean.setCreatedDatetime(rs.getTimestamp(16));
				bean.setModifiedDatetime(rs.getTimestamp(17));
	//System.out.println(" faculty  find by name 3");
			}
			rs.close();
		} catch (Exception e) {
			log.error("database exception ..." , e);
			throw new ApplicationException("Exception : Exception in faculty model in findbyName method");
		} finally {
			JDBCDataSource.closeConnection(conn);
		}
		//System.out.println(" faculty  find by name 4");
		log.debug("Faculty Model findbyName method End");
		return bean;
	}
	
	/**
	 * Find User by Faculty PK.
	 *
	 * @param pk            : get parameter
	 * @return bean
	 * @throws ApplicationException the application exception
	 */

	public FacultyBean findByPk(long pk) throws ApplicationException {
		log.debug("Faculty Model findByPK method Started");
		StringBuffer sql = new StringBuffer("SELECT * FROM ST_FACULTY WHERE ID=?");
		Connection conn = null;
		FacultyBean bean = null;
		try {
			conn = JDBCDataSource.getConnection();
			PreparedStatement pstmt = conn.prepareStatement(sql.toString());
			pstmt.setLong(1, pk);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				bean = new FacultyBean();
				bean.setId(rs.getLong(1));
				bean.setCollegeId(rs.getLong(2));
				bean.setSubjectId(rs.getLong(3));
				bean.setCourseId(rs.getLong(4));
				bean.setFirstName(rs.getString(5));
				bean.setLastName(rs.getString(6));
				bean.setGender(rs.getString(7));
				bean.setDob(rs.getDate(8));
				bean.setEmailId(rs.getString(9));
				bean.setMobileNo(rs.getString(10));
				bean.setCourseName(rs.getString(11));
				bean.setCollegeName(rs.getString(12));
				bean.setSubjectName(rs.getString(13));
				bean.setCreatedBy(rs.getString(14));
				bean.setModifiedBy(rs.getString(15));
				bean.setCreatedDatetime(rs.getTimestamp(16));
				bean.setModifiedDatetime(rs.getTimestamp(17));
}
			rs.close();
		} catch (Exception e) {
			e.printStackTrace();
			log.error("database exception ..." , e);
			throw new ApplicationException("Exception : Exception in findByPK in faculty model");
		} finally {
			JDBCDataSource.closeConnection(conn);
		}
		log.debug("Faculty Model FindByPK method end");
		return bean;
	}
	
	/**
	 * Search Faculty.
	 *
	 * @param bean            : Search Parameters
	 * @return the list
	 * @throws ApplicationException the application exception
	 */

	public List search(FacultyBean bean) throws ApplicationException{
		return search(bean,0,0);
	}
	
	/**
	 * Search Faculty with pagination.
	 *
	 * @param bean            : Search Parameters
	 * @param pageNo            : Current Page No.
	 * @param pageSize            : Size of Page
	 * @return list : List of Users
	 * @throws ApplicationException the application exception
	 */

	public List search(FacultyBean bean, int pageNo, int pageSize) throws ApplicationException {
		log.debug("Faculty Model search  method Started");
		//System.out.println("faculty model");
		StringBuffer sql = new StringBuffer("SELECT * FROM ST_FACULTY WHERE true");
		if (bean!=null) {
			if (bean.getId()>0) {
				sql.append(" AND id = " + bean.getId());
			}
			if (bean.getCollegeId() > 0) {
				sql.append(" AND college_Id = " + bean.getCollegeId());
			}
			if (bean.getFirstName() != null && bean.getFirstName().trim().length() > 0) {
				sql.append(" AND FIRST_NAME like '" + bean.getFirstName() + "%'");
			}
			if (bean.getLastName() != null && bean.getLastName().trim().length() > 0) {
				sql.append(" AND LAST_NAME like '" + bean.getLastName() + "%'");
			}
			
			if (bean.getEmailId()!=null && bean.getEmailId().length()>0) {
				sql.append(" AND Email_Id like '" + bean.getEmailId() + "%'");
			}
			
			if (bean.getGender()!=null && bean.getGender().length()>0) {
				sql.append(" AND Gender like '" + bean.getGender() + "%'");
			}
	
		
			if (bean.getMobileNo()!=null && bean.getMobileNo().length()>0) {
				sql.append(" AND Mobile_No like '" + bean.getMobileNo() + "%'");
			}
			
			if (bean.getCollegeName()!=null && bean.getCollegeName().length()>0) {
				sql.append(" AND college_Name like '" + bean.getCollegeName() + "%'");
			}
			if (bean.getCourseId() > 0) {
				sql.append(" AND course_Id = " + bean.getCourseId());
			}
			if (bean.getCourseName()!=null && bean.getCourseName().length()>0) {
				sql.append(" AND course_Name like '" + bean.getCourseName() + "%'");
			}
			if (bean.getSubjectId() > 0) {
				sql.append(" AND Subject_Id = " + bean.getSubjectId());
			}
			if (bean.getSubjectName()!=null && bean.getSubjectName().length()>0) {
				sql.append(" AND subject_Name like '" + bean.getSubjectName() + "%'");
			}
		}
		
		// if page no is greater then zero then apply pagination 
		//System.out.println("model page ........"+pageNo +" "+pageSize);
		if(pageSize>0){
			pageNo = (pageNo-1)*pageSize;
			sql.append(" limit "+pageNo+ " , " + pageSize);
		}
	     //System.out.println("final sql  "+sql);
		Connection conn = null;
		ArrayList list = new ArrayList();
		try{
			
			conn = JDBCDataSource.getConnection();
			PreparedStatement pstmt = conn.prepareStatement(sql.toString());
			ResultSet rs = pstmt.executeQuery(); 
			while (rs.next()) {
				
				bean = new FacultyBean();
				bean.setId(rs.getLong(1));
				bean.setCollegeId(rs.getLong(2));
				bean.setSubjectId(rs.getLong(3));
				bean.setCourseId(rs.getLong(4));
				bean.setFirstName(rs.getString(5));
				bean.setLastName(rs.getString(6));
				bean.setGender(rs.getString(7));
				bean.setDob(rs.getDate(8));
				bean.setEmailId(rs.getString(9));
				bean.setMobileNo(rs.getString(10));
				bean.setCourseName(rs.getString(11));
				bean.setCollegeName(rs.getString(12));
				bean.setSubjectName(rs.getString(13));
				bean.setCreatedBy(rs.getString(14));
				bean.setModifiedBy(rs.getString(15));
				bean.setCreatedDatetime(rs.getTimestamp(16));
				bean.setModifiedDatetime(rs.getTimestamp(17));
//System.out.println("out whiile");
				list.add(bean);
				//System.out.println("list esh size ----------->"+list.size());
			}
			rs.close();
			
		}catch(Exception e){
			log.error("database Exception .. " , e);
			e.printStackTrace();
	//	throw new ApplicationException("Exception : Exception in Search method of Faculty Model");
		}finally {
			JDBCDataSource.closeConnection(conn);
		}
		log.debug("Faculty Model search  method End");
	//	System.out.println("retuen >>>>>>>>>>>>>>>"+list.size());
		return list;
		
	}

	/**
	 * Get List of Faculty.
	 *
	 * @return list : List of Faculty
	 * @throws ApplicationException the application exception
	 */

	public List list() throws ApplicationException{
		return list(0,0);
	}

	/**
	 * Get List of Faculty with pagination.
	 *
	 * @param pageNo            : Current Page No.
	 * @param pageSize            : Size of Page
	 * @return list : List of Faculty
	 * @throws ApplicationException the application exception
	 */

	public List list(int pageNo, int pageSize) throws ApplicationException {
		log.debug("Faculty Model List method Started");
		StringBuffer sql = new StringBuffer("SELECT * FROM ST_FACULTY");
		Connection conn = null;
		ArrayList list = new ArrayList();
		
		// if page is greater than zero then apply pagination 
		if (pageSize>0) {
			pageNo = (pageNo-1)*pageSize;
			sql.append(" limit "+ pageNo+ " , " + pageSize);
		}
		try{
				conn = JDBCDataSource.getConnection();
				PreparedStatement pstmt = conn.prepareStatement(sql.toString());
				ResultSet rs = pstmt.executeQuery();
				while (rs.next()) {
					FacultyBean bean = new FacultyBean();
					bean.setId(rs.getLong(1));
					bean.setCollegeId(rs.getLong(2));
					bean.setSubjectId(rs.getLong(3));
					bean.setCourseId(rs.getLong(4));
					
					bean.setFirstName(rs.getString(5));
					bean.setLastName(rs.getString(6));
					bean.setGender(rs.getString(7));
					bean.setDob(rs.getDate(8));
					bean.setEmailId(rs.getString(9));
					bean.setMobileNo(rs.getString(10));
					bean.setCourseName(rs.getString(11));
					bean.setCollegeName(rs.getString(12));
					bean.setSubjectName(rs.getString(13));
					bean.setCreatedBy(rs.getString(14));
					bean.setModifiedBy(rs.getString(15));
					bean.setCreatedDatetime(rs.getTimestamp(16));
					bean.setModifiedDatetime(rs.getTimestamp(17));
					list.add(bean);
				}rs.close();
		}catch(Exception e){
			log.error("Database Exception ......" , e);
			throw new ApplicationException("Exception in list method of FacultyModel");
		}finally {
		JDBCDataSource.closeConnection(conn);	
		}
		log.debug("Faculty Model List method End");
		return list;
	}	

	
	
	
	
	
	
}
