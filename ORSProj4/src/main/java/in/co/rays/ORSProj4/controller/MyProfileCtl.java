package in.co.rays.ORSProj4.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import in.co.rays.ORSProj4.bean.BaseBean;
import in.co.rays.ORSProj4.bean.UserBean;
import in.co.rays.ORSProj4.exception.ApplicationException;
import in.co.rays.ORSProj4.exception.DuplicateRecordException;
import in.co.rays.ORSProj4.exception.RecordNotFoundException;
import in.co.rays.ORSProj4.model.UserModel;
import in.co.rays.ORSProj4.util.DataUtility;
import in.co.rays.ORSProj4.util.DataValidator;
import in.co.rays.ORSProj4.util.PropertyReader;
import in.co.rays.ORSProj4.util.ServletUtility;

@WebServlet (name = "MyProfileCtl", urlPatterns = {"/ctl/MyProfileCtl"})
public class MyProfileCtl extends BaseCtl{

	   public static final String OP_CHANGE_MY_PASSWORD = "Change Password";
	    
	    /** The log. */
	    private static Logger log = Logger.getLogger(MyProfileCtl.class);
 
	
	
	@Override
	    protected boolean validate(HttpServletRequest request) {

	        log.debug("MyProfileCtl Method validate Started");
	        System.out.println("inside myprofile ctl Validate>>>>>>>>");
	        boolean pass = true;
	        String op = DataUtility.getString(request.getParameter("operation"));

	        if (OP_CHANGE_MY_PASSWORD.equalsIgnoreCase(op)) {
	            return pass;
	        }

	        if (DataValidator.isNull(request.getParameter("firstName"))) {
	       // System.out.println("firstName" + request.getParameter("firstName"));
	            request.setAttribute("firstName", PropertyReader.getValue("error.require", "First Name"));
	            pass = false;
	        }
	        else if (!DataValidator.isValidName(request.getParameter("firstName"))) {
	        	  request.setAttribute("firstName",PropertyReader.getValue("error.name", "Invalid First"));
	              pass = false;
	  		}

	        if (DataValidator.isNull(request.getParameter("lastName"))) {
	            request.setAttribute("lastName", PropertyReader.getValue("error.require", "Last Name"));
	            pass = false;
	        }
	        else if (!DataValidator.isValidName(request.getParameter("lastName"))) {
	        	  request.setAttribute("lastName",PropertyReader.getValue("error.name", "Invalid First"));
	              pass = false;
	  		}

	        if (DataValidator.isNull(request.getParameter("gender"))) {
	            request.setAttribute("gender", PropertyReader.getValue("error.require", "Gender"));
	            pass = false;
	        }
	        if (DataValidator.isNull(request.getParameter("mobileNo"))) {
	            request.setAttribute("mobileNo", PropertyReader.getValue("error.require", "MobileNo"));
	            pass = false;
	        }
	        else if (!DataValidator.isMobileNo(request.getParameter("mobileNo"))) {
	        	request.setAttribute("mobileNo", PropertyReader.getValue("error.mobile", "Invalid"));
	        	pass = false;
				
			}
	        if (DataValidator.isNull(request.getParameter("dob"))) {
	            request.setAttribute("dob", PropertyReader.getValue("error.require", "Date Of Birth"));
	            pass = false;
	        }

	        log.debug("MyProfileCtl Method validate Ended");
	        return pass;

	    }

	    /* (non-Javadoc)
	     * @see in.co.rays.ors.controller.BaseCtl#populateBean(javax.servlet.http.HttpServletRequest)
	     */
	    @Override
	    protected BaseBean populateBean(HttpServletRequest request) {
	        log.debug("MyProfileCtl Method populatebean Started");
	        System.out.println("inside myprofile ctl populate>>>>>>>>");
	        UserBean bean = new UserBean();

	        bean.setId(DataUtility.getLong(request.getParameter("id")));
	        bean.setLogin(DataUtility.getString(request.getParameter("login")));
	        bean.setFirstName(DataUtility.getString(request .getParameter("firstName")));
	        bean.setLastName(DataUtility.getString(request.getParameter("lastName")));
	        bean.setMobileNo(DataUtility.getString(request.getParameter("mobileNo")));
	        bean.setGender(DataUtility.getString(request.getParameter("gender")));
	        bean.setDob(DataUtility.getDate(request.getParameter("dob")));

	        populateDTO(bean, request);
	        return bean;
	    }
	    
	    /**
	     * Contains Display logics.
	     *
	     * @param request the request
	     * @param response the response
	     * @throws ServletException the servlet exception
	     * @throws IOException Signals that an I/O exception has occurred.
	     */

	    protected void doGet(HttpServletRequest request,
	            HttpServletResponse response) throws ServletException, IOException {
	    	
	        HttpSession session = request.getSession();
	        System.out.println("inside myprofile ctl doget>>>>>>>>");
	        log.debug("MyprofileCtl Method doGet Started");

	        UserBean UserBean = (UserBean) session.getAttribute("user");
	        
	        long id = UserBean.getId();
	      System.out.println("======id=====))))"+id);
	        
	        String op = DataUtility.getString(request.getParameter("operation"));
	        	System.out.println("op>>>>>>>>.."+op);
	        // get model
	        UserModel model = new UserModel();
	        
	        if (id > 0 || op != null) {
	       //     System.out.println("in id > 0  condition");
	            UserBean bean;
	            try {
	                bean = model.findByPK(id);
	              System.out.println("======my profile=====))))"+bean.getGender());
	                ServletUtility.setBean(bean, request);
	            } catch (ApplicationException e) {
	                log.error(e);
	                ServletUtility.handleException(e, request, response);
	                return;
	            }
	        }
	        ServletUtility.forward(getView(), request, response);
	        log.debug("MyProfileCtl Method doGet Ended");
	    }

	    /**
	     * contain Submit Logic.
	     *
	     * @param request the request
	     * @param response the response
	     * @throws ServletException the servlet exception
	     * @throws IOException Signals that an I/O exception has occurred.
	     */
	    protected void doPost(HttpServletRequest request,
	            HttpServletResponse response) throws ServletException, IOException {
	        HttpSession session = request.getSession();
	        log.debug("MyprofileCtl Method doPost Started");
	        System.out.println("inside myprofile ctl doPost>>>>>>>>");
	        UserBean UserBean = (UserBean) session.getAttribute("user");
	        long id = UserBean.getId();
	        String op = DataUtility.getString(request.getParameter("operation"));
	        						
	        UserBean bean = (UserBean) populateBean(request);
	        // get model
	        UserModel model = new UserModel();

	        if (OP_SAVE.equalsIgnoreCase(op)) {
	            try {
	                if (id > 0) {
	                    UserBean.setFirstName(bean.getFirstName());
	                    UserBean.setLastName(bean.getLastName());
	                    UserBean.setGender(bean.getGender());
	                    UserBean.setMobileNo(bean.getMobileNo());
	                    UserBean.setDob(bean.getDob());
	                    model.update(UserBean);

	                }	
	                ServletUtility.setBean(UserBean, request);
	                ServletUtility.setSuccessMessage( "Profile is updated Successfully. ", request);
	            } catch (ApplicationException e) {
	            	e.printStackTrace();
	                log.error(e);
	                ServletUtility.handleException(e, request, response);
	                return;
	            } catch (DuplicateRecordException e) {
	                ServletUtility.setBean(bean, request);
	                ServletUtility.setErrorMessage("Login id already exists", request);
	            } 
	        } else if (OP_CHANGE_MY_PASSWORD.equalsIgnoreCase(op)) {

	            ServletUtility.redirect(ORSView.CHANGE_PASSWORD_CTL, request,
	                    response);
	            return;

	        }

	        ServletUtility.forward(getView(), request, response);

	        log.debug("MyProfileCtl Method doPost Ended");
	    }

	
	
	@Override
	protected String getView() {
		// TODO Auto-generated method stub
		return ORSView.MY_PROFILE_VIEW;
	}

}
