package hiberspring.service.impl;

import com.google.gson.Gson;
import hiberspring.common.GlobalConstants;
import hiberspring.domain.dto.fromJson.BranchSeedDto;
import hiberspring.domain.entity.Branch;
import hiberspring.domain.entity.Town;
import hiberspring.repository.BranchRepository;
import hiberspring.service.BranchService;
import hiberspring.service.TownService;
import hiberspring.util.ValidationUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

import static hiberspring.common.GlobalConstants.BRANCHES_FILE_PATH;
import static hiberspring.common.GlobalConstants.INCORRECT_DATA_MESSAGE;

@Service
public class BranchServiceImpl implements BranchService {
    private final BranchRepository branchRepository;
    private final ModelMapper modelMapper;
    private final ValidationUtil validationUtil;
    private final Gson gson;
    //щом импотваш клава в ентити бранч на таун cascade = {cascadeType.Merge}
    private final TownService townService;

    @Autowired
    public BranchServiceImpl(BranchRepository branchRepository,
                             ModelMapper modelMapper,
                             ValidationUtil validationUtil,
                             Gson gson,
                             TownService townService) {
        this.branchRepository = branchRepository;
        this.modelMapper = modelMapper;
        this.validationUtil = validationUtil;
        this.gson = gson;
        this.townService = townService;
    }


    @Override
    public Boolean branchesAreImported() {
        return branchRepository.count() > 0;
    }

    @Override
    public String readBranchesJsonFile() throws IOException {
        return Files.readString(
                Path.of(BRANCHES_FILE_PATH)
        );
    }

    @Override
    public String importBranches(String branchesFileContent) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();

        BranchSeedDto[] branchSeedDtos = gson
                .fromJson(readBranchesJsonFile(), BranchSeedDto[].class);

        Arrays.stream(branchSeedDtos)
                .forEach(branchSeedDto -> {
                    Branch exists = branchRepository.findByName(branchSeedDto.getName());
                    boolean isValid = validationUtil.isValid(branchSeedDto);

                    if (isValid && exists == null) {
                        Branch branch = modelMapper.map(branchSeedDto, Branch.class);
                        Town town = townService.getTownByName(
                                branchSeedDto.getTown()
                        );

                        branch.setTown(town);

                        stringBuilder.append(
                                String.format(GlobalConstants.SUCCESSFUL_IMPORT_MESSAGE,
                                        "Branch",
                                        branch.getName()
                                )
                        );

                        branchRepository.saveAndFlush(branch);
                    } else {
                        stringBuilder.append(INCORRECT_DATA_MESSAGE);
                    }

                    stringBuilder.append(System.lineSeparator());
                });

        return stringBuilder.toString().trim();
    }
}
