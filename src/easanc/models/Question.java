package easanc.models;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class Question {
	private int id;
	private String text;
	private List<Answer> answers;
	private Set<String> categories;
	
	public Question() {
		this.id = -1;
		this.text = "";
		this.answers = new ArrayList<Answer>();
		this.categories = new TreeSet<String>();
	}
	
	public Question(String text, List<Answer> answers, Set<String> categories) {
		this.id = -1;
		this.text = text;
		this.answers = answers;
		this.categories = categories;
	}
	
	public Question(int id, String text, List<Answer> answers, Set<String> categories) {
		this.id = id;
		this.text = text;
		this.answers = answers;
		this.categories = categories;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public List<Answer> getAnswers() {
		return answers;
	}
	public void setAnswers(List<Answer> answers) {
		this.answers = answers;
	}
	public Set<String> getCategories() {
		return categories;
	}
	public void setCategories(Set<String> categories) {
		this.categories = categories;
	}
	
	public String getCategoriesAsString() {
		StringBuilder result = new StringBuilder();
		for (String c : this.categories) {
			if (result.length() > 0) {
				result.append(", ");
			}
			result.append(c);
		}
		return result.toString();
	}
	
	public String toJSON() {
		StringBuilder json = new StringBuilder();
		
		json.append("{\"id\":");
		json.append(this.id);
		json.append(",\"text\":\"");
		json.append(this.text);
		json.append("\",\"answers\":[");
		for (int i = 0; i < this.answers.size(); i++) {
			if (i > 0) {
				json.append(',');
			}
			Answer a = this.answers.get(i);
			json.append("{\"id\":");
			json.append(a.getId());
			json.append(",\"text\":\"");
			json.append(a.getText());
			json.append("\",\"correct\":");
			json.append(a.isCorrect());
			json.append("}");
		}
		json.append("],\"categories\":[");
		int i = 0;
		for (String category : this.categories) {
			if (i > 0) {
				json.append(',');
			}
			json.append('"' + category + '"');
			i++;
		}
		json.append(']');
		json.append('}');
		
		return json.toString();
	}
	
	public boolean validate() {
		if (this.text.trim() == "") {
			return false;
		}
		
		boolean hasRightAnswer = false;
		for (Answer a : this.answers) {
			if (a.getText().trim() == "") {
				return false;
			}
			if (a.isCorrect()) {
				hasRightAnswer = true;
			}
		}
		
		return hasRightAnswer;
	}
}
