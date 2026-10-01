package com.bibliotech.dao;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import com.bibliotech.model.Emprunt;
import com.bibliotech.model.StatutEmprunt;

public class EmpruntDAO implements GenericDAO<Emprunt, Long> {

	@Override
	public List<Emprunt> findAll() {
		
	    List<Emprunt> emprunts = new ArrayList<>();

	    String sql = "SELECT id, livre_id, etudiant_id, date_emprunt, " +
	                 "date_retour_prevue, date_retour_effective, statut " +
	                 "FROM emprunt " +
	                 "WHERE date_retour_effective IS NULL";

	    try (Connection connection = DatabaseConnection.get();
	         PreparedStatement statement = connection.prepareStatement(sql);
	         ResultSet resultSet = statement.executeQuery()) {

	        while (resultSet.next()) {
	        	String statut = resultSet.getString("statut");
	        	StatutEmprunt statutEmprunt = null;
	        	switch (statut) {
	            case "EN_COURS" ->
	                statutEmprunt = new StatutEmprunt.EnCours(
	                    resultSet.getDate("date_retour_prevue").toLocalDate()
	                );

	            case "RENDU" ->
	                statutEmprunt = new StatutEmprunt.Rendu(
	                    resultSet.getDate("date_retour_effective").toLocalDate(),
	                    false
	                );

	            case "EN_RETARD" ->
	                statutEmprunt = new StatutEmprunt.EnRetard(
	                    resultSet.getDate("date_retour_prevue").toLocalDate(),
	                    0
	                );
	        }
	            Emprunt emprunt = new Emprunt(
	                    resultSet.getLong("id"),
	                    resultSet.getLong("livre_id"),
	                    resultSet.getLong("etudiant_id"),
	                    resultSet.getDate("date_emprunt").toLocalDate(),
	                    resultSet.getDate("date_retour_prevue").toLocalDate(),
	                    resultSet.getDate("date_retour_effective") != null
	                            ? resultSet.getDate("date_retour_effective").toLocalDate()
	                            : null,
	                            statutEmprunt
	            );

	            emprunts.add(emprunt);
	        }

	    } catch (SQLException e) {
	        throw new RuntimeException(e);
	    }

	    return emprunts;
	}
	
	public List<Emprunt> findEnRetard() {

	    String sql = "SELECT id, livre_id, etudiant_id, date_emprunt, " +
	                 "date_retour_prevue, date_retour_effective, statut " +
	                 "FROM emprunt " +
	                 "WHERE date_retour_prevue < CURRENT_DATE " +
	                 "AND date_retour_effective IS NULL";

	    List<Emprunt> emprunts = new ArrayList<>();

	    try (Connection connection = DatabaseConnection.get();
	    	     PreparedStatement statement = connection.prepareStatement(sql);
	    	     ResultSet resultSet = statement.executeQuery()) {

	    	    while (resultSet.next()) {

	    	        StatutEmprunt statutEmprunt =
	    	                new StatutEmprunt.EnRetard(
	    	                        resultSet.getDate("date_retour_prevue").toLocalDate(),
	    	                        0
	    	                );

	    	        Emprunt emprunt = new Emprunt(
	    	                resultSet.getLong("id"),
	    	                resultSet.getLong("livre_id"),
	    	                resultSet.getLong("etudiant_id"),
	    	                resultSet.getDate("date_emprunt").toLocalDate(),
	    	                resultSet.getDate("date_retour_prevue").toLocalDate(),
	    	                resultSet.getDate("date_retour_effective") != null
	    	                        ? resultSet.getDate("date_retour_effective").toLocalDate()
	    	                        : null,
	    	                statutEmprunt
	    	        );

	    	        emprunts.add(emprunt);
	    	    }

	    	} catch (SQLException e) {
	    	    throw new RuntimeException(e);
	    	}

	    return emprunts;
	}

	@Override
	public Optional<Emprunt> findById(Long id) {

	    String sql = "SELECT id, livre_id, etudiant_id, date_emprunt, " +
	                 "date_retour_prevue, date_retour_effective, statut " +
	                 "FROM emprunt WHERE id = ?";

	    try (Connection connection = DatabaseConnection.get();
	         PreparedStatement statement = connection.prepareStatement(sql)) {

	        statement.setLong(1, id);

	        try (ResultSet resultSet = statement.executeQuery()) {

	            if (resultSet.next()) {

	                String statut = resultSet.getString("statut");
	                StatutEmprunt statutEmprunt = null;

	                switch (statut) {
	                    case "EN_COURS" ->
	                        statutEmprunt = new StatutEmprunt.EnCours(
	                            resultSet.getDate("date_retour_prevue").toLocalDate()
	                        );

	                    case "RENDU" ->
	                        statutEmprunt = new StatutEmprunt.Rendu(
	                            resultSet.getDate("date_retour_effective").toLocalDate(),
	                            false
	                        );

	                    case "EN_RETARD" ->
	                        statutEmprunt = new StatutEmprunt.EnRetard(
	                            resultSet.getDate("date_retour_prevue").toLocalDate(),
	                            0
	                        );
	                }

	                Emprunt emprunt = new Emprunt(
	                        resultSet.getLong("id"),
	                        resultSet.getLong("livre_id"),
	                        resultSet.getLong("etudiant_id"),
	                        resultSet.getDate("date_emprunt").toLocalDate(),
	                        resultSet.getDate("date_retour_prevue").toLocalDate(),
	                        resultSet.getDate("date_retour_effective") != null
	                                ? resultSet.getDate("date_retour_effective").toLocalDate()
	                                : null,
	                        statutEmprunt
	                );

	                return Optional.of(emprunt);
	            }
	        }

	    } catch (SQLException e) {
	        throw new RuntimeException(e);
	    }

	    return Optional.empty();
	}

	@Override
	public void save(Emprunt entity) {

	    String sql = "INSERT INTO emprunt " +
	                 "(livre_id, etudiant_id, date_emprunt, date_retour_prevue, " +
	                 "date_retour_effective, statut) " +
	                 "VALUES (?, ?, ?, ?, ?, ?)";

	    try (Connection connection = DatabaseConnection.get();
	         PreparedStatement statement = connection.prepareStatement(sql)) {

	        statement.setLong(1, entity.livreId());
	        statement.setLong(2, entity.etudiantId());
	        statement.setDate(3, java.sql.Date.valueOf(entity.dateEmprunt()));
	        statement.setDate(4, java.sql.Date.valueOf(entity.dateRetourPrevue()));

	        if (entity.dateRetourEffective() != null) {
	            statement.setDate(5, java.sql.Date.valueOf(entity.dateRetourEffective()));
	        } else {
	            statement.setNull(5, java.sql.Types.DATE);
	        }

	        String statut;

	        if (entity.statut() instanceof StatutEmprunt.EnCours) {
	            statut = "EN_COURS";
	        } else if (entity.statut() instanceof StatutEmprunt.Rendu) {
	            statut = "RENDU";
	        } else {
	            statut = "EN_RETARD";
	        }

	        statement.setString(6, statut);

	        statement.executeUpdate();

	    } catch (SQLException e) {
	        throw new RuntimeException(e);
	    }
	}

	@Override
	public void update(Long id, Emprunt entity) {

	    String sql = "UPDATE emprunt SET " +
	                 "livre_id = ?, " +
	                 "etudiant_id = ?, " +
	                 "date_emprunt = ?, " +
	                 "date_retour_prevue = ?, " +
	                 "date_retour_effective = ?, " +
	                 "statut = ? " +
	                 "WHERE id = ?";

	    try (Connection connection = DatabaseConnection.get();
	         PreparedStatement statement = connection.prepareStatement(sql)) {

	        statement.setLong(1, entity.livreId());
	        statement.setLong(2, entity.etudiantId());
	        statement.setDate(3, java.sql.Date.valueOf(entity.dateEmprunt()));
	        statement.setDate(4, java.sql.Date.valueOf(entity.dateRetourPrevue()));

	        if (entity.dateRetourEffective() != null) {
	            statement.setDate(5, java.sql.Date.valueOf(entity.dateRetourEffective()));
	        } else {
	            statement.setNull(5, java.sql.Types.DATE);
	        }

	        String statut;

	        if (entity.statut() instanceof StatutEmprunt.EnCours) {
	            statut = "EN_COURS";
	        } else if (entity.statut() instanceof StatutEmprunt.Rendu) {
	            statut = "RENDU";
	        } else {
	            statut = "EN_RETARD";
	        }

	        statement.setString(6, statut);
	        statement.setLong(7, id);

	        statement.executeUpdate();

	    } catch (SQLException e) {
	        throw new RuntimeException(e);
	    }
	}

	@Override
	public void delete(Long id) {

	    String sql = "DELETE FROM emprunt WHERE id = ?";

	    try (Connection connection = DatabaseConnection.get();
	         PreparedStatement statement = connection.prepareStatement(sql)) {

	        statement.setLong(1, id);

	        statement.executeUpdate();

	    } catch (SQLException e) {
	        throw new RuntimeException(e);
	    }
	}

	public void enregistrerEmprunt(long livreId, long etudiantId, int dureeJours) {
		String selectSql =
			    "SELECT exemplaires_disponibles FROM livre WHERE id = ? FOR UPDATE";

	    String updateLivreSql =
	            "UPDATE livre SET exemplaires_disponibles = exemplaires_disponibles - 1 WHERE id = ?";

	    String insertEmpruntSql =
	            "INSERT INTO emprunt " +
	            "(livre_id, etudiant_id, date_emprunt, date_retour_prevue, statut) " +
	            "VALUES (?, ?, ?, ?, ?)";

	    try (Connection connection = DatabaseConnection.get()) {

	        connection.setAutoCommit(false);

	        try (
	            PreparedStatement selectStatement =
	                    connection.prepareStatement(selectSql);
	            PreparedStatement updateLivreStatement =
	                    connection.prepareStatement(updateLivreSql);
	            PreparedStatement insertEmpruntStatement =
	                    connection.prepareStatement(insertEmpruntSql)
	        ) {

	            selectStatement.setLong(1, livreId);

	            try (ResultSet resultSet = selectStatement.executeQuery()) {

	                if (!resultSet.next()) {
	                    throw new IllegalArgumentException("Livre introuvable");
	                }

	                int stock = resultSet.getInt("exemplaires_disponibles");

	                if (stock <= 0) {
	                    throw new LivreIndisponibleException(
	                            "Le livre n'est plus disponible"
	                    );
	                }
	            }

	            updateLivreStatement.setLong(1, livreId);
	            updateLivreStatement.executeUpdate();

	            java.time.LocalDate dateEmprunt =
	                    java.time.LocalDate.now();

	            java.time.LocalDate dateRetourPrevue =
	                    dateEmprunt.plusDays(dureeJours);

	            insertEmpruntStatement.setLong(1, livreId);
	            insertEmpruntStatement.setLong(2, etudiantId);
	            insertEmpruntStatement.setDate(
	                    3,
	                    java.sql.Date.valueOf(dateEmprunt)
	            );
	            insertEmpruntStatement.setDate(
	                    4,
	                    java.sql.Date.valueOf(dateRetourPrevue)
	            );
	            insertEmpruntStatement.setString(5, "EN_COURS");

	            insertEmpruntStatement.executeUpdate();

	            connection.commit();

	        } catch (Exception e) {
	            connection.rollback();
	            throw e;
	        }

	    } catch (SQLException e) {
	        throw new RuntimeException(e);
	    }
	}
	public void augmenterStock(long livreId) {
	    String sql =
	        "UPDATE livre " +
	        "SET exemplaires_disponibles = exemplaires_disponibles + 1 " +
	        "WHERE id = ?";

	    try (Connection connection = DatabaseConnection.get();
	         PreparedStatement statement = connection.prepareStatement(sql)) {

	        statement.setLong(1, livreId);
	        statement.executeUpdate();

	    } catch (SQLException e) {
	        throw new RuntimeException(e);
	    }
	}
}