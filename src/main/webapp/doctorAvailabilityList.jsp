<%@ page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="f" uri="http://java.sun.com/jsf/core"%>
<%@ taglib prefix="h" uri="http://java.sun.com/jsf/html"%>

<f:view>
	<html>
<head>
<title>Doctor Availability</title>
<script src="https://cdn.tailwindcss.com"></script>
<link rel="stylesheet"
	href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css" />
</head>
<body class="bg-gray-100 min-h-screen">

	<div class="max-w-5xl mx-auto py-10 px-6 bg-white shadow-lg rounded-lg">
		<h1 class="text-3xl font-bold text-blue-800 mb-4">Doctor
			Availability</h1>
		<p class="text-gray-700 mb-6">
			Doctor ID: <span class="font-semibold text-gray-800"> <h:outputText
					value="#{doctorAvailabilityController.doctorId}" />
			</span>
		</p>

		<!-- Search Form -->
		<h:form>
			<div class="flex flex-col sm:flex-row items-center gap-4 mb-8">
				<label for="searchDate" class="text-gray-600 text-sm font-semibold">Search
					by Date (yyyy-MM-dd):</label>
				<h:inputText id="searchDate"
					value="#{doctorAvailabilityController.searchDate}" required="false"
					styleClass="border rounded-md px-3 py-2 w-48"
					converterMessage="Invalid date format (yyyy-MM-dd)">
					<f:convertDateTime pattern="yyyy-MM-dd" />
				</h:inputText>

				<h:commandButton value="Search"
					action="#{doctorAvailabilityController.searchAvailabilityByDate}"
					styleClass="bg-blue-600 text-white px-4 py-2 rounded hover:bg-blue-700" />
			</div>
		</h:form>

		<!-- Upcoming 5 Days Availability -->
		<h2 class="text-xl font-semibold text-blue-700 mb-2">Upcoming 5
			Days Availability</h2>
		<div class="overflow-x-auto mb-8">
			<h:dataTable
				value="#{doctorAvailabilityController.futureAvailabilityList}"
				var="slot" styleClass="min-w-full text-sm text-left border"
				headerClass="bg-blue-600 text-white font-bold"
				rowClasses="bg-white even:bg-blue-50 odd:bg-blue-100">

				<h:column>
					<f:facet name="header">Date</f:facet>
					<h:outputText value="#{slot.available_date}" />
				</h:column>
				<h:column>
					<f:facet name="header">Start</f:facet>
					<h:outputText value="#{slot.start_time}" />
				</h:column>
				<h:column>
					<f:facet name="header">End</f:facet>
					<h:outputText value="#{slot.end_time}" />
				</h:column>
				<h:column>
					<f:facet name="header">Type</f:facet>
					<h:outputText value="#{slot.slot_type}" />
				</h:column>
				<h:column>
					<f:facet name="header">Max</f:facet>
					<h:outputText value="#{slot.max_capacity}" />
				</h:column>
				<h:column>
					<f:facet name="header">Recurring</f:facet>
					<h:outputText value="#{slot.is_recurring ? 'Yes' : 'No'}" />
				</h:column>
				<h:column>
					<f:facet name="header">Notes</f:facet>
					<h:outputText value="#{slot.notes}" />
				</h:column>

			</h:dataTable>
		</div>

		<!-- Search Result Section -->
		<h:panelGroup
			rendered="#{not empty doctorAvailabilityController.searchedAvailabilityList}">
			<h2 class="text-xl font-semibold text-blue-700 mb-2">Search
				Result</h2>
			<div class="overflow-x-auto">
				<h:dataTable
					value="#{doctorAvailabilityController.searchedAvailabilityList}"
					var="slot" styleClass="min-w-full text-sm text-left border"
					headerClass="bg-green-600 text-white font-bold"
					rowClasses="bg-white even:bg-green-50 odd:bg-green-100">

					<h:column>
						<f:facet name="header">Date</f:facet>
						<h:outputText value="#{slot.available_date}" />
					</h:column>
					<h:column>
						<f:facet name="header">Start</f:facet>
						<h:outputText value="#{slot.start_time}" />
					</h:column>
					<h:column>
						<f:facet name="header">End</f:facet>
						<h:outputText value="#{slot.end_time}" />
					</h:column>
					<h:column>
						<f:facet name="header">Type</f:facet>
						<h:outputText value="#{slot.slot_type}" />
					</h:column>
					<h:column>
						<f:facet name="header">Max</f:facet>
						<h:outputText value="#{slot.max_capacity}" />
					</h:column>
					<h:column>
						<f:facet name="header">Recurring</f:facet>
						<h:outputText value="#{slot.is_recurring ? 'Yes' : 'No'}" />
					</h:column>
					<h:column>
						<f:facet name="header">Notes</f:facet>
						<h:outputText value="#{slot.notes}" />
					</h:column>

				</h:dataTable>
			</div>
		</h:panelGroup>

	</div>
</body>
	</html>
</f:view>
