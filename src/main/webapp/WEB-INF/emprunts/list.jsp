```jsp
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Emprunts en cours</title>

    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #f4f6f8;
            margin: 0;
            padding: 40px;
        }

        h1 {
            text-align: center;
            color: #333;
        }

        table {
            width: 90%;
            margin: 30px auto;
            border-collapse: collapse;
            background-color: white;
        }

        th, td {
            padding: 12px;
            border: 1px solid #ddd;
            text-align: center;
        }

        th {
            background-color: #333;
            color: white;
        }

        .btn-retour {
            background-color: #28a745;
            color: white;
            border: none;
            padding: 8px 12px;
            border-radius: 5px;
            cursor: pointer;
        }

        .btn-retour:hover {
            background-color: #218838;
        }

        .btn-livres {
            display: block;
            width: fit-content;
            margin: 20px auto;
            padding: 10px 15px;
            background-color: #007bff;
            color: white;
            text-decoration: none;
            border-radius: 5px;
        }
    </style>
</head>

<body>

    <h1>Emprunts en cours</h1>

    <table>
        <thead>
            <tr>
                <th>Livre</th>
                <th>Étudiant</th>
                <th>Date d'emprunt</th>
                <th>Date de retour prévue</th>
                <th>Statut</th>
                <th>Action</th>
            </tr>
        </thead>

        <tbody>
            <c:forEach var="emprunt" items="${emprunts}">
                <tr>
                    <td>
                        <c:out value="${emprunt.livreId()}" />
                    </td>

                    <td>
                        <c:out value="${emprunt.etudiantId()}" />
                    </td>

                    <td>
                        <c:out value="${emprunt.dateEmprunt()}" />
                    </td>

                    <td>
                        <c:out value="${emprunt.dateRetourPrevue()}" />
                    </td>

                    <td>
                        <c:out value="${emprunt.statut()}" />
                    </td>

                    <td>
                        <form method="post"
                              action="${pageContext.request.contextPath}/emprunts/${emprunt.id()}/retour">

                            <button type="submit" class="btn-retour">
                                Retour
                            </button>

                        </form>
                    </td>
                </tr>
            </c:forEach>
        </tbody>
    </table>

    <a href="${pageContext.request.contextPath}/livres"
       class="btn-livres">
        Retour au catalogue
    </a>

</body>
</html>
```
