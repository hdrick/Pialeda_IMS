package pialeda.app.Invoice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import pialeda.app.Invoice.dto.ClientInfo;
import pialeda.app.Invoice.model.Client;
import pialeda.app.Invoice.repository.ClientRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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

    public Page<Client> getClientPaginated(int currentPage, int size){
        Pageable p = PageRequest.of(currentPage, size);
        return clientRepository.findAll(p);
    }

    public Client getSpecificClient(int id){
        return clientRepository.findById(id);
    }

    public Boolean updateClientById(int id, String suppName,String address,String cityAddress,String tin,
                                    String agent,String busStyle,String terms){
        Client client = getSpecificClient(id);
        
        if(client !=null){
            client.setName(suppName);
            client.setAddress(address);
            client.setCityAddress(cityAddress);
            client.setTin(tin);
            client.setAgent(agent);
            client.setBusStyle(busStyle);
            client.setTerms(terms);
    
            clientRepository.save(client);
            return true;
        }else{
            return false;
        }
    }

    public boolean deleteClient(int id){
        try {
            clientRepository.deleteById(id);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        // clientRepository.deleteById(id);
    }
}
