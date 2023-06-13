package co.group.camping.controller;

import java.io.IOException;
import java.util.HashMap;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import co.group.camping.common.Command;
import co.group.camping.main.command.MainCommand;
import co.group.camping.member.command.AjaxCheckId;
import co.group.camping.member.command.MemberInsert;
import co.group.camping.member.command.MemberList;
import co.group.camping.member.command.MemberLogin;
import co.group.camping.member.command.MemberLoginForm;
import co.group.camping.product.command.productInsertForm;
import co.group.camping.product.command.productList;
import co.group.camping.product.command.productSelect;
import co.group.camping.product.command.productDelete;
import co.group.camping.product.command.productEdit;
import co.group.camping.product.command.productEditForm;
import co.group.camping.product.command.productInsert;



@WebServlet("*.do")
public class FrontController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private HashMap<String, Command> map = new HashMap<String, Command>(); 
   
    public FrontController() {
        super();
        // TODO Auto-generated constructor stub
    }

    public void init(ServletConfig config) throws ServletException{
    	map.put("/main.do", new MainCommand());
    	map.put("/memberList.do", new MemberList());
    	map.put("/memberInsert.do", new MemberInsert());
    	map.put("/memberLogin.do", new MemberLogin());
    	map.put("/memberLoginForm.do", new MemberLoginForm());
    	map.put("/ajaxCheckId.do", new AjaxCheckId());
    	// product
    	map.put("/productList.do", new productList()); // 제품목록
    	map.put("/productInsertForm.do", new productInsertForm()); // 등록폼 열기 
    	map.put("/productInsert.do", new productInsert()); // 제품등록 
    	map.put("/productSelect.do", new productSelect()); // 제품 상세보기
    	map.put("/productEditForm.do", new productEditForm()); // 제품 수정 폼 호출
    	map.put("/productEdit.do", new productEdit()); // 제품 수정
    	map.put("/productDelete.do", new productDelete()); // 제품 삭제
    }
	
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// 요청을 분석하고, 수행할 Command를 찾아서 수행하고, 결과를 돌려준다.
		request.setCharacterEncoding("utf-8");  //한글깨짐 방지
		String uri = request.getRequestURI();   //호출한 URI를 구함
		String contextPath = request.getContextPath(); //root 를 구함
		String page = uri.substring(contextPath.length()); //요청한 페이지 구함
		
		Command command = map.get(page);  //수행할 command를 가져온다
		String viewPage = command.exec(request, response);
		
		if(!viewPage.endsWith(".do")) {
			if(viewPage.startsWith("ajax:")) {
				response.setContentType("text/html; charset=UTF-8");
				response.getWriter().append(viewPage.substring(5));
				return;
			}
			viewPage += ".tiles";
			
			RequestDispatcher dispatcher = request.getRequestDispatcher(viewPage);
			dispatcher.forward(request, response);
		}else {
			response.sendRedirect(viewPage);   //결과가 *.do이면 위임해버림
		}		
	}
}
