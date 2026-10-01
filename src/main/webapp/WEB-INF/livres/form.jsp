<%@ page contentType="text/html;charset=UTF-8" isELIgnored="false" %>
<%@ taglib uri="jakarta.tags.functions" prefix="fn" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Formulaire livre</title>

    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #f4f6f8;
            margin: 0;
            padding: 40px;
        }

        .form-container {
            width: 450px;
            margin: 40px auto;
            background-color: white;
            padding: 30px;
            border-radius: 10px;
            box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
        }

        h1 {
            text-align: center;
            margin-bottom: 30px;
        }

        label {
            display: block;
            margin-top: 15px;
            margin-bottom: 5px;
            font-weight: bold;
        }

        input {
            width: 100%;
            box-sizing: border-box;
            padding: 10px;
            border: 1px solid #ccc;
            border-radius: 5px;
        }

        button {
            display: block;
            width: 100%;
            margin-top: 25px;
            padding: 12px;
            border: none;
            border-radius: 5px;
            background-color: #2563eb;
            color: white;
            font-size: 16px;
            cursor: pointer;
        }

        button:hover {
            background-color: #1d4ed8;
        }
    </style>
</head>

<body>

<div class="form-container">

    <h1>Ajouter un livre</h1>
    <c:if test="${not empty error}">
    <p style="color: red;">
        <c:out value="${error}" />
    </p>
</c:if>

    <form method="post"
      action="${pageContext.request.contextPath}/livres${not empty livre ? '/' : ''}${not empty livre ? livre.id() : ''}">

        <label for="titre">Titre :</label>
       <input type="text"
       id="titre"
       name="titre"
       value="${fn:escapeXml(titre)}">

<c:if test="${not empty errors['titre']}">
    <p style="color:red;">
        <c:out value="${errors['titre']}" />
    </p>
</c:if>
       

        <label for="auteur">Auteur :</label>
        <input type="text"
       id="auteur"
       name="auteur"
       value="${fn:escapeXml(auteur)}">

<c:if test="${not empty errors['auteur']}">
    <p style="color:red;">
        <c:out value="${errors['auteur']}" />
    </p>
</c:if>

        <label for="anneePublication">Année :</label>
        <input type="number"
       id="anneePublication"
       name="anneePublication"
       value="${fn:escapeXml(anneePublication)}">

<c:if test="${not empty errors['anneePublication']}">
    <p style="color:red;">
        <c:out value="${errors['anneePublication']}" />
    </p>
</c:if>

        <label for="exemplairesTotal">Exemplaires total :</label>
        <input type="number"
       id="exemplairesTotal"
       name="exemplairesTotal"
       value="${fn:escapeXml(exemplairesTotal)}">

<c:if test="${not empty errors['exemplairesTotal']}">
    <p style="color:red;">
        <c:out value="${errors['exemplairesTotal']}" />
    </p>
</c:if>
        <label for="exemplairesDisponibles">Exemplaires disponibles :</label>
        <input type="number"
       id="exemplairesDisponibles"
       name="exemplairesDisponibles"
       value="${fn:escapeXml(exemplairesDisponibles)}">

<c:if test="${not empty errors['exemplairesDisponibles']}">
    <p style="color:red;">
        <c:out value="${errors['exemplairesDisponibles']}" />
    </p>
</c:if>

        <button type="submit">Enregistrer</button>

    </form>

</div>

</body>
</html>