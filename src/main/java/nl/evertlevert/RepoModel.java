package nl.evertlevert;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@Setter
@NoArgsConstructor
public class RepoModel {

    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private String language;
    private String full_name;
    private Boolean isPrivate;
    private Boolean isFork;
    private LocalDateTime created_at;
    private LocalDateTime  updated_at;
    private String ssh_url;
    private Integer size;
    private String description;

    private String created_at_string;
    private String updated_at_string;

    public String getCreated_at_string(){

        return created_at.format(formatter);
    }


    public String getUpdated_at_string(){

        return updated_at.format(formatter);

    }




}
