<%@ page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="f" uri="http://java.sun.com/jsf/core"%>
<%@ taglib prefix="h" uri="http://java.sun.com/jsf/html"%>

<f:view>
	<html>
<head>
<title>Doctor Availability</title>
<script src="https://cdn.tailwindcss.com"></script>
<style>
.scroll-container {
	scrollbar-width: thin;
	scrollbar-color: #3b82f6 #e5e7eb;
}

.scroll-container::-webkit-scrollbar {
	height: 8px;
}

.scroll-container::-webkit-scrollbar-track {
	background: #e5e7eb;
	border-radius: 10px;
}

.scroll-container::-webkit-scrollbar-thumb {
	background-color: #3b82f6;
	border-radius: 10px;
}

.availability-card {
	transition: all 0.3s ease;
}

.availability-card:hover {
	transform: translateY(-2px);
	box-shadow: 0 10px 15px -3px rgba(0, 0, 0, 0.1);
}

.availability-card.selected {
	border-color: #3b82f6;
	background-color: #eff6ff;
}
</style>
</head>
 <body class="bg-gray-50 min-h-screen">
        <div class="max-w-6xl mx-auto py-8 px-4">
            <!-- Header -->
            <div class="flex flex-col md:flex-row justify-between items-start md:items-center mb-8">
                <div>
                    <h1 class="text-3xl font-bold text-blue-800">Doctor Availability</h1>
                    <p class="text-gray-600 mt-1">
                        Doctor ID: <span class="font-medium text-gray-800">
                            <h:outputText value="#{doctorAvailabilityController.doctorId}" />
                        </span>
                    </p>
                </div>
                
                <!-- Search Form -->
                <div class="mt-4 md:mt-0">
                    <h:form>
                        <div class="flex items-center gap-2">
                            <h:inputText id="searchDate"
                                value="#{doctorAvailabilityController.searchDate}"
                                styleClass="border border-gray-300 rounded-md px-3 py-2 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-blue-500"
                                converterMessage="Invalid date format">
                                <f:convertDateTime pattern="yyyy-MM-dd" />
                                <f:attribute name="placeholder" value="yyyy-MM-dd" />
                            </h:inputText>
                            <h:commandButton value="Search"
                                action="#{doctorAvailabilityController.searchAvailabilityByDate}"
                                styleClass="bg-blue-600 text-white px-4 py-2 rounded-md hover:bg-blue-700 transition-colors focus:outline-none focus:ring-2 focus:ring-blue-500 focus:ring-offset-2" />
                        </div>
                    </h:form>
                </div>
            </div>


		<!-- Rest of your JSP content remains the same -->
		<!-- Upcoming Availability (Horizontal Scroll) -->
		<div class="mb-10">
			<h2 class="text-xl font-semibold text-gray-800 mb-4">Upcoming
				Availability</h2>
			<div class="scroll-container overflow-x-auto pb-4">
				<div class="flex gap-4 w-max">
					<h:dataTable
						value="#{doctorAvailabilityController.groupedAvailabilityList}"
						var="group" styleClass="flex gap-4">
						<h:column>
							<div
								class="availability-card bg-white border border-gray-200 rounded-lg p-4 w-64 flex-shrink-0"
								style="box-shadow: 0 4px 6px -1px rgba(0, 0, 0, 0.1), 0 2px 4px -1px rgba(0, 0, 0, 0.06);">
								<h3 class="font-bold text-lg text-blue-700 mb-3">
									<h:outputText value="#{group.date}">
										<f:convertDateTime pattern="EEEE, MMMM d" />
									</h:outputText>
								</h3>
								<div class="space-y-2">
									<h:dataTable value="#{group.slots}" var="slot"
										styleClass="w-full">
										<h:column>
											<h:commandLink
												action="#{doctorAvailabilityController.selectAvailability(slot)}"
												styleClass="block py-2 px-3 rounded hover:bg-blue-50 transition-colors">
												<div class="flex justify-between items-center">
													<span class="text-gray-700"> <h:outputText
															value="#{slot.start_time}" /> - <h:outputText
															value="#{slot.end_time}" />
													</span> <span class="text-xs font-medium px-2 py-1 rounded-full"
														style="background-color: #{slot.slot_type == 'STANDARD' ? '#dbeafe' : '#f0fdf4'}; color: #{slot.slot_type == 'STANDARD' ? '#1d4ed8' : '#047857'}">
														<h:outputText value="#{slot.slot_type}" />
													</span>
												</div>
											</h:commandLink>
										</h:column>
									</h:dataTable>
								</div>
							</div>
						</h:column>
					</h:dataTable>
				</div>
			</div>
		</div>

		<!-- Selected Availability Details -->
		<h:panelGroup
			rendered="#{doctorAvailabilityController.selectedAvailability != null}">
			<div
				class="bg-white rounded-lg shadow-md p-6 mb-8 border border-gray-200">
				<h2 class="text-xl font-semibold text-gray-800 mb-4">Availability
					Details</h2>
				<div class="grid grid-cols-1 md:grid-cols-2 gap-6">
					<div>
						<h3 class="text-lg font-medium text-gray-700 mb-2">Time
							Information</h3>
						<div class="space-y-2">
							<p>
								<span class="font-medium text-gray-600">Date:</span> <span
									class="ml-2 text-gray-800"> <h:outputText
										value="#{doctorAvailabilityController.selectedAvailability.available_date}">
										<f:convertDateTime pattern="EEEE, MMMM d, yyyy" />
									</h:outputText>
								</span>
							</p>
							<p>
								<span class="font-medium text-gray-600">Time Slot:</span> <span
									class="ml-2 text-gray-800"> <h:outputText
										value="#{doctorAvailabilityController.selectedAvailability.start_time}" />
									- <h:outputText
										value="#{doctorAvailabilityController.selectedAvailability.end_time}" />
								</span>
							</p>
							<p>
								<span class="font-medium text-gray-600">Type:</span> <span
									class="ml-2 text-gray-800"> <h:outputText
										value="#{doctorAvailabilityController.selectedAvailability.slot_type}" />
								</span>
							</p>
						</div>
					</div>
					<div>
						<h3 class="text-lg font-medium text-gray-700 mb-2">Capacity
							Information</h3>
						<div class="space-y-2">
							<p>
								<span class="font-medium text-gray-600">Max Capacity:</span> <span
									class="ml-2 text-gray-800"> <h:outputText
										value="#{doctorAvailabilityController.selectedAvailability.max_capacity}" />
								</span>
							</p>
							<p>
								<span class="font-medium text-gray-600">Recurring:</span> <span
									class="ml-2 text-gray-800"> <h:outputText
										value="#{doctorAvailabilityController.selectedAvailability.is_recurring ? 'Yes' : 'No'}" />
								</span>
							</p>
							<p>
								<span class="font-medium text-gray-600">Patient Window:</span> <span
									class="ml-2 text-gray-800"> <h:outputText
										value="#{doctorAvailabilityController.selectedAvailability.patient_window} minutes" />
								</span>
							</p>
						</div>
					</div>
				</div>
				<div class="mt-4">
					<h3 class="text-lg font-medium text-gray-700 mb-2">Notes</h3>
					<p class="text-gray-800 bg-gray-50 p-3 rounded">
						<h:outputText
							value="#{doctorAvailabilityController.selectedAvailability.notes}"
							rendered="#{not empty doctorAvailabilityController.selectedAvailability.notes}" />
						<span class="text-gray-500 italic"
							rendered="#{empty doctorAvailabilityController.selectedAvailability.notes}">
							No notes available </span>
					</p>
				</div>
			</div>
		</h:panelGroup>

		<!-- Search Results -->
		<h:panelGroup
			rendered="#{not empty doctorAvailabilityController.searchedAvailabilityList}">
			<div class="bg-white rounded-lg shadow-md p-6 border border-gray-200">
				<h2 class="text-xl font-semibold text-gray-800 mb-4">Search
					Results</h2>
				<div class="overflow-x-auto">
					<table class="min-w-full divide-y divide-gray-200">
						<thead class="bg-gray-50">
							<tr>
								<th
									class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Date</th>
								<th
									class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Time
									Slot</th>
								<th
									class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Type</th>
								<th
									class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Capacity</th>
								<th
									class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Actions</th>
							</tr>
						</thead>
						<tbody class="bg-white divide-y divide-gray-200">
							<h:dataTable
								value="#{doctorAvailabilityController.searchedAvailabilityList}"
								var="slot" rowClasses="hover:bg-gray-50">
								<h:column>
									<h:outputText value="#{slot.available_date}">
										<f:convertDateTime pattern="MMM d, yyyy" />
									</h:outputText>
								</h:column>
								<h:column>
									<h:outputText value="#{slot.start_time}" /> - <h:outputText
										value="#{slot.end_time}" />
								</h:column>
								<h:column>
									<span class="px-2 py-1 text-xs font-medium rounded-full"
										style="background-color: #{slot.slot_type == 'STANDARD' ? '#dbeafe' : '#f0fdf4'}; color: #{slot.slot_type == 'STANDARD' ? '#1d4ed8' : '#047857'}">
										<h:outputText value="#{slot.slot_type}" />
									</span>
								</h:column>
								<h:column>
									<h:outputText value="#{slot.max_capacity}" />
								</h:column>
								<h:column>
									<h:commandLink
										action="#{doctorAvailabilityController.selectAvailability(slot)}"
										styleClass="text-blue-600 hover:text-blue-800 hover:underline">
                                            View Details
                                        </h:commandLink>
								</h:column>
							</h:dataTable>
						</tbody>
					</table>
				</div>
			</div>
		</h:panelGroup>
	</div>
</body>
	</html>
</f:view>
