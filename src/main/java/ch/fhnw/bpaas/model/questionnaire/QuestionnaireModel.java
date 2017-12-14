package ch.fhnw.bpaas.model.questionnaire;

import java.util.ArrayList;

public class QuestionnaireModel {
	
	private boolean completed;
	private ArrayList<QuestionnaireItem> questions;
	private int rule_counter;
	
	public QuestionnaireModel(){
		questions = new ArrayList<QuestionnaireItem>();
		rule_counter = 0;
	}
	
	public ArrayList<QuestionnaireItem> getQuestions() {
		return questions;
	}

	public boolean isCompleted() {
		return completed;
	}

	public void setCompleted(boolean completed) {
		this.completed = completed;
	}

	public void setQuestions(ArrayList<QuestionnaireItem> questions) {
		this.questions = questions;
	}

	public int getRule_counter() {
		return rule_counter;
	}

	public void setRule_counter(int rule_counter) {
		this.rule_counter = rule_counter;
	}

	


}
