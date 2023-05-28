package pialeda.app.Invoice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import pialeda.app.Invoice.dto.SupplierInfo;
import pialeda.app.Invoice.model.Supplier;
import pialeda.app.Invoice.repository.SupplierRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import java.util.List;
import org.springframework.data.domain.Pageable;

@Service
public class SupplierService {
    @Autowired
    private SupplierRepository supplierRepository;

    public void createSupplier(SupplierInfo supplierInfo){
        Supplier supplier = new Supplier();

        supplier.setName(supplierInfo.getName());
        supplier.setAddress(supplierInfo.getAddress());
        supplier.setCityAddress(supplierInfo.getCityAddress());
        supplier.setTin(supplierInfo.getTin());
        supplier.setLimit(supplierInfo.getLimit());

        System.out.println(supplierInfo.getName());
        System.out.println(supplierInfo.getAddress());
        System.out.println(supplierInfo.getCityAddress());
        System.out.println(supplierInfo.getTin());
        System.out.println(supplierInfo.getLimit());

        System.out.println(supplierInfo.getSecNum());
        System.out.println(supplierInfo.getSecYear());
        System.out.println(supplierInfo.getAtp());
        System.out.println(supplierInfo.getCorNum());
        System.out.println(supplierInfo.getCorDate());
        if(supplierInfo.getSecNum()==null || supplierInfo.getSecNum().isEmpty()){
            supplier.setSecNum("NA");
        }else{
            supplier.setSecNum(supplierInfo.getSecNum());
        }
        
        if(supplierInfo.getSecYear()==null || supplierInfo.getSecYear().isEmpty()){
            supplier.setSecYear("NA");
        }else{
            supplier.setSecYear(supplierInfo.getSecYear());
        }

        if(supplierInfo.getAtp()==null || supplierInfo.getAtp().isEmpty()){
            supplier.setAtp("NA");
        }else{
            supplier.setAtp(supplierInfo.getAtp());
        }

        if(supplierInfo.getCorNum()==null || supplierInfo.getCorNum().isEmpty()){
            supplier.setCorNum("NA");
        }else{
            supplier.setCorNum(supplierInfo.getCorNum());
        }
        
        if(supplierInfo.getCorDate()==null || supplierInfo.getCorDate().isEmpty()){
            supplier.setCorDate("NA");
        }else{
            supplier.setCorDate(supplierInfo.getCorDate());
        }
        supplierRepository.save(supplier);
    }

    public long getSupplierCount(){
        return supplierRepository.count();
    }

    public List<Supplier> getAllSupplier(){
        return supplierRepository.findAll(Sort.by(Sort.Direction.ASC, "name"));
    }

    public List<String> getAllSupplierName()
    {
        return supplierRepository.getAllSupplierNames(Sort.by(Sort.Direction.ASC, "name"));
    }
    public Supplier findByName(String name){
        return supplierRepository.findByName(name);
    }

    public double findLimitByName(String name){
        Supplier supp = supplierRepository.findByName(name);

        return supp.getLimit();
    }

    public Page<Supplier> getSupplierPaginated(int currentPage, int size){
        Pageable p = PageRequest.of(currentPage, size);
        return supplierRepository.findAll(p);
    }

    public Supplier getSpecificSupplier(int id){
        return supplierRepository.findById(id);
    }

    public Boolean updateSupplierById(int id, String suppName, String address, String cityAddress, String tin,    
                                      String secNum, String secYear, String atp, String corNum, String corDate,
                                      double limit){
        Supplier supp = getSpecificSupplier(id);
        
        if(supp !=null){
            supp.setName(suppName);
            supp.setAddress(address);
            supp.setCityAddress(cityAddress);
            supp.setTin(tin);
            supp.setSecNum(secNum);
            supp.setSecYear(secYear);
            supp.setAtp(atp);
            supp.setCorNum(corNum);
            supp.setCorDate(corDate);
            supp.setLimit(limit);
    
            supplierRepository.save(supp);
    
            return true;
        }else{
            return false;
        }
    }

    public boolean deleteSupp(int id){
        try {
            supplierRepository.deleteById(id);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        // clientRepository.deleteById(id);
    }
}
