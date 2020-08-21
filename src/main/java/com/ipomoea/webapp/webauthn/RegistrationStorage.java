package com.ipomoea.webapp.webauthn;

import java.util.Optional;
import java.util.Set;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import com.ipomoea.webapp.controller.UserCont;
import com.yubico.webauthn.CredentialRepository;
import com.yubico.webauthn.RegisteredCredential;
import com.yubico.webauthn.data.ByteArray;
import com.yubico.webauthn.data.PublicKeyCredentialDescriptor;

public class RegistrationStorage implements CredentialRepository{
	private static final UserCont	INSTANCE					= new UserCont();
	private static EntityManagerFactory	entityManagerFactory	= Persistence
			.createEntityManagerFactory("webapp-persistence");

	@Override
	public Set<PublicKeyCredentialDescriptor> getCredentialIdsForUsername(String username) {
		// TODO Auto-generated method stub
		System.out.println("ToDo 1!");
		return null;
	}

	@Override
	public Optional<ByteArray> getUserHandleForUsername(String username) {
		// TODO Auto-generated method stub
		System.out.println("ToDo 2!");
		return null;
	}

	@Override
	public Optional<String> getUsernameForUserHandle(ByteArray userHandle) {
		// TODO Auto-generated method stub
		System.out.println("ToDo 3!");
		return null;
	}

	@Override
	public Optional<RegisteredCredential> lookup(ByteArray credentialId, ByteArray userHandle) {
		// TODO Auto-generated method stub
		System.out.println("ToDo 4!");
		return null;
	}

	@Override
	public Set<RegisteredCredential> lookupAll(ByteArray credentialId) {
		// TODO Auto-generated method stub
		System.out.println("ToDo 5!");
		return null;
	}

}
