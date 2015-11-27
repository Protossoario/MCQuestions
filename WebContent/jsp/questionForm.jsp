<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<link rel="stylesheet" href="css/bootstrap.min.css" type="text/css" />
<link rel="stylesheet" href="js/jquery-ui-1.11.4.autocomplete/jquery-ui.min.css" type="text/css" />
<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"></script>
<script src="js/jquery-ui-1.11.4.autocomplete/jquery-ui.min.js"></script>
<%
StringBuffer categoriesArray = new StringBuffer();
List<String> categories = (List<String>) request.getAttribute("categories");
for (String c : categories) {
	if (categoriesArray.length() > 0) {
		categoriesArray.append(',');
	}
	categoriesArray.append('"').append(c).append('"');
}
%>
<script>
var categories = [ <%= categoriesArray.toString() %> ];

var updateAnswerPlaceholders = function() {
	$(".input-answer").each(function(index, element) {
		element.placeholder = "Answer #" + (index + 1);
	});
	$("input[type=checkbox]").each(function(index, element) {
		element.value = index;
	});
}

var removeAnswer = function(event) {
	if (answers != 1) {
		var button = event.target;
		button.parentNode.parentNode.remove();
		answers--;
		
		updateAnswerPlaceholders();
	}
}

var answers = 1;

$(document).ready(function() {
	$answerTemplate = $(".input-group-answer");
	$answerTemplate.find("button").click(removeAnswer);
	$answerTemplate = $answerTemplate.first();
	//$answerTemplate.find("input[type=checkbox]").val(0);
	//$answerTemplate.removeAttr("id");
	console.log($answerTemplate);
	
	$("#addAnswerBttn").click(function() {
		answers++;
		var templateClone = $answerTemplate.clone();
		templateClone.addClass("col-sm-offset-2").appendTo("#answerContainer");
		templateClone.find("button").click(removeAnswer);
		templateClone.find(".input-answer").val("");
		templateClone.find("input[type=checkbox]").prop("checked", false);
		
		updateAnswerPlaceholders();
	});
	
	function split( val ) {
		return val.split( /,\s*/ );
	}
	function extractLast( term ) {
		return split( term ).pop();
	}
	$( "#categories" )
		// don't navigate away from the field on tab when selecting an item
		.bind( "keydown", function( event ) {
			if ( event.keyCode === $.ui.keyCode.TAB &&
				$( this ).autocomplete( "instance" ).menu.active ) {
				event.preventDefault();
			}
		})
		.autocomplete({
			minLength: 0,
			source: function( request, response ) {
				// delegate back to autocomplete, but extract the last term
				response( $.ui.autocomplete.filter(
					categories, extractLast( request.term ) ) );
			},
			focus: function() {
				// prevent value inserted on focus
				return false;
			},
			select: function( event, ui ) {
				var terms = split( this.value );
				// remove the current input
				terms.pop();
				// add the selected item
				terms.push( ui.item.value );
				// add placeholder to get the comma-and-space at the end
				terms.push( "" );
				this.value = terms.join( ", " );
				return false;
			}
		});
});
</script>
<style>
div.input-group-answer:not(:first-child) {
	margin-top: 15px;
}
</style>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Multiple-Choice Questions</title>
</head>
<body>
<div class="container">
	<div class="page-header">
		<h3><i class="glyphicon glyphicon-th-list"></i>&nbsp;Add a new question</h3>
	</div>
	<div class="container">
		<c:if test="${ requestScope.success }">
			<div class="alert alert-success" role="alert">
				<strong>Success!</strong> A new question has been added to the database.
			</div>
		</c:if>
		<c:if test="${ not empty requestScope.success && not requestScope.success }">
			<div class="alert alert-danger" role="alert">
				<strong>Error!</strong> ${ requestScope.message }
			</div>
		</c:if>
		<form class="form-horizontal col-sm-11" method="POST">
			<div class="form-group">
				<label for="questionText" class="col-sm-2 control-label">Question Text:</label>
				<div class="col-sm-10 input-group">
					<input class="form-control" type="text" name="questionText" id="questionText" value="${ param.questionText }" placeholder="Type the question text as it should appear to students" />
				</div>
			</div>
			<div class="form-group">
				<div class="col-sm-offset-2 col-sm-10">
					<span class="help-block">Check all answers that are correct. At least one answer must be correct.</span>
				</div>
				<label for="answers" class="col-sm-2 control-label">Answers:</label>
				<div id="answerContainer">
					<c:if test="${ not empty requestScope.answers }">
						<c:forEach var="answer" items="${ requestScope.answers }" varStatus="loopStatus">
							<div class="input-group input-group-answer col-sm-10 ${ loopStatus.index > 0 ? "col-sm-offset-2" : "" }">
								<span class="input-group-addon">
									<input type="checkbox" aria-label="Correct answer?" name="isCorrect" value="${ loopStatus.index }" ${ answer.isCorrect() ? "checked" : "" }>
								</span>
								<input class="form-control input-answer" type="text" name="answers" value="${ answer.getText() }" placeholder="Answer #${ loopStatus.count }" />
								<span class="input-group-btn">
									<button class="btn btn-default" type="button"><small><i class="glyphicon glyphicon-remove"></i></small> Remove</button>
								</span>
							</div>
						</c:forEach>
					</c:if>
					<c:if test="${ empty requestScope.answers }">
						<div class="input-group input-group-answer col-sm-10">
							<span class="input-group-addon">
								<input type="checkbox" aria-label="Correct answer?" name="isCorrect">
							</span>
							<input class="form-control input-answer" type="text" name="answers" placeholder="Answer #1" />
							<span class="input-group-btn">
								<button class="btn btn-default" type="button"><small><i class="glyphicon glyphicon-remove"></i></small> Remove</button>
							</span>
						</div>
					</c:if>
				</div>
			</div>
			<div class="form-group">
				<label for="categories" class="col-sm-2 control-label">Categories:</label>
				<div class="col-sm-10 input-group">
					<input class="form-control" type="text" name="categories" id="categories" value="${ param.categories }" placeholder="Type the categories separated by commas" />
				</div>
			</div>
			<hr class="col-sm-10 col-sm-offset-2">
			<div class="col-sm-10 col-sm-offset-2">
				<div class="col-sm-4 col-sm-offset-2">
					<button type="submit" class="btn btn-primary btn-block"><small><i class="glyphicon glyphicon-ok"></i></small> Submit</a>
				</div>
				<div class="col-sm-4">
					<a id="addAnswerBttn" class="btn btn-default btn-block" href="#" role="button"><small><i class="glyphicon glyphicon-plus"></i></small> Add new answer</a>
				</div>
			</div>
		</form>
	</div>
</div>
</body>
</html>