package api;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import models.Question;
import models.QuestionSQL;

@Path("/questions")
public class QuestionsResource {
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public String get() {
		List<Question> res;
		QuestionSQL sql = new QuestionSQL();
		try {
			res = sql.getAllQuestions();
		} catch (SQLException e) {
			System.out.println("Could not load questions from the database.");
			e.printStackTrace();
			res = new ArrayList<Question>();
		}
		StringBuilder json = new StringBuilder();
		json.append('[');
		int i = 0;
		for (Question q : res) {
			json.append(q.toJSON());
			i++;
			if (i < res.size()) {
				json.append(',');
			}
		}
		json.append(']');
		return json.toString();
	}
	
	@GET
	@Path("{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public String get(@PathParam("id") int id) {
		QuestionSQL sql = new QuestionSQL();
		try {
			Question q = sql.getQuestionWithId(id);
			if (q == null) {
				return "";
			} else {
				return q.toJSON();
			}
		} catch (SQLException e) {
			System.out.println("Could not load question from the database.");
			e.printStackTrace();
			return "";
		}
	}
}
