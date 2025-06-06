package com.hotel.app.dao;

import com.hotel.app.config.DatabaseConnection;
import com.hotel.app.model.User;
import com.hotel.app.model.Role;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Objeto de Acesso a Dados (DAO) para a entidade {@link User}.
 * Responsável por todas as operações de persistência relacionadas aos usuários no banco de dados,
 * utilizando JDBC puro.
 */
public class UserDAO {


    /**
     * Adiciona um novo usuário ao banco de dados.
     * O ID do usuário será gerado pelo banco de dados e atualizado no objeto User.
     *
     * @param user O objeto {@link User} a ser adicionado.
     * @throws SQLException Se ocorrer um erro de acesso ao banco de dados.
     */
    public void addUser(User user) throws SQLException {
        String sql = "INSERT INTO users (fullname, email, phone, birthday, role, password) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection connection = DatabaseConnection.getConnection();
             // Statement.RETURN_GENERATED_KEYS é necessário para recuperar o ID gerado automaticamente
             PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            statement.setString(1, user.getFullName());
            statement.setString(2, user.getEmail());
            statement.setString(3, user.getPhone());
            // Converte LocalDate para java.sql.Date
            statement.setDate(4, java.sql.Date.valueOf(user.getBirthday()));
            // Converte o enum Role para String
            statement.setString(5, user.getRole().name());
            statement.setString(6, user.getPassword());

            int affectedRows = statement.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Falha ao criar usuário, nenhuma linha afetada.");
            }

            // Recupera o ID gerado automaticamente pelo banco de dados
            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    user.setUserId(generatedKeys.getInt(1)); // Define o ID gerado no objeto User
                } else {
                    throw new SQLException("Falha ao criar usuário, nenhum ID gerado.");
                }
            }
            System.out.println("Usuário adicionado com sucesso! ID: " + user.getUserId());

        } catch (SQLException e) {
            System.err.println("Erro ao adicionar usuário: " + e.getMessage());
            throw e; // Relança a exceção para que a camada superior possa tratá-la
        }
    }

    /**
     * Busca um usuário no banco de dados pelo seu ID único.
     *
     * @param userId O ID do usuário a ser buscado.
     * @return O objeto {@link User} correspondente ao ID, ou null se não for encontrado.
     * @throws SQLException Se ocorrer um erro de acesso ao banco de dados.
     */
    public User getUserById(Integer userId) throws SQLException {
        String sql = "SELECT user_id, fullname, email, phone, birthday, role, password FROM users WHERE user_id = ?";
        User user = null;
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, userId); // Define o ID no placeholder

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    user = extractUserFromResultSet(resultSet); // Método auxiliar para extrair dados
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar usuário por ID: " + e.getMessage());
            throw e;
        }
        return user;
    }

    /**
     * Busca um usuário no banco de dados pelo seu endereço de e-mail.
     *
     * @param email O endereço de e-mail do usuário a ser buscado.
     * @return O objeto {@link User} correspondente ao e-mail, ou null se não for encontrado.
     * @throws SQLException Se ocorrer um erro de acesso ao banco de dados.
     */
    public User getUserByEmail(String email) throws SQLException {
        String sql = "SELECT user_id, fullname, email, phone, birthday, role, password FROM users WHERE email = ?";
        User user = null;
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, email); // Define o e-mail no placeholder

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    user = extractUserFromResultSet(resultSet);
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar usuário por e-mail: " + e.getMessage());
            throw e;
        }
        return user;
    }

    /**
     * Retorna uma lista de todos os usuários cadastrados no banco de dados.
     *
     * @return Uma {@link List} de objetos {@link User}. Pode ser vazia se não houver usuários.
     * @throws SQLException Se ocorrer um erro de acesso ao banco de dados.
     */
    public List<User> getAllUsers() throws SQLException {
        List<User> users = new ArrayList<>();
        String sql = "SELECT user_id, fullname, email, phone, birthday, role, password FROM users";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) { // ResultSet pode ser criado aqui para try-with-resources

            while (resultSet.next()) {
                users.add(extractUserFromResultSet(resultSet));
            }
        } catch (SQLException e) {
            System.err.println("Erro ao listar todos os usuários: " + e.getMessage());
            throw e;
        }
        return users;
    }

    /**
     * Atualiza as informações de um usuário existente no banco de dados.
     *
     * @param user O objeto {@link User} com as informações atualizadas.
     * @throws SQLException Se ocorrer um erro de acesso ao banco de dados.
     * @return true se o usuário foi atualizado, false caso contrário.
     */
    public boolean updateUser(User user) throws SQLException {
        String sql = "UPDATE users SET fullname = ?, email = ?, phone = ?, birthday = ?, role = ?, password = ? WHERE user_id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, user.getFullName());
            statement.setString(2, user.getEmail());
            statement.setString(3, user.getPhone());
            statement.setDate(4, java.sql.Date.valueOf(user.getBirthday()));
            statement.setString(5, user.getRole().name());
            statement.setString(6, user.getPassword());
            statement.setInt(7, user.getUserId()); // ID na cláusula WHERE

            int affectedRows = statement.executeUpdate();
            return affectedRows > 0;

        } catch (SQLException e) {
            System.err.println("Erro ao atualizar usuário: " + e.getMessage());
            throw e;
        }
    }

    /**
     * Deleta um usuário do banco de dados pelo seu ID.
     *
     * @param userId O ID do usuário a ser deletado.
     * @throws SQLException Se ocorrer um erro de acesso ao banco de dados.
     * @return true se o usuário foi deletado, false caso contrário.
     */
    public boolean deleteUser(Integer userId) throws SQLException {
        String sql = "DELETE FROM users WHERE user_id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, userId);

            int affectedRows = statement.executeUpdate();
            return affectedRows > 0;

        } catch (SQLException e) {
            System.err.println("Erro ao deletar usuário: " + e.getMessage());
            throw e;
        }
    }

    /**
     * Método auxiliar para extrair dados de um {@link ResultSet} e construir um objeto {@link User}.
     *
     * @param resultSet O ResultSet contendo os dados do usuário.
     * @return Um objeto User preenchido com os dados do ResultSet.
     * @throws SQLException Se ocorrer um erro ao acessar as colunas do ResultSet.
     */
    private User extractUserFromResultSet(ResultSet resultSet) throws SQLException {
        Integer userId = resultSet.getInt("user_id");
        String fullName = resultSet.getString("fullname");
        String email = resultSet.getString("email");
        String phone = resultSet.getString("phone");
        // Converte java.sql.Date para LocalDate
        LocalDate birthday = resultSet.getDate("birthday") != null ? resultSet.getDate("birthday").toLocalDate() : null;
        // Converte String para o enum Role. Lida com valores que não existem no enum.
        Role role;
        try {
            role = Role.valueOf(resultSet.getString("role"));
        } catch (IllegalArgumentException e) {
            System.err.println("AVISO: Papel de usuário inválido encontrado no DB: " + resultSet.getString("role"));
            role = null; // Ou defina um papel padrão como GUEST, se apropriado
        }
        String password = resultSet.getString("password");

        return new User(userId, fullName, email, phone, birthday, role, password);
    }
}