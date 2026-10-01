
package com.bibliotech.dao;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.bibliotech.model.Livre;

public class LivreDAO implements GenericDAO<Livre, Long> {

	@Override
	public List<Livre> findAll() {
	    List<Livre> livres = new ArrayList<>();

	    String sql = "SELECT id, titre, auteur, annee_publication, " +
	                 "exemplaires_total, exemplaires_disponibles " +
	                 "FROM livre";

	    try (Connection connection = DatabaseConnection.get();
	         PreparedStatement statement = connection.prepareStatement(sql);
	         ResultSet resultSet = statement.executeQuery()) {

	        while (resultSet.next()) {
	            Livre livre = new Livre(
	                    resultSet.getLong("id"),
	                    resultSet.getString("titre"),
	                    resultSet.getString("auteur"),
	                    resultSet.getInt("annee_publication"),
	                    resultSet.getInt("exemplaires_total"),
	                    resultSet.getInt("exemplaires_disponibles")
	            );

	            livres.add(livre);
	        }

	    } catch (SQLException e) {
	        throw new RuntimeException(e);
	    }

	    return livres;
	}

	@Override
	public Optional<Livre> findById(Long id) {

	    String sql = "SELECT id, titre, auteur, annee_publication, " +
	                 "exemplaires_total, exemplaires_disponibles " +
	                 "FROM livre WHERE id = ?";

	    try (Connection connection = DatabaseConnection.get();
	         PreparedStatement statement = connection.prepareStatement(sql)) {

	        statement.setLong(1, id);

	        try (ResultSet resultSet = statement.executeQuery()) {

	            if (resultSet.next()) {
	                Livre livre = new Livre(
	                        resultSet.getLong("id"),
	                        resultSet.getString("titre"),
	                        resultSet.getString("auteur"),
	                        resultSet.getInt("annee_publication"),
	                        resultSet.getInt("exemplaires_total"),
	                        resultSet.getInt("exemplaires_disponibles")
	                );

	                return Optional.of(livre);
	            }
	        }

	    } catch (SQLException e) {
	        throw new RuntimeException(e);
	    }

	    return Optional.empty();
	}

	@Override
	public void save(Livre entity) {

	    String sql = "INSERT INTO livre " +
	                 "(titre, auteur, annee_publication, exemplaires_total, exemplaires_disponibles) " +
	                 "VALUES (?, ?, ?, ?, ?)";

	    try (Connection connection = DatabaseConnection.get();
	         PreparedStatement statement = connection.prepareStatement(sql)) {

	        statement.setString(1, entity.titre());
	        statement.setString(2, entity.auteur());
	        statement.setInt(3, entity.anneePublication());
	        statement.setInt(4, entity.exemplairesTotal());
	        statement.setInt(5, entity.exemplairesDisponibles());

	        statement.executeUpdate();

	    } catch (SQLException e) {
	        throw new RuntimeException(e);
	    }
	}

	@Override
	public void update(Long id, Livre entity) {

	    String sql = "UPDATE livre SET " +
	                 "titre = ?, " +
	                 "auteur = ?, " +
	                 "annee_publication = ?, " +
	                 "exemplaires_total = ?, " +
	                 "exemplaires_disponibles = ? " +
	                 "WHERE id = ?";

	    try (Connection connection = DatabaseConnection.get();
	         PreparedStatement statement = connection.prepareStatement(sql)) {

	        statement.setString(1, entity.titre());
	        statement.setString(2, entity.auteur());
	        statement.setInt(3, entity.anneePublication());
	        statement.setInt(4, entity.exemplairesTotal());
	        statement.setInt(5, entity.exemplairesDisponibles());
	        statement.setLong(6, id);

	        statement.executeUpdate();

	    } catch (SQLException e) {
	        throw new RuntimeException(e);
	    }
	}

	@Override
	public void delete(Long id) {

	    String sql = "DELETE FROM livre WHERE id = ?";

	    try (Connection connection = DatabaseConnection.get();
	         PreparedStatement statement = connection.prepareStatement(sql)) {

	        statement.setLong(1, id);

	        statement.executeUpdate();

	    } catch (SQLException e) {
	        throw new RuntimeException(e);
	    }
	}

}