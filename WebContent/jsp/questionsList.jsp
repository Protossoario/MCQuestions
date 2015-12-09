<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<link rel="stylesheet" href="css/bootstrap.min.css" type="text/css" />
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Multiple Choice Questions</title>
</head>
<body>
<div class="container">
	<div class="page-header">
		<h3>
			<i class="glyphicon glyphicon-th-list"></i>&nbsp;List of Questions
			<span class="pull-right small"><a href="/MCQuestions_easanc/addQuestion"><i class="glyphicon glyphicon-plus"></i>&nbsp;Add new question</a></span>
		</h3>
	</div>
	<div class="container">
		<c:forEach var="question" items="${ requestScope.questions }" varStatus="questionLoop">
			<c:if test="${ questionLoop.index % 4 == 0 }">
				<div class="row">
			</c:if>
			<div class="col-sm-3">
				<div class="panel panel-default">
					<div class="panel-heading">
						<h5>
							Question ID #${ question.getId() }
							<span class="pull-right"><a href="/MCQuestions_easanc/editQuestion/${ question.getId() }"><i class="glyphicon glyphicon-edit"></i>&nbsp;Edit</a></span>
						</h5>
					</div>
					<div class="panel-body">
						${ question.getText() }
					</div>
					<table class="table">
						<c:forEach var="answer" items="${ question.getAnswers() }" varStatus="answerLoop">
							<tr class="${ answer.isCorrect() ? "success" : "" }">
								<td class="col-sm-2"><small>#${ answerLoop.count }</small></td>
								<td class="col-sm-8">${ answer.getText() }</td>
								<td class="col-sm-2">
									<c:if test="${ answer.isCorrect() }">
										<i class="glyphicon glyphicon-ok"></i>
									</c:if>
									<c:if test="${ not answer.isCorrect() }">
										<i class="glyphicon glyphicon-remove"></i>
									</c:if>
								</td>
							</tr>
						</c:forEach>
					</table>
					<div class="panel-footer">
						${ question.getCategories() }
					</div>
				</div>
			</div>
			<c:if test="${ questionLoop.index % 4 == 3 }">
				</div>
			</c:if>
		</c:forEach>
	</div>
</div>
</body>
</html>