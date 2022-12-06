package softuni.exam.service;


import softuni.exam.models.entity.Agent;

import java.io.IOException;

public interface AgentService {

    boolean areImported() throws IOException;

    String readAgentsFromFile() throws IOException;
	
	String importAgents() throws IOException;

    Agent getAgentByFirstName(String firstName);
}
