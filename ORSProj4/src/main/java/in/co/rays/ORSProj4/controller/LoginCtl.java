package in.co.rays.ORSProj4.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import in.co.rays.ORSProj4.bean.BaseBean;
import in.co.rays.ORSProj4.bean.RoleBean;
import in.co.rays.ORSProj4.bean.UserBean;
import in.co.rays.ORSProj4.exception.ApplicationException;
import in.co.rays.ORSProj4.model.RoleModel;
import in.co.rays.ORSProj4.model.UserModel;
import in.co.rays.ORSProj4.util.DataUtility;
import in.co.rays.ORSProj4.util.DataValidator;
import in.co.rays.ORSProj4.util.PropertyReader;
import in.co.rays.ORSProj4.util.ServletUtility;

/**
	 * Login functionality Controller. Performs operation for Login
	 *
	 * @author SunilOS
	 * @version 1.0
	 * @Copyright (c) SunilOS
	 */
	@ WebServlet(name="LoginCtl",urlPatterns={"/LoginCtl"})
		public class LoginCtl extends BaseCtl {

	    private static final long serialVersionUID = 1L;
	    public static final String OP_REGISTER = "Register";
	    public static final String OP_SIGN_IN = "SignIn";
	    public static final String OP_SIGN_UP = "SignUp";
	    public static final String OP_LOG_OUT = "logout";

	    private static Logger log = Logger.getLogger(LoginCtl.class);

	    @Override
	    protected boolean validate(HttpServletRequest request) {

	        log.debug("LoginCtl Method validate Started");
	        System.out.println("in validate in login ctl ");
	        boolean pass = true;

	        String op = request.getParameter("operation");
	        if (OP_SIGN_UP.equals(op) || OP_LOG_OUT.equals(op)) {
	            return pass;
	        }

	        String login = request.getParameter("login");

	        if (DataValidator.isNull(login)) {
	            request.setAttribute("login",
	                    PropertyReader.getValue("error.require", "Login Id"));
	            pass = false;
	        } else if (!DataValidator.isEmail(login)) {
	            request.setAttribute("login",
	                    PropertyReader.getValue("error.email", "Login "));
	            pass = false;
	        }
	        if (DataValidator.isNull(request.getParameter("password"))) {
	            request.setAttribute("password",
	                    PropertyReader.getValue("error.require", "Password"));
	            pass = false;
	        }

	        log.debug("LoginCtl Method validate Ended");

	        return pass;
	    }

	    @Override
	    protected BaseBean populateBean(HttpServletRequest request) {

	        log.debug("LoginCtl Method populatebean Started");
	        	System.out.println("inside populatebean of login ctl");
	        UserBean bean = new UserBean();

	        bean.setId(DataUtility.getLong(request.getParameter("id")));
	        bean.setLogin(DataUtility.getString(request.getParameter("login")));
	        bean.setPassword(DataUtility.getString(request.getParameter("password")));

	        log.debug("LoginCtl Method populatebean Ended");

	        return bean;
	    }

	    /**
	     * Display Login form
	     *
	     */
	    protected void doGet(HttpServletRequest request,
	            HttpServletResponse response) throws ServletException, IOException {
	        //HttpSession session = request.getSession(true);
	        log.debug(" Method doGet Started");
	        System.out.println("login ctl do get");
	        HttpSession session = request.getSession(false);
			String op = DataUtility.getString(request.getParameter("operation"));

			if (OP_LOG_OUT.equals(op) && !OP_SIGN_IN.equals(op)) {

				session.invalidate();
				ServletUtility.setSuccessMessage("User Logout Succesfully", request);
				ServletUtility.forward(getView(), request, response);
				return;
			} /*
				 * else{ ServletUtility.redirect(ORSView.WELCOME_CTL, request, response);
				 * return; }
				 */

			ServletUtility.forward(getView(), request, response);
		/*
		 * String op = DataUtility.getString(request.getParameter("operation"));
		 * 
		 * // get model UserModel model = new UserModel(); RoleModel role = new
		 * RoleModel();
		 * 
		 * long id = DataUtility.getLong(request.getParameter("id")); if (id > 0) {
		 * UserBean userbean; try { userbean = model.findByPK(id);
		 * ServletUtility.setBean(userbean, request); } catch (ApplicationException e) {
		 * log.error(e); ServletUtility.handleException(e, request, response); return; }
		 * } ServletUtility.forward(getView(), request, response);
		 * 
		 * log.debug("UserCtl Method doPost Ended");
		 */
	    }

	    /**
	     * Submitting or login action performing
	     *
	     */
	    protected void doPost(HttpServletRequest request,
	            HttpServletResponse response) throws ServletException, IOException {
	        HttpSession session = request.getSession(true); //session start here
	        log.debug(" Method doPost Started");

	        String op = DataUtility.getString(request.getParameter("operation"));
	        System.out.println("get op in login dopost"+op);
	        // get model
	        UserModel model = new UserModel();
	        RoleModel role = new RoleModel();

	        //long id = DataUtility.getLong(request.getParameter("id"));

	        UserBean bean = (UserBean) populateBean(request);
	        if (OP_SIGN_IN.equalsIgnoreCase(op)) {


	            try {
	            			
	                bean = model.authenticate(bean.getLogin(), bean.getPassword());

	                if (bean != null) {
	                    session.setAttribute("user", bean);
	                    long rollId = bean.getRoleId();
	                    System.out.println("my role id:- "+bean.getRoleId());
	                    System.out.println("roll id: "+ rollId);
	                    RoleBean rolebean = role.findByPK(rollId);

	                    if (rolebean != null) {
	                    	System.out.println("inside rolebean : "+rolebean.getName());
	                        session.setAttribute("role", rolebean.getName());
	                    }
	                    	String uri=(String) session.getAttribute("URI");
	                    	System.out.println(">>>>>>>>>>>>>"+uri);
	                    	if (uri == null || "null".equalsIgnoreCase(uri))
	                    	{
	                    ServletUtility.redirect(ORSView.WELCOME_CTL, request,
	                            response);
	                    return;
	                    	}
	                    	else
	                    	{
	                    		 ServletUtility.redirect(uri, request,
	     	                            response);
	     	                    return;
	     	                   
	                    	}
	                } else {
	                    bean = (UserBean) populateBean(request);
	                    ServletUtility.setBean(bean, request);
	                    ServletUtility.setErrorMessage(
	                            "Invalid LoginId/Password", request);
	                    ServletUtility.forward(getView(), request, response);
	                }

	            } catch (ApplicationException e) {
	                log.error(e);
	                ServletUtility.handleException(e, request, response);
	                return;
	            }

	        }  else if (OP_SIGN_UP.equalsIgnoreCase(op)) {
	        	System.out.println("redirect in userctl");
	            ServletUtility.redirect(ORSView.USER_REGISTRATION_CTL, request,
	                    response);
	            return;

	        }


	        log.debug("UserCtl Method doPost Ended");
	    }

	    @Override
	    protected String getView() {
	    	System.out.println("get view of login ctl");
	        return ORSView.LOGIN_VIEW;
	    }


	
	
}
