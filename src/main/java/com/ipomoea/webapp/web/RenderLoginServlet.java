package com.ipomoea.webapp.web;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;

import com.github.sqrlserverjava.AuthPageData;
import com.github.sqrlserverjava.SqrlConfig;
import com.github.sqrlserverjava.SqrlServerOperations;
import com.github.sqrlserverjava.exception.SqrlException;
import com.github.sqrlserverjava.util.SqrlConfigHelper;
import com.github.sqrlserverjava.util.SqrlUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(urlPatterns = { "/login" })
public class RenderLoginServlet extends HttpServlet {
	
	private static final long serialVersionUID = 4L;
	private SqrlConfig sqrlConfig = null;
	private SqrlServerOperations sqrlServerOperations = null;
	
	
	@Override
	protected void doGet(final HttpServletRequest request, final HttpServletResponse response)
			throws ServletException, IOException {
		
		System.out.println("RenderLogin");
		
		//init SqrlConfig and SqrlServerOperations
		if (sqrlConfig == null) {
			sqrlConfig = SqrlConfigHelper.loadFromClasspath();
		}
		if (sqrlServerOperations == null && sqrlConfig != null) {
			sqrlServerOperations = new SqrlServerOperations(sqrlConfig);
		}
		
		try {
			final AuthPageData pageData = sqrlServerOperations.browserFacingOperations().prepareSqrlAuthPageData(request, response, 150);
			final ByteArrayOutputStream baos = pageData.getQrCodeOutputStream();
			baos.flush();
			final byte[] imageInByteArray = baos.toByteArray();
			baos.close();

			// Since this is being passed to the browser, we use regular Base64 encoding, NOT SQRL specific
			// Base64URL encoding
			final String b64 = new StringBuilder("data:image/").append(pageData.getHtmlFileType(sqrlConfig))
					.append(";base64, ").append(Base64.getEncoder().encodeToString(imageInByteArray)).toString();
			// TODO_DOC add doc FAQ link
			final int pageRefreshSeconds = sqrlConfig.getNutValidityInSeconds() / 2;
			request.setAttribute(Constants.JSP_PAGE_REFRESH_SECONDS, Integer.toString(pageRefreshSeconds));
			request.setAttribute("sqrlqr64", b64);
			final String sqrlUrl = pageData.getUrl().toString();
			request.setAttribute("sqrlurl", sqrlUrl);
			request.setAttribute("cpsEnabled", Boolean.toString(sqrlConfig.isEnableCps()));
			request.setAttribute("cpsNotEnabled", Boolean.toString(!sqrlConfig.isEnableCps()));
			// The url that will get sent to the SQRL client via CPS must include a cancel page (can) if case of failure
			final String sqrlurlWithCan = sqrlUrl;
			request.setAttribute("sqrlurlwithcan64", SqrlUtil.sqrlBase64UrlEncode(sqrlurlWithCan));
			request.setAttribute("sqrlqrdesc", "Scan with mobile SQRL app");
			request.setAttribute("correlator", pageData.getCorrelator());
			
		} catch (SqrlException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		request.getRequestDispatcher("login.jsp").forward(request, response);
	}
}