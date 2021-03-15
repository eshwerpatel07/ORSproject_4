package in.co.rays.ORSProj4.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import in.co.rays.ORSProj4.bean.BaseBean;
import in.co.rays.ORSProj4.bean.MarksheetBean;
import in.co.rays.ORSProj4.exception.ApplicationException;
import in.co.rays.ORSProj4.model.MarksheetModel;
import in.co.rays.ORSProj4.util.DataUtility;
import in.co.rays.ORSProj4.util.PropertyReader;
import in.co.rays.ORSProj4.util.ServletUtility;

@WebServlet(name="MarksheetMeritListCtl", urlPatterns = {"/ctl/MarksheetMeritListCtl"})
public class MarksheetMeritListCtl extends BaseCtl{

	private static Logger log = Logger.getLogger(MarksheetMeritListCtl.class);
	/**
	 * 
	 */

	private static final long serialVersionUID = 1L;

	  @Override
	    protected BaseBean populateBean(HttpServletRequest request) {
	        MarksheetBean bean = new MarksheetBean();

	        return bean;
	    }

	  
	    protected void doGet(HttpServletRequest request,
	            HttpServletResponse response) throws ServletException, IOException {

	        log.debug("MarksheetMeritListCtl doGet Start");
	        
//	        String op = DataUtility.getString(request.getParameter("operation"));
	      System.out.println("mmlist doGet>>>>>>>>>");
	        
	        int pageNo = 1;
	        int pageSize = DataUtility.getInt(PropertyReader.getValue("page.size"));
	  //      MarksheetBean bean = (MarksheetBean) populateBean(request);
	        MarksheetModel model = new MarksheetModel();
	        List list;
	        try {
	        	System.out.println("call getmmlist from marksheet model");
	            list = model.getMeritList(pageNo, pageSize);
	            ServletUtility.setList(list, request);
	            } catch (ApplicationException e) {
	            log.error(e);
	            e.printStackTrace();
	            ServletUtility.handleException(e, request, response);
	            return;
	        }
	        if (list == null || list.size() == 0) {
	            ServletUtility.setErrorMessage("No record found ", request);
	        }
	        ServletUtility.setList(list, request);
	        ServletUtility.setPageNo(pageNo, request);
	        ServletUtility.setPageSize(pageSize, request);
	        ServletUtility.forward(getView(), request,response);

	        log.debug("MarksheetMeritListCtl doGet End");
	    }

	    @Override
	    protected void doPost(HttpServletRequest request,
	            HttpServletResponse response) throws ServletException, IOException {
	        log.debug("MarksheetMeritListCtl doGet Start");
	      
	        String op = DataUtility.getString(request.getParameter("operation"));
	        
	        List list = null;
	        int pageNo = DataUtility.getInt(request.getParameter("pageNo"));
	        int pageSize = DataUtility.getInt(request.getParameter("pageSize"));
	        pageNo = (pageNo == 0) ? 1 : pageNo;
	        pageSize = (pageSize == 0) ? DataUtility.getInt(PropertyReader
	                .getValue("page.size")) : pageSize;
	        MarksheetBean bean = (MarksheetBean) populateBean(request);
	        MarksheetModel model = new MarksheetModel();
	        try {
	            if (OP_BACK.equalsIgnoreCase(op)) {
	                ServletUtility.redirect(ORSView.WELCOME_CTL, request, response);
	                return;
	            }
	           list = model.getMeritList(pageNo, pageSize);
	            ServletUtility.setList(list, request);
	            if (list == null || list.size() == 0) {
	                ServletUtility.setErrorMessage("No record found ", request);
	            }
	            ServletUtility.setList(list, request);
	            ServletUtility.setPageNo(pageNo, request);
	            ServletUtility.setPageSize(pageSize, request);
	            ServletUtility.forward(ORSView.MARKSHEET_MERIT_LIST_VIEW, request,
	                    response);
	        } catch (ApplicationException e) {
	            log.error(e);
	            ServletUtility.handleException(e, request, response);
	            return;
	        }
	      log.debug("MarksheetMeritListCtl doPost End");
	    }

	
	
	
	
	
	

	@Override
	protected String getView() {
		// TODO Auto-generated method stub
		return ORSView.MARKSHEET_MERIT_LIST_VIEW;
	}

}