package com.ipomoea.webapp.controller;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.ipomoea.webapp.exception.CustomException;
import com.ipomoea.webapp.model.User;
import com.ipomoea.webapp.web.Constants;

public class UserCont {
	private static final UserCont	INSTANCE					= new UserCont();
	private static EntityManagerFactory	entityManagerFactory	= Persistence
			.createEntityManagerFactory("webapp-persistence");
	
	public static UserCont getInstance() {
		return INSTANCE;
	}

	public User fetchUserByUsername(final String username) throws CustomException {
        System.out.print(entityManagerFactory);
		final EntityManager entityManager = entityManagerFactory.createEntityManager();
		try {
			@SuppressWarnings("unchecked")
			final List<User> resultList = entityManager
			.createQuery("SELECT u FROM User u WHERE u.username = :aUsername")
			.setParameter("aUsername", username).getResultList();
			if (resultList == null || resultList.isEmpty()) {
				return null;
			} else if (resultList.size() > 1) {
				throw new CustomException(
						"App user result list contained multiple entries for username: " + username);
			} else {
				return resultList.get(0);
			}
		} finally {
			entityManager.close();
		}
	}

	public void createUser(final User user) {
		try {
			if (fetchUserByUsername(user.getUsername()) != null) {
				throw new Exception("User already exists for username " + user.getUsername());
			}
			final EntityManager entityManager = entityManagerFactory.createEntityManager();
			try {
				entityManager.getTransaction().begin();
				entityManager.persist(user);
				entityManager.getTransaction().commit();
			} finally {
				entityManager.close();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public User fetchUserById(final long id) {
		final EntityManager entityManager = entityManagerFactory.createEntityManager();
		try {
			return entityManager.find(User.class, id);
		} finally {
			entityManager.close();
		}
	}

    private boolean authenticate(String username, String password){
    	try {
			User user = UserCont.getInstance().fetchUserByUsername(username);
			
			if(user.getPassword().equals(password)) {	//succesfull authentication
	            return true;
			}
			else {	//Password does not match
				return false;
			}
		} catch (Exception e) {
	        e.printStackTrace();
			return false;
		}
    }
}
