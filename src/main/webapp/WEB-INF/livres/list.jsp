<%@ page contentType="text/html;charset=UTF-8" isELIgnored="false" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Liste des livres</title>

    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #f4f6f8;
            margin: 0;
            padding: 40px;
        }

        .container {
            max-width: 1000px;
            margin: 40px auto;
            background-color: white;
            padding: 30px;
            border-radius: 10px;
            box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
        }

        h1 {
            text-align: center;
        }

        table {
            width: 100%;
            border-collapse: collapse;
            margin-top: 20px;
        }

        th, td {
            padding: 12px;
            border-bottom: 1px solid #ddd;
            text-align: left;
        }

        th {
            background-color: #2563eb;
            color: white;
        }

        tr:hover {
            background-color: #f1f5f9;
        }

        a {
            color: #2563eb;
            text-decoration: none;
            font-weight: bold;
        }
        .actions {
    display: flex;
    justify-content: center;
    align-items: center;
    gap: 8px;
}

.btn {
    display: inline-block;
    padding: 8px 12px;
    border-radius: 5px;
    color: white;
    text-decoration: none;
    border: none;
    cursor: pointer;
    font-weight: bold;
}

.btn-detail {
    background-color: green;
}

.btn-modifier {
    background-color: blue;
}

.btn-ajouter {
    background-color: #eab308;
}

.btn-supprimer {
    background-color: red;
}
    </style>
</head>

<body>

<div class="container">

    <h1>Liste des livres</h1>
    <div style="text-align:center; margin-top:20px;">
    <a class="btn btn-ajouter"
       href="${pageContext.request.contextPath}/livres/nouveau">
        Ajouter un livre
    </a>
</div>

    <table>

        <thead>
            <tr>
                <th>Titre</th>
                <th>Auteur</th>
                <th>Année</th>
                <th>Disponibles</th>
                <th>Actions</th>
            </tr>
        </thead>

        <tbody>

            <c:forEach var="livre" items="${livres}">

                <tr>

                    <td>
                        <c:out value="${livre.titre()}" />
                    </td>

                    <td>
                        <c:out value="${livre.auteur()}" />
                    </td>

                    <td>
                        <c:out value="${livre.anneePublication()}" />
                    </td>

                    <td>
                        <c:out value="${livre.exemplairesDisponibles()}" />
                    </td>

                    <td>
    <div class="actions">

        <a class="btn btn-detail"
           href="${pageContext.request.contextPath}/livres/${livre.id()}">
            Détail
        </a>

        <a class="btn btn-modifier"
           href="${pageContext.request.contextPath}/livres/${livre.id()}/edit">
            Modifier
        </a>

        <form method="post"
              action="${pageContext.request.contextPath}/livres/${livre.id()}/delete"
              style="display:inline;">
            <button class="btn btn-supprimer" type="submit">
                Supprimer
            </button>
        </form>

    </div>
</td>
                </tr>

            </c:forEach>

        </tbody>

    </table>

</div>

</body>
</html>