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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
@Service
public class ClientService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ClientService.class);

    @Autowired
    private ClientRepository clientRepository;

    /**
     * Creates a new client.
     *
     * @param clientInfo The client information to be saved.
     */
    public void createClient(ClientInfo clientInfo) {
        try {
            Client client = new Client();
            client.setName(clientInfo.getName());
            client.setAddress(clientInfo.getAddress());
            client.setCityAddress(clientInfo.getCityAddress());
            client.setTin(clientInfo.getTin());
            client.setAgent(clientInfo.getAgent());

            if (clientInfo.getBusStyle() == null || clientInfo.getBusStyle().isEmpty()) {
                client.setBusStyle("NA");
            } else {
                client.setBusStyle(clientInfo.getBusStyle());
            }

            if (clientInfo.getTerms() == null || clientInfo.getTerms().isEmpty()) {
                client.setTerms("NA");
            } else {
                client.setTerms(clientInfo.getTerms());
            }

            clientRepository.save(client);
            LOGGER.info("Client created: {}", client.getName()); // Log client creation
        } catch (Exception e) {
            LOGGER.error("Error creating client: {}", e.getMessage(), e);
        }
    }

    /**
     * Gets the total number of clients.
     *
     * @return The number of clients.
     */
    public long getClientCount() {
        try {
            long clientCount = clientRepository.count();
            LOGGER.info("Total client count: {}", clientCount); // Log the total client count
            return clientCount;
        } catch (Exception e) {
            LOGGER.error("Error getting client count: {}", e.getMessage(), e);
            return -1L; // Handle the error gracefully in your application, returning -1 to indicate an error
        }
    }

    /**
     * Retrieves a list of all clients sorted by name.
     *
     * @return A list of clients sorted by name.
     */
    public List<Client> getAllClient() {
        try {
            List<Client> clients = clientRepository.findAll(Sort.by(Sort.Direction.ASC, "name"));
            LOGGER.info("Retrieved {} clients", clients.size()); // Log the number of clients retrieved
            return clients;
        } catch (Exception e) {
            LOGGER.error("Error getting all clients: {}", e.getMessage(), e);
            return null; // Handle the error gracefully in your application
        }
    }

    /**
     * Finds a client by their name.
     *
     * @param name The name of the client to find.
     * @return The client if found, or null if not found.
     */
    public Client findByName(String name) {
        try {
            Client client = clientRepository.findByName(name);
            if (client != null) {
                LOGGER.info("Client found by name: {}", name);
            } else {
                LOGGER.info("Client not found for name: {}", name);
            }
            return client;
        } catch (Exception e) {
            LOGGER.error("Error finding client by name: {}", e.getMessage(), e);
            return null; // Handle the error gracefully in your application
        }
    }

    /**
     * Filters clients by name.
     *
     * @param name The name to filter clients by.
     * @return A list of clients matching the filter.
     */
    public List<Client> filterClient(String name) {
        try {
            List<Client> filteredClients = (List<Client>) clientRepository.findByName(name);
            LOGGER.info("Filtered {} clients by name: {}", filteredClients.size(), name);
            return filteredClients;
        } catch (Exception e) {
            LOGGER.error("Error filtering clients by name: {}", e.getMessage(), e);
            return null; // Handle the error gracefully in your application
        }
    }

    /**
     * Retrieves a paginated list of clients.
     *
     * @param currentPage The current page number.
     * @param size        The number of clients per page.
     * @return A page of clients.
     */
    public Page<Client> getClientPaginated(int currentPage, int size) {
        try {
            Pageable p = PageRequest.of(currentPage, size);
            Page<Client> paginatedClients = clientRepository.findAll(p);
            LOGGER.info("Retrieved {} clients on page {}, page size: {}", paginatedClients.getNumberOfElements(), currentPage, size);
            return paginatedClients;
        } catch (Exception e) {
            LOGGER.error("Error getting paginated clients: {}", e.getMessage(), e);
            return null; // Handle the error gracefully in your application
        }
    }

    /**
     * Retrieves a specific client by ID.
     *
     * @param id The ID of the client to retrieve.
     * @return The client if found, or null if not found.
     */
    public Client getSpecificClient(int id) {
        try {
            Client client = clientRepository.findById(id);
            if (client != null) {
                LOGGER.info("Client found with ID: " + id);
            } else {
                LOGGER.error("Client not found with ID: " + id);
            }
            return client;
        } catch (Exception e) {
            LOGGER.error("Error getting client by ID: " + e.getMessage());
            // Handle the error gracefully in your application or re-throw it
            return null;
        }
    }

    /**
     * Updates a client by ID with new information.
     *
     * @param id       The ID of the client to update.
     * @param suppName The new name of the client.
     * @param address  The new address of the client.
     * @param cityAddress The new city address of the client.
     * @param tin      The new TIN (Tax Identification Number) of the client.
     * @param agent    The new agent of the client.
     * @param busStyle The new business style of the client.
     * @param terms    The new terms of the client.
     * @return True if the update is successful, false otherwise.
     */
    public Boolean updateClientById(int id, String suppName, String address, String cityAddress, String tin,
                                    String agent, String busStyle, String terms) {
        try {
            Client client = getSpecificClient(id);

            if (client != null) {
                client.setName(suppName);
                client.setAddress(address);
                client.setCityAddress(cityAddress);
                client.setTin(tin);
                client.setAgent(agent);
                client.setBusStyle(busStyle);
                client.setTerms(terms);

                clientRepository.save(client);
                LOGGER.info("Client updated successfully with ID: " + id);
                return true;
            } else {
                LOGGER.error("Client not found with ID: " + id);
                return false; // Client not found
            }
        }catch(Exception e){
                LOGGER.error("Error updating client by ID: {}", e.getMessage(), e);
                return false; // Handle the error gracefully in your application
            }
        }
        /**
         * Deletes a client by ID.
         *
         * @param id The ID of the client to delete.
         * @return True if the deletion is successful, false otherwise.
         */
        public boolean deleteClient(int id) {
            try {
                clientRepository.deleteById(id);
                LOGGER.info("Client deleted successfully with ID: " + id);
                return true;
            } catch (Exception e) {
                LOGGER.error("Error deleting client by ID: " + e.getMessage(), e);
                // Handle the error gracefully in your application
                return false;
            }
        }
}
