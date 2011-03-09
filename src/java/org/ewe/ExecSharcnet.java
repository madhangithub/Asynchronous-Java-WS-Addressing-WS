
package org.ewe;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for execSharcnet complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="execSharcnet">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="server" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="login" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="senha" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="comando" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="arqOutput" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="requisitos" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "execSharcnet", propOrder = {
    "server",
    "login",
    "senha",
    "comando",
    "arqOutput",
    "requisitos"
})
public class ExecSharcnet {

    protected String server;
    @XmlElement(required = true)
    protected String login;
    @XmlElement(required = true)
    protected String senha;
    @XmlElement(required = true)
    protected String comando;
    @XmlElement(required = true)
    protected String arqOutput;
    protected String requisitos;

    /**
     * Gets the value of the server property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getServer() {
        String resposta=server;
        if(server==null){
            resposta="auto";
        }else{
            if(server.equals("")){
                resposta="auto";
            }
        }
        return resposta;
    }

    /**
     * Sets the value of the server property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setServer(String value) {
        this.server = value;
    }

    /**
     * Gets the value of the login property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLogin() {
        return login;
    }

    /**
     * Sets the value of the login property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLogin(String value) {
        this.login = value;
    }

    /**
     * Gets the value of the senha property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSenha() {
        return senha;
    }

    /**
     * Sets the value of the senha property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSenha(String value) {
        this.senha = value;
    }

    /**
     * Gets the value of the comando property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getComando() {
        return comando;
    }

    /**
     * Sets the value of the comando property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setComando(String value) {
        this.comando = value;
    }

    /**
     * Gets the value of the arqOutput property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getArqOutput() {
        return arqOutput;
    }

    /**
     * Sets the value of the arqOutput property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setArqOutput(String value) {
        this.arqOutput = value;
    }

    /**
     * Gets the value of the requisitos property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRequisitos() {
        String resposta=requisitos;
        if(requisitos==null){
            resposta="N";
        }else{
            if(requisitos.equals("")){
                resposta="N";
            }
        }
        return resposta;
    }

    /**
     * Sets the value of the requisitos property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRequisitos(String value) {
        this.requisitos = value;
    }

}
