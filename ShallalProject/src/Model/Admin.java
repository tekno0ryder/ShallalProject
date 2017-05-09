
package Model;

/**
 *
 * @author Ryder
 */
public class Admin extends Employee {

    public Admin(String userName, String password) {
        super(userName, password);
    }

    public boolean addItem() {
        return true;
    }

    public boolean addCategory() {
        return true;
    }

    public boolean addEmployee() {
        return true;
    }

    public boolean updateEmployee() {
        return true;
    }
    
}
