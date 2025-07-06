<%@ page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="f" uri="http://java.sun.com/jsf/core"%>
<%@ taglib prefix="h" uri="http://java.sun.com/jsf/html"%>

<f:view>
<html>
<head>
    <title>Doctor Availability</title>
    <script src="https://cdn.tailwindcss.com"></script>
    <style>
        .scroll-x-container {
            overflow-x: auto;
            -webkit-overflow-scrolling: touch;
            scrollbar-width: thin;
        }
        .scroll-x-container::-webkit-scrollbar {
            height: 8px;
        }
        .scroll-x-container::-webkit-scrollbar-track {
            background: #e5e7eb;
        }
        .scroll-x-container::-webkit-scrollbar-thumb {
            background: #3b82f6;
            border-radius: 8px;
        }
    </style>
</head>
<body class="bg-gray-100 min-h-screen">
    <div class="max-w-7xl mx-auto py-10 px-4 sm:px-6 lg:px-8 bg-white rounded shadow">
        <h1 class="text-3xl font-bold text-blue-800 mb-6">Doctor Availability</h1>
        
        <p class="text-gray-700 mb-6">
            Doctor ID: <span class="font-semibold text-gray-900">
                <h:outputText value="#{doctorAvailabilityController.doctorId}" />
            </span>
        </p>

        <!-- Search by Date -->
        <h:form>
            <div class="flex flex-col sm:flex-row gap-4 items-center mb-10">
                <label for="searchDate" class="font-semibold text-sm text-gray-600">Search by Date (yyyy-MM-dd):</label>
                <h:inputText id="searchDate" value="#{doctorAvailabilityController.searchDate}"
                    styleClass="border rounded px-3 py-2 w-52"
                    converterMessage="Date format should be yyyy-MM-dd">
                    <f:convertDateTime pattern="yyyy-MM-dd" />
                </h:inputText>
                <h:commandButton value="Search"
                    action="#{doctorAvailabilityController.searchAvailabilityByDate}"
                    styleClass="bg-blue-600 text-white px-4 py-2 rounded hover:bg-blue-700" />
            </div>
        </h:form>

        <!-- Upcoming Grouped Availability 
        <h2 class="text-2xl font-semibold text-blue-700 mb-4">Upcoming Availability</h2>
        <div class="scroll-x-container mb-12">
            <div class="flex space-x-6">
                <h:dataTable value="#{doctorAvailabilityController.groupedAvailabilityList}" var="group"
                             styleClass="flex space-x-6" rowClasses="">
                    <h:column>
                        <div class="bg-blue-50 border border-blue-200 p-4 w-64 rounded-lg shadow-sm">
                            <p class="font-bold text-blue-900 text-lg mb-2">
                                <h:outputText value="#{group.date}">
                                    <f:convertDateTime pattern="yyyy-MM-dd" />
                                </h:outputText>
                            </p>
                            <h:dataTable value="#{group.slots}" var="slot">
                                <h:column>
                                    <div class="text-sm text-gray-800 mb-2">
                                        • <h:outputText value="#{slot.start_time}" /> - 
                                        <h:outputText value="#{slot.end_time}" />
                                    </div>
                                </h:column>
                            </h:dataTable>
                        </div>
                    </h:column>
                </h:dataTable>
            </div>
        </div>
-->
        <!-- Search Results Table -->
        <h:panelGroup rendered="#{not empty doctorAvailabilityController.searchedAvailabilityList}">
            <h2 class="text-2xl font-semibold text-green-700 mb-4">Search Results</h2>
            <div class="overflow-x-auto">
                <h:dataTable value="#{doctorAvailabilityController.searchedAvailabilityList}" var="slot"
                    styleClass="min-w-full text-sm border"
                    headerClass="bg-green-600 text-white"
                    rowClasses="bg-white even:bg-green-50 odd:bg-green-100">
                    
                    <h:column>
                        <f:facet name="header">Date</f:facet>
                        <h:outputText value="#{slot.available_date}">
                            <f:convertDateTime pattern="yyyy-MM-dd" />
                        </h:outputText>
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
