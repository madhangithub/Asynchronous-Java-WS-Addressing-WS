/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author thiago
 */
 package org.ewe;

import javax.jws.WebParam;
import org.xmlsoap.schemas.ws._2004._08.addressing.EndpointReferenceType;


public interface AsyncSharcnet {
    public void execSharcnet(

        @WebParam(name = "MessageID", targetNamespace = "http://schemas.xmlsoap.org/ws/2004/08/addressing", header = true, partName = "MessageID")
        String messageID,
        @WebParam(name = "ReplyTo", targetNamespace = "http://schemas.xmlsoap.org/ws/2004/08/addressing", header = true, partName = "ReplyTo")
        EndpointReferenceType replyTo,
        @WebParam(name = "execSharcnet", targetNamespace = "http://ewe.org/", partName = "parameters")
        ExecSharcnet parameters);
}
