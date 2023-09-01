import java.util.ArrayList;
import java.util.List;

public class AccountDao {
    private List<UserAccount> userAccounts = new ArrayList<>();

    public List<UserAccount> getUserAccounts() {
        return userAccounts;
    }

    public void setUserAccounts(List<UserAccount> userAccounts) {
        this.userAccounts = userAccounts;
    }

    @Override
    public String toString() {
        return "AccountDao{" +
                "userAccounts=" + userAccounts +
                '}';
    }
}
