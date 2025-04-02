package com.example.finance;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.example.finance.User;
import com.example.finance.HibernateUtil;

public class UserDao {
    public void saveUser(User student) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            // start a transaction
            transaction = session.beginTransaction();
            System.out.println("Session has started");
            // save the student object
            session.persist(student);
            // commit transaction
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }

    public User getUser(String username, String password)  {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            // Update the query to filter by username and password and fetch a single result
            return session.createQuery("FROM User WHERE username = :username AND password = :password", User.class)
                          .setParameter("username", username)
                          .setParameter("password", password)
                          .uniqueResult();  // Fetches a single user or null if no match is found
        }
    }
}