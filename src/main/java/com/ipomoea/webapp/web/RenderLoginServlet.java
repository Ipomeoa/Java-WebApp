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
import javax.servlet.http.HttpSession;

@WebServlet(urlPatterns = { "/login" })
public class RenderLoginServlet extends HttpServlet {
	
	private static final long serialVersionUID = 4L;
	private SqrlConfig sqrlConfig = null;
	private SqrlServerOperations sqrlServerOperations = null;
	
	
	@Override
	protected void doGet(final HttpServletRequest request, final HttpServletResponse response)
			throws ServletException, IOException {
		//Set the message attribute to be an empty placeholder
		request.setAttribute("message", " ");

		//Make sure that no session is active anymore
		final HttpSession session = request.getSession(false);
		if (session != null) {
			session.invalidate();
		}
		
		//initialise SqrlConfig and SqrlServerOperations
		if (sqrlConfig == null) {
			sqrlConfig = SqrlConfigHelper.loadFromClasspath();
		}
		if (sqrlServerOperations == null && sqrlConfig != null) {
			sqrlServerOperations = new SqrlServerOperations(sqrlConfig);
		}
		
		try {
			//Set SQRL data
			final AuthPageData pageData = sqrlServerOperations.browserFacingOperations().prepareSqrlAuthPageData(request, response, 150);
			
			// Generate SQRL QR code
			final ByteArrayOutputStream baos = pageData.getQrCodeOutputStream();
			baos.flush();
			final byte[] imageInByteArray = baos.toByteArray();
			baos.close();
			final String sqrlqr64 = new StringBuilder("data:image/").append(pageData.getHtmlFileType(sqrlConfig))
					.append(";base64, ").append(Base64.getEncoder().encodeToString(imageInByteArray)).toString();
			// Get the remaining data needed to render the SQRL login page
			final String sqrlUrl = pageData.getUrl().toString();
			final String pageRefreshSeconds = Integer.toString(sqrlConfig.getNutValidityInSeconds() / 2);
			String cpsEnabled = Boolean.toString(sqrlConfig.isEnableCps());
			String correlator = pageData.getCorrelator();
			
			// Set the request attributes so the login page can access them
			request.setAttribute(Constants.JSP_PAGE_REFRESH_SECONDS, pageRefreshSeconds);
			request.setAttribute("sqrlqr64", sqrlqr64);
			request.setAttribute("sqrlurl", sqrlUrl);
			request.setAttribute("cpsEnabled", cpsEnabled);
			request.setAttribute("correlator", correlator);
			// Check for errors
			checkForErrorState(request, response);
			
		} catch (SqrlException e) {
			e.printStackTrace();
		}
		//Render the login page
		request.getRequestDispatcher("login.jsp").forward(request, response);
	}

	public static void redirectToLoginPageWithError(final HttpServletResponse response, ErrorId errorId) {
		if (errorId == null) {
			errorId = ErrorId.GENERIC;
		}
		response.setHeader("Location", "login?error=" + errorId.getId());
		response.setStatus(302);
	}

	private void checkForErrorState(final HttpServletRequest request, final HttpServletResponse response)
			throws ServletException, IOException, SqrlException {
		try {
			final String errorParam = request.getParameter("error");
			if (errorParam == null || errorParam.trim().length() == 0) {
				return;
			}
			final ErrorId errorId = ErrorId.lookup(errorParam);
			// If we have access to the correlator, append the first 5 chars to the message in case it gets reported
			final String correlatorString = sqrlServerOperations.extractSqrlCorrelatorStringFromRequestCookie(request);
			final String errorMessage = errorId.buildErrorMessage(correlatorString);

			displayErrorAndKillSession(request, errorMessage, errorId.isDisplayInRed());
		}
		catch(final Exception e){
			e.printStackTrace();
		}
	}

	private void displayErrorAndKillSession(final HttpServletRequest request, final String errorText, final boolean displayInRed) {
		// Set error text to be displayed on the login.jsp
		String content = errorText;
		if (displayInRed) {
			final StringBuilder buf = new StringBuilder("<font color='red'>");
			buf.append(content);
			buf.append("</font></p>");
			content = buf.toString();
		}
		request.setAttribute("message", content);
		
		// Since we are in an error state, kill the session
		final HttpSession session = request.getSession(false);
		if (session != null) {
			session.invalidate();
		}
	}
}
