package com.ipomoea.webapp.controller;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import com.ipomoea.webapp.exception.CustomException;
import com.ipomoea.webapp.model.User;

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
}
