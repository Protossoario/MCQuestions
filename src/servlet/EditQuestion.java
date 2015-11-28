package servlet;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.TreeSet;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import models.Answer;
import models.Question;
import models.QuestionSQL;

/**
 * Servlet implementation class EditQuestion
 */
@WebServlet("/editQuestion/*")
public class EditQuestion extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private QuestionSQL sql;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public EditQuestion() {
        super();
        this.sql = new QuestionSQL();
    }
    
    private int getIdFromPathInfo(String pathInfo) {
    	if (pathInfo == null || pathInfo.length() == 1) {
    		return -1;
    	} else {
    		int id;
			try {
				id = Integer.parseInt(pathInfo.substring(1));
			} catch (NumberFormatException e) {
				// Path parameter could not be parsed to a number
				id = -1;
			}
			return id;
    	}
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String pathInfo = request.getPathInfo();
		int id = getIdFromPathInfo(pathInfo);
		
		if (id == -1) {
			response.sendRedirect(response.encodeRedirectURL("http://localhost:8080/MCQuestions/questions"));
		} else {
			try {
				Question q = sql.getQuestionWithId(id);
				request.setAttribute("questionText", q.getText());
				request.setAttribute("answers", q.getAnswers());
				request.setAttribute("categories", q.getCategoriesAsString());
			} catch (SQLException e) {
				// Question not found
				e.printStackTrace();
			}
			
			List<String> categoriesList = sql.getAllCategories();
			request.setAttribute("categoriesList", categoriesList);
			
			RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/jsp/questionForm.jsp");
			dispatcher.forward(request, response);
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		int id = getIdFromPathInfo(request.getPathInfo());
		if (id == -1) {
			response.sendRedirect(response.encodeRedirectURL("http://localhost:8080/MCQuestions/questions"));
		} else {
			Set<String> categories = new TreeSet<String>();
			StringTokenizer st = new StringTokenizer(request.getParameter("categories"), ", ");
			while (st.hasMoreTokens()) {
				categories.add(st.nextToken());
			}
			
			List<Answer> answers = new ArrayList<Answer>();
			for (String a : request.getParameterValues("answers")) {
				answers.add(new Answer(a));
			}
			if (request.getParameterValues("isCorrect") != null) {
				for (String correctIndex : request.getParameterValues("isCorrect")) {
					int correctInd = Integer.parseInt(correctIndex);
					answers.get(correctInd).setCorrect(true);
				}
			}
			
			Question q = new Question(request.getParameter("questionText"), answers, categories);
			q.setId(id);
			
			if (q.validate()) {
				if (this.sql.updateQuestion(q)) {
					request.setAttribute("success", true);
					request.setAttribute("message", "Your changes to Question #" + q.getId() + " have been saved.");
				} else {
					request.setAttribute("success", false);
					request.setAttribute("message", "Unfortunately, the question could not be saved to the database. Please, try again.");
				}
			} else {
				request.setAttribute("success", false);
				request.setAttribute("message", "Sorry, please make sure all answers and the question text are <strong>not empty</strong>, and that there's <strong>at least one right answer.</strong>");
			}
			
			request.setAttribute("questionText", q.getText());
			request.setAttribute("answers", q.getAnswers());
			request.setAttribute("categories", q.getCategoriesAsString());
			
			List<String> categoriesList = sql.getAllCategories();
			request.setAttribute("categoriesList", categoriesList);
			
			RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/jsp/questionForm.jsp");
			dispatcher.forward(request, response);
		}
	}

}
