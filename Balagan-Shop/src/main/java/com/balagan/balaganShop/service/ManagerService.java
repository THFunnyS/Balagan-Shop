package com.balagan.balaganShop.service;

import com.balagan.balaganShop.models.Manager;
import com.balagan.balaganShop.util.JwtUtil;
import com.balagan.balaganShop.util.PasswordUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.List;

@Service
public class ManagerService {

    // EntityManager для работы с базой данных (JPA)
    @PersistenceContext
    private EntityManager em;

    // Утилита для работы с JWT (создание и валидация токенов)
    private final JwtUtil jwtUtil;

    // Кодировщик паролей BCrypt (для шифрования и проверки паролей)

    // Конструктор для внедрения зависимостей JwtUtil и BCryptPasswordEncoder
    public ManagerService(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    /**
     * Метод аутентификации менеджера по логину и паролю.
     * Если логин и пароль верны — генерирует JWT токен с ролью менеджера.
     *
     * @param login - логин менеджера
     * @param password - пароль менеджера (в открытом виде)
     * @return JWT токен с ролью пользователя
     * @throws RuntimeException если логин не найден или пароль неверный
     */
    @Transactional
    public String authenticate(String login, String password) {
        // Запрос к базе для поиска менеджера по логину
        String query = "SELECT m FROM Manager m WHERE m.login = :login";
        List<Manager> users = em.createQuery(query, Manager.class)
                .setParameter("login", login)
                .getResultList();

        // Если пользователь с таким логином не найден — выбрасываем исключение
        if (users.isEmpty()) {
            throw new RuntimeException("Менеджер с таким login не найден");
        }

        Manager manager = users.get(0);

        // Проверяем пароль с помощью PasswordUtil (BCrypt проверка)
        if (!PasswordUtil.checkPassword(password, manager.getPassword())) {
            throw new RuntimeException("Неверный пароль");
        }

        // Генерируем JWT токен, добавляя роль менеджера (например, ROLE_MANAGER)
        return jwtUtil.generateToken(login,"ROLE_" + manager.getRole());
    }

    /**
     * Получение списка всех менеджеров из базы данных
     *
     * @return список всех менеджеров
     */
    public List<Manager> getAllUsers() {
        String query = "SELECT m FROM Manager m";
        return em.createQuery(query, Manager.class).getResultList();
    }

    /**
     * Получение менеджера по его ID
     *
     * @param id - идентификатор менеджера
     * @return объект Manager или null, если не найден
     */
    public Manager getManagerById(Integer id) {
        return em.find(Manager.class, id);
    }

    /**
     * Поиск менеджеров по части логина (LIKE запрос)
     * Используется для поиска по подстроке в логине
     *
     * @param nameQuery - часть логина для поиска
     * @return список менеджеров, чьи логины содержат nameQuery
     */
    public List<Manager> searchManagerByLogin(String nameQuery) {
        String query = "SELECT m FROM Manager m WHERE m.login LIKE :nameQuery";
        return em.createQuery(query, Manager.class)
                .setParameter("nameQuery", "%" + nameQuery + "%")
                .getResultList();
    }
}

