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
    @PersistenceContext
    private EntityManager em;
    private final JwtUtil jwtUtil;
    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();


    public ManagerService(JwtUtil jwtUtil, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.jwtUtil = jwtUtil;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Transactional
    public String authenticate(String login, String password) {
        String query = "SELECT m FROM Manager m WHERE m.login = :login";
        List<Manager> users = em.createQuery(query, Manager.class)
                .setParameter("login", login)
                .getResultList();

        if (users.isEmpty()) {
            throw new RuntimeException("Менеджер с таким login не найден");
        }

        Manager manager = users.get(0);

        if (!PasswordUtil.checkPassword(password, manager.getPassword())) {
            throw new RuntimeException("Неверный пароль");
        }

        return jwtUtil.generateToken(login,"ROLE_" + manager.getRole());
    }

    public List<Manager> getAllUsers() {
        String query = "SELECT m FROM Manager m";
        return em.createQuery(query, Manager.class).getResultList();
    }

    public Manager getManagerById(Integer id) {
        return em.find(Manager.class, id);
    }

    public List<Manager> searchManagerByLogin(String nameQuery) {
        String query = "SELECT m FROM Manager m WHERE m.login LIKE :nameQuery";
        return em.createQuery(query, Manager.class)
                .setParameter("nameQuery", "%" + nameQuery + "%")
                .getResultList();
    }
}
