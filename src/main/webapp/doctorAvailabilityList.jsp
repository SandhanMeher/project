<%@ page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="f" uri="http://java.sun.com/jsf/core"%>
<%@ taglib prefix="h" uri="http://java.sun.com/jsf/html"%>
<f:view>
	<html>
<head>
<title>Horizontal Date View</title>
<link rel="stylesheet"
	href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
<style>
.scroll-x {
	overflow-x: auto;
	max-width: 100%;
}

table.horizontal-table {
	border-collapse: collapse;
}

table.horizontal-table>tbody {
	display: flex;
	flex-direction: row;
}

table.horizontal-table>tbody>tr {
	display: flex;
	flex-direction: column;
	margin-left: 6px;
}

table.horizontal-table td {
	border: 1px solid #ccc;
	padding: 10px;
	text-align: center;
	width: 140px;
	background: #f3f4f6;
	border-radius: 6px;
}
</style>
</head>
<body class="bg-gray-50">

	<h:form>
		<div class="scroll-x">
			<h:dataTable
				value="#{doctorAvailabilityController.groupedAvailabilityList}"
				var="group" styleClass="horizontal-table">
				<h:column>
					<h:outputText value="#{group.date}">
						<f:convertDateTime pattern="yyyy-MM-dd" />
					</h:outputText>
				</h:column>
			</h:dataTable>
		</div>
	</h:form>

</body>
	</html>
</f:view>
