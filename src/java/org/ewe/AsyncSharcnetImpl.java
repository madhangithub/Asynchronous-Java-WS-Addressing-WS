/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.ewe;

import javax.annotation.Resource;
import javax.jws.WebService;
import javax.ejb.Stateless;
import javax.xml.ws.WebServiceContext;
import javax.jws.soap.SOAPBinding;
import javax.jws.WebMethod;
import org.xmlsoap.schemas.ws._2004._08.addressing.EndpointReferenceType;

import com.sun.xml.ws.api.message.Header;
import com.sun.xml.ws.api.message.HeaderList;

// internal
import com.sun.xml.ws.developer.JAXWSProperties;
import com.sun.xml.ws.developer.WSBindingProvider;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import javax.jws.Oneway;
import javax.jws.WebParam;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

/**
 *
 * @author thiago
 */
@Stateless
@WebService(name = "AsyncSharcnetImpl", targetNamespace = "http://ewe.org/")
@SOAPBinding(parameterStyle = SOAPBinding.ParameterStyle.BARE)
@XmlSeeAlso({
    org.xmlsoap.schemas.ws._2004._08.addressing.ObjectFactory.class,
    org.ewe.ObjectFactory.class
})
public class AsyncSharcnetImpl implements AsyncSharcnet{
    /** namespace : addressing 2004. */
    private static final String NS_ADDRESSING_2004 = "http://schemas.xmlsoap.org/ws/2004/08/addressing";
    /** header : reply to. */
    private static final String HEADER_REPLYTO = "ReplyTo";
    /** header : address. */
    private static final String HEADER_ADDRESS = "Address";
    /** header : message id. */
    private static final String HEADER_MESSAGEID = "MessageID";
    /** header : relates to. */
    private static final String HEADER_RELATESTO = "RelatesTo";

    @Resource
    WebServiceContext context;

    /**
     *
     * @param replyTo
     * @param messageID
     * @param parameters
     */
    @WebMethod
    @Oneway
    //(String server, String login, String senha, String comando,  String arqOutput, String requisitos){
    public void execSharcnet(
        @WebParam(name = "MessageID", targetNamespace = "http://schemas.xmlsoap.org/ws/2004/08/addressing", header = true, partName = "MessageID")
        String messageID,
        @WebParam(name = "ReplyTo", targetNamespace = "http://schemas.xmlsoap.org/ws/2004/08/addressing", header = true, partName = "ReplyTo")
        EndpointReferenceType replyTo,
        @WebParam(name = "execSharcnet", targetNamespace = "http://ewe.org/", partName = "parameters")
        ExecSharcnet parameters){

        
        System.out.println("Iniciando execução");
        System.out.println("Comando Sharcnet: "+parameters.getComando());

        
        String respostaSN=this.executaComandoSharcnet(parameters.getServer(),parameters.getLogin(),parameters.getSenha(),parameters.getRequisitos(),parameters.getComando());
        System.out.println(respostaSN);
        

        /////////////////////////////////////////////////////////
        System.out.println("========");
        System.out.println("Verificando Callback");
        HeaderList hl =(HeaderList) context.getMessageContext().get(JAXWSProperties.INBOUND_HEADER_LIST_PROPERTY);

        //Pega Dados do Header
        String address=this.getWSAddressing(hl);
//        String messageId =this.getMessageID(hl);

        System.out.println("MessageID: "+messageID);
        //String address=replyTo.getAddress().toString();//N sei pq assim nao ta funcionando
        System.out.println("WS_Addressing: "+address);

        //Se tem um endereço para responder, Responde.
        if(!address.equals("")){

            System.out.println("========");
            System.out.println("Monitorando Job");
            //Obtendo dados da resposta
            String lines[] = respostaSN.split("\\r?\\n");
            String serverUsed=lines[lines.length-1];//Ultima linha
            String jobID=lines[lines.length-2];//Penultima linha
            lines=jobID.split(" ");
            jobID=lines[lines.length-1];//Ultima palavra
            
            respostaSN= this.monitorar(serverUsed,parameters.getLogin(),parameters.getSenha(),jobID);

            System.out.println(respostaSN);

            System.out.println("Job Concluido");

            System.out.println("========");
            System.out.println("Lendo resultado");
            respostaSN=this.readResult(serverUsed,parameters.getLogin(),parameters.getSenha(),parameters.getArqOutput());
            System.out.println(respostaSN);

            System.out.println("========");
            System.out.println("Preparando resposta");

            //Monta a mensagem
            AsyncSharcnetResponseImplService srv = new AsyncSharcnetResponseImplService();
            AsyncSharcnetResponseImpl portType = srv.getAsyncSharcnetResponseImplPort();
            WSBindingProvider bp = (WSBindingProvider) portType;

            bp.setAddress(address);//seta o endereço

//            if(!messageId.equals("")){//Se tem messageId, seta o RELATESTO na resposta
//                bp.setOutboundHeaders(Headers.create(new QName(NS_ADDRESSING_2004,HEADER_RELATESTO),messageId));
//            }

            //Envia a mensagem com o texto
            Response response=new Response();
            response.setResult(respostaSN);
            portType.response(messageID,response);

            System.out.println("Resposta enviada");
        }else{
            System.out.println("========");
            System.out.println("Sem endereço para responder");
        }
                        
    }

//    /**
//     * Método para obter o MessageID do Invoke do WS
//     *
//     * @param hl HeaderList vindo do XML de invoke do WS
//     * @return MessageID que estava setado no Invoke, ou "" se não foi enviado
//     */
//    private String getMessageID(HeaderList hl) {
//        String messageId="";
//        if (hl != null){ //Se tem algum header
//            try{
//                    messageId = hl.get(NS_ADDRESSING_2004, HEADER_MESSAGEID, false).getStringContent();
//
//                    System.out.println("Achei MESSAGEID: "+messageId);
//                }catch(NullPointerException e){
//                    System.out.println("Erro pegando MESSAGEID");
//                }
//        }
//        return messageId;
//    }

    /**
     * Método para obter o WSAddress do Invoke do WS
     *
     * @param hl HeaderList vindo do XML de invoke do WS
     * @return WSAddress que estava setado no Invoke, ou "" se não foi enviado
     */
    private String getWSAddressing(HeaderList hl){
         String WSAddress = "";

        if (hl != null){//Se tem algum header
            try {
                Header replyTo = hl.get(NS_ADDRESSING_2004, HEADER_REPLYTO, false);
                if(replyTo != null){//Se tem algum replyto
                    XMLStreamReader replyToReader = replyTo.readHeader();
                    while ( (WSAddress.equals("") ) && (replyToReader.getEventType() != XMLStreamConstants.END_DOCUMENT)) {
                        if (replyToReader.next() == XMLStreamConstants.START_ELEMENT) {
                            if (replyToReader.getLocalName().equals(HEADER_ADDRESS)) {
                                replyToReader.next();
                                WSAddress = replyToReader.getText();
                                System.out.println("Achei WSAddress");
                            }
                        }
                    }
                }
            } catch (XMLStreamException xe) {
                System.out.println("Erro pegando WSAddress");
                xe.printStackTrace();
            }
        }

        return WSAddress;
    }

    /**
     * Método que executa o EscalonadorGlobal implementado em python para rodar um comando na SHARCNET
     *
     * @param msg Comando a ser executado na SHARCNET
     * @return Resposta vinda do programa em python
     */
    private String executaComandoSharcnet(String server, String login, String senha, String props, String comando)/* throws ExecPythonException*/ {
        String texto= "";

        if(server==null){
            server="auto";
        }
        if(server.equals("")){
            server="auto";
        }
        if(props.equals("")){
            props="N";
        }
        
		try
        {
            System.out.println("Executando comando: "+server+" "+login+" "+senha+" "+props+" "+comando);
            //Comando padrão: auto tlechuga ymq2.bBQ N sqsub -q mpi -n 7 -r 4 -o hello2.log ./hello2
            Process p = Runtime.getRuntime().exec("python /home/thiago/workspace/Escalonador/src/principal/sharcexec.py "+server+" "+login+" "+senha+" "+props+" "+comando);
            p.waitFor();
            BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line = "";
            while ((line = br.readLine()) != null)
            {                
                texto+=line+'\n';
            }

            if(p.exitValue()!=0){//erro executando python
                throw new ExecPythonException("Erro Executando comando");
            }

        }
//        catch (ExecPythonException pe){
//            throw pe;
//        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return texto;
    }

    private String monitorar(String server, String login, String senha, String jobID)/* throws ExecPythonException*/ {
		String texto="";
        try
        {
            //Comando padrão: narwhal.sharcnet.ca tlechuga ymq2.bBQ 374391
            System.out.println("Monitorando: "+server+" "+login+" "+senha+" "+jobID);
            Process p = Runtime.getRuntime().exec("python /home/thiago/workspace/Escalonador/src/principal/sharcmonitor.py "+server+" "+login+" "+senha+" "+jobID);
            p.waitFor();
            BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line = "";
            while ((line = br.readLine()) != null)
            {                
                texto+=line+'\n';
            }
            if(p.exitValue()!=0){//erro executando python
                throw new ExecPythonException("Erro Executando comando");
            }

        }
//        catch (ExecPythonException pe){
//            throw pe;
//        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return texto;
    }

    private String readResult(String server, String login, String senha, String arqOutput) {
        String texto="";
        try
        {
            //Comando padrão: narwhal tlechuga ymq2.bBQ /home/tlechuga/hello2
            System.out.println("Lendo Resultado: "+server+" "+login+" "+senha+" "+arqOutput);
            Process p = Runtime.getRuntime().exec("python /home/thiago/workspace/Escalonador/src/principal/sharcresult.py READ "+server+" "+login+" "+senha+" "+arqOutput);
            p.waitFor();
            BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line = "";
            while ((line = br.readLine()) != null)
            {
                texto+=line+'\n';
            }
            if(p.exitValue()!=0){//erro executando python                
                throw new ExecPythonException("Erro Executando comando");
            }

        }
//        catch (ExecPythonException pe){
//            throw pe;
//        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return texto;
    }

    private String downloadResult(String server, String login, String senha, String arqOutput) {
        String texto="";
        String arqLocal="/tmp/sharcnet.result";
        try
        {
            //Comando padrão: narwhal tlechuga ymq2.bBQ /home/tlechuga/hello2
            System.out.println("Baixando Resultado: "+server+" "+login+" "+senha+" "+arqOutput);
            Process p = Runtime.getRuntime().exec("python /home/thiago/workspace/Escalonador/src/principal/sharcresult.py GET "+server+" "+login+" "+senha+" "+arqOutput+" "+arqLocal);
            p.waitFor();         
        }
//        catch (ExecPythonException pe){
//            throw pe;
//        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return texto;
    }

//    private String join( String token, String[] strings,int inicio, int fim )
//    {
//        StringBuffer sb = new StringBuffer();
//
//        for( int x = inicio; x < fim; x++ )
//        {
//            sb.append( strings[x] );
//            sb.append( token );
//        }
//        sb.append( strings[ fim ] );
//
//        return( sb.toString() );
//    }
}
