package nl.evertlevert;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RepoModel {


    private String language;
    private String full_name;
    private Boolean isPrivate;
    private Boolean isFork;
    private String ssh_url;
    private Integer size;
    private String description;

    private String created_at_string;
    private String updated_at_string;
}
