package easanc.servlet;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import easanc.models.Question;
import easanc.models.QuestionSQL;

/**
 * Servlet implementation class QuestionList
 */
@WebServlet("/questions")
public class QuestionList extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private QuestionSQL sql;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public QuestionList() {
        super();
        this.sql = new QuestionSQL();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		List<Question> questions;
		try {
			questions = sql.getAllQuestions();
		} catch (SQLException e) {
			questions = null;
			e.printStackTrace();
		}
		
		request.setAttribute("questions", questions);
		
		RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/jsp/questionsList.jsp");
		dispatcher.forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
