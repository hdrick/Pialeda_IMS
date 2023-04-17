package pialeda.app.Invoice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import pialeda.app.Invoice.dto.ClientInfo;
import pialeda.app.Invoice.model.Client;
import pialeda.app.Invoice.repository.ClientRepository;

import java.util.List;

@Service
public class ClientService {
    @Autowired
    private ClientRepository clientRepository;

    public void createClient(ClientInfo clientInfo){
        Client client = new Client();

        client.setName(clientInfo.getName());
        client.setAddress(clientInfo.getAddress());
        client.setCityAddress(clientInfo.getCityAddress());
        client.setTin(clientInfo.getTin());
        client.setAgent(clientInfo.getAgent());
        if(clientInfo.getBusStyle() == null || clientInfo.getBusStyle().isEmpty()){
            client.setBusStyle("NA");
        }else{
            client.setBusStyle(clientInfo.getBusStyle());
        }

        if(clientInfo.getTerms() == null || clientInfo.getTerms().isEmpty()){
            client.setTerms("NA");
        }else{
            client.setTerms(clientInfo.getTerms());
        }

        clientRepository.save(client);
    }

    public long getClientCount(){
        return clientRepository.count();
    }
    public List<Client> getAllClient(){
        return clientRepository.findAll(Sort.by(Sort.Direction.ASC, "name"));
    }

    public Client findByName(String name){
        return clientRepository.findByName(name);
    }

    public List<Client> filterClient(String name)
    {
        return (List<Client>) clientRepository.findByName(name);
    }
}
