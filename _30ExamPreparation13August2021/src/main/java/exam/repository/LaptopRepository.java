package exam.repository;

import exam.model.entity.Laptop;
import exam.model.entity.Town;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface LaptopRepository extends JpaRepository<Laptop,Long> {
    Laptop getLaptopByMacAddress(String macAddress);
    List<Laptop> getAllByIdNotNullOrderByCpuSpeedDescRamDescStorageDescMacAddress();

//    това работи също
//    findAllByOrderByCpuSpeedDescRamDescStorageDescMacAddressAsc
}
