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
<body class="bg-gray-50">
	<div class="container mx-auto px-4 py-8">
		<h:form>
			<div class="bg-white rounded-lg shadow-md p-6 space-y-6">

				<!-- Page Title -->
				<div class="text-2xl font-bold text-blue-800">
					Doctor Availability for ID:
					<h:outputText value="#{doctorAvailabilityController.doctorId}"
						styleClass="ml-2 text-gray-700" />
				</div>

				<!-- Date Picker & Load Button -->
				<div
					class="flex flex-col sm:flex-row items-start sm:items-center gap-4">
					<div>
						<label class="block font-medium text-gray-700 mb-1">Select
							Date</label>
						<h:inputText value="#{doctorAvailabilityController.selectedDate}"
							styleClass="border rounded px-3 py-2 w-48"
							converter="javax.faces.DateTime">
							<f:convertDateTime pattern="yyyy-MM-dd" />
						</h:inputText>
					</div>

					<div class="mt-1">
						<h:commandButton value="Load Availability"
							action="#{doctorAvailabilityController.loadAvailabilityByDate}"
							styleClass="bg-blue-600 hover:bg-blue-700 text-white font-semibold py-2 px-4 rounded" />
					</div>
				</div>

				<!-- Availability Table -->
				<div class="overflow-x-auto">
					<h:dataTable
						value="#{doctorAvailabilityController.availabilityList}"
						var="slot"
						styleClass="min-w-full text-sm text-left border-collapse"
						headerClass="bg-blue-700 text-white font-semibold"
						rowClasses="bg-white even:bg-blue-50 odd:bg-blue-100">

						<h:column>
							<f:facet name="header">Date</f:facet>
							<h:outputText value="#{slot.available_date}" />
						</h:column>

						<h:column>
							<f:facet name="header">Start Time</f:facet>
							<h:outputText value="#{slot.start_time}" />
						</h:column>

						<h:column>
							<f:facet name="header">End Time</f:facet>
							<h:outputText value="#{slot.end_time}" />
						</h:column>

						<h:column>
							<f:facet name="header">Slot Type</f:facet>
							<h:outputText value="#{slot.slot_type}" />
						</h:column>

						<h:column>
							<f:facet name="header">Max Capacity</f:facet>
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
			</div>
		</h:form>
	</div>
</body>
	</html>
</f:view>