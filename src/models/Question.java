package models;

import java.util.List;
import java.util.Set;

public class Question {
	private int id;
	private String text;
	private List<Answer> answers;
	private Set<String> categories;
	
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
	
	public String toJSON() {
		StringBuilder json = new StringBuilder();
		
		json.append("{id:");
		json.append(this.id);
		json.append(",text:");
		json.append(this.text);
		json.append(",answers:[");
		for (int i = 0; i < this.answers.size(); i++) {
			Answer a = this.answers.get(i);
			json.append("{id:");
			json.append(a.getId());
			json.append(",text:");
			json.append(a.getText());
			json.append(",correct:");
			json.append(a.isCorrect());
			json.append('}');
			if (i != this.answers.size() + 1) {
				json.append(',');
			}
		}
		json.append("],categories:[");
		int i = 0;
		for (String category : this.categories) {
			json.append(category);
			i++;
			if (i < this.categories.size()) {
				json.append(',');
			}
		}
		json.append(']');
		json.append('}');
		
		return json.toString();
	}
}
