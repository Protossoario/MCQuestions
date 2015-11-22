package servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import models.QuestionSQL;

/**
 * Servlet implementation class QuestionsServlet
 */
@WebServlet("/addQuestion")
public class QuestionForm extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public QuestionForm() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		QuestionSQL sql = new QuestionSQL();
		List<String> categories = sql.getAllCategories();
		request.setAttribute("categories", categories);
		RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/jsp/questionForm.jsp");
		dispatcher.forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String[] answers = request.getParameterValues("answers");
		String[] isCorrect = request.getParameterValues("isCorrect");
		System.out.println("Answers:");
		int it = 0;
		for (String a : answers) {
			System.out.println("[" + it + "]=" + a);
			it++;
		}
		it = 0;
		System.out.println("Correct answers: ");
		for (String c : isCorrect) {
			System.out.print(c);
			it++;
			if (it < isCorrect.length) {
				System.out.print(", ");
			}
		}
		System.out.println();
		System.out.println("Question: " + request.getParameter("questionText"));
		doGet(request, response);
	}

}
