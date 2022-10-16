package softuni.exam.domain.dto.fromXml;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "teams")
@XmlAccessorType(XmlAccessType.FIELD)
public class TeamSeedRoot11Dto {
    @XmlElement(name = "team")
    private List<TeamSeed22Dto> teams;

    public TeamSeedRoot11Dto() {
    }

    public List<TeamSeed22Dto> getTeams() {
        return teams;
    }

    public void setTeams(List<TeamSeed22Dto> teams) {
        this.teams = teams;
    }
}
