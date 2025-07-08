<%@ page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="f" uri="http://java.sun.com/jsf/core"%>
<%@ taglib prefix="h" uri="http://java.sun.com/jsf/html"%>
<f:view>
<html>
<head>
    <title>Horizontal Date View</title>

    <!-- Font Awesome -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">

    <!-- ✅ Tailwind CSS CDN -->
    <script src="https://cdn.tailwindcss.com"></script>

    <style>
        .scroll-x {
            overflow-x: auto;
            max-width: 100%;
            padding: 1rem;
        }

        table.horizontal-table {
            border-collapse: collapse;
        }

        table.horizontal-table > tbody {
            display: flex;
            flex-direction: row;
            gap: 1rem;
        }

        table.horizontal-table > tbody > tr {
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
        <div class="scroll-x">
            <h:dataTable
                value="#{doctorAvailabilityController.groupedAvailabilityList}"
                var="day" styleClass="horizontal-table">
                <h:column>
                    <div class="bg-white shadow-md rounded-xl p-4 text-center w-36 hover:shadow-lg transition-all">
                        <div class="text-base font-bold text-gray-800">
                            <h:outputText value="#{day.displayDate}" />
                        </div>
                        <div class="text-sm mt-2 text-green-600 font-semibold">
                            <h:outputText value="#{day.totalSlots} slots available" />
                        </div>
                    </div>
                </h:column>
            </h:dataTable>
        </div>
    </h:form>

</body>
</html>
</f:view>
