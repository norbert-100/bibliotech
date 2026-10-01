<%@ page contentType="text/html;charset=UTF-8" isELIgnored="false" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>

<!DOCTYPE html>

<html>
<head>
    <meta charset="UTF-8">
    <title>Détail du livre</title>

```
<style>
    body {
        font-family: Arial, sans-serif;
        background-color: #f4f6f8;
        margin: 0;
        padding: 40px;
    }

    .container {
        max-width: 700px;
        margin: 60px auto;
        background-color: white;
        padding: 35px;
        border-radius: 10px;
        box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
    }

    h1 {
        text-align: center;
        margin-bottom: 30px;
    }

    .info {
        padding: 15px;
        border-bottom: 1px solid #ddd;
    }

    .label {
        font-weight: bold;
    }

    .back-link {
        display: inline-block;
        margin-top: 25px;
        padding: 10px 16px;
        background-color: #2563eb;
        color: white;
        text-decoration: none;
        border-radius: 5px;
    }

    .back-link:hover {
        background-color: #1d4ed8;
    }
</style>
```

</head>

<body>

<div class="container">

```
<h1>Détail du livre</h1>

<div class="info">
    <span class="label">Titre :</span>
    <c:out value="${livre.titre()}" />
</div>

<div class="info">
    <span class="label">Auteur :</span>
    <c:out value="${livre.auteur()}" />
</div>

<div class="info">
    <span class="label">Année de publication :</span>
    <c:out value="${livre.anneePublication()}" />
</div>

<div class="info">
    <span class="label">Exemplaires total :</span>
    <c:out value="${livre.exemplairesTotal()}" />
</div>

<div class="info">
    <span class="label">Exemplaires disponibles :</span>
    <c:out value="${livre.exemplairesDisponibles()}" />
</div>

<a class="back-link"
   href="${pageContext.request.contextPath}/livres">
    ← Retour à la liste
</a>
```

</div>

</body>
</html>
