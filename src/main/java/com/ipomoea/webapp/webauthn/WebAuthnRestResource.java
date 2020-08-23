// Copyright (c) 2018, Yubico AB
// All rights reserved.
//
// Redistribution and use in source and binary forms, with or without
// modification, are permitted provided that the following conditions are met:
//
// 1. Redistributions of source code must retain the above copyright notice, this
//    list of conditions and the following disclaimer.
//
// 2. Redistributions in binary form must reproduce the above copyright notice,
//    this list of conditions and the following disclaimer in the documentation
//    and/or other materials provided with the distribution.
//
// THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
// AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
// IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
// DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
// FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
// DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
// SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
// CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
// OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
// OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

package com.ipomoea.webapp.webauthn;

import java.net.MalformedURLException;
import java.util.concurrent.ExecutionException;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Path("/v1")
@Produces(MediaType.APPLICATION_JSON)
public class WebAuthnRestResource {
    private static final Logger logger = LoggerFactory.getLogger(WebAuthnRestResource.class);

    @Path("registerXYZ")
    @POST
    public Response startRegistration(
        @FormParam("username") String username,
        @FormParam("displayName") String displayName,
        @FormParam("credentialNickname") String credentialNickname,
        @FormParam("requireResidentKey") @DefaultValue("false") boolean requireResidentKey,
        @FormParam("sessionToken") String sessionTokenBase64
    ) throws MalformedURLException, ExecutionException {
        logger.trace("startRegistration username: {}, displayName: {}, credentialNickname: {}, requireResidentKey: {}", username, displayName, credentialNickname, requireResidentKey);
        System.out.println("registerXYZ");
        return null;
    }

    @Path("register/finish")
    @POST
    public Response finishRegistration(String responseJson) {
        logger.trace("finishRegistration responseJson: {}", responseJson);
        System.out.println("/finish");
        return null;
    }
}
