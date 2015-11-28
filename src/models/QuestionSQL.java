package models;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class QuestionSQL {
	private Connection conn;
	
	public QuestionSQL() {
		conn = null;
	}
	
	/**
	 * Opens a connection to the mcquestions database on localhost.
	 * @return true if connection was created successfully
	 * @return false if connection failed to create
	 */
	private boolean initConnection() {
		if (conn != null) {
			return true;
		}
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/mcquestions", "root", "");
		} catch (ClassNotFoundException e) {
			System.out.println("MySQL JDBC drivers not properly configured! Verify that the .jar file is in the build path for this project.");
			e.printStackTrace();
		} catch (SQLException e) {
			System.out.println("Could not connect to the database. Make sure the following are true:");
			System.out.println("- A MySQL instance is running on localhost and listening on port 3306");
			System.out.println("- The schema \"mcquestions\" exists on the server");
		}
		return conn != null;
	}
	
	private void closeConnection() {
		if (conn != null) {
			try {
				conn.close();
			} catch (SQLException e) {
				System.out.println("Could not close the connection to the database.");
				e.printStackTrace();
			}
			conn = null;
		}
	}
	
	public Set<String> getCategoriesWithQuestionId(int questionId) throws SQLException {
		TreeSet<String> res = new TreeSet<String>();
		
		PreparedStatement ps = conn.prepareStatement("SELECT * FROM question_category WHERE question_id_question = ?");
		ps.setInt(1, questionId);
		
		ResultSet rs = ps.executeQuery();
		while (rs.next()) {
			res.add(rs.getString(1));
		}
		
		return res;
	}
	
	public List<Answer> getAnswersWithQuestionId(int questionId) throws SQLException {
		ArrayList<Answer> res = new ArrayList<Answer>();
		
		PreparedStatement ps = conn.prepareStatement("SELECT * FROM answer WHERE question_id_question = ?");
		ps.setInt(1, questionId);
		
		ResultSet rs = ps.executeQuery();
		while (rs.next()) {
			Answer a = new Answer();
			a.setId(rs.getInt(1));
			a.setText(rs.getString(3));
			a.setCorrect(rs.getBoolean(4));
			res.add(a);
		}
		
		return res;
	}
	
	private void loadQuestions(ResultSet rs, List<Question> l) throws SQLException {
		while (rs.next()) {
			Question q = new Question();
			q.setId(rs.getInt(1));
			q.setText(rs.getString(2));
			q.setAnswers(getAnswersWithQuestionId(q.getId()));
			q.setCategories(getCategoriesWithQuestionId(q.getId()));
			l.add(q);
		}
	}
	
	public List<Question> getAllQuestions() throws SQLException {
		ArrayList<Question> res = new ArrayList<Question>();
		if ( ! initConnection()) {
			return res;
		}
		
		Statement stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery("SELECT * FROM question");
		
		loadQuestions(rs, res);
		
		closeConnection();
		return res;
	}
	
	public Question getQuestionWithId(int id) throws SQLException {
		if ( ! initConnection()) {
			return null;
		}
		
		PreparedStatement ps = conn.prepareStatement("SELECT * FROM question WHERE id_question = ?");
		ps.setInt(1, id);
		ResultSet rs = ps.executeQuery();
		if (rs.next()) {
			Question q = new Question();
			q.setId(rs.getInt(1));
			q.setText(rs.getString(2));
			q.setAnswers(getAnswersWithQuestionId(q.getId()));
			q.setCategories(getCategoriesWithQuestionId(q.getId()));
			return q;
		}
		return null;
	}
	
	public List<String> getAllCategories() {
		List<String> res = new ArrayList<String>();
		
		if (! initConnection()) {
			return res;
		}
		
		try {
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT DISTINCT name FROM question_category");
			
			while (rs.next()) {
				res.add(rs.getString(1));
			}
		} catch (SQLException e) {
			System.out.println("Error fetching categories from the database.");
			e.printStackTrace();
		}
		
		closeConnection();
		return res;
	}
	
	public boolean insertQuestion(Question q) {
		if (! initConnection()) {
			return false;
		}
		int rows = 0;
		
		try {
			boolean autocommit = conn.getAutoCommit();
			conn.setAutoCommit(false);
			PreparedStatement ps = conn.prepareStatement("INSERT INTO question (text) VALUES (?)", Statement.RETURN_GENERATED_KEYS);
			ps.setString(1, q.getText());
			
			rows = ps.executeUpdate();
			
			ResultSet rs = ps.getGeneratedKeys();
			if (rs.next()) {
				q.setId(rs.getInt(1));
			} else {
				throw new SQLException("No question id generated.");
			}
			
			ps = conn.prepareStatement("INSERT INTO question_category (name, question_id_question) VALUES(?, ?)");
			ps.setInt(2, q.getId());
			for (String c : q.getCategories()) {
				ps.setString(1, c);
				rows += ps.executeUpdate();
			}
			
			ps = conn.prepareStatement("INSERT INTO answer (question_id_question, text, correct) VALUES (?, ?, ?)");
			ps.setInt(1, q.getId());
			for (Answer a : q.getAnswers()) {
				ps.setString(2, a.getText());
				ps.setBoolean(3, a.isCorrect());
				rows += ps.executeUpdate();
			}
			
			conn.commit();
			conn.setAutoCommit(autocommit);
		} catch (SQLException e) {
			System.out.println("Could not insert question into the database.");
			e.printStackTrace();
			return false;
		}
		
		closeConnection();
		return rows == (1 + q.getAnswers().size() + q.getCategories().size());
	}
	
	public boolean updateQuestion(Question q) {
		if (q.getId() == -1) {
			return false;
		}
		
		if (! initConnection()) {
			return false;
		}
		int rows = 0;
		try {
			boolean autocommit = conn.getAutoCommit();
			conn.setAutoCommit(false);
			
			PreparedStatement ps = conn.prepareStatement("UPDATE question SET text = ? WHERE id_question = ?");
			ps.setString(1, q.getText());
			ps.setInt(2, q.getId());
			
			ps.executeUpdate();
			
			ps = conn.prepareStatement("DELETE FROM answer WHERE question_id_question = ?");
			ps.setInt(1, q.getId());
			
			ps.executeUpdate();
			
			ps = conn.prepareStatement("DELETE FROM question_category WHERE question_id_question = ?");
			ps.setInt(1, q.getId());
			
			ps.executeUpdate();
			
			ps = conn.prepareStatement("INSERT INTO question_category (name, question_id_question) VALUES (?, ?)");
			ps.setInt(2, q.getId());
			for (String c : q.getCategories()) {
				ps.setString(1, c);
				rows += ps.executeUpdate();
			}
			
			ps = conn.prepareStatement("INSERT INTO answer (question_id_question, text, correct) VALUES (?, ?, ?)");
			ps.setInt(1, q.getId());
			for (Answer a : q.getAnswers()) {
				ps.setString(2, a.getText());
				ps.setBoolean(3, a.isCorrect());
				rows += ps.executeUpdate();
			}
			
			conn.commit();
			conn.setAutoCommit(autocommit);
		} catch (SQLException e) {
			System.out.println("Could not update question in the database.");
			e.printStackTrace();
			return false;
		}
		
		closeConnection();
		return rows == (q.getAnswers().size() + q.getCategories().size());
	}
	
	public List<Question> findQuestionsWithTags(List<String> tags) {
		List<Question> result = new ArrayList<Question>();
		
		if (tags.size() == 0) {
			return result;
		}
		
		if (! initConnection()) {
			return result;
		}
		
		StringBuilder regex = new StringBuilder();
		regex.append('\"');
		int i = 0;
		for (String t : tags) {
			if (i > 0) {
				regex.append('|');
			}
			
			regex.append(t);
			i++;
		}
		regex.append('\"');
				
		try {
			PreparedStatement ps = conn.prepareStatement("SELECT * FROM question WHERE id_question IN (SELECT question_id_question FROM question_category WHERE LOWER(name) REGEXP " + regex.toString() + ")");
			
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				int id = rs.getInt(1);
				String text = rs.getString(2);
				List<Answer> answers = getAnswersWithQuestionId(id);
				Set<String> categories = getCategoriesWithQuestionId(id);
								
				result.add(new Question(id, text, answers, categories));
			}
		} catch (SQLException e) {
			System.out.println("Search with tags failed. Make sure the database is running and the schema mcquestions exists.");
			e.printStackTrace();
		}
		
		closeConnection();
		return result;
	}
}
