package hiberspring.service;


import hiberspring.domain.entity.Branch;

import java.io.FileNotFoundException;
import java.io.IOException;

// TODO ready
public interface BranchService {

    Boolean branchesAreImported();

    String readBranchesJsonFile() throws IOException;

    String importBranches(String branchesFileContent) throws IOException;

    Branch getByStringName(String name);
}
