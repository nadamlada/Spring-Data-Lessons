package hiberspring.service;


import java.io.FileNotFoundException;
import java.io.IOException;

// TODO ready
public interface BranchService {

    Boolean branchesAreImported();

    String readBranchesJsonFile() throws IOException;

    String importBranches(String branchesFileContent) throws IOException;
}
