package modele;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.util.Set;

@Entity
public class Solution {

    @Id
    private Long id;
    @ManyToOne
    private Instance instance;
    @OneToMany(mappedBy = "appartient")
    private Set<Shift> shifts;

    public Solution() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
