package tcc.greendao;// THIS CODE IS GENERATED BY greenDAO, EDIT ONLY INSIDE THE "KEEP"-SECTIONS

// KEEP INCLUDES - put your custom includes here
// KEEP INCLUDES END
/**
 * Entity mapped to table "ACCOUNT".
 */
public class Account {

    private Long id;
    private String name;
    private String password;

    // KEEP FIELDS - put your custom fields here
    // KEEP FIELDS END

    public Account() {
    }

    public Account(Long id) {
        this.id = id;
    }

    public Account(Long id, String name, String password) {
        this.id = id;
        this.name = name;
        this.password = password;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    // KEEP METHODS - put your custom methods here
    // KEEP METHODS END

}
