package ch.fhnw.bpaas.webservice;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import org.apache.jena.query.ParameterizedSparqlString;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import com.google.gson.Gson;
import com.google.gson.JsonArray;

import ch.fhnw.bpaas.model.cloudservice.CloudServiceElementModel;
import ch.fhnw.bpaas.model.questionnaire.Answer;
import ch.fhnw.bpaas.model.questionnaire.QuestionnaireItem;
import ch.fhnw.bpaas.model.questionnaire.QuestionnaireModel;
import ch.fhnw.bpaas.webservice.exceptions.DomainSelectionException;
import ch.fhnw.bpaas.webservice.exceptions.NoDomainQuestionLeftException;
import ch.fhnw.bpaas.webservice.exceptions.NoResultsException;
import ch.fhnw.bpaas.webservice.ontology.OntologyManager;
import ch.fhnw.bpaas.webservice.persistence.EntropyCalculation;
import ch.fhnw.bpaas.webservice.persistence.GlobalVariables;

@Path("/questionnaire")
public class Questionnaire {
	
	private Gson gson = new Gson();
	private OntologyManager ontology = OntologyManager.getInstance();
	private boolean debug_properties = false;
	private String hotword_rule = "?value";
	
	@GET
	@Path("/getDomains")
	public Response getDomains() {
		System.out.println("\n####################<start>####################");
		System.out.println("/requested parameters to get question domains" );
		System.out.println("####################<end>####################");
		ArrayList<Answer> result = new ArrayList<Answer>();
		
		try {
				result = queryDomains();
				if (debug_properties){
				for (int index = 0; index < result.size(); index++){
				System.out.println("Element "+index+": ");
				System.out.println("Label -->" + result.get(index).getAnswerLabel());
				System.out.println("ID Name -->" + result.get(index).getAnswerID());
				System.out.println("");
				}
				}
		} catch (NoResultsException e) {
			e.printStackTrace();
		}
		
		String json = gson.toJson(result);
		System.out.println("\n####################<start>####################");
		System.out.println("/search genereated json: " +json);
		System.out.println("####################<end>####################");
		return Response.status(Status.OK).entity(json).build();
	}

	private ArrayList<Answer> queryDomains() throws NoResultsException{
		ParameterizedSparqlString queryStr = new ParameterizedSparqlString();
		ArrayList<Answer> allDomains = new ArrayList<Answer>();
		
		queryStr.append("SELECT ?domain ?label ?order WHERE {");
		queryStr.append("?domain rdfs:subClassOf questionnaire:Question .");
		queryStr.append("?domain rdfs:label ?label .");
		queryStr.append("OPTIONAL { ?domain questionnaire:hasOrderNumberForVisualization ?order  } .");
		queryStr.append("}");
		queryStr.append("ORDER BY DESC(?order)  ?label");

		QueryExecution qexec = ontology.query(queryStr);
		ResultSet results = qexec.execSelect();
		
		if (results.hasNext()) {
			while (results.hasNext()) {
				Answer domain = new Answer();
				
				QuerySolution soln = results.next();
				domain.setAnswerID(soln.get("?domain").toString());
				domain.setAnswerLabel(soln.get("?label").toString());
				
				allDomains.add(domain);
			}
		} else {
			throw new NoResultsException("nore more results");
		}
		qexec.close();
		return allDomains;
	}
	
	@GET
	@Path("/getQuestionsFromDomains")
	public Response getQuestionsFromDomains(@QueryParam("domains") String domains_received) {
		Gson gson = new Gson(); 
		Answer[] domains = gson.fromJson(domains_received, Answer[].class);
		System.out.println("\n####################<start>####################");
		System.out.println("/requested questions from domains" );
		System.out.println("/received " + domains_received + " domains" );
		System.out.println("/received " + domains.length + " domains" );
		System.out.println("####################<end>####################");
		ArrayList<QuestionnaireItem> result = new ArrayList<QuestionnaireItem>();
		
		try {
				result = queryQuestionsFromDomains(domains);
				if (debug_properties){
				for (int index = 0; index < result.size(); index++){
				System.out.println("Element "+index+": ");
				System.out.println("QuestionLabel -->" + result.get(index).getQuestionLabel());
				System.out.println("QuestionURI -->" + result.get(index).getQuestionURI());
				System.out.println("QuestionURI -->" + result.get(index).getAnswerType());
				System.out.println("");
				}
				}
		} catch (NoResultsException e) {
			e.printStackTrace();
		}
		
		String json = gson.toJson(result);
		System.out.println("\n####################<start>####################");
		System.out.println("/search genereated json: " +json);
		System.out.println("####################<end>####################");
		return Response.status(Status.OK).entity(json).build();
	}

	private ArrayList<QuestionnaireItem> queryQuestionsFromDomains(Answer[] domain_received) throws NoResultsException{
		ParameterizedSparqlString queryStr = new ParameterizedSparqlString();
		ArrayList<QuestionnaireItem> allQuestions = new ArrayList<QuestionnaireItem>();
		
		
			
			queryStr.append("SELECT ?question ?label ?qType ?relation ?datatype ?searchnamespace ?searchType ?dTypeLabel ?rule WHERE {");
			queryStr.append("?question rdfs:label ?label .");
			queryStr.append("?question rdf:type ?qType . ");
			queryStr.append("?qType rdfs:subClassOf* questionnaire:AnswerType .");
			queryStr.append("?question rdf:type ?dType .");
			queryStr.append("?dType rdfs:label ?dTypeLabel .");
			queryStr.append("?dType rdfs:subClassOf questionnaire:Question . ");
			queryStr.append("?question questionnaire:questionHasAnnotationRelation ?relation . ");
			queryStr.append("OPTIONAL {?question questionnaire:valueInsertAnswerTypeHasDatatype ?datatype .}");
			queryStr.append("OPTIONAL {?question questionnaire:searchSelectionHasSearchNamespace ?searchnamespace .}");
			queryStr.append("OPTIONAL {?question questionnaire:searchSelectionOnClassesInsteadOfInstances ?searchType .}");
			queryStr.append("OPTIONAL {?dType questionnaire:hasOrderNumberForVisualization ?orderD}");
			queryStr.append("OPTIONAL {?question questionnaire:hasOrderNumberForVisualization ?orderQ}");
			queryStr.append("OPTIONAL {?question questionnaire:questionHasRuleToApply ?rule}");
			String first_part = "FILTER (";
			String middle_part = "";
			String last_part = ")";
			for (int i = 0; i < domain_received.length; i++){
				if (middle_part != ""){
					middle_part =  middle_part + " || ";
				}
				middle_part = middle_part + "?dType = <" + domain_received[i].getAnswerID()+">";
			}
			queryStr.append(first_part + middle_part + last_part);
			queryStr.append("}");
			queryStr.append("ORDER BY DESC(?orderD) DESC(?orderQ)");
		
		QueryExecution qexec = ontology.query(queryStr);
		ResultSet results = qexec.execSelect();
		
		if (results.hasNext()) {
			while (results.hasNext()) {
				QuestionnaireItem question = new QuestionnaireItem();
				
				QuerySolution soln = results.next();
				question.setQuestionURI(soln.get("?question").toString());
				question.setQuestionLabel(soln.get("?label").toString());
				question.setAnswerType(soln.get("?qType").toString());
				question.setDomainLabel(soln.get("?dTypeLabel").toString());
				question.setAnnotationRelation(soln.get("?relation").toString());
				
				if (soln.get("?rule") != null ){
					question.setRuleToApply(soln.get("?rule").toString());
					}
				
				if (soln.get("?qType").toString().equals(GlobalVariables.ANSWERTYPE_SINGLE_SELECTION) || 
						soln.get("?qType").toString().equals(GlobalVariables.ANSWERTYPE_MULTI_SELECTION)){
					//Call for the answers
					question.setAnswerList(new ArrayList<Answer>(getAnswerList(soln.get("?question").toString())));
				}else if (soln.get("?qType").toString().equals(GlobalVariables.ANSWERTYPE_SEARCH_SELECTION)){
					//Call for the namespace
					question.setSearchNamespace(soln.get("?searchnamespace").toString());
					//System.out.println("=====================SEARCHTYPE: " + soln.get("?searchType"));
					//Call for the searchType (Classes of Instances)
					if (soln.get("?searchType").toString() == null ||
						soln.get("?searchType").toString().equals("") ||
						soln.get("?searchType").toString().equals(GlobalVariables.BOOLEAN_FALSE_URI)){
						question.setSearchOnClassesInsteadOfInstances(false);
					} else {
						question.setSearchOnClassesInsteadOfInstances(true);
					}	
				}else if (soln.get("?qType").toString().equals(GlobalVariables.ANSWERTYPE_VALUEINSERT)){
					//Call for the Datatype
					question.setAnswerDatatype(soln.get("?datatype").toString());
					question.setComparisonOperationAnswers(getComparisonOperations());
				}
				
				allQuestions.add(question);
			}
		} else {
			throw new NoResultsException("nore more results");
		}
		qexec.close();
		return allQuestions;
	}
	
	@GET
	@Path("/getSuitableCloudservices")
	public Response getSuitableCloudServices(@QueryParam("questionList") String questionList_received) {
		System.out.println("ecco quante sono le domande: " + questionList_received);
		System.out.println("\n####################<start>####################");
		System.out.println("/requested suitable cloudservices " );
//		ArrayList<QuestionnaireItem> questionList = new ArrayList<QuestionnaireItem>();
//		for (int i = 0; i < questionList_received.size(); i++){
//			Gson gson = new Gson(); 
//			questionList.add(gson.fromJson(questionList_received.get(i), QuestionnaireItem.class));
//		}
		
		//QuestionnaireItem[] questionList = gson.fromJson(questionList_received, QuestionnaireItem[].class);
		Gson gson = new Gson(); 
		//QuestionnaireItem[] questionList = gson.fromJson(questionList_received, QuestionnaireItem[].class);
		QuestionnaireItem[] questionList = gson.fromJson(questionList_received, QuestionnaireItem[].class);
		System.out.println("/received " + questionList_received );
		//System.out.println("/received " + questionList.length + " questions" );
		System.out.println("####################<end>####################");
		ArrayList<Answer> result = new ArrayList<Answer>();
		
		try {
				result = querySuitableCloudservices(questionList);
				
				if (debug_properties){
				for (int index = 0; index < result.size(); index++){
				System.out.println("CS number "+index+": ");
				System.out.println("CloudServiceID -->" + result.get(index).getAnswerID());
				System.out.println("CloudServiceLabel -->" + result.get(index).getAnswerLabel());
				System.out.println("");
				}
				}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		String json = gson.toJson(result);
		System.out.println("\n####################<start>####################");
		System.out.println("/search genereated json: " +json);
		System.out.println("####################<end>####################");
		return Response.status(Status.OK).entity(json).build();
	}
	
	private ArrayList<Answer> querySuitableCloudservices(QuestionnaireItem[] questions){
		
		ArrayList<Answer> cloudServices = new ArrayList<Answer>();
		//this method takes in input all the questions, it has to:
		//1. define which questions were answered
		ArrayList<QuestionnaireItem> answeredQuestion = new ArrayList<QuestionnaireItem>();
		for (int i = questions.length-1; i>=0 ; i--){
			if (questions[i].getGivenAnswerList().size() > 0){
				answeredQuestion.add(questions[i]);
				System.out.println("=== ANSERED QUESTIONS: "+ questions[i].getQuestionLabel() +"===");
			}
		}
		
		//2. apply rules to those answers and query the triplestore to find the cloudservices suitable
		//BEWARE: "?value" will be replace with the answerID of the answer!!
		ParameterizedSparqlString queryStr = new ParameterizedSparqlString();
		ArrayList<String> rules = new ArrayList<String>();
		ArrayList<String> filters = new ArrayList<String>();
		
		queryStr.append("SELECT ?cloudService ?csLabel WHERE {");
		queryStr.append("?cloudService rdf:type bpaas:CloudService .");
		queryStr.append("?cloudService rdfs:label ?csLabel .");
		for (int i = 0; i < answeredQuestion.size(); i++){
			switch (answeredQuestion.get(i).getAnswerType()){
				case GlobalVariables.ANSWERTYPE_MULTI_SELECTION:
					
					if (answeredQuestion.get(i).getRuleToApply()!=null){
						for (int j = 0; j < answeredQuestion.get(i).getGivenAnswerList().size(); j++){
							String id = UUID.randomUUID().toString();
							//rules.add("BIND (" + answeredQuestion.get(i).getRuleToApply().replaceAll(hotword_rule, answeredQuestion.get(i).getGivenAnswerList().get(0).getAnswerID()) + ") AS " + id + ") .");
							queryStr.append("BIND (" + answeredQuestion.get(i).getRuleToApply().replaceAll(hotword_rule, answeredQuestion.get(i).getGivenAnswerList().get(0).getAnswerID()) + ") AS " + id + ") .");
							queryStr.append("?cloudService <" + answeredQuestion.get(i).getAnnotationRelation() + "> " + id + " .");
						}
					} else {
						for (int j = 0; j < answeredQuestion.get(i).getGivenAnswerList().size(); j++){
							queryStr.append("?cloudService <" + answeredQuestion.get(i).getAnnotationRelation() + "> " + formatURIForQueries(answeredQuestion.get(i).getGivenAnswerList().get(0).getAnswerID()) + " .");
						}
					}	
					
					break;
				case GlobalVariables.ANSWERTYPE_VALUEINSERT:
					
					if (answeredQuestion.get(i).getRuleToApply()!=null){
						String id = "?"+UUID.randomUUID().toString();
						String id2 = "?"+UUID.randomUUID().toString();
						queryStr.append("?cloudService <" + answeredQuestion.get(i).getAnnotationRelation() + "> " + id + " .");
						//rules.add("BIND (" + answeredQuestion.get(i).getRuleToApply().replaceAll(hotword_rule, answeredQuestion.get(i).getGivenAnswerList().get(0).getAnswerID()) + ") AS " + id2 + ") .");
						queryStr.append("BIND (" + answeredQuestion.get(i).getRuleToApply().replaceAll(hotword_rule, answeredQuestion.get(i).getGivenAnswerList().get(0).getAnswerID()) + ") AS " + id2 + ") .");
						//filters.add("FILTER( " + id + " " + GlobalVariables.getComparisonOperatorString(answeredQuestion.get(i).getComparisonAnswer()) + id2 + " ) .");
						queryStr.append("FILTER( " + id + " " + GlobalVariables.getComparisonOperatorString(answeredQuestion.get(i).getComparisonAnswer()) + id2 + " ) .");

					} else {
						String id = "?"+UUID.randomUUID().toString();
						queryStr.append("?cloudService <" + answeredQuestion.get(i).getAnnotationRelation() + "> " + id + " .");
						//filters.add("FILTER( " + id + " " + GlobalVariables.getComparisonOperatorString(answeredQuestion.get(i).getComparisonAnswer()) + answeredQuestion.get(i).getGivenAnswerList().get(0).getAnswerID() + ") .");
						queryStr.append("FILTER( " + id + " " + GlobalVariables.getComparisonOperatorString(answeredQuestion.get(i).getComparisonAnswer()) + answeredQuestion.get(i).getGivenAnswerList().get(0).getAnswerID() + ") .");

					}
					
					break;
					
				
				default: //single and search answers
					
					if (answeredQuestion.get(i).getRuleToApply()!=null) {
						String id = UUID.randomUUID().toString();
						//rules.add("BIND (" + answeredQuestion.get(i).getRuleToApply().replaceAll(hotword_rule, answeredQuestion.get(i).getGivenAnswerList().get(0).getAnswerID()) + ") AS " + id + ") .");
						queryStr.append("?cloudService <" + answeredQuestion.get(i).getAnnotationRelation() + "> " + id + " .");
						queryStr.append("BIND (" + answeredQuestion.get(i).getRuleToApply().replaceAll(hotword_rule, answeredQuestion.get(i).getGivenAnswerList().get(0).getAnswerID()) + ") AS " + id + ") .");

						
					} else {
						queryStr.append("?cloudService <" + answeredQuestion.get(i).getAnnotationRelation() + "> " + formatURIForQueries(answeredQuestion.get(i).getGivenAnswerList().get(0).getAnswerID()) + " .");
					}
					
					break;
			}
			
		}
		queryStr.append("}");
		queryStr.append("ORDER BY ?csLabel");
		QueryExecution qexec = ontology.query(queryStr);
		ResultSet results = qexec.execSelect();
		
		while (results.hasNext()) {
			QuerySolution soln = results.next();
			cloudServices.add(new Answer(soln.get("?cloudService").toString(), soln.get("?csLabel").toString()));
		}
		return cloudServices;
	}
	
	private Set<Answer> getComparisonOperations() {
		ParameterizedSparqlString queryStr = new ParameterizedSparqlString();
		queryStr.append("SELECT ?operation ?label WHERE {");
		queryStr.append("?operation rdf:type ?type .");
		queryStr.append("?type rdfs:subClassOf* questionnaire:LogicalOperators .");
		queryStr.append("?operation rdfs:label ?label .");
		queryStr.append("}");
		
		QueryExecution qexec = ontology.query(queryStr);
		ResultSet results = qexec.execSelect();
		
		Set<Answer> comparisonOps = new HashSet<Answer>();

		while (results.hasNext()) {
			QuerySolution soln = results.next();
			comparisonOps.add(new Answer(soln.get("?operation").toString(), soln.get("?label").toString()));
		}
		qexec.close();
		return comparisonOps;
	}
	
	private Set<Answer> getAnswerList(String element_URI) {
		ParameterizedSparqlString queryStr = new ParameterizedSparqlString();
		queryStr.append("SELECT ?answer ?label WHERE {");
		queryStr.append("<"+element_URI+">" + " questionnaire:questionHasAnswers ?answer .");
		queryStr.append("?answer rdfs:label ?label .");
		queryStr.append("}");
		
		QueryExecution qexec = ontology.query(queryStr);
		ResultSet results = qexec.execSelect();
		
		Set<Answer> answers = new HashSet<Answer>();

		while (results.hasNext()) {
			QuerySolution soln = results.next();
			answers.add(new Answer(soln.get("?answer").toString(), soln.get("?label").toString()));
		}
		qexec.close();
		return answers;
	}
	
	private String formatURIForQueries(String URI){
		System.out.println("Ecco qui: "+URI);
		if (URI.startsWith("http://")){
			return "<"+URI+">";
		}else{
			return URI;
		}
	}
}

