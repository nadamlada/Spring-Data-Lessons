package exam.repository;

import exam.model.entity.Laptop;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LaptopRepository extends JpaRepository<Laptop,Long> {
    Laptop getLaptopByMacAddress(String macAddress);
    List<Laptop> getAllByIdNotNullOrderByCpuSpeedDescRamDescStorageDescMacAddress();

//    това работи също
//    findAllByOrderByCpuSpeedDescRamDescStorageDescMacAddressAsc
}
