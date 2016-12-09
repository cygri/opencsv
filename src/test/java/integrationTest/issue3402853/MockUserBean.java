package integrationTest.issue3402853;

import org.apache.commons.lang3.StringUtils;

public class MockUserBean {
    public static final String DEFAULT_BLANK_FIELD = "";
    private String first_name = DEFAULT_BLANK_FIELD;
    private String last_name = DEFAULT_BLANK_FIELD;
    private String profile_id = "";
    private String email = "";
    private String secondary_email = "";

    public String getProfile_Id() {
        return profile_id;
    }

    public void setProfile_Id(String userId) {
        this.profile_id = userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirst_Name() {
        return first_name;
    }

    public void setFirst_Name(String firstName) {
        this.first_name = firstName;
    }

    public String getLast_Name() {
        return last_name;
    }

    public void setLast_Name(String lastName) {
        this.last_name = lastName;
    }

    @Override
    public String toString() {
        String userId = StringUtils.isEmpty(getProfile_Id()) ? "" : getProfile_Id().trim();
        String firstname = StringUtils.isEmpty(getFirst_Name()) ? "" : getFirst_Name().trim();
        String lastname = StringUtils.isEmpty(getLast_Name()) ? "" : getLast_Name().trim();
        String email = StringUtils.isEmpty(getEmail()) ? "" : getEmail().trim();
        StringBuilder value = new StringBuilder();
        value.append(",user id:").append(userId);
        value.append(",email:").append(email);
        value.append(",first name:").append(firstname);
        value.append(",last name:").append(lastname);

        return value.toString();
    }

    public boolean equals(Object obj) {
        if ((null == obj) || !(obj instanceof MockUserBean)) {
            return false;
        }

        return this == obj || (obj).toString().equalsIgnoreCase(this.toString());
    }

    public int hashCode() {
        return toString().hashCode();
    }

    public boolean isFirstNameEmpty() {
        return isNameEmpty(getFirst_Name());
    }

    public boolean isLastNameEmpty() {
        return isNameEmpty(getLast_Name());
    }

    private boolean isNameEmpty(String name) {
        return name == null || name.length() == 0 || DEFAULT_BLANK_FIELD.equals(name);

    }

    public String getSecondary_Email() {
        return secondary_email;
    }

    public void setSecondary_Email(String secondaryEmail) {
        this.secondary_email = secondaryEmail;
    }
}
