package com.bibliotech;

import java.io.IOException;
import com.bibliotech.dao.LivreDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import com.bibliotech.model.Livre;
import java.util.HashMap;
import java.util.Map;
@WebServlet("/livres/*")
public class LivreServlet extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest request,
	                     HttpServletResponse response)
	        throws ServletException, IOException {

		String path = request.getPathInfo();

		if (path == null || path.equals("/")) {

		    LivreDAO livreDAO = new LivreDAO();

		    request.setAttribute("livres", livreDAO.findAll());

		    request.getRequestDispatcher("/WEB-INF/livres/list.jsp")
		           .forward(request, response);

		} else if (path.equals("/nouveau")) {

		    request.getRequestDispatcher("/WEB-INF/livres/form.jsp")
		           .forward(request, response);
		    
	} else if (path.endsWith("/edit")) {

	    long id = Long.parseLong(path.substring(1, path.length() - 5));

	    LivreDAO livreDAO = new LivreDAO();
	    Livre livre = livreDAO.findById(id).orElse(null);

	    request.setAttribute("livre", livre);
	    request.setAttribute("titre", livre.titre());
	    request.setAttribute("auteur", livre.auteur());
	    request.setAttribute("anneePublication", livre.anneePublication());
	    request.setAttribute("exemplairesTotal", livre.exemplairesTotal());
	    request.setAttribute("exemplairesDisponibles", livre.exemplairesDisponibles());


	    request.getRequestDispatcher("/WEB-INF/livres/form.jsp")
	           .forward(request, response);

	} 
		
		else {

		    long id = Long.parseLong(path.substring(1));

		    LivreDAO livreDAO = new LivreDAO();

		    Livre livre = livreDAO.findById(id).orElse(null);

		    request.setAttribute("livre", livre);

		    request.getRequestDispatcher("/WEB-INF/livres/detail.jsp")
		           .forward(request, response);
		}
	}
	@Override
	protected void doPost(HttpServletRequest request,
	                      HttpServletResponse response)
	        throws ServletException, IOException {

	    String path = request.getPathInfo();
	    Map<String, String> errors = new HashMap<>();

	    if (path == null || path.equals("/")) {

	        String titre = request.getParameter("titre");
	        String auteur = request.getParameter("auteur");
	        String anneePublicationParam = request.getParameter("anneePublication");
	        String exemplairesTotalParam = request.getParameter("exemplairesTotal");
	        String exemplairesDisponiblesParam = request.getParameter("exemplairesDisponibles");
	        
	        if (titre == null || titre.isEmpty()) {
	            errors.put("titre", "Le titre ne doit pas être vide");
	        }
	        if (auteur == null || auteur.isEmpty()) {
	            errors.put("auteur", "L'auteur ne doit pas être vide");
	        }
	        if (anneePublicationParam == null || anneePublicationParam.isEmpty()) {
	            errors.put("anneePublication", "L'année de publication ne doit pas être vide");
	        }
	        if (exemplairesTotalParam == null || exemplairesTotalParam.isEmpty()) {
	            errors.put("exemplairesTotal", "Le nombre d'exemplaires total ne doit pas être vide");
	        }
	        if (exemplairesDisponiblesParam == null || exemplairesDisponiblesParam.isEmpty()) {
	            errors.put("exemplairesDisponibles", "Le nombre d'exemplaires disponibles ne doit pas être vide");
	        }
	        if (!errors.isEmpty()) {
	            request.setAttribute("errors", errors);

	            request.setAttribute("titre", titre);
	            request.setAttribute("auteur", auteur);
	            request.setAttribute("anneePublication", anneePublicationParam);
	            request.setAttribute("exemplairesTotal", exemplairesTotalParam);
	            request.setAttribute("exemplairesDisponibles", exemplairesDisponiblesParam);

	            request.getRequestDispatcher("/WEB-INF/livres/form.jsp")
	                   .forward(request, response);

	            return;
	        }
	        try {

	            int anneePublication = Integer.parseInt(anneePublicationParam);
	            int exemplairesTotal = Integer.parseInt(exemplairesTotalParam);
	            int exemplairesDisponibles = Integer.parseInt(exemplairesDisponiblesParam);

	            Livre livre = new Livre(
	                    0,
	                    titre,
	                    auteur,
	                    anneePublication,
	                    exemplairesTotal,
	                    exemplairesDisponibles
	            );

	            LivreDAO livreDAO = new LivreDAO();
	            livreDAO.save(livre);

	            response.sendRedirect(request.getContextPath() + "/livres");

	        } catch (IllegalArgumentException e) {
	            errors.put("anneePublication", e.getMessage());
	            request.setAttribute("errors", errors);
	            request.setAttribute("error", e.getMessage());


	            request.setAttribute("titre", titre);
	            request.setAttribute("auteur", auteur);
	            request.setAttribute("anneePublication", anneePublicationParam);
	            request.setAttribute("exemplairesTotal", exemplairesTotalParam);
	            request.setAttribute("exemplairesDisponibles", exemplairesDisponiblesParam);

	            request.getRequestDispatcher("/WEB-INF/livres/form.jsp")
	                   .forward(request, response);
	        }

	    } else if (path.endsWith("/delete")) {

	        long id = Long.parseLong(path.substring(1, path.length() - 7));

	        LivreDAO livreDAO = new LivreDAO();
	        livreDAO.delete(id);

	        response.sendRedirect(request.getContextPath() + "/livres");

	    } else {

	        long id = Long.parseLong(path.substring(1));

	        String titre = request.getParameter("titre");
	        String auteur = request.getParameter("auteur");
	        String anneePublicationParam = request.getParameter("anneePublication");
	        String exemplairesTotalParam = request.getParameter("exemplairesTotal");
	        String exemplairesDisponiblesParam = request.getParameter("exemplairesDisponibles");

	        try {

	            int anneePublication = Integer.parseInt(anneePublicationParam);
	            int exemplairesTotal = Integer.parseInt(exemplairesTotalParam);
	            int exemplairesDisponibles = Integer.parseInt(exemplairesDisponiblesParam);

	            Livre livre = new Livre(
	                    id,
	                    titre,
	                    auteur,
	                    anneePublication,
	                    exemplairesTotal,
	                    exemplairesDisponibles
	            );

	            LivreDAO livreDAO = new LivreDAO();
	            livreDAO.update(id, livre);

	            response.sendRedirect(request.getContextPath() + "/livres");

	        } catch (IllegalArgumentException e) {

	            errors.put("anneePublication", e.getMessage());

	            request.setAttribute("errors", errors);

	            request.setAttribute("titre", titre);
	            request.setAttribute("auteur", auteur);
	            request.setAttribute("anneePublication", anneePublicationParam);
	            request.setAttribute("exemplairesTotal", exemplairesTotalParam);
	            request.setAttribute("exemplairesDisponibles", exemplairesDisponiblesParam);

	            request.getRequestDispatcher("/WEB-INF/livres/form.jsp")
	                   .forward(request, response);
	        }
	    }
	}
}

