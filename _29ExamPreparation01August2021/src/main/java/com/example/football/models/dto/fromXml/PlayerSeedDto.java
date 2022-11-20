package com.example.football.models.dto.fromXml;

import com.example.football.models.entity.Position;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)
public class PlayerSeedDto {
    @XmlElement(name = "first-name")
    @Length(min = 2)
    private String firstName;
    @XmlElement(name = "last-name")
    @Length(min = 2)
    private String lastName;
    @XmlElement
    @Email
    private String email;
    @XmlElement(name = "birth-date")
    private String birthDate;
    @XmlElement
    private Position position;
    @XmlElement(name = "town")
    private TownNameSeedDto townNameSeedDto;
    @XmlElement(name = "team")
    private TeamNameSeedDto teamNameSeedDto;
    @XmlElement(name = "stat")
    private StatIdSeedDto statIdSeedDto;

    public PlayerSeedDto() {
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public TownNameSeedDto getTownNameSeedDto() {
        return townNameSeedDto;
    }

    public void setTownNameSeedDto(TownNameSeedDto townNameSeedDto) {
        this.townNameSeedDto = townNameSeedDto;
    }

    public TeamNameSeedDto getTeamNameSeedDto() {
        return teamNameSeedDto;
    }

    public void setTeamNameSeedDto(TeamNameSeedDto teamNameSeedDto) {
        this.teamNameSeedDto = teamNameSeedDto;
    }

    public StatIdSeedDto getStatIdSeedDto() {
        return statIdSeedDto;
    }

    public void setStatIdSeedDto(StatIdSeedDto statIdSeedDto) {
        this.statIdSeedDto = statIdSeedDto;
    }
}
