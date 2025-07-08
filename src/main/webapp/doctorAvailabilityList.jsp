<%@ page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="f" uri="http://java.sun.com/jsf/core"%>
<%@ taglib prefix="h" uri="http://java.sun.com/jsf/html"%>

<f:view>
	<html>
<head>
<title>Available Dates</title>

<!-- Tailwind CSS -->
<script src="https://cdn.tailwindcss.com"></script>

<!-- Font Awesome (optional) -->
<link rel="stylesheet"
	href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">

<style>
.scroll-x {
	overflow-x: auto;
	max-width: 100%;
	padding: 1rem;
}

table.horizontal-table {
	border-collapse: collapse;
}

table.horizontal-table>tbody {
	display: flex;
	flex-direction: row;
	gap: 1rem;
}

table.horizontal-table>tbody>tr {
	display: flex;
	flex-direction: column;
}

table.horizontal-table td {
	padding: 1rem;
}
</style>
</head>

<body class="bg-gray-100 min-h-screen">
	<h:form>
		<!-- Horizontal Date Cards -->
		<div class="scroll-x">
			<h:dataTable
				value="#{doctorAvailabilityController.groupedAvailabilityList}"
				var="day" styleClass="horizontal-table">
				<h:column>
					<div
						class="bg-white shadow-md rounded-xl p-4 text-center w-36 hover:shadow-lg transition-all">
						<!-- Clickable date button -->
						<h:commandButton value="#{day.displayDate}"
							action="#{appointmentController.loadAvailableSlots}"
							styleClass="w-full bg-blue-500 text-white py-2 rounded hover:bg-blue-600">
							<f:setPropertyActionListener
								target="#{appointmentController.availabilityId}"
								value="#{day.availabilityId}" />
						</h:commandButton>

						<!-- Total slots display -->
						<h:outputText value="#{day.totalSlots} slots available"
							styleClass="block mt-2 text-sm text-green-600 font-semibold" />
					</div>
				</h:column>
			</h:dataTable>
		</div>

		<!-- Selected Availability ID Display -->
		<h:outputText
			value="Selected Availability ID: #{appointmentController.availabilityId}"
			rendered="#{not empty appointmentController.availabilityId}"
			styleClass="block mt-6 text-center text-indigo-600 text-lg font-semibold" />

		<!-- Show message directly with color class based on success -->
		<h:outputText value="#{appointmentController.message}"
			rendered="#{not empty appointmentController.message}"
			styleClass="block mt-2 text-center text-sm font-medium"
			style="color: #{appointmentController.success ? 'green' : 'red'};" />

	</h:form>
</body>
	</html>
</f:view>
