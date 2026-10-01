package com.bibliotech;
import com.bibliotech.dao.LivreIndisponibleException;
import java.io.IOException;
import com.bibliotech.dao.EmpruntDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/emprunts/*")
public class EmpruntServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        String path = request.getPathInfo();

        if (path == null || path.equals("/")) {
        	EmpruntDAO empruntDAO = new EmpruntDAO();

        	request.setAttribute("emprunts", empruntDAO.findAll());

        	request.getRequestDispatcher("/WEB-INF/emprunts/list.jsp")
        	       .forward(request, response);
        }
    }
    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        String path = request.getPathInfo();

        if ("/nouveau".equals(path)) {

            long livreId = Long.parseLong(request.getParameter("livreId"));
            long etudiantId = Long.parseLong(request.getParameter("etudiantId"));
            int dureeJours = Integer.parseInt(request.getParameter("dureeJours"));

            EmpruntDAO empruntDAO = new EmpruntDAO();

            try {
                empruntDAO.enregistrerEmprunt(
                    livreId,
                    etudiantId,
                    dureeJours
                );
            } catch (LivreIndisponibleException e) {
                response.sendRedirect(
                        request.getContextPath() + "/livres?error=indisponible"
                    );
                    return;
                }

            response.sendRedirect(
                request.getContextPath() + "/emprunts"
            );

        } else if (path != null && path.endsWith("/retour")) {

            long id = Long.parseLong(
                path.substring(1, path.length() - 7)
            );

            EmpruntDAO empruntDAO = new EmpruntDAO();

            // On récupère l'emprunt
            var emprunt = empruntDAO.findById(id);

            if (emprunt.isPresent()) {

                var ancienEmprunt = emprunt.get();

                // On crée l'emprunt avec la date de retour
                var empruntRendu = new com.bibliotech.model.Emprunt(
                    ancienEmprunt.id(),
                    ancienEmprunt.livreId(),
                    ancienEmprunt.etudiantId(),
                    ancienEmprunt.dateEmprunt(),
                    ancienEmprunt.dateRetourPrevue(),
                    java.time.LocalDate.now(),
                    new com.bibliotech.model.StatutEmprunt.Rendu(
                        java.time.LocalDate.now(),
                        java.time.LocalDate.now()
                            .isAfter(ancienEmprunt.dateRetourPrevue())
                    )
                );

                empruntDAO.update(id, empruntRendu);
                empruntDAO.augmenterStock(ancienEmprunt.livreId());
            }

            response.sendRedirect(
                request.getContextPath() + "/emprunts"
            );
        }
    }
}