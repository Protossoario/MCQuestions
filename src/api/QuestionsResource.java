package api;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import models.Question;
import models.QuestionSQL;

@Path("/questions")
public class QuestionsResource {
	
	private String questionListToJSON(List<Question> list) {
		StringBuilder json = new StringBuilder();
		json.append('[');
		int i = 0;
		for (Question q : list) {
			if (i > 0) {
				json.append(',');
			}
			json.append(q.toJSON());
			i++;
		}
		json.append(']');
		return json.toString();
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAll() {
		List<Question> res;
		QuestionSQL sql = new QuestionSQL();
		try {
			res = sql.getAllQuestions();
		} catch (SQLException e) {
			System.out.println("Could not load questions from the database.");
			e.printStackTrace();
			res = new ArrayList<Question>();
		}
		return Response.ok(questionListToJSON(res))
				.header("Access-Control-Allow-Origin", "*")
				.header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT")
				.allow("OPTIONS").build();
	}
	
	@GET
	@Path("{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public String getWithId(@PathParam("id") int id) {
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
	
	/**
	 * Consumes an arbitrary number of parameters for each tag to search for.
	 * @param path A series of tags separated by forward-slashes ("/")
	 * @return JSON array of questions which match any of the tags.
	 */
	@GET
	@Path("/tags/{path:.*}")
	@Produces(MediaType.APPLICATION_JSON)
	public String getQuestionsWithTags(@PathParam("path") String path) {
		path = path.replaceAll("[;\'\"%]", ""); // sanitize string to avoid possible SQL injection
		StringTokenizer tok = new StringTokenizer(path, "/");
		List<String> tags = new ArrayList<String>();
		while (tok.hasMoreTokens()) {
			tags.add(tok.nextToken().toLowerCase());
		}
		QuestionSQL sql = new QuestionSQL();
		return questionListToJSON(sql.findQuestionsWithTags(tags));
	}
	
	@POST
	@Path("/find")
	@Produces(MediaType.APPLICATION_JSON)
	public Response findQuestionsWithIds(@FormParam("ids") List<Integer> ids) {
		QuestionSQL sql = new QuestionSQL();
		return Response.ok(questionListToJSON(sql.findQuestionsWithIds(ids)))
				.header("Access-Control-Allow-Origin", "*")
				.header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT")
				.allow("OPTIONS").build();
	}
}
