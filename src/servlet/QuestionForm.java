package servlet;

import java.io.IOException;
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
 * Servlet implementation class QuestionsServlet
 */
@WebServlet("/addQuestion")
public class QuestionForm extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private QuestionSQL sql;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public QuestionForm() {
        super();
        this.sql = new QuestionSQL();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		List<String> categories = sql.getAllCategories();
		request.setAttribute("categories", categories);
		RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/jsp/questionForm.jsp");
		dispatcher.forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Set<String> categories = new TreeSet<String>();
		StringTokenizer st = new StringTokenizer(request.getParameter("categories"), ", ");
		while (st.hasMoreTokens()) {
			categories.add(st.nextToken());
		}
		
		List<Answer> answers = new ArrayList<Answer>();
		for (String a : request.getParameterValues("answers")) {
			answers.add(new Answer(a));
		}
		for (String correctIndex : request.getParameterValues("isCorrect")) {
			int correctInd = Integer.parseInt(correctIndex);
			answers.get(correctInd).setCorrect(true);
		}
		
		Question q = new Question(request.getParameter("questionText"), answers, categories);
		if (this.sql.insertQuestion(q)) {
			request.setAttribute("success", true);
		} else {
			request.setAttribute("success", false);
		}
		
		doGet(request, response);
	}

}
